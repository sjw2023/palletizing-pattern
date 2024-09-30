package com.wsapoa.utils.pattern;

import com.wsapoa.entity.*;
import com.wsapoa.utils.shape.Coordinate;
import lombok.Getter;
import lombok.Setter;

import java.math.MathContext;
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
    protected long actualPatternHeight;
    protected long actualPatternLength;
    protected long actualPatternWidth;

    //TODO : Add origin and end
    public AbstractPattern(AbstractPattern abstractPattern) {
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
        this.actualPatternVolume = calcActualPatternVolume();
        this.actualPatternHeight = calcNumberOfLayers() * productInfo.getHeight() + palletInfo.getHeight();
    }

//    public long calcActualPatternHeight(){
//        this.actualPatternHeight = calcNumberOfLayers()*productInfo.getHeight()+palletInfo.getHeight();
//        return this.actualPatternHeight;
//    }

    public long getPatternWidth(){
        return Math.max(actualPatternWidth, totalPatternWidth);
    }

    public long getPatternLength(){
        return Math.max(actualPatternLength, totalPatternLength);
    }

    public float getPatterVolume(){
        return Math.min(actualPatternVolume, calcTotalPatternVolume());
    }

    public long calcNumberOfLayers() {
        return (containerInfo.getHeight() - palletInfo.getHeight()) / productInfo.getHeight();
    }

    public long calcTotalProducts() {
        return calcProductPerLayer() * calcNumberOfLayers();
    }

    public long calcTotalPatternLength() {
        totalPatternLength = palletInfo.getLength() + exceedLimit * 2;
        return totalPatternLength;
    }

    public long calcTotalPatternWidth() {
        totalPatternWidth = palletInfo.getWidth() + exceedLimit * 2;
        return totalPatternWidth;
    }

    public float calcTotalPatternVolume() {
        return containerInfo.getHeight() * totalPatternWidth * totalPatternLength;
    }

    public float calcActualPatternVolume() {
        this.actualPatternVolume = calcTotalProducts() * productInfo.getProductVolume() + palletInfo.getHeight() * palletInfo.getWidth() * palletInfo.getLength();
        return this.actualPatternVolume;
    }

    public float calcAreaEfficiency() {
        var patternVolume = calcTotalPatternVolume();
        this.actualPatternVolume = calcActualPatternVolume();
        return (( this.actualPatternVolume) / patternVolume ) * 100F;
    }

    abstract public long calcProductPerLayer();

    abstract public List<ReportResultProduct> calculatePatterns();

    abstract public long calcActualPatternLength();

    abstract public  long calcActualPatternWidth();
}