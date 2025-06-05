window.latestReviewData = null;

export function openReviewModal(original, response) {
    console.log("🧩 AI 제안:", response);
    window.latestReviewData = response;
    const tbody = document.getElementById("reviewComparisonBody");
    if (!tbody) {
        console.warn("📛 reviewComparisonBody 요소를 찾을 수 없습니다.");
        return;
    }

    tbody.innerHTML = "";

    const fieldMap = {
        title: "공고 제목",
        description: "설명",
        mainTask: "주요 업무",
        requiredSkills: "필요 역량",
        requiredTraits: "인재상",
        notice: "안내사항"
    };

    for (const key in fieldMap) {
        const label = fieldMap[key];
        const ai = response[label] || "";     // ✅ label 기준 접근
        const orig = original[key] || "";

        const row = `
      <tr>
        <td>${label}</td>
        <td>${orig}</td>
        <td>${ai}</td>
      </tr>`;
        tbody.insertAdjacentHTML("beforeend", row);
    }

    document.getElementById("jobReviewModal").style.display = "block";
}

export function closeReviewModal() {
    document.getElementById("jobReviewModal").style.display = "none";
}

export function applyReview() {
    console.trace("🐞 applyReview 호출 경로 추적");
    if (!latestReviewData) {
        console.warn("❗ latestReviewData 없음");
        return;
    }

    const fieldMap = {
        title: "공고 제목",
        description: "설명",
        mainTask: "주요 업무",
        requiredSkills: "필요 역량",
        requiredTraits: "인재상",
        notice: "안내사항"
    };

    for (const key in fieldMap) {
        const label = fieldMap[key];
        const el = document.querySelector(`[name='${key}']`);

        if (!el) {
            console.warn(`❗ 요소 없음: [name="${key}"]`);
            continue;
        }

        const value = window.latestReviewData[label];
        console.log(`✅ [${key}] ←`, value);
        el.value = value || "";
    }

    console.log("✅ 모달 닫기 시도");
    closeReviewModal();
    console.log("✅ 알림 및 복원버튼 노출");
    alert("AI 제안이 적용되었습니다.");
    document.getElementById("restoreBtn").style.display = "inline-block";
}
