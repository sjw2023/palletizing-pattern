package com.wsapoa.utils.pattern;

import com.wsapoa.entity.*;
import com.wsapoa.utils.container.DiagonalProductList;
import com.wsapoa.utils.shape.Coordinate;
import com.wsapoa.utils.shape.ObjectAreaInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * Diagonal pattern 생성 클래스, 항상 정사각형이 모양
 * 정의 : Diagonal 패턴은 상품을 대각선 방향으로 점진적으로 쌓아 올리는 패턴이다.
 *      쳣 패턴은 1단 Pinwheel을 그려주며 시작되며 공간이 허용된다면 첫 패턴 아래와 오른쪽으로 상자를 추가적으로 그려준다.
 *      - |
 *      | _
 *      오른쪽은 위에서부터 회전되지 않은 Product를 공간이 되는대로 그리고 마지막엔 회전된 Product를 그려준다.
 *          -
 *          -
 *          |
 *      그후 아래쪽에 회전되지 않은 Product를 오른쪽 마지막에 회전된 Product로부터 왼쪽으로 그려준다.
 *             -
 *             -
 *         - - |
 *      1번 연장된 Diagonal 패턴의 최종 모습은 다음과 같다.
 *      - | -
 *      | - -
 *      - - |
 *      이후 공간이 되는대로 추가적으로 더해준다면 다음과 같은 것이다
 *       - | - -
 *  *    | - - -
 *  *    - - | -
 *       - - - |
 *      * @version 1.0
 * @since 1.0
 * @see AbstractPattern
 * @see DiagonalProductList
 * @see ObjectAreaInfo
 * @see Coordinate
 * @see Product
 * @see Pallet
 *
 */
@Slf4j
//TODO : Get optimized number of products in width and length
//TODO : Add calculation of limit of products in width and length
public class Diagonal extends AbstractPattern{
    public Diagonal(AbstractPattern abstractPattern) {
        super(abstractPattern);
        this.patternType = abstractPattern.getPatternType();
    }
    public Diagonal(Product product, Pallet pallet, Container container, String patternType, boolean rotate, long margin, long exceedLimit) {
        super(product, pallet, container, rotate, margin, exceedLimit);
        this.patternType = patternType;
    }

    @Override
    public long calcProductPerLayer() {
        var limitWidth = calcNumberOfProductsInPalletWidth();
        var limitLength = calcNumberOfProductsInPalletLength();
        assert limitLength > 0;
        assert limitWidth > 0;
        var numberOfOuterLayers = Math.min(limitLength, limitWidth);
        var baseLayer = 5;
        var increaseFactor = 2;
        var layerProducts = baseLayer*numberOfOuterLayers+(increaseFactor*(numberOfOuterLayers-1));
        return 4 + layerProducts;
    }

    @Override
    public long calcTotalProducts() {
        var totalNumberOfLayers = calcNumberOfLayers();
        var totalNumberOfProductsInLayer = calcProductPerLayer();
        return totalNumberOfProductsInLayer * totalNumberOfLayers;
    }

    public boolean isPossible(){
        assert palletInfo != null;
        assert productInfo != null;
        return( (palletInfo.getWidth() - productInfo.getLength()) / productInfo.getWidth() > 1 )
            && ( (palletInfo.getLength() - productInfo.getWidth()) / productInfo.getLength() > 1 );
    }

    @Override
    public List<ReportResultProduct> calculatePatterns() {
        if( isPossible() ) {
            DiagonalProductList diagonalProductList = new DiagonalProductList( productInfo, palletInfo );
            assert !addFirstRectanglePattern(diagonalProductList);
            addFirstRectanglePattern(diagonalProductList);
            for(int i =1; i <= calcNumberOfProductsInPalletLength(); i++){
                addOuterProducts(diagonalProductList, i);
            }
            diagonalProductList.addLayers(calcNumberOfLayers());
            return diagonalProductList.copyProductToResult(diagonalProductList);
        }
        return null;
    }

