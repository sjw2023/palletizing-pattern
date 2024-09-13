// import {Canvas} from "@react-three/fiber";
// import {OrbitControls} from "@react-three/drei";
// import React from "react";
//
// function PatternCanvas({ boxes : Box }) {
//     return (
//         <Canvas orthographic camera={{zoom: 1, position: [400, 400, 600]}} style={{width: '100%', height: '100%'}}>
//             <ambientLight/>
//             <pointLight position={[10, 10, 10]}/>
//             <axesHelper args={[100]} position={[0, 0, 0]}/>
//             <gridHelper args={[500, 50]} position={[0, 0, 0]}/>
//             <OrbitControls/>
//             {boxes.map((box, index) => (
//                 <Box
//                     key={index}
//                     center={[box.x, box.y, box.z]}
//                     dimensions={[box.width, box.height, box.length]}
//                     rotate={box.rotate}
//                     orderIndex={box.orderIndex}
//                 />
//             ))}
//         </Canvas>
//     );
// }