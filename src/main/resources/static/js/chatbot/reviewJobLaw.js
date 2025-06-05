import { openReviewModal } from './openReviewModal.js';

let originalFormData = null;

export async function analyzeJob() {
    console.log("analyzeJob호출")
    const form = document.querySelector("form");
    const formData = new FormData(form);
    const json = {};

    formData.forEach((val, key) => {
        if (!json[key]) json[key] = val;
        else if (Array.isArray(json[key])) json[key].push(val);
        else json[key] = [json[key], val];
    });

    originalFormData = { ...json };

    try {
        const res = await fetchWithAuth("/api/v1/chatbot/law-review", {
            method: "POST",
            body: JSON.stringify(json),
        });
        const response = await res.json();
        console.log("🧪 AI 응답:", response);


        // form 값 덮어쓰기
        /*document.querySelector("[name='title']").value = review["공고 제목"]?.suggest || "";
        document.querySelector("[name='description']").value = review["설명"]?.suggest || "";
        document.querySelector("[name='mainTask']").value = review["주요 업무"]?.suggest || "";
        document.querySelector("[name='requiredSkills']").value = review["필요 역량"]?.suggest || "";
        document.querySelector("[name='requiredTraits']").value = review["인재상"]?.suggest || "";
        document.querySelector("[name='notice']").value = review["안내사항"]?.suggest || "";*/

        openReviewModal(originalFormData, response);
        document.getElementById("restoreBtn").style.display = "inline-block";
        // alert("✅ AI 제안안이 반영되었습니다.");
    } catch (err) {
        console.error("AI 분석 실패", err);
        alert("AI 분석 실패");
    }
}

export function restoreOriginalJob() {
    if (!originalFormData) {
        alert("되돌릴 데이터가 없습니다.");
        return;
    }

    for (const key in originalFormData) {
        const el = document.querySelector(`[name='${key}']`);
        if (el && typeof originalFormData[key] === "string") {
            el.value = originalFormData[key];
        }
    }

    alert("원래 값으로 복구했습니다.");
    document.getElementById("restoreBtn").style.display = "none";
}