    private boolean addFirstRectanglePattern(DiagonalProductList diagonalProductList){
        assert diagonalProductList != null;
        //TODO : Extract to method the boilerplate code
        diagonalProductList.addProductAreaInfo(
                new ObjectAreaInfo(productInfo, Coordinate.builder()
                        .x((int) (productInfo.getLength()/2))
                        .y((int) (productInfo.getWidth()/2))
                        .z((int) (productInfo.getHeight()/2))
                        .build(),
                        false));
        var lastInfo = diagonalProductList.getLastProductAreaInfo();
        diagonalProductList.addProductAreaInfo(
                new ObjectAreaInfo(productInfo, Coordinate.builder()
                        .x((int) (lastInfo.getOrigin().getX() + productInfo.getLength() + productInfo.getWidth()/2))
                        .y((int) (lastInfo.getOrigin().getY() + productInfo.getLength()/2))
                        .z((int) (productInfo.getHeight()/2))
                        .build(),
                        true));
        lastInfo = diagonalProductList.getLastProductAreaInfo();
        diagonalProductList.addProductAreaInfo(
                new ObjectAreaInfo(productInfo, Coordinate.builder()
                        .x((int) (lastInfo.getEnd().getX() - productInfo.getLength() / 2))
                        .y((int) (lastInfo.getEnd().getY() + productInfo.getWidth() / 2))
                        .z((int) (productInfo.getHeight()/2))
                        .build(),
                        false));
        lastInfo = diagonalProductList.getLastProductAreaInfo();
        diagonalProductList.addProductAreaInfo(
                new ObjectAreaInfo(productInfo, Coordinate.builder()
                        .x((int) (lastInfo.getEnd().getX() - productInfo.getWidth()/2 - productInfo.getLength()))
                        .y((int) (lastInfo.getEnd().getY() - productInfo.getLength()/2))
                        .z((int) (productInfo.getHeight()/2))
                        .build(),
                        true)) ;
        return true;
    }

    private void addOuterProducts(DiagonalProductList diagonalProductList, int outerLevel){
        var totalNumberOfProducts = outerLevel*2+3;
        var mid = totalNumberOfProducts/2;
        var firstProductIndexOfLastOuterLayer = Math.pow(outerLevel, 2);
        //TODO : Check Bug, type casting from double to int
        var firstProductInfoOfLastOuterLayer = diagonalProductList.getProductAreaInfo((int) firstProductIndexOfLastOuterLayer);
        boolean makeTurn = false;
        for(int i = 0; i < totalNumberOfProducts; i++){
            if(i==0){
                //First product of current outer layer
                diagonalProductList.addProductAreaInfo(
                        new ObjectAreaInfo(productInfo, Coordinate.builder()
                                .x((int) (firstProductInfoOfLastOuterLayer.getOrigin().getX() + productInfo.getWidth() + productInfo.getWidth()/2))
                                .y((int) (firstProductInfoOfLastOuterLayer.getOrigin().getY() + productInfo.getLength()/2))
                                .z((int) (productInfo.getHeight()/2))
                                .build(),
                                true));
            }
            else{
                if(!makeTurn){
                    var lastInfo = diagonalProductList.getLastProductAreaInfo();
                    //At turning point
                    if(i==mid){
                        diagonalProductList.addProductAreaInfo(
                                new ObjectAreaInfo(productInfo, Coordinate.builder()
                                        .x((int) (lastInfo.getEnd().getX() - productInfo.getLength()/2))
                                        .y((int) (lastInfo.getEnd().getY() + productInfo.getWidth()/2 ))
                                        .z((int) (productInfo.getHeight()/2))
                                        .build(),
                                        false));
                        makeTurn = true;
                    }
                    else {
                        diagonalProductList.addProductAreaInfo(
                                new ObjectAreaInfo(productInfo, Coordinate.builder()
                                        .x((int) (lastInfo.getOrigin().getX() + productInfo.getWidth() / 2))
                                        .y((int) (lastInfo.getOrigin().getY() + productInfo.getLength() + productInfo.getLength() / 2))
                                        .z((int) (productInfo.getHeight() / 2))
                                        .build(),
                                        true)
                        );
                    }
                }
                else {
                    var lastInfo = diagonalProductList.getLastProductAreaInfo();
                    diagonalProductList.addProductAreaInfo(
                            new ObjectAreaInfo(productInfo, Coordinate.builder()
                                    .x((int) (lastInfo.getOrigin().getX() - productInfo.getWidth()/2))
                                    .y((int) (lastInfo.getEnd().getY() - productInfo.getLength() / 2))
                                    .z((int) (productInfo.getHeight() / 2))
                                    .build(),
                                    true
                            ));
                }
            }
        }
//        return true;
    }

    //TODO : Check bug, number can be smaller than what we want because of type casting
    private int calcNumberOfProductsInPalletLength(){
        return (int) ((totalPatternLength - productInfo.getWidth()) / productInfo.getLength())-1;
    }

    private int calcNumberOfProductsInPalletWidth(){
        return (int) ((totalPatternWidth - productInfo.getLength()) / productInfo.getWidth())-1;
    }
}
