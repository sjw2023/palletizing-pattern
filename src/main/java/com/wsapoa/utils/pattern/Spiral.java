package com.wsapoa.utils.pattern;

import com.wsapoa.entity.*;
import com.wsapoa.utils.container.SpiralProductList;
import com.wsapoa.utils.shape.Coordinate;
import com.wsapoa.utils.shape.ObjectAreaInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j

//TODO : Get optimized number of products in width and length
public class Spiral extends AbstractPattern{
    public Spiral(AbstractPattern abstractPattern) {
        super(abstractPattern);
        this.patternType = abstractPattern.getPatternType();
    }

    public Spiral(Product product, Pallet pallet, Container container, String patternType, boolean rotate, long margin, long exceedLimit) {
        super(product, pallet, container, rotate, margin, exceedLimit);
        this.patternType = patternType;
    }

    @Override
    public long calcProductPerLayer() {
        return calcProductInWidth()*2 + calcProductInLength()*2;
    }

    private long calcProductInLength(){
        return ( palletInfo.getWidth() - productInfo.getLength() ) / productInfo.getWidth();
    }

    private long calcProductInWidth(){
        return ( palletInfo.getLength() - productInfo.getWidth() ) / productInfo.getLength();
    }

    @Override
    public List<ReportResultProduct> calculatePatterns() {
        AtomicInteger orderIndex = new AtomicInteger();
        List<ReportResultProduct> products = new ArrayList<>();
        SpiralProductList spiralProductList = new SpiralProductList(productInfo, palletInfo);
        var productInWidth = calcProductInWidth();
        var productInLength = calcProductInLength();

        for (int i = 0; i < productInWidth; i++) {
            spiralProductList.addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                    .x((int) (productInfo.getWidth() / 2 + i * productInfo.getWidth()))
                    .y((int) (productInfo.getLength() / 2))
                    .z((int) (productInfo.getHeight() / 2))
                    .build(), true));
        }

        var lastProductAreaInfo = spiralProductList.getLastProductAreaInfo();
        for (int i = 0; i < productInLength; i++) {
            spiralProductList.addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                    .x((int) ((int) (lastProductAreaInfo.getCenter().getX() + productInfo.getWidth() / 2) + productInfo.getLength() / 2))
                    .y((int) (productInfo.getWidth() / 2 + i * productInfo.getWidth()))
                    .z((int) (productInfo.getHeight() / 2))
                    .build(), false));
        }

        lastProductAreaInfo = spiralProductList.getLastProductAreaInfo();
        for (int i = 0; i < productInWidth; i++) {
            spiralProductList.addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                    .x(((int) (lastProductAreaInfo.getEnd().getX() - productInfo.getWidth() / 2 - (i) * productInfo.getWidth())))
                    .y((int) ((int) (lastProductAreaInfo.getEnd().getY()) + productInfo.getLength() / 2))
                    .z((int) (productInfo.getHeight() / 2))
                    .build(), true));
        }

        lastProductAreaInfo = spiralProductList.getLastProductAreaInfo();
        for (int i = 0; i < productInWidth; i++) {
            spiralProductList.addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                    .x(((int) (lastProductAreaInfo.getOrigin().getX() - productInfo.getLength() / 2)))
                    .y((int) ((int) (lastProductAreaInfo.getEnd().getY() - productInfo.getWidth() / 2) - i * productInfo.getWidth()))
                    .z(productInfo.getHeight() / 2)
                    .build(), false));
        }
        //TODO : Implement fill center if needed

        //TODO : Implement with DFS later.
//        while( spiralProductList.hasEnoughArea() ){
//            var nextMove = spiralProductList.getAndPutNextMove();
//            if( nextMove == null ){
//                log.info("No more moves available");
//                break;
//            };
//        }
        spiralProductList.addLayers( calcNumberOfLayers() );
        spiralProductList.getMap().forEach(
                productAreaInfo -> products.add(new ReportResultProduct(productAreaInfo, orderIndex.getAndIncrement()))
        );
        return products;
    }
}
