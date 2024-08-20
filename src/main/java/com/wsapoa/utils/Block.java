package com.wsapoa.utils;

import com.wsapoa.entity.*;
import lombok.AllArgsConstructor;

import java.util.List;

public class Block extends AbstractPattern{
    public Block(Product product,
                     Pallet pallet,
                     Pattern pattern,
                     Container container,
                     boolean rotate,
                     long margin,
                     long exceedLimit
    ) {
        super( product, pallet, pattern, container, rotate, margin, exceedLimit);
    }
    @Override
    public long calcAreaEfficiency() {
        return 0;
    }

    @Override
    public long calcProductPerLayer() {
        return 0;
    }

    @Override
    public long calcPalletPerContainer() {
        return 0;
    }

    @Override
    public long calcNumberOfLayers() {
        return 0;
    }

    @Override
    public long calcTotalProducts() {
        return 0;
    }

    @Override
    public List<ReportResultProduct> calculatePatterns( ) {
        // TODO : Product in width
        // totalWidth = pallet width + exceed limit * 2
        //    if totalWidth / productWidth < 0
        //        return 0
        //    if totalWidth / productWidth > 1 || totalWidth / productWidth < 2
        //        return 1
        //    if totalWidth / productWidth > 2
        //        quotient = totalWidth / productWidth
        //        remainder = totalWidth % productWidth
        //        totalPatternWidth = quotient * productWidth + margin * (quotient - 1)
        // TODO : Product in length
        // totalWidth = pallet width + exceed limit * 2
        //     if totalWidth / productWidth < 0
        //         return 0
        //     if totalWidth / productWidth > 1 || totalWidth / productWidth < 2
        //         return 1
        //     if totalWidth / productWidth > 2
        //         quotient = totalWidth / productWidth
        //         remainder = totalWidth % productWidth
        //         totalPatternWidth = quotient * productWidth + margin * (quotient - 1)

        return null;
    }
    // TODO : Method to calculate pattern with rotated boxes
}
