import {Dropdown, DropdownTrigger, DropdownMenu, DropdownSection, DropdownItem} from "@nextui-org/dropdown";
import {useEffect, useState} from "react";
import {Product} from "@/app/types/Product";
import {Button} from "@nextui-org/react";

export const DropDownButton = () => {
    const [products, setProducts] = useState<Product[]>([]);

    useEffect(() => {
        fetchProducts();
    }, []);
    const fetchProducts = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/products',{
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            const data = await response.json();
            // console.log('Fetched products:', data); // Debug log
            setProducts(data);
            // console.log('Updated products state:', products); // Debug log
            setProducts(data);
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    };
    return (
        <Dropdown >
            <DropdownTrigger>
                <Button variant={"bordered"} color={"primary"} onClick={fetchProducts}>Products</Button>
            </DropdownTrigger>
            <DropdownMenu>
                <DropdownSection>
                    {products.length > 0 ? (
                        products.map((product) => (
                            <DropdownItem key={product.id}>{product.name}</DropdownItem>
                        ))
                    ) : (
                        <DropdownItem>No products available</DropdownItem>
                    )}
                </DropdownSection>
            </DropdownMenu>
        </Dropdown>
    );
};