package com.wsapoa.utils.container;

import com.wsapoa.entity.Pallet;
import com.wsapoa.entity.Product;
import com.wsapoa.entity.ReportResult;

import java.awt.*;

public class InterlockProductList extends ProductList {
    public InterlockProductList(Product productInfo, Pallet palletInfo) {
        super(productInfo, palletInfo);
    }
}
