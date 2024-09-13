import React, {useEffect, useState} from "react";
import {map} from "maath/buffer";

function ProductList() {
    const [products, setProducts] = useState([]);
    useEffect(() => {
            fetch(
                'http://localhost:8080/products', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            )
                .then((response) => response.json())
                .then((data) => setProducts(data));
        },
        []
    )
    ;
    return (
        <div>
            {
                products.map((product) => (
                    <div key = {product.id} >
                        <h3>{product.name} < /h3>
                        < p > {product.description} < /p>
                    </div>
                ))
            }
        </div>
    );
}
