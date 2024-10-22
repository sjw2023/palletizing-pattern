// src/components/ProductDropdown.js
import React, {useState, useEffect} from 'react';
import axios from 'axios';

const ProductDropdown = () => {
    const [products, setProducts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState('');

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/products');
                setProducts(response.data);
            } catch (error) {
                console.error('Error fetching products:', error);
            }
        };

        fetchProducts();
    }, []);

    const handleChange = (event) => {
        setSelectedProduct(event.target.value);
    };

    return (
        <select value={selectedProduct} onChange={handleChange} style={{width: '100%', height: '100%'}}>
            <option value="" disabled>Select a product</option>
            {products.map((product) => (
                <option key={product.id} value={product.id}>
                    {product.name}
                </option>
            ))}
        </select>
    );
};

export default ProductDropdown;