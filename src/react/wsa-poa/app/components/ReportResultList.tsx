import React, {useEffect, useState} from "react";
import axios from "axios";
import {ReportResult} from "@/app/types/ReportResult";
import {useRecoilState} from "recoil";
import {detailState, palletState} from "@/app/atom/atom";
import PatternCanvas from "@/app/components/PatternCanvas";
import CanvasSection from "@/app/components/CanvasSection";
import * as THREE from "three";
import {Container} from "@/app/types/Container";
import {Pallet} from "@/app/types/Pallet";

function adjustRatio({reportResponse, productDimensions, reducingFactor}: { reportResponse: any, productDimensions: any, reducingFactor: number }) {
    return reportResponse.data.reportResultProducts.map((box: any) => {
        let width = productDimensions.width / reducingFactor;
        let length = productDimensions.length / reducingFactor;
        let height = productDimensions.height / reducingFactor;
        if (!box.rotate) {
            [width, length, height] = [height, width, length];
        }
        return {
            ...box,
            width: width,
            height: height,
            length: length,
            x: box.x / reducingFactor,
            y: box.z / reducingFactor,
            z: box.y / reducingFactor,
        };
    });
}
function ReportResultList() {
    const [detail, setDetail] = useRecoilState(detailState);
    const [reportResults, setReportResults] = useState<ReportResult[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [pallets, setPallets] = useRecoilState(palletState);
    const [container, setContainer] = useState<Container | undefined>(
        undefined
    );

    useEffect(() => {
        fetchData();
    }, []);

    async function fetchData() {
        try {
            let reportResponse = await axios
                .post("http://192.168.20.66:8080/api/reports/createReport", {
                    productId: 1,
                    marginSetting: 0,
                    exceedLengthSetting: 0,
                })
                .then((response: any) => {
                    setReportResults(response.data);
                    setLoading(false);
                });
        } catch (error: any) {
            setError(error.message);
            setLoading(false);
        }
    }

    const handleReportClick = async (reportResult: ReportResult) => {
        const modifiedPallets: Pallet[] = reportResult.reportResultPallets.map((pallet: any) => {
            return {
                ...pallet,
                x: pallet.x / (reducingFactor ) ,
                y: pallet.z / (reducingFactor ),
                z: pallet.y / (reducingFactor ),
            }
        })
        setPallets(modifiedPallets);
        try {
            const productResponse = await axios.get(
                "http://192.168.20.66:8080/api/products/1"
            );
            const productDimensions = productResponse.data;
            const containerResponse = await axios.get(
                `http://192.168.20.66:8080/api/containers/${reportResult.usedContainer}`
            );

            const adjustedBoxes = adjustRatio({
                reportResponse: {data: reportResult},
                productDimensions: productDimensions,
                reducingFactor
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

    const reducingFactor = 10;
    const reducedDimensions = container
        ? {
            width: container.width / reducingFactor,
            height: container.height / reducingFactor,
            length: container.length / reducingFactor,
        }
        : {width: 0, height: 0, length: 0};

    const cameraPosition = new THREE.Vector3(3000, 2000, 3000);
    const zoom = 0.6;
    const farFactor = 5000;

    return (
        <div
            style={{
                display: "flex",
                flexDirection: "row",
                width: "100%",
                height: "100%",
            }}
        >
            <div style={{overflow: "auto", width: "50%", height: "100%"}}>
                <table>
                    <tbody>
                    {reportResults.map((reportResult: ReportResult) => (
                        <tr
                            key={reportResult.reportResultId}
                            style={{
                                backgroundClip: "border-box",
                                backgroundColor: "white",
                                border: "1px",
                                borderColor: "black",
                                borderStyle: "solid",
                                borderRadius: "5px",
                                display: "flex",
                                flexDirection: "column",
                            }}
                            onClick={() => handleReportClick(reportResult)}
                        >
                            <td>
                                Container area efficiency:{" "}
                                {reportResult.containerAreaEfficiency}
                            </td>
                            <td>
                                Pattern area efficiency:{" "}
                                {reportResult.patternAreaEfficiency}
                            </td>
                            <td>
                                Number of boxes:{" "}
                                {reportResult.totalProducts}
                            </td>
                            <td>
                                Total layers: {reportResult.numberOfLayers}
                            </td>
                            <td></td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <div style={{flexDirection: "column", width:"100%"}}>
                <div style={{flex: "auto", width: "50%", height: "50%"}}>
                    {<PatternCanvas boxes={detail}/>}
                </div>
                <div style={{flex: "auto", width: "50%", height: "50%"}}>
                    <CanvasSection
                        boxes={detail}
                        reducedDimensions={reducedDimensions}
                        cameraPosition={cameraPosition}
                        zoom={zoom}
                        farFactor={farFactor}
                    />
                </div>
            </div>
        </div>
    );
}

export {ReportResultList as default};
