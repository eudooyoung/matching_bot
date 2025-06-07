// // ✅ React 및 createRoot 불러오기
import React from "https://cdn.jsdelivr.net/npm/react@18.2.0/+esm";
import { createRoot } from "https://cdn.jsdelivr.net/npm/react-dom@18.2.0/client/+esm";
//
// // ✅ 신입/경력 선택 컴포넌트
// const CareerTypeSelector = () => {
//     const handleChange = (e) => {
//         const selectedValue = e.target.value;
//         const hiddenInput = document.getElementById("filter-career-type");
//         if (hiddenInput) {
//             hiddenInput.value = selectedValue;
//         }
//     };
//
//     return (
//         <select className="form-select" onChange={handleChange}>
//             <option value="">전체</option>
//             <option value="new">신입</option>
//             <option value="exp">경력</option>
//         </select>
//     );
// };
//
// // ✅ 렌더링 대상 컨테이너 찾고 마운트
// const container = document.getElementById("react-career-type-selector");
// if (container) {
//     const root = createRoot(container);
//     root.render(<CareerTypeSelector />);
// }
// ✅ React 및 createRoot 불러오기
import React, { useEffect, useState } from "https://cdn.jsdelivr.net/npm/react@18.2.0/+esm";
import { createRoot } from "https://cdn.jsdelivr.net/npm/react-dom@18.2.0/client/+esm";

// ✅ 신입/경력 필터 선택 컴포넌트
function CareerTypeSelector() {
    const [selectedCareer, setSelectedCareer] = useState("");

    const handleChange = (e) => {
        const value = e.target.value;
        setSelectedCareer(value);
        const hiddenInput = document.getElementById("filter-career-type");
        if (hiddenInput) {
            hiddenInput.value = value;
        }
    };

    return (
        <select className="form-select" onChange={handleChange} value={selectedCareer}>
            <option value="">전체</option>
            <option value="new">신입</option>
            <option value="exp">경력</option>
        </select>
    );
}

// ✅ 마운트
const container = document.getElementById("react-career-type-selector");
if (container) {
    const root = createRoot(container);
    root.render(<CareerTypeSelector />);
}
