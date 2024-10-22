import React, { useEffect, useState } from "react";
import {useRecoilState, useRecoilValue} from "recoil";
import {palletState, productState, triggerState} from "@/app/atom/atom";
import axios from "axios";
import PatternCanvas from "./PatternCanvas";
import CanvasSection from "./CanvasSection";
import {Pallet} from "@/app/types/Pallet";
import {ReportResult} from "@/app/types/ReportResult";
import * as THREE from "three";

function adjustRatio({reportResponse, productDimensions, reducingFactor}: {
    reportResponse: any,
    productDimensions: any,
    reducingFactor: number
}) {
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
    const [reportResults, setReportResults] = useState<ReportResult[]>([]);
    const [selectedReportId, setSelectedReportId] = useState<number | null>(null);
    const [detail, setDetail] = useState<any>(null);
    const [container, setContainer] = useState<any>(null);
    const [palletInfo, setPalletInfo] = useState<any>(null);
    const [productInfo, setProductInfo] = useState<any>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const trigger = useRecoilValue(triggerState);
    const [selectedProductInfo,setSelectedProuctInfo] = useRecoilState(productState);
    const [pallets, setPallets] = useRecoilState(palletState);
    const baseUrl = "http://localhost:8080/api";

    useEffect(() => {
        const fetchReportResults = async () => {
            setLoading(true);
            try {
                const productId = selectedProductInfo?.productId ?? 1; // Use nullish coalescing to provide a default value
                const response = await axios.post(
                    'http://localhost:8080/api/reports/createReport',
                    {
                        productId: productId,
                        marginSetting: 0,
                        exceedLengthSetting: 0,
                    }
                );
                setReportResults(response.data);
            } catch (error: any) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };
        fetchReportResults();
    }, [trigger]);

    const handleReportClick = async (reportResult: ReportResult) => {
        setSelectedReportId(reportResult.reportResultId);
        const modifiedPallets: Pallet[] = reportResult.reportResultPallets.map((pallet: any) => {
            console.log(pallet);
            return {
                ...pallet,
                x: pallet.x / (reducingFactor),
                y: pallet.z / (reducingFactor),
                z: pallet.y / (reducingFactor),
            }
        });
        setPallets(modifiedPallets);
        try {
            const productResponse = await axios.get(
                baseUrl + `/products/${reportResult.usedProduct}`
            );
            setProductInfo(productResponse.data);
            const productDimensions = productResponse.data;
            const containerResponse = await axios.get(
                baseUrl + `/containers/${reportResult.usedContainer}`
            );
            const palletResponse = await axios.get(
                baseUrl + `/pallets/${reportResult.usedPallet}`
            );
            setPalletInfo(palletResponse.data);
            const adjustedBoxes = adjustRatio({
                reportResponse: {data: reportResult},
                productDimensions: productDimensions,
                reducingFactor
            });
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
        : { width: 0, height: 0, length: 0 };

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
            <div style={{ overflow: "auto", width: "100%", height: "100%" }}>
                <table>
                    <tbody style={{ width: "100%" }}>
                    {reportResults.map((reportResult: ReportResult) => (
                        <tr
                            key={reportResult.reportResultId}
                            style={{
                                backgroundClip: "border-box",
                                backgroundColor: selectedReportId === reportResult.reportResultId ? "lightgray" : "white",
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
                            <td>
                                패턴명 : {reportResult.patternType}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <div style={{ flexDirection: "column", width: "100%" }}>
                <div style={{ flex: "auto", width: "50%", height: "50%" }}>
                    {<PatternCanvas boxes={detail}
                        palletInfo={palletInfo}
                    />}
                </div>
                <div style={{ flex: "auto", width: "100%", height: "50%" }}>
                    <CanvasSection
                        boxes={detail}
                        reducedDimensions={reducedDimensions}
                        cameraPosition={cameraPosition}
                        zoom={zoom}
                        farFactor={farFactor}
                        palletInfo={palletInfo}
                    />
                </div>
            </div>
            <div style={{ flex: "auto", width: "100%" }}>
                <div style={{ backgroundColor: "brown" }}>
                    <h1>Container</h1>
                    <p>Width: {container?.width}</p>
                    <p>Height: {container?.height}</p>
                    <p>Length: {container?.length}</p>
                </div>
                <div style={{ backgroundColor: "blueviolet" }}>
                    <h1>Pallet</h1>
                    <p>Width: {palletInfo?.width}</p>
                    <p>Height: {palletInfo?.height}</p>
                    <p>Length: {palletInfo?.length}</p>
                </div>
                <div style={{ backgroundColor: "yellowgreen" }}>
                    <h1>Product</h1>
                    <p>Width: {productInfo?.width}</p>
                    <p>Height: {productInfo?.height}</p>
                    <p>Length: {productInfo?.length}</p>
                </div>
            </div>
        </div>
    );
}

export { ReportResultList as default };