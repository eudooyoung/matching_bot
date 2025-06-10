/*function toggleCareer(isExperienced) {
    const section = document.getElementById("career-section");
    section.style.display = isExperienced ? "block" : "none";

    const inputs = section.querySelectorAll("input, select, textarea");
    inputs.forEach(input => input.disabled = !isExperienced);
}*/

function addCareerRow() {
    const list = document.getElementById("career-list");
    const index = list.querySelectorAll(".career-entry").length;

    const row = document.createElement("div");
    row.className = "career-entry";
    row.innerHTML = `
        <button type="button" class="career-remove-btn" onclick="removeCareer(this)">✕</button>
        <div class="inline-group">
            <div class="form-group">
                <label for="careers[${index}].companyName">회사명</label>
                <input type="text" name="careers[${index}].companyName" id="careers[${index}].companyName" maxlength="50" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].departmentName">부서명</label>
                <input type="text" name="careers[${index}].departmentName" id="careers[${index}].departmentName" maxlength="50" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].positionTitle">직급/직책</label>
                <input type="text" name="careers[${index}].positionTitle" id="careers[${index}].positionTitle" maxlength="50" />
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
<!--                <input type="text" name="careers[${index}].careerSummary" id="careers[${index}].careerSummary" maxlength="255" />-->
                <textarea name="careers[${index}].careerSummary" id="careers[${index}].careerSummary" rows="3" maxlength="500"></textarea>

            </div>
        </div>
    `;
    const buttonArea = list.querySelector(".button-area");
    list.insertBefore(row, buttonArea);
    // list.appendChild(row);
}

function removeCareer(button) {
    const entry = button.closest(".career-entry");
    if (entry) entry.remove();
}

window.addEventListener("DOMContentLoaded", () => {
    const selected = document.querySelector('input[name="careerType"]:checked');
    // toggleCareer(selected?.value === "EXP");
});
