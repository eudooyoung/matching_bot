import {openReviewModal} from './openReviewModal.js';

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

    originalFormData = {...json};

   /* document.getElementById("jobReviewModal").style.display = "block";
    const spinner = document.getElementById("reviewSpinner");
    if (spinner) spinner.style.display = "block";
*/
    document.getElementById("globalLoadingOverlay").style.display = "flex";


    try {
        const res = await fetchWithAuth("/api/v1/chatbot/law-review", {
            method: "POST",
            body: JSON.stringify(json),
        });
        const response = await res.json();
        console.log("🧪 AI 응답:", response);

        document.getElementById("globalLoadingOverlay").style.display = "none";

        openReviewModal(originalFormData, response);
        document.getElementById("restoreBtn").style.display = "inline-block";
        // alert("✅ AI 제안안이 반영되었습니다.");
    } catch (err) {
        console.error("AI 분석 실패", err);
        document.getElementById("globalLoadingOverlay").style.display = "none";
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
