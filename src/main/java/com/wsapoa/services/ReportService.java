package com.wsapoa.services;

import com.wsapoa.dto.ReportRequestDTO;
import com.wsapoa.entity.*;
import com.wsapoa.repository.*;
import com.wsapoa.utils.container.ContainerList;
import com.wsapoa.utils.pattern.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService implements BaseService<ReportResult, Long, ReportRequestDTO> {
    List<String> patternTypes = List.of("INTERLOCK", "BLOCK", "SPIRAL", "DIAGONAL");

    @Override
    public void create(ReportRequestDTO reportRequestDTO) {

    }

    @Override
    public void update(Long aLong, ReportRequestDTO reportRequestDTO) {

    }

    @Override
    public void delete(Long aLong) {
    }

    @Override
    public ReportResult read(Long aLong) {
        return reportResultRepository.findById(aLong).orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public List<ReportResult> getAll() {
        return reportResultRepository.findAll();
    }

    private final ReportRepository reportRepository;
    private final PalletRepository palletRepository;
    private final ProductRepository productRepository;
    private final ContainerRepository containerRepository;
    private final ReportResultRepository reportResultRepository;

    @Transactional
    public List<ReportResult> createReport(ReportRequestDTO reportRequestDTO) {
        Report report = new Report(reportRequestDTO);
        reportRepository.save(report);
        List<Pallet> pallets = getPallets();
        List<Container> containerInfos = getContainers();
        Product product = getProduct(reportRequestDTO.getProductId());
        List<ReportResult> reportResults = new ArrayList<>();
        for (var patternType : patternTypes) {
            log.debug("Pattern type : {}", patternType);
            for (var containerInfo : containerInfos) {
                for (var pallet : pallets) {
                    AbstractPattern abstractPattern = createPattern(patternType, reportRequestDTO, product, pallet, containerInfo);
                    //TODO : Create pattern usese the container information to calculate the pattern's height,
                    // consider to use another parameter to seperate the container information from the pattern
                    List<ReportResultProduct> reportResultProducts = abstractPattern.calculatePatterns();
//                ReportResult reportResult = createReportResult(reportRequestDTO, product, pallet, pattern, containerInfo);
//                report.addReportResult(reportResult);
//                    ContainerList container = new ContainerList(containerInfo);
                    ContainerList container = new ContainerList(abstractPattern);
                    if( !fillContainerWithPatterns(container, abstractPattern) ){
                        continue;
                    }
                    List<ReportResultPallet> reportResultPallets = createReportResultPallets(container);

                    ReportResult reportResult = buildReportResult(abstractPattern, container, reportRequestDTO.getProductId(), pallet.getPalletId(), containerInfo.getContainerId());
                    reportResultRepository.save(reportResult);

                    reportResultProducts.forEach(reportResult::addReportResultProduct);
                    reportResultPallets.forEach(reportResult::addReportResultPallet);
                    reportResults.add(reportResult);
                    log.debug("Report result creation has been done per pallet");
                }
                log.debug("Report result creation has been done per container");
            }
            log.debug("Report result creation has been done per pattern type");
        }
        log.debug("Report results creation has been done");
        return reportResults;
    }

    private List<Pallet> getPallets() {
        return palletRepository.findAll();
    }

    private List<Container> getContainers() {
        return containerRepository.findAll();
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    //TODO : Use patternType in AbstractPattern make ctor get String patternType
    private AbstractPattern createPattern(String patternType, ReportRequestDTO reportRequestDTO, Product product, Pallet pallet, Container container) {
        return switch (patternType) {
            case "INTERLOCK" ->
                    new Interlock(product, pallet, container, patternType, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "BLOCK" ->
                    new Block(product, pallet, container, patternType, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "SPIRAL" ->
                    new Spiral(product, pallet, container, patternType, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "DIAGONAL" ->
                    new Diagonal(product, pallet, container, patternType, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            default -> throw new IllegalArgumentException("Invalid pattern type");
        };
    }

    //TODO : Modify to set rotate when container's width is fit with other orientation.
    private boolean  fillContainerWithPatterns(ContainerList containerList, AbstractPattern abstractPattern) {
        var length = containerList.getContainerInfo().getWidth()/abstractPattern.getPatternLength();
        var width = containerList.getContainerInfo().getWidth()/abstractPattern.getPatternWidth();
        while (containerList.isSpaceLeft(abstractPattern) && containerList.isLengthLeft(abstractPattern, length > width)) {
            if( length > width ){
                if(length == 0){
                    return false;
                }
                addPatterns(length, abstractPattern, "Adding pattern {} to the container along with Length direction", containerList, true);
            }else if(width >= length){
                if(width == 0){
                    return false;
                }
                addPatterns(width, abstractPattern, "Adding pattern {} to the container along with Width direction", containerList, false);
            }
        }
        log.debug("Filling  has been done {}", containerList);
        return true;
    }

    private void addPatterns(long length, AbstractPattern abstractPattern, String s, ContainerList containerList, boolean rotate) {
        for (int i = 0; i < length; i++) {
            AbstractPattern temp = copyPattern(abstractPattern);
            log.debug(s, temp);
            if( !containerList.addPattern( temp, rotate, i ) ){
                log.debug("Pattern {} could not be added to the container", temp);
            }
        }
    }

    private List<ReportResultPallet> createReportResultPallets(ContainerList containerList) {
        List<ReportResultPallet> reportResultPallets = new ArrayList<>();
        AtomicInteger orderIndex = new AtomicInteger(1);
        containerList.getPatterns().forEach(pattern -> {
            ReportResultPallet reportResultPallet = ReportResultPallet.builder()
                    .x(pattern.getCenter().getX())
                    .y(pattern.getCenter().getY())
                    .z(pattern.getCenter().getZ())
                    .orderIndex(orderIndex.getAndIncrement())
                    .rotate(pattern.isRotate())
                    .build();
            reportResultPallets.add(reportResultPallet);
        });
        return reportResultPallets;
    }

    private ReportResult buildReportResult(AbstractPattern abstractPattern, ContainerList containerList, long productId, long palletId, long containerId) {
        return ReportResult.builder()
                .numberOfLayers(abstractPattern.calcNumberOfLayers())
                .totalProducts(abstractPattern.calcTotalProducts())
                .productPerLayer(abstractPattern.calcProductPerLayer())
                .patternAreaEfficiency(abstractPattern.calcAreaEfficiency())
                .numberOfPatternsInContainer(containerList.getNumberOfPatterns())
                .containerAreaEfficiency(containerList.calcAreaEfficiency())
                .usedPallet(palletId)
                .usedProduct(productId)
                .usedContainer(containerId)
                .patternType(abstractPattern.getPatternType())
                .totalProductsInContainer( abstractPattern.calcTotalProducts() * containerList.getNumberOfPatterns() )
                .build();
    }

    public AbstractPattern copyPattern(AbstractPattern pattern) {
        return switch (pattern.getPatternType()) {
            case "INTERLOCK" -> new Interlock(pattern);
            case "BLOCK" -> new Block(pattern);
            case "SPIRAL" -> new Spiral(pattern);
            case "DIAGONAL" -> new Diagonal(pattern);
            default -> throw new IllegalArgumentException("Invalid pattern type");
        };
    }
}