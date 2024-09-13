import React, {useEffect, useRef} from "rGeact";
import * as THREE from "three";
import {Text} from "@react-three/drei";

function Box({center, dimensions, rotate, orderIndex, color = "#C3AA83"}) {
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
                <boxGeometry args={adjustedDimensions}/>
                <meshStandardMaterial color={color}/>
            </mesh>
            <lineSegments ref={edgesRef} frustumCulled={false}>
                <edgesGeometry attach="geometry" args={[new THREE.BoxGeometry(...adjustedDimensions)]}/>
                <lineBasicMaterial attach="material" color="black"/>
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