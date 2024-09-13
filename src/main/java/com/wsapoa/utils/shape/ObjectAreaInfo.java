package com.wsapoa.utils.shape;

import com.wsapoa.entity.Container;
import com.wsapoa.entity.Product;
import com.wsapoa.entity.ReportResultProduct;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Class for ObjectAreaInfo treat the information of the dimension related values of the product
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectAreaInfo {
    private Coordinate origin;
    private Coordinate end;
    private Coordinate center;
    private Product productInfo;
    private Container containerInfo;
    private boolean rotated;
    private long area;

    /**
     * Constructor for ObjectAreaInfo, it creates a product with given center coordinate by
     * calculating the origin and end coordinates
     * @param productInfo Product standard information
     * @param center Coordinate center of the product area
     * @param rotated boolean if the product is rotated
     */
    public ObjectAreaInfo(Product productInfo, Coordinate center, boolean rotated) {
        if(rotated){
            this.origin = Coordinate.builder()
                    .x((int) (center.getX() - productInfo.getWidth() / 2))
                    .y((int) (center.getY() - productInfo.getLength() / 2))
                    .z((int) (center.getZ() - productInfo.getHeight() / 2))
                    .build();
            this.end = Coordinate.builder()
                    .x((int) (center.getX() + productInfo.getWidth() / 2))
                    .y((int) (center.getY() + productInfo.getLength() / 2))
                    .z((int) (center.getZ() + productInfo.getHeight() / 2))
                    .build();
        }
        else {
            this.origin = Coordinate.builder()
                    .x((int) (center.getX() - productInfo.getLength() / 2))
                    .y((int) (center.getY() - productInfo.getWidth() / 2))
                    .z((int) (center.getZ() - productInfo.getHeight() / 2))
                    .build();
            this.end = Coordinate.builder()
                    .x((int) (center.getX() + productInfo.getLength() / 2))
                    .y((int) (center.getY() + productInfo.getWidth() / 2))
                    .z((int) (center.getZ() + productInfo.getHeight() / 2))
                    .build();
        }
        this.center = center;
        this.productInfo = productInfo;
        this.rotated = rotated;
    }

    //TODO : Change to take generic entity type.
    public ObjectAreaInfo(Container productInfo, Coordinate center, boolean rotated) {
        if(rotated){
            this.origin = Coordinate.builder()
                    .x((int) (center.getX() - productInfo.getWidth() / 2))
                    .y((int) (center.getY() - productInfo.getLength() / 2))
                    .z((int) (center.getZ() - productInfo.getHeight() / 2))
                    .build();
            this.end = Coordinate.builder()
                    .x((int) (center.getX() + productInfo.getWidth() / 2))
                    .y((int) (center.getY() + productInfo.getLength() / 2))
                    .z((int) (center.getZ() + productInfo.getHeight() / 2))
                    .build();
        }
        else {
            this.origin = Coordinate.builder()
                    .x((int) (center.getX() - productInfo.getLength() / 2))
                    .y((int) (center.getY() - productInfo.getWidth() / 2))
                    .z((int) (center.getZ() - productInfo.getHeight() / 2))
                    .build();
            this.end = Coordinate.builder()
                    .x((int) (center.getX() + productInfo.getLength() / 2))
                    .y((int) (center.getY() + productInfo.getWidth() / 2))
                    .z((int) (center.getZ() + productInfo.getHeight() / 2))
                    .build();
        }
        this.center = center;
        this.containerInfo = productInfo;
        this.rotated = rotated;
    }
    /**
     * Create ObjectAreaInfo from ReportResultProduct
     *
     */
    public ObjectAreaInfo(ReportResultProduct reportResultProduct, Product product){
        this.productInfo = product;
        this.center = new Coordinate((int) reportResultProduct.getX(), (int) reportResultProduct.getY(), Math.toIntExact(reportResultProduct.getZ()));
        this.rotated = reportResultProduct.isRotate();
        if(this.rotated){
            this.origin = Coordinate.builder()
                    .x((int) (this.center.getX() - this.productInfo.getWidth() / 2))
                    .y((int) (this.center.getY() - this.productInfo.getLength() / 2))
                    .z((int) (this.center.getZ() - this.productInfo.getHeight() / 2))
                    .build();
            this.end = Coordinate.builder()
                    .x((int) (this.center.getX() + this.productInfo.getWidth() / 2))
                    .y((int) (this.center.getY() + this.productInfo.getLength() / 2))
                    .z((int) (this.center.getZ() + this.productInfo.getHeight() / 2))
                    .build();
        }
        else {
            this.origin = Coordinate.builder()
                    .x((int) (this.center.getX() - this.productInfo.getLength() / 2))
                    .y((int) (this.center.getY() - this.productInfo.getWidth() / 2))
                    .z((int) (this.center.getZ() - this.productInfo.getHeight() / 2))
                    .build();
            this.end = Coordinate.builder()
                    .x((int) (this.center.getX() + this.productInfo.getLength() / 2))
                    .y((int) (this.center.getY() + this.productInfo.getWidth() / 2))
                    .z((int) (this.center.getZ() + this.productInfo.getHeight() / 2))
                    .build();
        }
        this.calcArea();
    }
    /**
     * Calculate the area of the product area
     * @return long area of the product with x, y
     */
    //TODO : Add height later
    public long calcArea() {
        return this.area = (long) (this.end.getX() - this.origin.getX()) * ( this.end.getY()-this.origin.getY() );// * ( this.end.getZ()-this.origin.getZ() );
    }

    /**
     * Check if the product area intersects with another product area
     * @param objectAreaInfo
     * @return true if the product area intersects with another product area
     */
    // TODO : Add height check later
    public boolean checkIntersection( @NotNull ObjectAreaInfo objectAreaInfo) {
        long x1, y1, w1, l1;
        long x2, y2, w2, l2;

        if (!this.rotated) {
            x1 = this.origin.getX();
            y1 = this.origin.getY();
            w1 = this.productInfo.getLength();
            l1 =  this.productInfo.getWidth();
        } else {
            x1 = this.origin.getX();
            y1 = this.origin.getY();
            w1 = this.productInfo.getWidth();
            l1 = this.productInfo.getLength();
        }
        if (!objectAreaInfo.rotated) {
            x2 = objectAreaInfo.getOrigin().getX();
            y2 = objectAreaInfo.getOrigin().getY();
            w2 = objectAreaInfo.getProductInfo().getLength();
            l2 = objectAreaInfo.getProductInfo().getWidth();
        } else {
            x2 = objectAreaInfo.getOrigin().getX();
            y2 = objectAreaInfo.getOrigin().getY();
            w2 = objectAreaInfo.getProductInfo().getWidth();
            l2 = objectAreaInfo.getProductInfo().getLength();
        }

        long tx = x1;
        long ty = y1;
        long ax = x2;
        long ay = y2;

        long tw = w1;
        long tl = l1;
        long rw = w2;
        long rl = l2;

        tw += tx;
        tl += ty;
        rw += ax;
        rl += ay;

        return ((rw < ax || rw > tx) &&
                (rl < ay || rl > ty) &&
                (tw < tx || tw > ax) &&
                (tl < ty || tl > ay));
    }

    /**
     * Increase the x coordinate of the product area
     * @param x
     * @param rotated
     * @return ObjectAreaInfo
     */
    public ObjectAreaInfo increaseX(long x, boolean rotated){
        return new ObjectAreaInfo(
                this.productInfo,
                Coordinate.builder()
                        .x(this.center.getX() + x)
                        .y(this.center.getY())
                        .z(this.center.getZ())
                        .build(),
                rotated
        );
    }
    /**
     * Increase the y coordinate of the product area
     * @param y
     * @param rotated
     * @return ObjectAreaInfo
     */
    public ObjectAreaInfo increaseY(long y, boolean rotated){
        return new ObjectAreaInfo(
                this.productInfo,
                Coordinate.builder()
                        .x(this.center.getX())
                        .y(this.center.getY() + y)
                        .z(this.center.getZ())
                        .build(),
                rotated
        );
    }

    /**
     * Increase the z coordinate of the product area
     * @param z
     * @param rotated
     * @return ObjectAreaInfo
     */
    public ObjectAreaInfo increaseZ(long z, boolean rotated){
        return new ObjectAreaInfo(
                this.productInfo,
                Coordinate.builder()
                        .x(this.center.getX())
                        .y(this.center.getY())
                        .z(this.center.getZ() + z)
                        .build(),
                rotated
        );
    }
}
