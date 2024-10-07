import React, {useEffect, useState} from "react";
import axios from "axios";
import * as THREE from "three";
import CanvasSection from "@/app/components/CanvasSection";
import {Box} from "@/app/types/Box";
import {Container} from "@/app/types/Container";
import {ReportResult} from "@/app/types/ReportResult";

function extracted({reportResponse, productDimensions}: { reportResponse: any, productDimensions: any }) {
    return reportResponse.data.reportResultProducts.map((box: any) => {
        let width = productDimensions.width / 10;
        let length = productDimensions.length / 10;
        let height = productDimensions.height / 10;
        if (!box.rotate) {
            [width, length, height] = [height, width, length];
        }
        return {
            ...box,
            width: width,
            height: height,
            length: length,
            x: box.x / 10,
            y: box.z / 10,
            z: box.y / 10
        };
    });
}

function ContainerCanvas() {
    const [pallets, setPallets] = useState([]);
    const [error, setError] = useState(null);
    const [container, setContainer] = useState<Container>();

    const [boxesDiagonal, setBoxesDiagonal] = useState<Box[]>([]);
    const [boxesSpiral, setBoxesSpiral] = useState<Box[]>([]);
    const [boxesBlock, setBoxesBlock] = useState<Box[]>([]);
    const [boxesInterlock, setBoxesInterlock] = useState<Box[]>([]);


    useEffect(() => {
        fetchData();
    }, []);

    async function fetchData() {
        try {
            const productResponse = await axios.get('http://localhost:8080/api/products/1');
            const productDimensions = productResponse.data;
            const containerResponse = await axios.get('http://localhost:8080/api/containers/1');

            const patternTypes = ["DIAGONAL", "SPIRAL", "BLOCK", "INTERLOCK"];
            const boxStateSetters = [setBoxesDiagonal, setBoxesSpiral, setBoxesBlock, setBoxesInterlock];
            let reportResponse;
            for (let i = 0; i < patternTypes.length; i++) {
                reportResponse = await axios.post('http://localhost:8080/api/reports/createReport', {
                    productId: 1,
                    marginSetting: 0,
                    exceedLengthSetting: 0,
                });
                const reportResult: ReportResult = reportResponse.data;
                const adjustedBoxes = extracted({reportResponse: reportResponse, productDimensions: productDimensions});
                boxStateSetters[i](adjustedBoxes);
            }

            if (reportResponse) {
                const adjustedPallets = reportResponse.data.reportResultPallets.map((pallet: any) => ({
                    ...pallet,
                    x: pallet.x / 10,
                    y: pallet.z / 10,
                    z: pallet.y / 10
                }));
                setPallets(adjustedPallets);
            }

            setContainer(containerResponse.data);
        } catch (error: any) {
            setError(error.message);
        }
    }

    if (error) {
        return (
            <div>
                <p> Error: {error} </p>
            </div>);
    }

    if (!container) {
        return <div>Loading...</div>;
    }

    const reducedDimensions = {
        width: container.width / 10,
        height: container.height / 10,
        length: container.length / 10
    };

    const cameraPosition = new THREE.Vector3(3000, 2000, 3000);
    const zoom = 0.6;
    const farFactor = 20000;

    return (
        <div style={{display: 'flex', flexDirection: 'column', width: '100vw', height: '100vh'}}>
            {/*<div>*/}
            {/*<label style={{textAlign: 'center', fo ntSize: '20px'}}>Container</label>*/}
            <CanvasSection boxes={boxesDiagonal} pallets={pallets} reducedDimensions={reducedDimensions}
                           cameraPosition={cameraPosition} zoom={zoom} farFactor={farFactor}/>
            {/*</div>*/}
            <CanvasSection boxes={boxesSpiral} pallets={pallets} reducedDimensions={reducedDimensions}
                           cameraPosition={cameraPosition} zoom={zoom} farFactor={farFactor}/>
            <CanvasSection boxes={boxesBlock} pallets={pallets} reducedDimensions={reducedDimensions}
                           cameraPosition={cameraPosition} zoom={zoom} farFactor={farFactor}/>
            <CanvasSection boxes={boxesInterlock} pallets={pallets} reducedDimensions={reducedDimensions}
                           cameraPosition={cameraPosition} zoom={zoom} farFactor={farFactor}/>
        </div>
    );
}

export {extracted, ContainerCanvas as default};