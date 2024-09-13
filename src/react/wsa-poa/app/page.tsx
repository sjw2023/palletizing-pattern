"use client"
import Test from "./pages/test";
import React from "react";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import mythree from "./pages/mythree/MyThree";
import ProductDropdown from "@/app/components/ProductDropdown";

export default function Home() {
    return (
        // <Router>
        //     <Routes>
        //         <Route path="/mythree" element={mythree} />
        //         {/* Add other routes here */}
        //     </Routes>
        // </Router>
        <div style={{ display: 'flex', width: '100vw', height: '100vh'}}>
            {/*<Test/>*/}
            {/*<MyThree/>*/}
            <div style={{display: 'flex', width:'50%', height:'20%'}}>
                <div style={{width: '70%', height: '100%'}}>
                    <ProductDropdown/>
                    <div style={{width: '30%', height: '100%'}}>
                        <ProductDropdown/>
                    </div>
                </div>
            </div>
        </div>
    );
}
