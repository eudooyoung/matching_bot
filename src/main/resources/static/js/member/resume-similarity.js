export function initSimilarityFeature() {
    // if (userRole !== "COMPANY") return;

    const jobSelect = document.getElementById("jobSelect");
    const container = document.getElementById("similarity-data");

    jobSelect?.addEventListener("change", async () => {
        const jobId = jobSelect.value;
        if (!jobId) {
            console.warn("⛔ jobId가 비어 있음:", jobId);
            return;
        }

        try {
            const resumeSkillRaw = container.dataset.resumeSkill;
            const resumeTraitRaw = container.dataset.resumeTrait;
            const {skillKeywords: jobSkill, traitKeywords: jobTrait} = await fetchJobKeywords(jobId);

            container.dataset.jobSkill = jobSkill.join(',');
            container.dataset.jobTrait = jobTrait.join(',');

            const reqBody = {
                resume_skill_keys: resumeSkillRaw.split(',').map(s => s.trim()).filter(Boolean),
                resume_trait_keys: resumeTraitRaw.split(',').map(s => s.trim()).filter(Boolean),
                job_skill_keys: jobSkill,
                job_trait_keys: jobTrait
            };

            console.log("📦 매칭률 요청 데이터:", JSON.stringify(reqBody, null, 2)); // pretty-print

            const percent = await calculateSimilarity(reqBody);
            console.log("✅ 매칭률 결과:", percent + "%"); // 매칭률 결과 확인용 로그
            renderSimilarityScore(percent);

        } catch (e) {
            console.error("매칭률 계산 오류:", e);
            renderSimilarityScore(null);
        }
    });
}

async function fetchJobKeywords(jobId) {
    const res = await fetch(`/api/jobs/${jobId}/keywords`);
    if (!res.ok) throw new Error("채용공고 키워드 불러오기 실패");
    return await res.json();
}

async function calculateSimilarity(body) {
    const res = await fetch("http://localhost:8081/calculate-similarity", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(body)
    });
    /*const res = await fetchWithAuth("/api/jobs/similarity", {
        method: "POST",
        body: JSON.stringify(body)
    });*/

    if (!res.ok) throw new Error("매칭률 계산 API 실패");
    const result = await res.json();
    return Math.round(result.similarity * 100);
}

function renderSimilarityScore(percent) {
    const el = document.getElementById("similarity-score");
    el.textContent = percent != null ? ` | 매칭률: ${percent}%` : " | 매칭률 계산 실패";
}
