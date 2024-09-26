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
    protected Container containerInfo;
    protected Coordinate center;
//    protected Coordinate origin;
//    protected Coordinate end;
    protected boolean rotate;
    protected long margin;
    protected long exceedLimit;
    protected long totalPatternLength;
    protected long totalPatternWidth;
    protected long totalPatternHeight;
    protected float actualPatternVolume;
    //TODO : Implement to use this Diagonal, InterLock, Spiral
    protected long actualPatternLength;
    protected long actualPatternWidth;
    protected long actualPatternHeight;

    //TODO : Add origin and end
    public AbstractPattern(AbstractPattern abstractPattern){
        this.productInfo = abstractPattern.productInfo;
        this.palletInfo = abstractPattern.palletInfo;
        this.containerInfo = abstractPattern.containerInfo;
        this.rotate = abstractPattern.rotate;
        this.margin = abstractPattern.margin;
        this.exceedLimit = abstractPattern.exceedLimit;
        this.totalPatternHeight = abstractPattern.totalPatternHeight;
        this.totalPatternLength = abstractPattern.totalPatternLength;
        this.totalPatternWidth = abstractPattern.totalPatternWidth;
        this.actualPatternVolume = abstractPattern.actualPatternVolume;
        this.actualPatternLength = abstractPattern.actualPatternLength;
        this.actualPatternWidth = abstractPattern.actualPatternWidth;
        this.actualPatternHeight = abstractPattern.actualPatternHeight;
        this.patternType = abstractPattern.patternType;
        this.center = abstractPattern.center;
    }

    //TODO : Update Super in each pattern ctor
    public AbstractPattern(
            Product product,
            Pallet pallet,
            Container container,
            boolean rotate,
            long margin,
            long exceedLimit
    ) {
        this.productInfo = product;
        this.palletInfo = pallet;
        this.containerInfo = container;
        this.rotate = rotate;
        this.margin = margin;
        this.exceedLimit = exceedLimit;
        this.totalPatternHeight = containerInfo.getHeight();
        this.totalPatternLength = calcTotalPatternLength();
        this.totalPatternWidth = calcTotalPatternWidth();
    }

//    public long calcActualPatternHeight(){
//        this.actualPatternHeight = calcNumberOfLayers()*productInfo.getHeight()+palletInfo.getHeight();
//        return this.actualPatternHeight;
//    }

    public long calcNumberOfLayers(){
        return (containerInfo.getHeight() - palletInfo.getHeight()) / productInfo.getHeight();
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
        var patternVolume = (containerInfo.getHeight()-palletInfo.getHeight())* palletInfo.getWidth() * palletInfo.getLength();
        return (actualPatternVolume / (float)patternVolume) * 100F;
    }

    public float getPatternVolume(){
        return (containerInfo.getHeight()-palletInfo.getHeight())* totalPatternWidth * totalPatternLength;
    }

    abstract public long calcProductPerLayer();
    abstract public List<ReportResultProduct> calculatePatterns();
}