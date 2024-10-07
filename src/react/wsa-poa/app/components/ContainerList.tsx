import React, { useEffect, useState } from "react";
import { Product } from "@/app/types/Product";
import {Container} from "@/app/types/Container";

function ContainerList() {
    const [containers, setContainers] = useState<Container[]>([]);

    useEffect(() => {
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
    }, []);

    return (
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
    );
}

export default ContainerList;