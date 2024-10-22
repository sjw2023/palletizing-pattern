import { Canvas } from "@react-three/fiber";
import { OrbitControls } from "@react-three/drei";
import React from "react";
import BoxDraw from "@/app/components/BoxDraw";
import { Box } from "@/app/types/Box";
import {Pallet} from "@/app/types/Pallet";
import DrawPallet from "@/app/components/DrawPallet";
import {palletState} from "@/app/atom/atom";
import {useRecoilState} from "recoil";

function PatternCanvas({ boxes, palletInfo }: { boxes: Box[], palletInfo: any }) {
    // console.log("Drawing boxes: ", boxes);
    return (
        <Canvas orthographic camera={{ zoom: 1, position: [400, 400, 600] }} style={{ width: '100%', height: '100%' }}>
            <ambientLight />
            <pointLight position={[10, 10, 10]} />
            <axesHelper args={[100]} position={[0, 0, 0]} />
            <gridHelper args={[500, 50]} position={[0, 0, 0]} />
            <OrbitControls />
            {boxes && boxes.map((box, index) => {
                // console.log(box, index);
                    return (
                    <BoxDraw
                        key={index}
                        center={[box.x, box.y, box.z]}
                        dimensions={[box.width, box.height, box.length]}
                        rotate={box.rotate}
                        orderIndex={box.orderIndex}
                        color={box.color}
                    />
                );
            })}

        </Canvas>
    );
}

export default PatternCanvas;