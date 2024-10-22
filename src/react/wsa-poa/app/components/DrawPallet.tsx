import * as THREE from "three";
import React from "react";

function DrawPallet({ center, dimensions, color = "yellow" }: { center: THREE.Vector3, dimensions: number[], color?: string }) {
    return (
        <group position={new THREE.Vector3(...center)}>
            <mesh frustumCulled={false}>
                <boxGeometry args={dimensions} />
                <meshStandardMaterial color={color} side={THREE.DoubleSide} />
            </mesh>
            <lineSegments frustumCulled={false}>
                <edgesGeometry attach="geometry" args={[new THREE.BoxGeometry(...dimensions)]} />
                <lineBasicMaterial attach="material" color="black" />
            </lineSegments>
        </group>
    );
}

export default DrawPallet;