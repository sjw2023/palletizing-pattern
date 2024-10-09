import React from "react";
import * as THREE from "three";
import {Canvas} from "@react-three/fiber";
import {OrbitControls} from "@react-three/drei";
import DrawContainer from "@/app/components/DrawContainer";
import BoxDraw from "@/app/components/BoxDraw";
import {Box} from "@/app/types/Box";
import {useRecoilState} from "recoil";
import {palletState} from "../atom/atom";

interface CanvasSectionProps {
    boxes: Box[];
    // pallets: Pallet[];
    reducedDimensions: { width: number; height: number; length: number };
    cameraPosition: THREE.Vector3;
    zoom: number;
    farFactor: number;
}

function CanvasSection({
                           boxes,
                           reducedDimensions,
                           cameraPosition,
                           zoom,
                           farFactor,
                       }: CanvasSectionProps) {
    const [pallets, setPallets] = useRecoilState(palletState);
    console.log(pallets);

    return (
        <div style={{flexDirection: "row", width: "100%", height: "100%"}}>
            <div style={{width: "100%"}}>
                <label style={{textAlign: "center", fontSize: "20px"}}>
                    Container
                </label>
            </div>
            <Canvas
                orthographic
                camera={{
                    zoom: zoom,
                    position: cameraPosition,
                    near: 0.1,
                    far: farFactor,
                }}
                style={{width: "100%", height: "100%"}}
            >
                <ambientLight/>
                <pointLight position={[10, 10, 10]}/>
                <axesHelper args={[100]} position={[0, 0, 0]}/>
                <gridHelper args={[500, 50]} position={[0, 0, 0]}/>
                <OrbitControls/>
                <DrawContainer
                    center={
                    [reducedDimensions.length / 2 + pallets[0].x/2, reducedDimensions.height, reducedDimensions.width / 2 + pallets[0].y/2]}
                    dimensions={[
                        reducedDimensions.length,
                        reducedDimensions.height,
                        reducedDimensions.width,
                    ]}
                />
                {pallets?.length > 0 &&
                    pallets?.map((pallet: any, index: number) => (
                        <group
                            key={index}
                            position={
                                new THREE.Vector3(
                                    pallet.x ,
                                    pallet.y ,
                                    pallet.z ,
                                )
                            }
                            rotation={
                                // pallet.rotate
                                //     ? new THREE.Euler( 0, Math.PI /2, 0)
                                     new THREE.Euler( 0, 0, 0)
                            }
                        >
                            {boxes?.length > 0 &&
                                boxes.map((box, boxIndex) => (
                                    <BoxDraw
                                        key={boxIndex}
                                        center={[box.x, box.y, box.z]}
                                        dimensions={[
                                            box.width,
                                            box.height,
                                            box.length,
                                        ]}
                                        rotate={box.rotate}
                                        orderIndex={box.orderIndex}
                                        color={box.color}
                                    />
                                ))}
                        </group>
                    ))}
            </Canvas>
        </div>
    );
}

export default CanvasSection;