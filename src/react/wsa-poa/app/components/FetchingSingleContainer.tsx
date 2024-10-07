import axios from "axios";
import { useState, useEffect } from "react";
import PatternCanvas from "@/app/components/PatternCanvas";
import { Box } from "@/app/types/Box";
import { Result } from "@/app/types/Result";
import { Product } from "@/app/types/Product";

function FetchingSingleContainer(props: { id: number }) {
    const [result, setResult] = useState<Result | null>(null);
    const [productInfo, setProductInfo] = useState<Product | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await axios.get(`http://localhost:8080/api/reports/${props.id}`);
                const productInfoResponse = await axios.get(`http://localhost:8080/api/products/1`);
                setResult(response.data);
                setProductInfo(productInfoResponse.data);
                setLoading(false);
            } catch (error) {
                console.error("Error fetching data:", error);
                setLoading(false);
            }
        }
        if (props.id) {
            fetchData();
        }
    }, [props.id]);

    useEffect(() => {
        if (result && productInfo) {
            const updatedProducts = result.reportResultProducts.map(box => ({
                ...box,
                width: productInfo.width,
                height: productInfo.height,
                length: productInfo.length,
                color: 'blue'
            }));
            setResult(prevResult => prevResult ? { ...prevResult, reportResultProducts: updatedProducts } : null);
        }
    }, [productInfo]); // Only depend on productInfo to avoid infinite loop

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            {result && <PatternCanvas boxes={result.reportResultProducts as Box[]} />}
        </div>
    );
}

export default FetchingSingleContainer;