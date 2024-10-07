"use client"
import React, {useState} from "react";
import PatternAndContainer from "@/app/components/PatternAndContainer";
import ProductList from "@/app/components/ProductList";
import ContainerList from "@/app/components/ContainerList";
import PalletList from "@/app/components/PalletList";
import PatternCanvas from "@/app/components/PatternCanvas";
import ReportResultList from "@/app/components/ReportResultList";
import {RecoilRoot, useRecoilState} from "recoil";
import {detailState} from "@/app/atom/atom";
import Container from "@/app/Container";

export default function Home() {

    return(
    <RecoilRoot>
      <Container />
    </RecoilRoot>
    );
}
