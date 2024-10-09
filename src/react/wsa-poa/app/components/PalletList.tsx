import React, { useEffect, useState } from "react";
import { Product } from "@/app/types/Product";
import {Container} from "@/app/types/Container";
import {Pallet} from "@/app/types/Pallet";

function PalletList() {
    const [pallets, setPallets] = useState<Pallet[]>([]);

    useEffect(() => {
        fetch('http://192.168.20.66:8080/api/pallets', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((response) => response.json())
            .then((data) => {
                if (Array.isArray(data)) {
                    setPallets(data);
                } else {
                    console.error('Fetched data is not an array:', data);
                }
            })
            .catch((error) => console.error('Error fetching products:', error));
    }, []);

    return (
        <div>
            {pallets.map((pallet: Pallet) => (
                <div key={pallet.id} style={{
                    border: '1px solid black',
                    backgroundColor: 'brown',
                }}>
                    <h1>Name: {pallet.name}</h1>
                    <p>Width: {pallet.width}</p>
                    <p>Length: {pallet.length}</p>
                    <p>Height: {pallet.height}</p>
                </div>
            ))}
        </div>
    );
}

export default PalletList;