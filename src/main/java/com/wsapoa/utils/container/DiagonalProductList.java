package com.wsapoa.utils.container;

import com.wsapoa.entity.Pallet;
import com.wsapoa.entity.Product;
import com.wsapoa.entity.ReportResult;

import java.awt.*;
import java.util.ArrayList;

public class DiagonalProductList extends ProductList {
    public DiagonalProductList(Product productInfo, Pallet palletInfo) {
        super(productInfo, palletInfo);
        map = new ArrayList<>();
    }
}