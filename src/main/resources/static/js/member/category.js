// category.js
import React from "https://cdn.jsdelivr.net/npm/react@18.2.0/+esm";
import { createRoot } from "https://cdn.jsdelivr.net/npm/react-dom@18.2.0/client/+esm";

window.React = React;
window.createRoot = createRoot;

document.addEventListener("DOMContentLoaded", () => {
    // React 직무 선택기 렌더링
    import("/js/member/JobCategorySelectorResume.js").then((mod) => {
        const container = document.getElementById("react-job-category-selector");
        if (container) {
            const selectedId = container.dataset.selectedId; // ✅ 초기 선택값 읽기
            const root = createRoot(container);
            root.render(React.createElement(mod.default, { selectedId }));
        }
    });

    // 직무 선택 유효성 검사
    const form = document.querySelector("form");
    if (form) {
        form.addEventListener("submit", function (e) {
            const occInput = document.getElementById("occupationId");
            if (!occInput || !occInput.value) {
                alert("직무를 선택해 주세요!");
                e.preventDefault();
            } else {
                console.log("✅ 선택된 occupationId:", occInput.value);
            }
        });
    }
});
