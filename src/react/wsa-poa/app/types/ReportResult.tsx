import {Box} from "@/app/types/Box";
import {Pallet} from "@/app/types/Pallet";

export type ReportResult = {
    id: number;
    reportResultId: number;
    patternAreaEfficiency: number;
    productPerLayer: number;
    numberOfPatternsInContainer: number;
    containerAreaEfficiency: number;
    numberOfLayers: number;
    totalProducts:number;
    reportResultProducts: Box[];
    usedContainer: number;
    usedPallet: number;
    usedProduct: number;
    patternType: string;
    reportResultPallets: Pallet[];
}