package com.wsapoa.utils.pattern;

import com.wsapoa.entity.*;
import com.wsapoa.utils.container.BlockProductList;
import com.wsapoa.utils.shape.Coordinate;
import com.wsapoa.utils.shape.ObjectAreaInfo;

import java.util.List;

/**
 * Block pattern 가장 기본적인 형태의 패턴
 * - - - -
 * - - - -
 * - - - -
 * - - - -
 * or
 * \ \ \ \
 * \ \ \ \
 * \ \ \ \
 * \ \ \ \
 */
public class Block extends AbstractPattern {
    private boolean boxRotated;
    private long totalProductInLength;
    private long totalProductInWidth;
    private long length;
    private long width;

    public Block(AbstractPattern abstractPattern) {
        super(abstractPattern);
        this.patternType = abstractPattern.getPatternType();
        this.actualPatternVolume = abstractPattern.getActualPatternVolume();
        this.actualPatternLength = abstractPattern.getActualPatternLength();
        this.actualPatternWidth = abstractPattern.getActualPatternWidth();
        this.totalProductInLength = boxRotated ? this.calcRotatedProductInLength() : this.calcProductInLength();
        this.totalProductInWidth = boxRotated ? this.calcRotatedProductInWidth() : this.calcProductInWidth();
        this.width = boxRotated ? productInfo.getWidth() : this.productInfo.getLength();
        this.length = boxRotated ? productInfo.getLength() : this.productInfo.getWidth();
    }

    public Block(
            Product product,
            Pallet pallet,
            Container container,
            String patternType,
            boolean rotate,
            long margin,
            long exceedLimit
    ) {
        super(product, pallet, container, rotate, margin, exceedLimit);
        this.patternType = patternType;
        this.actualPatternLength = calcActualPatternLength();
        this.actualPatternWidth = calcActualPatternWidth();
        this.actualPatternVolume = calcActualPatternVolume();
        this.totalProductInLength = boxRotated ? this.calcRotatedProductInLength() : this.calcProductInLength();
        this.totalProductInWidth = boxRotated ? this.calcRotatedProductInWidth() : this.calcProductInWidth();
        this.width = boxRotated ? productInfo.getWidth() : this.productInfo.getLength();
        this.length = boxRotated ? productInfo.getLength() : this.productInfo.getWidth();
    }

    @Override
    public long calcProductPerLayer() {
        if (findBetterOrientation()) {
            return calcRotatedProductInLength() * calcRotatedProductInWidth();
        } else {
            return calcProductInLength() * calcProductInWidth();
        }
    }

    public long calcProductInWidth() {
        return this.totalPatternWidth / productInfo.getWidth();
    }

    public long calcProductInLength() {
        return this.totalPatternLength / productInfo.getLength();
    }

    public long calcRotatedProductInWidth() {
        return this.totalPatternWidth / this.productInfo.getLength();
    }

    public long calcRotatedProductInLength() {
        return this.totalPatternLength / this.productInfo.getWidth();
    }

    /**
     * Find better orientation of the product and set the orientation of box to the better orientation
     *
     * @return true if rotated orientation is better
     */
    public boolean findBetterOrientation() {
        var rotatedProduct = calcRotatedProductInLength() * calcRotatedProductInWidth();
        var product = calcProductInLength() * calcProductInWidth();
        return rotatedProduct > product;
    }

    //TODO : Add functionality to set origin of pattern in the super
    @Override
    public List<ReportResultProduct> calculatePatterns() {
        BlockProductList blockProductList = new BlockProductList(productInfo, palletInfo);
        for (int i = 0; i < totalProductInWidth; i++) {
            for (int j = 0; j < totalProductInLength; j++) {
                blockProductList.addProductAreaInfo(new ObjectAreaInfo(
                        productInfo,
                        new Coordinate(
                                (j) * (width) + width / 2,
                                (i) * (length) + length / 2,
                                productInfo.getHeight() / 2
                        ),
                        boxRotated
                ));
            }
        }
        blockProductList.addLayers(calcNumberOfLayers());
        return blockProductList.copyProductToResult(blockProductList);
    }

    @Override
    public long calcActualPatternLength() {
        this.actualPatternLength = boxRotated ? calcRotatedProductInLength() * productInfo.getWidth() : calcProductInLength() * productInfo.getLength();
        return this.actualPatternLength;
    }

    @Override
    public long calcActualPatternWidth() {
        this.actualPatternLength = boxRotated ? calcRotatedProductInWidth() * productInfo.getLength() : calcProductInWidth() * productInfo.getWidth();
        return this.actualPatternWidth;
    }
}
