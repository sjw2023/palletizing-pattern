package com.wsapoa.services;

import com.wsapoa.dto.ReportRequestDTO;
import com.wsapoa.entity.*;
import com.wsapoa.repository.*;
import com.wsapoa.utils.container.ContainerList;
import com.wsapoa.utils.pattern.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ReportService implements BaseService<ReportResult, Long, ReportRequestDTO> {
    @Override
    public void create(ReportRequestDTO reportRequestDTO) {
        createReport(reportRequestDTO);
    }

    @Override
    public void update(Long aLong, ReportRequestDTO reportRequestDTO) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public ReportResult read(Long aLong) {
        return null;
    }

    @Override
    public List<ReportResult> getAll() {
        return List.of();
    }

    private final ReportRepository reportRepository;
    private final PalletRepository palletRepository;
    private final ProductRepository productRepository;
    private final ContainerRepository containerRepository;
    private final PatternRepository patternRepository;
    private final ReportResultRepository reportResultRepository;

    @Transactional
    public ReportResult createReport(ReportRequestDTO reportRequestDTO) {
        Report report = new Report(reportRequestDTO);
        reportRepository.save(report);

        Pallet pallet = getPallet(reportRequestDTO.getPalletId());
        Pattern pattern = getPattern(reportRequestDTO.getPatternId());
        Container container = getContainer(reportRequestDTO.getContainerId());
        Product product = getProduct(reportRequestDTO.getProductId());

        AbstractPattern abstractPattern = createPattern(reportRequestDTO, product, pallet, pattern, container);

        List<ReportResultProduct> reportResultProducts = abstractPattern.calculatePatterns();

        ContainerList containerList = new ContainerList(container);
        fillContainerWithPatterns(containerList, abstractPattern, reportRequestDTO);

        List<ReportResultPallet> reportResultPallets = createReportResultPallets(containerList);

        ReportResult reportResult = buildReportResult(abstractPattern, containerList);
        reportResultRepository.save(reportResult);

        reportResultProducts.forEach(reportResult::addReportResultProduct);
        reportResultPallets.forEach(reportResult::addReportResultPallet);

        return reportResult;
    }

    private Pallet getPallet(Long palletId) {
        return palletRepository.findById(palletId).orElseThrow(() -> new RuntimeException("Pallet not found"));
    }

    private Pattern getPattern(Long patternId) {
        return patternRepository.findById(patternId).orElseThrow(() -> new RuntimeException("Pattern not found"));
    }

    private Container getContainer(Long containerId) {
        return containerRepository.findById(containerId).orElseThrow(() -> new RuntimeException("Container not found"));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private AbstractPattern createPattern(ReportRequestDTO reportRequestDTO, Product product, Pallet pallet, Pattern pattern, Container container) {
        return switch (reportRequestDTO.getPatternType()) {
            case "INTERLOCK" -> new Interlock(product, pallet, pattern, container, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "BLOCK" -> new Block(product, pallet, pattern, container, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "INTERLOCK_WITH_ROTATION" -> new Interlock(product, pallet, pattern, container, true, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "BLOCK_WITH_ROTATION" -> new Block(product, pallet, pattern, container, true, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "SPIRAL" -> new Spiral(product, pallet, pattern, container, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            case "DIAGONAL" -> new Diagonal(product, pallet, pattern, container, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
            default -> throw new IllegalArgumentException("Invalid pattern type");
        };
    }

    private void fillContainerWithPatterns(ContainerList containerList, AbstractPattern abstractPattern, ReportRequestDTO reportRequestDTO) {
        while (containerList.isSpaceLeft(abstractPattern) && containerList.isLengthLeft(abstractPattern)) {
            var numOfPatternsInWidth = containerList.getContainerInfo().getWidth() / abstractPattern.getTotalPatternWidth();
            for (int i = 0; i < numOfPatternsInWidth; i++) {
                containerList.addPattern(copyPattern(abstractPattern, reportRequestDTO), abstractPattern.isRotate(), i);
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
                    .build();
            reportResultPallets.add(reportResultPallet);
        });
        return reportResultPallets;
    }
    private ReportResult buildReportResult(AbstractPattern abstractPattern, ContainerList containerList) {
        return ReportResult.builder()
                .numberOfLayers(abstractPattern.calcNumberOfLayers())
                .totalProducts(abstractPattern.calcTotalProducts())
                .productPerLayer(abstractPattern.calcProductPerLayer())
                .patternAreaEfficiency(abstractPattern.calcAreaEfficiency())
                .numberOfPatternsInContainer(containerList.getNumberOfPatterns())
                .containerAreaEfficiency(containerList.calcAreaEfficiency())
                .build();
    }

    public AbstractPattern copyPattern(AbstractPattern pattern, ReportRequestDTO reportRequestDTO) {
        return switch (reportRequestDTO.getPatternType()) {
            case "INTERLOCK", "INTERLOCK_WITH_ROTATION" -> new Interlock(pattern);
            case "BLOCK", "BLOCK_WITH_ROTATION" -> new Block(pattern);
            case "SPIRAL" -> new Spiral(pattern);
            case "DIAGONAL" -> new Diagonal(pattern);
            default -> throw new IllegalArgumentException("Invalid pattern type");
        };
    }
}