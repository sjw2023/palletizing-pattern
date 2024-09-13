package com.wsapoa.utils.container;

import com.wsapoa.entity.Pallet;
import com.wsapoa.entity.Product;
import com.wsapoa.entity.ReportResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpiralProductList extends ProductList {
    public SpiralProductList(Product productInfo, Pallet palletInfo) {
        super(productInfo, palletInfo);
        this.map = new java.util.ArrayList<>();
    }
}