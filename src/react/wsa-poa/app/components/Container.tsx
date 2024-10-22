import * as THREE from "three";
import React from "react";

function Container({ center, dimensions, color = "lightblue" }: { center: number[], dimensions: number[], color?: string }) {
    const planes = [
        { position: [0, 0, -dimensions[2] / 2] as [number, number, number], rotation: [0, 0, 0] as [number, number, number] }, // Front
        { position: [0, 0, dimensions[2] / 2] as [number, number, number], rotation: [0, Math.PI, 0] as [number, number, number] }, // Back
        { position: [-dimensions[0] / 2, 0, 0] as [number, number, number], rotation: [0, Math.PI / 2, 0] as [number, number, number] }, // Left
        { position: [0, -dimensions[1] / 2, 0] as [number, number, number], rotation: [-Math.PI / 2, 0, 0] as [number, number, number] } // Bottom
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
export default Container;