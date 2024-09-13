package com.wsapoa.utils.pattern;

import com.wsapoa.entity.*;
import com.wsapoa.utils.shape.Coordinate;
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
    protected Coordinate center;
    protected Coordinate origin;
    protected Coordinate end;
    protected boolean rotate;
    protected long margin;
    protected long exceedLimit;
    protected long totalPatternLength;
    protected long totalPatternWidth;
    protected long totalPatternHeight;
    protected float actualPatternVolume;

    //TODO : Add origin and end
    public AbstractPattern(AbstractPattern abstractPattern){
        this.productInfo = abstractPattern.productInfo;
        this.palletInfo = abstractPattern.palletInfo;
        this.patternInfo = abstractPattern.patternInfo;
        this.containerInfo = abstractPattern.containerInfo;
        this.rotate = abstractPattern.rotate;
        this.margin = abstractPattern.margin;
        this.exceedLimit = abstractPattern.exceedLimit;
        this.totalPatternHeight = abstractPattern.totalPatternHeight;
        this.totalPatternLength = abstractPattern.totalPatternLength;
        this.totalPatternWidth = abstractPattern.totalPatternWidth;
        this.center = abstractPattern.center;
    }

    public AbstractPattern(
            Product product,
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
        this.totalPatternHeight = patternInfo.getHeight();
        this.totalPatternLength = calcTotalPatternLength();
        this.totalPatternWidth = calcTotalPatternWidth();
    }

    public long calcNumberOfLayers(){
        return (patternInfo.getHeight() - palletInfo.getHeight()) / productInfo.getHeight();
    }

    public long calcTotalProducts(){
        return calcProductPerLayer() * calcNumberOfLayers();
    }

    public long calcTotalPatternLength(){
        totalPatternLength = palletInfo.getLength() + exceedLimit * 2;
        return totalPatternLength;
    }

    public long calcTotalPatternWidth(){
        totalPatternWidth = palletInfo.getWidth() + exceedLimit * 2;
        return totalPatternWidth;
    }

    //TODO : Add margin and exceedLimit to the calculation later
    //TODO : Add pallet volume to calculation
    public float calcAreaEfficiency() {
        this.actualPatternVolume = calcTotalProducts() * productInfo.getProductVolume();
        var patternVolume = (patternInfo.getHeight()-palletInfo.getHeight())* palletInfo.getWidth() * palletInfo.getLength();
        return ((float)actualPatternVolume / (float)patternVolume) * 100F;
    }

    public float getPatternVolume(){
        return (patternInfo.getHeight()-palletInfo.getHeight())* totalPatternWidth * totalPatternLength;
    }

    abstract public long calcProductPerLayer();
    abstract public List<ReportResultProduct> calculatePatterns();
}