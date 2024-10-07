import React, {useEffect, useRef, useState} from "react";
import * as THREE from "three";
import { Text } from "@react-three/drei";

function BoxDraw({ center, dimensions, rotate, orderIndex, color }: { center: number[], dimensions: [number, number, number], rotate: boolean, orderIndex?: number, color: string }) {
    const meshRef = useRef<THREE.Mesh>(null)
    ;
    const [ data, setData] = useState<any>();
    const edgesRef = useRef<THREE.LineSegments>(null);
    const [hovered, setHovered] = React.useState(false);

    useEffect(() => {
        if (meshRef.current) {
            meshRef.current.rotation.set(0, 0, 0);
        }
        if (edgesRef.current) {
            edgesRef.current.rotation.set(0, 0, 0);
        }
    }, [rotate]);

    const adjustedDimensions: [number, number, number] = rotate ? dimensions : [dimensions[1], dimensions[0], dimensions[2]];

    return (
        <group position={new THREE.Vector3(center[0], center[1], center[2])}>
            <mesh
                ref={meshRef}
                frustumCulled={false}
                onPointerOver={(event) => setHovered(true)}
                onPointerOut={(event) => setHovered(false)}
            >
                <boxGeometry args={adjustedDimensions} />
                <meshStandardMaterial color={hovered ? color : 'orange'} />
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

export default BoxDraw;