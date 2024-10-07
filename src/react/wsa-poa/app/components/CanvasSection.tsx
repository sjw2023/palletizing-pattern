import React from "react";
import * as THREE from "three";
import {Canvas} from "@react-three/fiber";
import {OrbitControls} from "@react-three/drei";
import Container from "@/app/components/Container";
import BoxDraw from "@/app/components/BoxDraw";
import {Box} from "@/app/types/Box";
import {Pallet} from "@/app/types/Pallet";

interface CanvasSectionProps {
    boxes: Box[];
    pallets: Pallet[];
    reducedDimensions: { width: number; height: number; length: number };
    cameraPosition: THREE.Vector3;
    zoom: number;
    farFactor: number;
}
function CanvasSection({ boxes, pallets, reducedDimensions, cameraPosition, zoom, farFactor }: CanvasSectionProps) {
    console.log("Pallets : ", pallets);
    console.log(boxes)
    return (
        <div style={{flexDirection: 'row', width: '100%', height: '100%'}}>
            <div style={{width: '100%'}}>
                <label style={{textAlign: 'center', fontSize: '20px'}}>Container</label>
            </div>
            <Canvas orthographic camera={{zoom: zoom, position: cameraPosition, near: 0.1, far: farFactor}}
                    style={{width: '100%', height: '100%'}}>
                <ambientLight/>
                <pointLight position={[10, 10, 10]}/>
                <axesHelper args={[100]} position={[0, 0, 0]}/>
                <gridHelper args={[500, 50]} position={[0, 0, 0]}/>
                <OrbitControls/>
                <Container center={[0, reducedDimensions.height / 2, 0]}
                           dimensions={[reducedDimensions.width, reducedDimensions.height, reducedDimensions.length]}/>
                {
                    pallets?.map((pallet, index) => (
                        <group key={index}
                               position={new THREE.Vector3(pallet.x / 10 - 150, pallet.y / 10 - 115, pallet.z / 10 - 650)}>
                            {
                                boxes.map((box, boxIndex) => (
                                    <BoxDraw key={boxIndex} center={[box.x, box.y, box.z]}
                                             dimensions={[box.width, box.height, box.length]} rotate={box.rotate}
                                             orderIndex={box.orderIndex} color={box.color}/>))
                            }
                        </group>
                    ))
                }
            </Canvas>
        </div>
    );
}

export default CanvasSection;