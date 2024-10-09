import React, { useEffect, useState } from "react";
import { Product } from "@/app/types/Product";

function ProductList() {
    const [products, setProducts] = useState<Product[]>([]);

    useEffect(() => {
        fetch('http://192.168.20.66:8080/api/products', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((response) => response.json())
            .then((data) => {
                if (Array.isArray(data)) {
                    setProducts(data);
                } else {
                    console.error('Fetched data is not an array:', data);
                }
            })
            .catch((error) => console.error('Error fetching products:', error));
    }, []);

    return (
        <div>
            {products.map((product: Product) => (
                <div key={product.id} style={{
                    border: '1px solid black',
                    backgroundColor: 'yellowgreen',
                }}>
                    <h3>Name: {product.name}</h3>
                    <p>Width: {product.width}</p>
                    <p>Length: {product.length}</p>
                    <p>Height: {product.height}</p>
                    <p>Volume: {product.volume}</p>
                </div>
            ))}
        </div>
    );
}

export default ProductList;