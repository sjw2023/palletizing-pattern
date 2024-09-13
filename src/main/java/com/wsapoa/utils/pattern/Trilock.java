package com.wsapoa.utils.pattern;

import com.wsapoa.entity.*;

import java.util.List;


//TODO : Get optimized number of products in width and length
public class Trilock extends AbstractPattern {
    public Trilock(Product product, Pallet pallet, Pattern pattern, Container container, boolean rotate, long margin, long exceedLimit) {
        super(product, pallet, pattern, container, rotate, margin, exceedLimit);
    }

    @Override
    public List<ReportResultProduct> calculatePatterns() {
        return null;
    }

    @Override
    public float calcAreaEfficiency() {
        return 0;
    }

    @Override
    public long calcProductPerLayer() {
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
}
