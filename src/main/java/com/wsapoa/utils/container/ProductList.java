package com.wsapoa.utils.container;

import com.wsapoa.entity.Pallet;
import com.wsapoa.entity.Product;
import com.wsapoa.entity.ReportResultProduct;
import com.wsapoa.utils.shape.ObjectAreaInfo;
import com.wsapoa.utils.shape.Coordinate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@NoArgsConstructor
public abstract class ProductList {
    //TODO : Remove possibleCoordinates
    protected List<ObjectAreaInfo> map;
    protected Product productInfo;
    protected Pallet palletInfo;

    public ProductList(Product productInfo, Pallet palletInfo) {
        this.productInfo = productInfo;
        this.palletInfo = palletInfo;
        this.map = new ArrayList<>();
    }

    public boolean removeProductAreaInfo(ObjectAreaInfo productInfo) {
        return map.remove(productInfo);
    }

    public ObjectAreaInfo getProductAreaInfo(int index) {
        return map.get(index);
    }

    public boolean addFirstProductAreaInfo(boolean rotate){
        var width = productInfo.getWidth()/2;
        var length = productInfo.getLength()/2;
        if(rotate){
            width = productInfo.getLength()/2;
            length = productInfo.getWidth()/2;
        }
        assert map.isEmpty();
        var productAreaInfo = new ObjectAreaInfo(
                productInfo,
                Coordinate.builder()
                        .x((int) (width))
                        .y((int) (length))
                        .z((int) (productInfo.getHeight()/2)).build(),
                rotate
        );
        map.add(productAreaInfo);
        return true;
    }

    /**
     * Add a product to the list, this can be overridden for each type of pattern
     * @param productInfo product area information to be added
     * @return true if added, false otherwise
     */
    public boolean addProductAreaInfo(ObjectAreaInfo productInfo) {
        assert productInfo != null;
        if(map != null){
            if(!checkIntersection(productInfo)){
                map.add(productInfo);
                return true;
            }
            else{
                return false;
            }
        }
        map = new ArrayList<>();
        map.add(productInfo);
        return true;
    }

    public ObjectAreaInfo getLastProductAreaInfo() {
        return map.get(map.size() - 1);
    }

    public ObjectAreaInfo getFirstProductAreaInfoFromMap() {
        return map.get(0);
    }

    public boolean removeLastProductAreaInfoFromMap() {
        if (map.isEmpty()) {
            return false;
        }
        map.remove(map.size() - 1);
        return true;
    }

    /**
     * Check if the objectAreaInfo intersects with any of the objectAreaInfo in the map
     * @param objectAreaInfo product area information to check
     * @return true if intersects, false otherwise
     */
    public boolean checkIntersection(ObjectAreaInfo objectAreaInfo) {
        for (ObjectAreaInfo objectAreaInfo1 : map) {
            if (objectAreaInfo1.checkIntersection(objectAreaInfo)) {
                return true;
            }
        }
        return false;
    }

    public boolean addLayers( long numberOfLayers ){
        return map.addAll( this.addLayer( numberOfLayers ) );
    }

    /**
     * Add a layer of products to the list in same orientation
     * @param numberOfLayers number of layers to add
     * @return List of ObjectAreaInfo
     */
    public List<ObjectAreaInfo> addLayer(long numberOfLayers ){
        List<ObjectAreaInfo> objectAreaInfos = new ArrayList<>(map);
        AtomicLong orderIndexLong = new AtomicLong();
        for(int i =0; i< numberOfLayers; i++){
            map.forEach(
                    productAreaInfo -> {
                        objectAreaInfos.add(productAreaInfo.increaseZ( (productInfo.getHeight() * orderIndexLong.get()), productAreaInfo.isRotated() ) );
                    });
            orderIndexLong.incrementAndGet();
        }
        return objectAreaInfos;
    }

    public List<ReportResultProduct> copyProductToResult(ProductList productList) {
        List<ReportResultProduct> reportResultProducts = new ArrayList<>();
        AtomicInteger orderIndex = new AtomicInteger();
        productList.getMap().forEach(
                productAreaInfo -> {
                    reportResultProducts.add(new ReportResultProduct(productAreaInfo, orderIndex.getAndIncrement()));
                });
        return reportResultProducts;
    }

    public void addIncreasedXFromEnd(long increasingFactor, boolean rotate) {
        var lastProductAreaInfo = getLastProductAreaInfo();
        boolean b = addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                .x((int) (lastProductAreaInfo.getEnd().getX() + increasingFactor))
                .y((int) (lastProductAreaInfo.getCenter().getY()))
                .z((int) (lastProductAreaInfo.getCenter().getZ())).build(), rotate));
    }
    public void addIncreasedXFromCenterFromIthProduct(long ith, long increasingFactor, boolean rotate) {
        var lastProductAreaInfo = getProductAreaInfo((int) (getMap().size() - ith));
        boolean b = addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                .x((int) (lastProductAreaInfo.getCenter().getX() + increasingFactor))
                .y((int) (lastProductAreaInfo.getCenter().getY()))
                .z((int) (lastProductAreaInfo.getCenter().getZ())).build(), rotate));
    }

//    public void addIncreasedYFromCenter(long increasingFactor, boolean rotate) {
//        var lastProductAreaInfo = getLastProductAreaInfo();
//        boolean b = addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
//                .x((int) (this.productInfo.getWidth() / 2))
//                .y((int) (lastProductAreaInfo.getCenter().getY() + increasingFactor))
//                .z((int) (lastProductAreaInfo.getCenter().getZ())).build(), rotate));
//    }
    public void addIncreasedYFromCenter(long increasingFactor, boolean rotate) {
        var lastProductAreaInfo = getLastProductAreaInfo();
        boolean b = addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                .x((int) (lastProductAreaInfo.getCenter().getX()))
                .y((int) (lastProductAreaInfo.getCenter().getY() + increasingFactor))
                .z((int) (lastProductAreaInfo.getCenter().getZ())).build(), rotate));
    }

    public void addIncreasedXYFromOriginEndFromIthProduct(long ith, long xIncreasingFactor, long yIncreasingFactor, boolean rotate) {
        var lastProductAreaInfo = getProductAreaInfo((int) (getMap().size() - ith));
        boolean b = addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                .x((int) (lastProductAreaInfo.getOrigin().getX() + xIncreasingFactor))
                .y((int) (lastProductAreaInfo.getEnd().getY() + yIncreasingFactor))
                .z((int) (productInfo.getHeight() / 2)).build(), rotate));
    }
    public void addIncreasedXYFromOriginFromIthProduct(long ith, long xIncreasingFactor, long yIncreasingFactor, boolean rotate) {
        var lastProductAreaInfo = getProductAreaInfo((int) (getMap().size() - ith));
        boolean b = addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                .x((int) (lastProductAreaInfo.getOrigin().getX() + xIncreasingFactor))
                .y((int) (lastProductAreaInfo.getOrigin().getY() + yIncreasingFactor))
                .z((int) (productInfo.getHeight() / 2)).build(), rotate));
    }
    public void addYFromIthProductCenter(long ith, long increasingFactor, boolean rotate) {
        var lastProductAreaInfo = getProductAreaInfo((int) (ith));
        boolean b = addProductAreaInfo(new ObjectAreaInfo(productInfo, Coordinate.builder()
                .x((int) (lastProductAreaInfo.getCenter().getX()))
                .y((int) (lastProductAreaInfo.getCenter().getY() + increasingFactor))
                .z((int) (lastProductAreaInfo.getCenter().getZ())).build(), rotate));
    }
}
