import React, { useEffect, useState } from "react";
import { Product } from "@/app/types/Product";
import { useRecoilState } from "recoil";
import { productState, triggerState } from "@/app/atom/atom";

function ProductList() {
    const [products, setProducts] = useState<Product[]>([]);
    const [name, setName] = useState('');
    const [width, setWidth] = useState(0);
    const [length, setLength] = useState(0);
    const [height, setHeight] = useState(0);
    const [productInfo, setProductInfo] = useRecoilState(productState);
    const [selectedProductId, setSelectedProductId] = useState<number | null>(null);
    const [trigger, setTrigger] = useRecoilState(triggerState);

    const handleClickProduct = (product: Product) => {
        setProductInfo(product);
        setSelectedProductId(product.productId);
    };

    const fetchProducts = () => {
        fetch('http://localhost:8080/api/products', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((response) => response.json())
            .then((data) => {
                if (Array.isArray(data)) {
                    setProductInfo(data[0]);
                    setProducts(data);
                } else {
                    console.error('Fetched data is not an array:', data);
                }
            })
            .catch((error) => console.error('Error fetching products:', error));
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    const requestCreateProduct = () => {
        fetch('http://localhost:8080/api/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: name,
                used: true,
                productVolume: width * length * height,
                width: width,
                length: length,
                height: height,
            }),
        })
            .then((response) => {
                if (response.ok) {
                    window.location.reload(); // Refresh the page
                    return response.json();
                } else {
                    throw new Error('Failed to create product');
                }
            })
            .then((data) => {
                console.log('Created product:', data);
            })
            .catch((error) => console.error('Error creating product:', error));
    };

    const handleCreateReport = () => {
        setTrigger(prev => prev + 1); // Update the trigger state
    };

    return (
        <div>
            <div>
                <label>Name</label>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
            </div>
            <div>
                <label>Width</label>
                <input type="number" value={width} onChange={(e) => setWidth(Number(e.target.value))} />
            </div>
            <div>
                <label>Length</label>
                <input type="number" value={length} onChange={(e) => setLength(Number(e.target.value))} />
            </div>
            <div>
                <label>Height</label>
                <input type="number" value={height} onChange={(e) => setHeight(Number(e.target.value))} />
            </div>
            <div>
                <button onClick={requestCreateProduct}>
                    Add Product
                </button>
            </div>
            <div>
                {products.map((product: Product) => (
                    <div
                        onClick={() => handleClickProduct(product)}
                        key={product.productId}
                        style={{
                            border: '1px solid black',
                            backgroundColor: selectedProductId === product.productId ? 'yellowgreen' : 'green',
                        }}>
                        <h3>Name: {product.name}</h3>
                        <p>Width: {product.width}</p>
                        <p>Length: {product.length}</p>
                        <p>Height: {product.height}</p>
                        <p>Volume: {product.volume}</p>
                        <button onClick={handleCreateReport}>
                            Create Pattern
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ProductList;