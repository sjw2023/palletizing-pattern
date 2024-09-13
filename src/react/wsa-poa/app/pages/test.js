"use client";

import React, { useState, useEffect, useRef } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, Text } from '@react-three/drei';
import * as THREE from 'three';
import axios from 'axios';

function Box({ center, dimensions, rotate, orderIndex, color = "#C3AA83" }) {
    const meshRef = useRef(null);
    const edgesRef = useRef(null);

    useEffect(() => {
        if (meshRef.current) {
            meshRef.current.rotation.set(0, 0, 0);
        }
        if (edgesRef.current) {
            edgesRef.current.rotation.set(0, 0, 0);
        }
    }, [rotate]);

    const adjustedDimensions = rotate ? dimensions : [dimensions[1], dimensions[0], dimensions[2]];

    return (
        <group position={new THREE.Vector3(center[0], center[1], center[2])}>
            <mesh ref={meshRef} frustumCulled={false}>
                <boxGeometry args={adjustedDimensions} />
                <meshStandardMaterial color={color} />
            </mesh>
            <lineSegments ref={edgesRef} frustumCulled={false}>
                <edgesGeometry attach="geometry" args={[new THREE.BoxGeometry(...adjustedDimensions)]} />
                <lineBasicMaterial attach="material" color="black" />
            </lineSegments>
            {orderIndex !== undefined && (
                <Text
                    position={[0, 0, 0]}
                    fontSize={10}
                    color="black"
                    anchorX="center"
                    anchorY="middle"
                >
                    {orderIndex}
                </Text>
            )}

        </group>
    );
}

function PatternCanvas({ boxes }) {
    return (
        <Canvas orthographic camera={{ zoom: 1, position: [400, 400, 600] }} style={{ width: '100%', height: '100%' }}>
            <ambientLight />
            <pointLight position={[10, 10, 10]} />
            <axesHelper args={[100]} position={[0, 0, 0]} />
            <gridHelper args={[500, 50]} position={[0, 0, 0]} />
            <OrbitControls />
            {boxes.map((box, index) => (
                <Box
                    key={index}
                    center={[box.x, box.y, box.z]}
                    dimensions={[box.width, box.height, box.length]}
                    rotate={box.rotate}
                    orderIndex={box.orderIndex}
                />
            ))}
        </Canvas>
    );
}

function Container({ center, dimensions, color = "lightblue" }) {
    const planes = [
        { position: [0, 0, -dimensions[2] / 2], rotation: [0, 0, 0] }, // Front
        { position: [0, 0, dimensions[2] / 2], rotation: [0, Math.PI, 0] }, // Back
        { position: [-dimensions[0] / 2, 0, 0], rotation: [0, Math.PI / 2, 0] }, // Left
        { position: [0, -dimensions[1] / 2, 0], rotation: [-Math.PI / 2, 0, 0] } // Bottom
    ];

    return (
        <group position={new THREE.Vector3(...center)}>
            {planes.map((plane, index) => (
                <mesh key={index} position={plane.position} rotation={plane.rotation} frustumCulled={false}>
                    <planeGeometry args={[dimensions[0], dimensions[1]]} />
                    <meshStandardMaterial color={color} side={THREE.DoubleSide} />
                </mesh>
            ))}
            <lineSegments frustumCulled={false}>
                <edgesGeometry attach="geometry" args={[new THREE.BoxGeometry(...dimensions)]} />
                <lineBasicMaterial attach="material" color="black" />
            </lineSegments>
        </group>
    );
}

