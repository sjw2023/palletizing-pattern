"use client"
import React from "react";
import PatternAndContainer from "@/app/components/PatternAndContainer";

export default function Home() {
    return (
        <div className={'flexContainer'}
             style={{display: 'flex', width: '100vw', height: '100vh'}}>
            <div style={{width: '100%', height: '100%'}}>
                <PatternAndContainer/>
            </div>
        </div>

    );
}
