package com.wsapoa.utils;

import com.wsapoa.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class AbstractPattern extends ReportResult {
    protected Product productInfo;
    protected Pallet palletInfo;
    protected Pattern patternInfo;
    protected Container containerInfo;
    protected boolean rotate;
    protected long margin;
    protected long exceedLimit;
    protected long totalPatternLength;
    protected long totalPatternWidth;

    public AbstractPattern(Product product,
                           Pallet pallet,
                           Pattern pattern,
                           Container container,
                           boolean rotate,
                           long margin,
                           long exceedLimit
    ) {
        this.productInfo = product;
        this.palletInfo = pallet;
        this.patternInfo = pattern;
        this.containerInfo = container;
        this.rotate = rotate;
        this.margin = margin;
        this.exceedLimit = exceedLimit;
    }
    abstract public long calcAreaEfficiency();
    abstract public long calcProductPerLayer();
    abstract public long calcPalletPerContainer();
    abstract public long calcNumberOfLayers();
    abstract public long calcTotalProducts();
    public List<ReportResultProduct> calculatePatterns() {
        return null;
    }
}
