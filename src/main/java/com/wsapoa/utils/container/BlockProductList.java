package com.wsapoa.utils.container;

import com.wsapoa.entity.Pallet;
import com.wsapoa.entity.Product;
import com.wsapoa.entity.ReportResult;

public class BlockProductList extends ProductList {
    public BlockProductList(Product productInfo, Pallet palletInfo
                            ) {
        super(productInfo, palletInfo);
    }
}
