import React, {useEffect, useState} from "react";
import {Container} from "@/app/types/Container";

function ContainerList() {
    const [containers, setContainers] = useState<Container[]>([]);
    const [name, setName] = useState('');
    const [width, setWidth] = useState(0);
    const [length, setLength] = useState(0);
    const [height, setHeight] = useState(0);

    const fetchContainers = () => {
        fetch('http://localhost:8080/api/containers', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then((response) => response.json())
            .then((data) => {
                if (Array.isArray(data)) {
                    setContainers(data);
                } else {
                    console.error('Fetched data is not an array:', data);
                }
            })
            .catch((error) => console.error('Error fetching products:', error));
    };

    useEffect(() => {
        fetchContainers();
    }, []);

    const requestCreate = () => {
        fetch('http://localhost:8080/api/containers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: name,
                used: true,
                containerVolume: width * length * height,
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
                    throw new Error('Failed to create container');
                }
            })
            .then((data) => {
                console.log('Created container:', data);
            })
            .catch((error) => console.error('Error creating container:', error));
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
                    Add Container
                </button>
            </div>
            <div>
                {containers.map((container: Container) => (
                    <div key={container.id} style={{
                        border: '1px solid black',
                        backgroundColor: 'lightblue',
                    }}>
                        <h3>Name: {container.name}</h3>
                        <p>Width: {container.width}</p>
                        <p>Length: {container.length}</p>
                        <p>Height: {container.height}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ContainerList;