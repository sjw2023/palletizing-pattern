import React, {useEffect, useState} from "react";
import axios from "axios";
import PatternCanvas from "@/app/components/PatternCanvas";
import ContainerCanvas from "@/app/components/ContainerCanvas";
import { extracted } from "@/app/components/ContainerCanvas";

export default function PatternAndContainer() {
    const [boxes, setBoxes] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchBoxes() {
            try {
                const productResponse = await axios.get('http://localhost:8080/api/products/1');
                const productDimensions = productResponse.data;

                const response = await axios.post('http://localhost:8080/api/reports/createReport', {
                    productId: 1,
                    palletId: 1,
                    containerId: 1,
                    patternId: 1,

                    marginSetting: 0,
                    exceedLengthSetting: 0,
                    patternType: "INTERLOCK",
                });

                const adjustedBoxes = extracted({ reportResponse: response, productDimensions: productDimensions });
                setBoxes(adjustedBoxes);
            } catch (error:any) {
                setError(error.message);
            }
        }
        fetchBoxes();
    }, []);

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div style={{ display: 'flex', width: '100vw', height: '100vh' }}>
            <div style={{ width: '50%', height: '100%' }}>
                <PatternCanvas boxes={boxes} />
            </div>
            <div style={{ width: '50%', height: '100%' }}>
                <ContainerCanvas />
            </div>
        </div>
    );
}