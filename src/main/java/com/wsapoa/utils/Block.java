package com.wsapoa.utils;

import com.wsapoa.entity.*;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
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
        return calcProductInLength()*calcProductInWidth();
    }
    public long calcProductInWidth(){
        this.totalPatternWidth = this.palletInfo.getWidth()+this.exceedLimit*2;
        if(this.totalPatternWidth / this.productInfo.getWidth() < 0)
            return 0;
        //TODO : Use margin to calculate the quotient
        if(this.totalPatternWidth / this.productInfo.getWidth() > 1 && this.totalPatternWidth / this.productInfo.getWidth() < 2) {
            this.totalPatternWidth=1;
        }
        if (this.totalPatternWidth / this.productInfo.getWidth() > 2) {
            this.totalPatternWidth /= this.productInfo.getWidth();
            long remainder = this.totalPatternWidth % this.productInfo.getWidth();
        }
        return this.totalPatternWidth;
    }
    public long calcProductInLength() {
        this.totalPatternLength = this.palletInfo.getLength()  + this.exceedLimit * 2;
        // TODO : Product in length
        //     if totalWidth / productWidth < 0
        if(this.totalPatternLength / this.productInfo.getLength() < 0)
            return 0;
        if(this.totalPatternLength / this.productInfo.getLength() > 1 && this.totalPatternLength / this.productInfo.getLength() < 2)
            this.totalPatternLength=1;
        if(this.totalPatternLength / this.productInfo.getLength() > 2)
        {
            this.totalPatternLength /= this.productInfo.getLength();
            long remainder = this.totalPatternLength % this.productInfo.getLength();
        }
       return this.totalPatternLength;
    }
    @Override
    public long calcPalletPerContainer() {
        return 0;
    }

    @Override
    public long calcNumberOfLayers() {
        this.totalPatternHeight = this.patternInfo.getHeight();
        this.totalPatternHeight /= this.productInfo.getHeight();
        return this.totalPatternHeight;
    }

    @Override
    public long calcTotalProducts() {
        return calcProductPerLayer()*calcNumberOfLayers();
    }

    @Override
    public List<ReportResultProduct> calculatePatterns( ) {
        var totalLength = this.calcProductInLength();
        var totalWidth = this.calcProductInWidth();
        var totalLayer = this.calcNumberOfLayers();

        int index = 0;
        List<ReportResultProduct> products = new ArrayList<>();
        for(int k = 0; k < totalLayer; k++){
            for(int i = 0 ; i < totalWidth; i++){
                for(int j = 0; j< totalLength; j++){
                    var product = new ReportResultProduct();
                    product.setX((j+1)*(this.productInfo.getLength()/2));
                    product.setY((i+1)*(this.productInfo.getWidth()/2));
                    product.setZ(this.productInfo.getHeight()/2*(k+1));
                    product.setOrderIndex(index++);
                    products.add(product);
                }
            }
        }
        return products;
    }
    // TODO : Method to calculate pattern with rotated boxes
}
