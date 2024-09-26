package com.wsapoa.utils.pattern;

import com.wsapoa.entity.*;
import com.wsapoa.utils.container.InterlockProductList;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Interlock pattern 생성 클래스
 * 정의 : Interlock 패턴은 몸통으로 정의한 rotate된 product들을 Column 타입 패턴 처럼 쌓은뒤 rotate되지 않은 prroduct들이 한줄 생성되는 패턴이다
 * 예시 : - - - |
 * - - - |
 * - - - |
 * 편의상 rotate되지 않은 product를 topRow라고 정의, 나머지 rotate되지 않은 product를 restOfTheRows라고 정의하였다.
 */

//TODO : Get optimized number of products in width and length
public class Interlock extends AbstractPattern {
    long theNumOfProductsInFirstRow;
    long productsInRestOfTheRowsInWidth;
    long productsInRestOfTheRowsInLength;

    public Interlock(@NotNull AbstractPattern abstractPattern) {
        super(abstractPattern);
        this.patternType = abstractPattern.getPatternType();
        calcTopRow();
        calcBelowCol();
        calcBelowRow();
    }

    public Interlock(Product product, Pallet pallet, Container container, String patternType, boolean rotate, long margin, long exceedLimit) {
        super(product, pallet, container, rotate, margin, exceedLimit);
        this.patternType = patternType;
        calcTopRow();
        calcBelowCol();
        calcBelowRow();
    }

    @Override
    public long calcProductPerLayer() {
        //TODO : Check margin and exceedLimit later
        if ((this.theNumOfProductsInFirstRow - 1) * margin + this.theNumOfProductsInFirstRow * getProductInfo().getWidth() > totalPatternWidth) {
            this.theNumOfProductsInFirstRow--;
        }
        if ((productsInRestOfTheRowsInWidth - 1) * margin + productsInRestOfTheRowsInWidth * getProductInfo().getLength() > totalPatternWidth) {
            productsInRestOfTheRowsInWidth--;
        }
        if ((productsInRestOfTheRowsInLength - 1) * margin + productsInRestOfTheRowsInLength * getProductInfo().getWidth() > totalPatternLength) {
            productsInRestOfTheRowsInLength--;
        }
        var productsInRestOfTheRows = productsInRestOfTheRowsInLength * productsInRestOfTheRowsInWidth;
        return this.theNumOfProductsInFirstRow + productsInRestOfTheRows;
    }

    private boolean isRotatedBetter() {
        long rotatedProducts = calcRotatedTopRow() + calcRotatedBelowRow() * calcRotatedBelowCol();
        long products = calcTopRow() + calcBelowRow() * calcBelowCol();
//        return false;
        return rotatedProducts > products;
    }

    private long calcRotatedBelowCol() {
        return this.productsInRestOfTheRowsInLength = (totalPatternWidth) / productInfo.getWidth();
    }

    private long calcRotatedBelowRow() {
        return this.productsInRestOfTheRowsInWidth = (totalPatternLength - productInfo.getWidth()) / productInfo.getLength();
    }

    private long calcBelowCol() {
        return this.productsInRestOfTheRowsInLength = (totalPatternLength) / productInfo.getWidth();
    }

    private long calcBelowRow() {
        return this.productsInRestOfTheRowsInWidth = (totalPatternWidth - productInfo.getWidth()) / productInfo.getLength();
    }

    private long calcTopRow() {
        return this.theNumOfProductsInFirstRow = totalPatternLength / productInfo.getLength();
    }

    private long calcRotatedTopRow() {
        return this.theNumOfProductsInFirstRow = totalPatternWidth / productInfo.getLength();
    }

    @Override
    public List<ReportResultProduct> calculatePatterns() {
        InterlockProductList interlockProductList = new InterlockProductList(productInfo, palletInfo);
        //TODO : Put into map class
        //TODO : Calculate the marginal factor of the first row and add
        //TODO : This method must be called after set the width length values.
        if (!isRotatedBetter()) {
            for (int j = 0; j < this.productsInRestOfTheRowsInWidth; j++) {
                for (int i = 0; i < this.productsInRestOfTheRowsInLength; i++) {
                    if (j == 0 && i == 0) {
                        interlockProductList.addFirstProductAreaInfo(true);
                    } else if (i == 0 && j > 0) {
                        //TODO : Change to use find last ith product version
                        interlockProductList.addIncreasedYFromCenter(productInfo.getLength(), true);
                    } else {
                        interlockProductList.addIncreasedXFromEnd(productInfo.getWidth() / 2, true);
                    }
                }
            }
            //TODO : Didn't handle the case which top row is shorter than the body
            for (int i = 0; i < this.theNumOfProductsInFirstRow; i++) {
                if (i == 0) {
                    interlockProductList.addIncreasedXYFromOriginEndFromIthProduct(this.productsInRestOfTheRowsInLength, productInfo.getLength() / 2, (productInfo.getWidth() / 2), false);
                } else {
                    interlockProductList.addIncreasedXFromEnd(productInfo.getLength() / 2, false);
                }
            }
        } else {
            calcRotatedBelowRow();
            calcRotatedBelowCol();
            calcRotatedTopRow();
            for (int j = 0; j < this.productsInRestOfTheRowsInWidth; j++) {
                for (int i = 0; i < this.productsInRestOfTheRowsInLength; i++) {
                    if (j == 0 && i == 0) {
                        interlockProductList.addFirstProductAreaInfo(false);
                    } else if (i == 0 && j > 0) {
                        interlockProductList.addIncreasedXFromCenterFromIthProduct(this.productsInRestOfTheRowsInLength, productInfo.getLength(), false);
                    } else {
                        interlockProductList.addIncreasedYFromCenter(productInfo.getWidth(), false);
                    }
                }
            }
            //TODO : Didn't handle the case which top row is shorter than the body
            for (int i = 0; i < this.theNumOfProductsInFirstRow; i++) {
                if (i == 0) {
                    interlockProductList.addIncreasedXYFromOriginFromIthProduct(this.productsInRestOfTheRowsInLength, productInfo.getLength() + productInfo.getWidth()/2, (productInfo.getLength() / 2), true);
                } else {
                    interlockProductList.addIncreasedYFromCenter(productInfo.getLength(), true);
                }
            }
        }
        interlockProductList.addLayers(calcNumberOfLayers());
        return interlockProductList.copyProductToResult(interlockProductList);
    }
}
