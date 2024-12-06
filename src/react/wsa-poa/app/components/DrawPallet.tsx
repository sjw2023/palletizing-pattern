import * as THREE from "three";
import React from "react";

function DrawPallet({ center, dimensions, color = "yellow", rotate }: { center: THREE.Vector3, dimensions: number[], color?: string ,rotate: boolean}) {
    return (
        <group position={new THREE.Vector3(...center)}
               rotation={
                   rotate
                       ? new THREE.Euler(0, -Math.PI / 2, 0)
                       : new THREE.Euler(0, 0, 0)
               }>
            <mesh frustumCulled={false} >
                <boxGeometry args={dimensions} />
                <meshStandardMaterial color={color} side={THREE.DoubleSide} />
            </mesh>
            <lineSegments frustumCulled={false} >
                <edgesGeometry attach="geometry" args={[new THREE.BoxGeometry(...dimensions)]}  />
                <lineBasicMaterial attach="material" color="black" />
            </lineSegments>
        </group>
    );
}

export default DrawPallet;