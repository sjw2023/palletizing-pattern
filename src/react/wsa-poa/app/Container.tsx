"use client"
import React, {useState} from "react";
import ProductList from "@/app/components/ProductList";
import ContainerList from "@/app/components/ContainerList";
import PalletList from "@/app/components/PalletList";
import ReportResultList from "@/app/components/ReportResultList";

export default function Container() {
    return (
        // eslint-disable-next-line @next/next/no-sync-scripts
        <div
            style={{display: 'flex', flexDirection: 'row', width: '100vw', height: '100vh'}}>
            <div style={{display: 'flex', flexDirection: 'column', width: '50%', height: '100%'}}>
                <div style={{width: '100%'}}>
                    <div className={'list-header'} style={{
                        border: '2px solid black',
                        backgroundColor: 'cornflowerblue',
                    }}>
                        <h1 style={{
                            fontSize: '2em',
                        }}>
                            Products
                        </h1>
                    </div>
                    <ProductList/>
                </div>
                <div style={{width: '100%'}}>
                    <div className={'list-header'} style={{
                        border: '2px solid black',
                        backgroundColor: 'cornflowerblue',
                    }}>
                        <h1 style={{
                            fontSize: '2em',
                        }}>
                            Containers
                        </h1>
                    </div>
                    <ContainerList/>
                </div>
                <div style={{width: '100%'}}>
                    <div className={'list-header'} style={{
                        border: '2px solid black',
                        backgroundColor: 'cornflowerblue',
                    }}>
                        <h1 style={{
                            fontSize: '2em',
                        }}>
                            Pallets
                        </h1>
                    </div>
                    <PalletList/>
                </div>
            </div>
            <div style={{flexDirection: 'row', width: '100%', height: '100%'}}>
                <div className={'list-header'} style={{
                    border: '2px solid black',
                    backgroundColor: 'cornflowerblue',
                }}>
                    <h1 style={{
                        fontSize: '2em',
                    }}>Report result</h1>
                </div>
                <ReportResultList/>
            </div>
        </div>
    )
}