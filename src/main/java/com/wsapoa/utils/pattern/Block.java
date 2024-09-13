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
    boolean boxRotated;

    public Block(AbstractPattern abstractPattern) {
        super(abstractPattern);
    }

    public Block(
            Product product,
            Pallet pallet,
            Pattern pattern,
            Container container,
            boolean rotate,
            long margin,
            long exceedLimit
    ) {
        super(product, pallet, pattern, container, rotate, margin, exceedLimit);
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
        long totalLength = this.calcProductInLength();
        long totalWidth = this.calcProductInWidth();
        long width = this.productInfo.getLength();
        long length = this.productInfo.getWidth();
        long height = this.productInfo.getHeight();
        if (boxRotated) {
            totalLength = this.calcRotatedProductInLength();
            totalWidth = this.calcRotatedProductInWidth();
            width = this.productInfo.getWidth();
            length = this.productInfo.getLength();
        }
        for (int i = 0; i < totalWidth; i++) {
            for (int j = 0; j < totalLength; j++) {
                blockProductList.addProductAreaInfo(new ObjectAreaInfo(
                        productInfo,
                        new Coordinate(
                                (j) * (width) + width / 2,
                                (i) * (length) + length / 2,
                                height / 2
                        ),
                        boxRotated
                ));
            }
        }
        blockProductList.addLayers(calcNumberOfLayers());
        return blockProductList.copyProductToResult(blockProductList);
    }
}
