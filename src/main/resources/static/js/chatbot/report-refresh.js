document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("companyEditForm");

    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault(); // 기본 제출 막기
            showModal();        // 모달 열기
        });
    }
});

// 모달 열기
function showModal() {
    const modal = document.getElementById("confirmModal");
    if (modal) modal.style.display = "flex";
}

// 모달 닫기
function closeModal() {
    const modal = document.getElementById("confirmModal");
    if (modal) modal.style.display = "none";
}

// 그냥 저장
function submitWithoutAI() {
    closeModal();
    const form = document.getElementById("companyEditForm");
    if (form) form.submit();
}

// AI 평가 갱신 후 저장
function submitWithAI() {
    closeModal();
    const overlay = document.getElementById("loadingOverlay");
    if (overlay) overlay.style.display = "flex";

    handleAIRefresh(); // 실제 작업 수행
}

// 실제 AI 갱신 요청 로직
async function handleAIRefresh() {
    const overlay = document.getElementById("loadingOverlay");

    const getValue = name => document.querySelector(`input[name="${name}"]`)?.value || '';
    const toNumber = val => Number(val) || 0;

    const requiredFields = {
        name: getValue("name"),
        yearFound: toNumber(getValue("yearFound")),
        headcount: toNumber(getValue("headcount")),
        industry: getValue("industry"),
        annualRevenue: toNumber(getValue("annualRevenue")),
        operatingIncome: toNumber(getValue("operatingIncome")),
        jobsLastYear: toNumber(getValue("jobsLastYear"))
    };

    const missing = Object.entries(requiredFields)
        .filter(([_, value]) => value === '' || value === null)
        .map(([key]) => key);

    if (missing.length > 0) {
        alert("다음 항목은 필수 입력입니다: " + missing.join(", "));
        if (overlay) overlay.style.display = "none";
        return;
    }

    const companyId = document.getElementById("refreshReportBtn")?.dataset.companyId;
    if (!companyId) {
        alert("회사 ID가 유효하지 않습니다.");
        if (overlay) overlay.style.display = "none";
        return;
    }

    try {
        console.log("유효성 검증 완료. Ai 요청 시작")
        const response = await fetchWithAuth(`/api/v1/attached/${companyId}/refresh-report`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requiredFields)
        });

        if (response.ok) {
            alert("AI 평가가 성공적으로 갱신되었습니다.");
            document.getElementById("companyEditForm").submit();
        } else {
            alert("갱신 중 오류가 발생했습니다.");
            if (overlay) overlay.style.display = "none";
        }
    } catch (e) {
        console.error("AI 평가 오류:", e);
        alert("네트워크 오류가 발생했습니다.");
        if (overlay) overlay.style.display = "none";
    }
}
