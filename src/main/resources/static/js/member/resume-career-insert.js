/*
function toggleCareer(isExperienced) {
    const section = document.getElementById("career-section");
    section.style.display = isExperienced ? "block" : "none";

    const inputs = section.querySelectorAll("input, select, textarea");
    inputs.forEach(input => input.disabled = !isExperienced);
}
*/

function addCareerRow() {
    const list = document.getElementById("career-list");
    const index = list.children.length;

    const row = document.createElement("div");
    row.className = "career-entry";
    row.innerHTML = `
        <button type="button" class="career-remove-btn" onclick="removeCareer(this)">✕</button>
        <div class="inline-group">
            <div class="form-group">
                <label for="careers[${index}].companyName">회사명</label>
                <input type="text" name="careers[${index}].companyName" id="careers[${index}].companyName" maxlength="50"/>
            </div>
            <div class="form-group">
                <label for="careers[${index}].departmentName">부서명</label>
                <input type="text" name="careers[${index}].departmentName" id="careers[${index}].departmentName" maxlength="50"/>
            </div>
            <div class="form-group">
                <label for="careers[${index}].positionTitle">직급/직책</label>
                <input type="text" name="careers[${index}].positionTitle" id="careers[${index}].positionTitle" maxlength="50"/>
            </div>
            <div class="form-group">
                <label for="careers[${index}].salary">최종 연봉</label>
                <input type="text" name="careers[${index}].salary" id="careers[${index}].salary" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].startDateRaw">입사년월</label>
                <input type="month" name="careers[${index}].startDateRaw" id="careers[${index}].startDate" onclick="this.showPicker()" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].endDateRaw">퇴사년월</label>
                <input type="month" name="careers[${index}].endDateRaw" id="careers[${index}].endDate" onclick="this.showPicker()" />
            </div>
        </div>
        <div class="inline-group">
            <div class="form-group" style="flex: 2;">
                <label for="careers[${index}].careerSummary">담당 업무 및 성과 요약</label>
<!--                <input type="text" name="careers[${index}].careerSummary" id="careers[${index}].careerSummary" maxlength="255"/>-->
                <textarea name="careers[${index}].careerSummary" id="careers[${index}].careerSummary" rows="3" maxlength="500"></textarea>
            </div>
        </div>
    `;
    list.appendChild(row);
}

function removeCareer(button) {
    const entry = button.closest(".career-entry");
    if (entry) entry.remove();
}

function renderCareersFromData(data) {
    const list = document.getElementById("career-list");
    list.innerHTML = ""; // 초기화

    data.forEach((career, index) => {
        const row = document.createElement("div");
        row.className = "career-entry";
        row.innerHTML = `
            <button type="button" class="career-remove-btn" onclick="removeCareer(this)">✕</button>
            <div class="inline-group">
                <div class="form-group">
                    <label>회사명</label>
                    <input type="text" name="careers[${index}].companyName" value="${career.companyName || ''}" />
                </div>
                <div class="form-group">
                    <label>부서명</label>
                    <input type="text" name="careers[${index}].departmentName" value="${career.departmentName || ''}" />
                </div>
                <div class="form-group">
                    <label>직급/직책</label>
                    <input type="text" name="careers[${index}].positionTitle" value="${career.positionTitle || ''}" />
                </div>
                <div class="form-group">
                    <label>최종 연봉</label>
                    <input type="text" name="careers[${index}].salary" value="${career.salary || ''}" />
                </div>
                <div class="form-group">
                    <label>입사년월</label>
                    <input type="month" name="careers[${index}].startDateRaw" value="${career.startDateRaw || ''}" />
                </div>
                <div class="form-group">
                    <label>퇴사년월</label>
                    <input type="month" name="careers[${index}].endDateRaw" value="${career.endDateRaw || ''}" />
                </div>
            </div>
            <div class="inline-group">
                <div class="form-group" style="flex: 2;">
                    <label>담당 업무 및 성과 요약</label>
<!--                    <input type="" name="careers[${index}].careerSummary" value="${career.careerSummary || ''}" maxlength="255"/>-->
                    <textarea name="careers[${index}].careerSummary" rows="3" maxlength="500">${career.careerSummary || ''}</textarea>
                </div>
            </div>
        `;
        list.appendChild(row);
    });
}

window.addEventListener("DOMContentLoaded", () => {
    if (typeof initialCareers !== "undefined" && Array.isArray(initialCareers) && initialCareers.length > 0) {
        renderCareersFromData(initialCareers);
    }

    const form = document.getElementById("resumeForm");
    form.addEventListener("submit", (e) => {
        const careers = document.querySelectorAll("#career-list .career-entry");

        for (const entry of careers) {
            const company = entry.querySelector("input[name*='companyName']");
            const department = entry.querySelector("input[name*='departmentName']");
            const position = entry.querySelector("input[name*='positionTitle']");
            const startDate = entry.querySelector("input[name*='startDateRaw']");
            const endDate = entry.querySelector("input[name*='endDateRaw']");

            if (
                company.value.trim() === "" ||
                department.value.trim() === "" ||
                position.value.trim() === "" ||
                startDate.value.trim() === "" ||
                endDate.value.trim() === ""
            ) {
                alert("경력 항목에 빠진 정보가 있습니다. 모든 필드를 입력해주세요.");
                e.preventDefault(); // 제출 막기
                return;
            }
        }
    });
});


