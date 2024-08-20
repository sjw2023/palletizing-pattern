package com.wsapoa.services;

import com.wsapoa.dto.ReportRequestDTO;
import com.wsapoa.entity.*;
import com.wsapoa.repository.*;
import com.wsapoa.utils.AbstractPattern;
import com.wsapoa.utils.Block;
import com.wsapoa.utils.Interlock;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.processing.SQL;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    //TODO : Change below to DAO and make it used of ReportRepository later
    private final ReportRepository reportRepository;
    private final PalletRepository palletRepository;
    private final ProductRepository productRepository;
    private final ContainerRepository containerRepository;
    private final PatternRepository patternRepository;
    private final ReportResultRepository reportResultRepository;

    public void createReport(ReportRequestDTO reportRequestDTO) {
        Report report = new Report(reportRequestDTO);
        reportRepository.save(report);
        //Get all the information
        Pallet pallet = palletRepository.findById(reportRequestDTO.getPalletId()).orElseThrow(() -> new RuntimeException("Pallet not found"));
        Pattern pattern = patternRepository.findById(reportRequestDTO.getPatternId()).orElseThrow(() -> new RuntimeException("Pattern not found"));
        Container container = containerRepository.findById(reportRequestDTO.getContainerId()).orElseThrow(() -> new RuntimeException("Container not found"));
        Product product = productRepository.findById(reportRequestDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        long productVolume = product.getLength() * product.getWidth() * product.getHeight();

        ReportResult reportResult = new ReportResult();
        AbstractPattern abstractPattern = null;
        if(reportRequestDTO.getPatternType().equals("INTERLOCK")){
            abstractPattern = new Interlock( product, pallet, pattern, container, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
        }
        else if(reportRequestDTO.getPatternType().equals("BLOCK")){
            abstractPattern = new Block( product, pallet, pattern, container, false, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
        }
        else if(reportRequestDTO.getPatternType().equals("INTERLOCK_WITH_ROTATION")){
            abstractPattern = new Interlock( product, pallet, pattern, container, true, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
        }
        else if(reportRequestDTO.getPatternType().equals("BLOCK_WITH_ROTATION")){
            abstractPattern = new Block( product, pallet, pattern, container, true, reportRequestDTO.getMarginSetting(), reportRequestDTO.getExceedLengthSetting());
        }
        if(abstractPattern != null){
            reportResult.setReportResultProducts(abstractPattern.calculatePatterns());
            reportResult.setAreaEfficiency(abstractPattern.calcAreaEfficiency());
            reportResult.setProductPerLayer(abstractPattern.calcProductPerLayer());
            reportResult.setPalletPerContainer(abstractPattern.calcPalletPerContainer());
            reportResult.setNumberOfLayers(abstractPattern.calcNumberOfLayers());
            reportResult.setTotalProducts(abstractPattern.calcTotalProducts());
            reportResult.setReportResultPallets(null);
            reportResultRepository.save(reportResult);
        }
    }
}
