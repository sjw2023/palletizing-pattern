import {DataType} from "csstype";
import {Box} from "@/app/types/Box";

export type Result = {
    id: number;
    containerAreaEfficiency: number;
    patternAreaEfficiency: number;
    totalProducts: number;
    numberOfLayers: number;
    usedPallet: number;
    usedContainer: number;
    usedProduct: number;
    reportResultPallets: [];
    reportResultProducts: Box[];
}