import {ReportResult} from "@/app/types/ReportResult";
import {useRecoilState} from "recoil";
import {productState} from "@/app/atom/atom";
import {useEffect, useState} from "react";
import axios from "axios";

export function useCreateReport(): ReportResult[] {
    const [productInfo, setProductInfo] = useRecoilState(productState);
    const [reportResults, setReportResults] = useState<ReportResult[]>([]);
    const reducingFactor: number = 10;

    useEffect(() => {
        if (productInfo && productInfo.productId) {
            axios.post("http://localhost:8080/api/reports/createReport", {
                productId: productInfo.productId=1,
                marginSetting: 0,
                exceedLengthSetting: 0,
            })
                .then((response: any) => {
                    console.log(response.data);
                    setReportResults(response.data);
                })
                .catch((error: any) => {
                    console.error("Error creating report:", error);
                });
        }
    }, [productInfo]);

    return (
        reportResults
    );
}