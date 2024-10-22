import { atom } from "recoil";

export const detailState = atom<any>({
    key: "detail",
    default: null,
});

export const palletState = atom<any>({
    key: "pallet",
    default: null,
});

export const productState = atom<any>({
    key: "product",
    default: null,
});

export const triggerState = atom({
    key: 'triggerState',
    default: 0,
});