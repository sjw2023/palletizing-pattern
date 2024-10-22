import React, {useEffect, useState} from "react";
import {Pallet} from "@/app/types/Pallet";

function PalletList() {
    const [pallets, setPallets] = useState<Pallet[]>([]);
    const [name, setName] = useState('');
    const [width, setWidth] = useState(0);
    const [length, setLength] = useState(0);
    const [height, setHeight] = useState(0);

    const fetchPallets = () => {
        fetch('http://localhost:8080/api/pallets', {
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
            .catch((error) => console.error('Error fetching pallets:', error));
    };

    useEffect(() => {
        fetchPallets();
    }, []);

    const requestCreate = () => {
        fetch('http://localhost:8080/api/pallets', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: name,
                used: true,
                palletVolume: width * length * height,
                width: width,
                length: length,
                height: height,
            }),
        })
            .then((response) => {
                if (response.ok) {
                    window.location.reload(); // Refresh the page
                    return response.json();
                } else {
                    throw new Error('Failed to create pallet');
                }
            })
            .then((data) => {
                console.log('Created pallet:', data);
            })
            .catch((error) => console.error('Error creating pallet:', error));
    };

    return (
        <div>
            <div>
                <label>Name</label>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
            </div>
            <div>
                <label>Width</label>
                <input type="number" value={width} onChange={(e) => setWidth(Number(e.target.value))} />
            </div>
            <div>
                <label>Length</label>
                <input type="number" value={length} onChange={(e) => setLength(Number(e.target.value))} />
            </div>
            <div>
                <label>Height</label>
                <input type="number" value={height} onChange={(e) => setHeight(Number(e.target.value))} />
            </div>
            <div>
                <button onClick={requestCreate}>
                    Add Pallet
                </button>
            </div>
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
        </div>
    );
}

export default PalletList;