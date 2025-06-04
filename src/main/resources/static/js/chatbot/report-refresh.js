document.addEventListener("DOMContentLoaded", function () {
    const refreshBtn = document.getElementById("refreshReportBtn");

    refreshBtn?.addEventListener("click", async function () {
        if (!confirm("AI 평가를 갱신하시겠습니까?")) return;

        const getValue = name => document.querySelector(`input[name="${name}"]`)?.value || '';
        const toNumber = val => Number(val) || 0;

        const requiredFields = {
            name: getValue("name"), // ✅ 이제 포함
            yearFound: toNumber(getValue("yearFound")), // ✅ 이것도 포함
            headcount: toNumber(getValue("headcount")),
            industry: getValue("industry"),
            annualRevenue: toNumber(getValue("annualRevenue")),
            operatingIncome: toNumber(getValue("operatingIncome")),
            jobsLastYear: toNumber(getValue("jobsLastYear"))
        };

        // 누락된 필드 찾기
        const missing = Object.entries(requiredFields)
            .filter(([_, value]) => value === '' || value === null)
            .map(([key]) => key);

        // 있으면 알림
        if (missing.length > 0) {
            alert("다음 항목은 필수 입력입니다: " + missing.join(", "));
            return;
        }

        const data = { ...requiredFields };
        const companyId = refreshBtn.dataset.companyId;

        try {
            const response = await fetchWithAuth(`/api/v1/attached/${companyId}/refresh-report`, {
                method: 'POST',
                body: JSON.stringify(data)
            });

            if (response.ok) {
                alert("AI 평가가 성공적으로 갱신되었습니다.");
                location.reload();
            } else {
                alert("갱신 중 오류가 발생했습니다.");
            }
        } catch (e) {
            console.error("AI 평가 오류:", e);
            alert("네트워크 오류가 발생했습니다.");
        }
    });
});
