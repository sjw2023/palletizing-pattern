"use client"
import React, {useState} from "react";
import {RecoilRoot, useRecoilState} from "recoil";
import Container from "@/app/Container";

export default function Home() {

    return (
    <RecoilRoot>
        <Container/>
    </RecoilRoot>
    );
}
