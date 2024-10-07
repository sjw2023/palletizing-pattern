import React, {useEffect, useState} from "react";
import axios from "axios";
import {ReportResult} from "@/app/types/ReportResult";
import {useRecoilState} from "recoil";
import {detailState} from "@/app/atom/atom";
import PatternCanvas from "@/app/components/PatternCanvas";
import {extracted} from "@/app/components/ContainerCanvas";
import CanvasSection from "@/app/components/CanvasSection";
import * as THREE from "three";
import {Container} from "@/app/types/Container";
import {Pallet} from "@/app/types/Pallet";

function ReportResultList() {
    const [detail, setDetail] = useRecoilState(detailState);
    const [reportResults, setReportResults] = useState<ReportResult[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [pallets, setPallets] = useState<Pallet[]>([]);
    const [container, setContainer] = useState<Container | undefined>(undefined);

    useEffect(() => {
        fetchData();
    }, []);

    async function fetchData() {
        try {
            let reportResponse = await axios.post('http://localhost:8080/api/reports/createReport', {
                productId: 1,
                marginSetting: 0,
                exceedLengthSetting: 0,
            }).then((response: any) => {
                console.log(response.data)
                // console.log("Report Response:", reportResponse.data); // Log report response
                setReportResults(response.data);
                // const adjustedPallets = reportResponse.data.reportResultPallets.map((pallet: any) => ({
                //     ...pallet,
                //     width: pallet.width,
                //     height: pallet.height,
                //     length: pallet.length,
                //     x: pallet.x / 10,
                //     y: pallet.z / 10,
                //     z: pallet.y / 10
                // }));
                // console.log("Report result pallets : ",reportResponse.data.reportResultPallets)
                // reportResponse.data.map
                setPallets(response.data.reportResultPallets)
            })
        } catch (error: any) {
            setError(error.message);
            setLoading(false);
        }
    }

    const handleReportClick = async (reportResult: ReportResult) => {
        try {
            const productResponse = await axios.get('http://localhost:8080/api/products/1');
            const productDimensions = productResponse.data;
            const containerResponse = await axios.get('http://localhost:8080/api/containers/1');
            const adjustedBoxes = extracted({
                reportResponse: {data: reportResult},
                productDimensions: productDimensions
            });
            // console.log("Adjusted Boxes:", adjustedBoxes); // Log adjusted boxes
            setDetail(adjustedBoxes);
            setContainer(containerResponse.data);
        } catch (error: any) {
            setError(error.message);
        }
    };

    if (error) {
        return (
            <div>
                <p>Error: {error}</p>
            </div>
        );
    }

    if (loading) {
        return (
            <div>
                <p>Loading...</p>
            </div>
        );
    }

    const reducedDimensions = container ? {
        width: container.width / 10,
        height: container.height / 10,
        length: container.length / 10
    } : {width: 0, height: 0, length: 0};

    const cameraPosition = new THREE.Vector3(3000, 2000, 3000);
    const zoom = 0.6;
    const farFactor = 5000;

    return (
        <div style={{display: 'flex', flexDirection: 'row', width: '100%', height: '100%'}}>
            <div style={{overflow: 'auto', width: '50%', height: '100%'}}>
                <table>
                    <tbody>
                    {(reportResults.map((reportResult: ReportResult) => (
                            <tr
                                key={reportResult.reportResultId}
                                style={{
                                    backgroundClip: 'border-box',
                                    backgroundColor: 'white',
                                    border: '1px',
                                    borderColor: 'black',
                                    borderStyle: 'solid',
                                    borderRadius: '5px',
                                    display: 'flex',
                                    flexDirection: 'column',
                                }}
                                onClick={() => handleReportClick(reportResult)}
                            >
                                <td>Container area efficiency: {reportResult.containerAreaEfficiency}</td>
                                <td>Pattern area efficiency: {reportResult.patternAreaEfficiency}</td>
                                <td>Number of boxes: {reportResult.totalProducts}</td>
                                <td>Total layers: {reportResult.numberOfLayers}</td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
            <div style={{flex: 'auto', width: '50%', height: '50%'}}>
                {<PatternCanvas boxes={detail}/>}
            </div>
            <div style={{flex: 'auto', width: '50%', height: '50%'}}>
                <CanvasSection boxes={detail} pallets={pallets} reducedDimensions={reducedDimensions}
                               cameraPosition={cameraPosition} zoom={zoom} farFactor={farFactor}/>
            </div>
        </div>
    );
}

export {ReportResultList as default};