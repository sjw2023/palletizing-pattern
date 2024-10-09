import { atom } from "recoil";

export const detailState = atom<any>({
    key: "detail",
    default: null,
});

export const palletState = atom<any>({
    key: "pallet",
    default: null,
});