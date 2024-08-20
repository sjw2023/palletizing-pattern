package com.wsapoa.utils;

import com.wsapoa.entity.*;

import java.util.ArrayList;
import java.util.List;

public class Interlock extends AbstractPattern{
    public Interlock(Product product,
                     Pallet pallet,
                     Pattern pattern,
                     Container container,
                     boolean rotate,
                     long margin,
                     long exceedLimit
    ) {
        super( product, pallet, pattern, container, rotate, margin, exceedLimit);
    }
    long calcProductVolume(){
        return this.getProductInfo().getLength() * this.getProductInfo().getWidth() * this.getProductInfo().getHeight();
    }
    long calcPatternVolume(){
        return this.getPatternInfo().getLength() * this.getPatternInfo().getWidth() * this.getPatternInfo().getHeight();
    }

    @Override
    public long calcAreaEfficiency() {
        return calcProductVolume()*calcTotalProducts()/calcPatternVolume()*100;
    }

    @Override
    public long calcProductPerLayer() {
        var totalPatternWidth = getPalletInfo().getWidth()+exceedLimit*2;
        var totalPatternLength = getPalletInfo().getLength()+exceedLimit*2;
        if(!rotate){
            //get first row
            var theNumOfProductsInFirstRow = totalPatternWidth / getProductInfo().getWidth();
            if((theNumOfProductsInFirstRow-1)*margin+theNumOfProductsInFirstRow*getProductInfo().getWidth()>totalPatternWidth){
                theNumOfProductsInFirstRow--;
            }
            var theNumOfProductsInRestOfTheRowsInWidth = totalPatternWidth/getProductInfo().getLength();
            if((theNumOfProductsInRestOfTheRowsInWidth-1)*margin+theNumOfProductsInRestOfTheRowsInWidth*getProductInfo().getLength()>totalPatternWidth){
                theNumOfProductsInRestOfTheRowsInWidth--;
            }
            var theNumOfProductsInRestOfTheRowsInLength = totalPatternLength-getProductInfo().getLength()-margin
                    /getProductInfo().getWidth();
            if((theNumOfProductsInRestOfTheRowsInLength-1)*margin+theNumOfProductsInRestOfTheRowsInLength*getProductInfo().getWidth()>totalPatternLength) {
                theNumOfProductsInRestOfTheRowsInLength--;
            }
            var theNumOfProductsInRestOfTheRows = theNumOfProductsInRestOfTheRowsInLength*theNumOfProductsInRestOfTheRowsInWidth;
            return theNumOfProductsInFirstRow+theNumOfProductsInRestOfTheRows;
        }
        else{
            return 0;
        }
    }

    @Override
    public long calcPalletPerContainer() {
        return 0;
    }

    @Override
    public long calcNumberOfLayers() {
        return getPatternInfo().getHeight()/getProductInfo().getHeight();
    }

    @Override
    public long calcTotalProducts() {
        return calcNumberOfLayers()*calcProductPerLayer();
    }

    @Override
    public List<ReportResultProduct> calculatePatterns( ) {
        this.totalPatternLength = this.palletInfo.getLength() + this.exceedLimit * 2;
        this.totalPatternWidth = this.palletInfo.getWidth() + this.exceedLimit * 2;
        // 1st form
        // TODO : Product in width
        // Fill the first row with rotated boxes
        var firstRow = this.palletInfo.getLength() / this.productInfo.getWidth();
            // Calculate the margin between the boxes
        var firstRowRemainder = this.palletInfo.getWidth() % this.productInfo.getWidth();
        List<ReportResultProduct> products = new ArrayList<>();
        for (int i = 0; i < firstRow; i++) {
            ReportResultProduct productElement = new ReportResultProduct();
            //Calculate the coordinates of the boxes
            var X = this.productInfo.getWidth()/2 * (i+1) + margin * i;
            productElement.setX(X);
            var Y = this.productInfo.getLength()/2;
            productElement.setY(Y);
            // TODO : Height should be calculated based on the number of layers
            productElement.setZ(this.productInfo.getHeight()/2);
            productElement.setOrderIndex(i);
            productElement.setRotate(true);
            products.add(productElement);
        }


        // TODO : Need to use margin to calculate the number
        var theNumberOfProductsInRow = this.palletInfo.getLength() / this.productInfo.getLength();
        // TODO : Need to use maring to calculate the number of rows
        var theNumberOfRows = ((this.palletInfo.getWidth()-this.productInfo.getLength()))/this.productInfo.getWidth();
            // Calculate the margin between the boxes
        var theNumberOfExtrarowRemainder = this.palletInfo.getLength() % this.productInfo.getWidth();
        // Calculate the coordinates of the boxes
        int index = 0;
        for (int j = 0; j < theNumberOfRows; j++) {
            for (int i = 0; i < theNumberOfProductsInRow; i++) {
                ReportResultProduct productElement = new ReportResultProduct();
                //Calculate the coordinates of the boxes
                var X = ( this.productInfo.getLength() / 2 ) * ( i + 1 ) + margin * i;
                productElement.setX(X);
                var Y = ( this.productInfo.getWidth() / 2 ) * ( j + 1 ) + margin * i + this.productInfo.getLength() ;
                productElement.setY(Y);
                // TODO : Height should be calculated based on the number of layers
                productElement.setZ(this.productInfo.getHeight() / 2);
                productElement.setOrderIndex((int) ((index++)+firstRow));
                productElement.setRotate(false);
                products.add(productElement);
            }
        }
        return products;
    }
}