function ContainerCanvas() {
    const [container, setContainer] = useState(null);
    const [boxesDiagonal, setBoxesDiagonal] = useState([]);
    const [boxesSpiral, setBoxesSpiral] = useState([]);
    const [boxesBlock, setBoxesBlock] = useState([]);
    const [boxesInterlock, setBoxesInterlock] = useState([]);
    const [pallets, setPallets] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchData();
    }, []);
    async function fetchData() {
        try {
            const productResponse = await axios.get('http://localhost:8080/api/products/1');
            const productDimensions = productResponse.data;
            const containerResponse = await axios.get('http://localhost:8080/api/containers/1');

            const patternTypes= ["INTERLOCK"];
            // const patternTypes = ["DIAGONAL", "SPIRAL", "BLOCK", "INTERLOCK"];
            const boxStateSetters = [setBoxesDiagonal, setBoxesSpiral, setBoxesBlock, setBoxesInterlock];
            let reportResponse;
            for (let i = 0; i < patternTypes.length; i++) {
                reportResponse = await axios.post('http://localhost:8080/api/reports', {
                    productId: 1,
                    palletId: 1,
                    containerId: 1,
                    patternId: 1,
                    marginSetting: 0,
                    exceedLengthSetting: 0,
                    patternType: patternTypes[i]
                });

                const adjustedBoxes = reportResponse.data.reportResultProducts.map(box => {
                    let width = productDimensions.width / 10;
                    let length = productDimensions.length / 10;
                    let height = productDimensions.height / 10;
                    if (!box.rotate) {
                        [width, length, height] = [height, width, length];
                    }
                    return {
                        ...box,
                        width: width,
                        height: height,
                        length: length,
                        x: box.x / 10,
                        y: box.z / 10,
                        z: box.y / 10
                    };
                });

                boxStateSetters[i](adjustedBoxes);
            }

            const adjustedPallets = reportResponse.data.reportResultPallets.map(pallet => ({
                ...pallet,
                x: pallet.x / 10,
                y: pallet.z / 10,
                z: pallet.y / 10
            }));

            setContainer(containerResponse.data);
            setPallets(adjustedPallets);
        } catch (error) {
            setError(error.message);
        }
    }
    if (error) {
        return <div>Error: {error}</div>;
    }

    if (!container) {
        return <div>Loading...</div>;
    }

    const reducedDimensions = {
        width: container.width / 10,
        height: container.height / 10,
        length: container.length / 10
    };

    const cameraPosition = new THREE.Vector3(
        3000,
        2000,
        3000
    );

    const zoom = 0.6;
    const farFactor = 5000;

    return (
        <div style={{ display: 'flex', flexDirection: 'column', width: '100vw', height: '100vh' }}>
            <div style={{ width: '100%', height: '25%' }}>
                <Canvas orthographic camera={{ zoom: zoom, position: cameraPosition, near: 0.1, far: farFactor }} style={{ width: '100%', height: '100%' }}>
                    <ambientLight />
                    <pointLight position={[10, 10, 10]} />
                    <axesHelper args={[100]} position={[0, 0, 0]} />
                    <gridHelper args={[500, 50]} position={[0, 0, 0]} />
                    <OrbitControls />
                    <Container center={[0, reducedDimensions.height / 2, 0]} dimensions={[reducedDimensions.width, reducedDimensions.height, reducedDimensions.length]} />
                    {pallets.map((pallet, index) => (
                        <group key={index} position={new THREE.Vector3(pallet.x - 150, pallet.y - 115, pallet.z - 650)}>
                            {boxesDiagonal.map((box, boxIndex) => (
                                <Box key={boxIndex} center={[box.x, box.y, box.z]} dimensions={[box.width, box.height, box.length]} rotate={box.rotate} orderIndex={box.orderIndex} />
                            ))}
                        </group>
                    ))}
                </Canvas>
            </div>
            <div style={{ width: '100%', height: '25%' }}>
                <Canvas orthographic camera={{ zoom: zoom, position: cameraPosition, near: 0.1, far: farFactor }} style={{ width: '100%', height: '100%' }}>
                    <ambientLight />
                    <pointLight position={[10, 10, 10]} />
                    <axesHelper args={[100]} position={[0, 0, 0]} />
                    <gridHelper args={[500, 50]} position={[0, 0, 0]} />
                    <OrbitControls />
                    <Container center={[0, reducedDimensions.height / 2, 0]} dimensions={[reducedDimensions.width, reducedDimensions.height, reducedDimensions.length]} />
                    {pallets.map((pallet, index) => (
                        <group key={index} position={new THREE.Vector3(pallet.x - 150, pallet.y - 115, pallet.z - 650)}>
                            {boxesSpiral.map((box, boxIndex) => (
                                <Box key={boxIndex} center={[box.x, box.y, box.z]} dimensions={[box.width, box.height, box.length]} rotate={box.rotate} orderIndex={box.orderIndex} />
                            ))}
                        </group>
                    ))}
                </Canvas>
            </div>
            <div style={{ width: '100%', height: '25%' }}>
                <Canvas orthographic camera={{ zoom: zoom, position: cameraPosition, near: 0.1, far: farFactor }} style={{ width: '100%', height: '100%' }}>
                    <ambientLight />
                    <pointLight position={[10, 10, 10]} />
                    <axesHelper args={[100]} position={[0, 0, 0]} />
                    <gridHelper args={[500, 50]} position={[0, 0, 0]} />
                    <OrbitControls />
                    <Container center={[0, reducedDimensions.height / 2, 0]} dimensions={[reducedDimensions.width, reducedDimensions.height, reducedDimensions.length]} />
                    {pallets.map((pallet, index) => (
                        <group key={index} position={new THREE.Vector3(pallet.x - 150, pallet.y - 115, pallet.z - 650)}>
                            {boxesBlock.map((box, boxIndex) => (
                                <Box key={boxIndex} center={[box.x, box.y, box.z]} dimensions={[box.width, box.height, box.length]} rotate={box.rotate} orderIndex={box.orderIndex} />
                            ))}
                        </group>
                    ))}
                </Canvas>
            </div>
            <div style={{ width: '100%', height: '25%' }}>
                <Canvas orthographic camera={{ zoom: zoom, position: cameraPosition, near: 0.1, far: farFactor }} style={{ width: '100%', height: '100%' }}>
                    <ambientLight />
                    <pointLight position={[10, 10, 10]} />
                    <axesHelper args={[100]} position={[0, 0, 0]} />
                    <gridHelper args={[500, 50]} position={[0, 0, 0]} />
                    <OrbitControls />
                    <Container center={[0, reducedDimensions.height / 2, 0]} dimensions={[reducedDimensions.width, reducedDimensions.height, reducedDimensions.length]} />
                    {pallets.map((pallet, index) => (
                        <group key={index} position={new THREE.Vector3(pallet.x - 150, pallet.y - 115, pallet.z - 650)}>
                            {boxesInterlock.map((box, boxIndex) => (
                                <Box key={boxIndex} center={[box.x, box.y, box.z]} dimensions={[box.width, box.height, box.length]} rotate={box.rotate} orderIndex={box.orderIndex} />
                            ))}
                        </group>
                    ))}
                </Canvas>
            </div>
        </div>
    );
}

export default function Test() {
    const [boxes, setBoxes] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchBoxes() {
            try {
                const productResponse = await axios.get('http://localhost:8080/api/products/1');
                const productDimensions = productResponse.data;

                const response = await axios.post('http://localhost:8080/api/reports', {
                    productId: 1,
                    palletId: 1,
                    containerId: 1,
                    patternId: 1,
                    marginSetting: 0,
                    exceedLengthSetting: 0,
                    patternType: "INTERLOCK",
                });

                const adjustedBoxes = response.data.reportResultProducts.map(box => {
                    let width = productDimensions.width / 10;
                    let length = productDimensions.length / 10;
                    let height = productDimensions.height / 10;
                    if (!box.rotate) {
                        [width, length, height] = [height, width, length];
                    }
                    return {
                        ...box,
                        width: width,
                        height: height,
                        length: length,
                        x: box.x / 10,
                        y: box.z / 10,
                        z: box.y / 10
                    };
                });
                setBoxes(adjustedBoxes);
            } catch (error) {
                setError(error.message);
            }
        }
        fetchBoxes();
    }, []);

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div style={{ display: 'flex', width: '100vw', height: '100vh' }}>
            <div style={{ width: '50%', height: '100%' }}>
                <PatternCanvas boxes={boxes} />
            </div>
            <div style={{ width: '50%', height: '100%' }}>
                <ContainerCanvas />
            </div>
        </div>
    );
}