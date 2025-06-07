function toggleCareer(isExperienced) {
    const section = document.getElementById("career-section");
    section.style.display = isExperienced ? "block" : "none";
}

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
                <input type="text" name="careers[${index}].companyName" id="careers[${index}].companyName" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].department">부서명</label>
                <input type="text" name="careers[${index}].department" id="careers[${index}].department" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].position">직급/직책</label>
                <input type="text" name="careers[${index}].position" id="careers[${index}].position" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].lastSalary">최종 연봉</label>
                <input type="text" name="careers[${index}].lastSalary" id="careers[${index}].lastSalary" />
            </div>
            <div class="form-group">
                <label for="careers[${index}].joinDate">입사년월</label>
                <input type="month" name="careers[${index}].joinDate" id="careers[${index}].joinDate" onclick="this.showPicker()"/>
            </div>
            <div class="form-group">
                <label for="careers[${index}].leaveDate">퇴사년월</label>
                <input type="month" name="careers[${index}].leaveDate" id="careers[${index}].leaveDate" onclick="this.showPicker()"/>
            </div>
        </div>
        <div class="inline-group">
            <div class="form-group" style="flex: 2;">
                <label for="careers[${index}].summary">담당 업무 및 성과 요약</label>
                <input type="text" name="careers[${index}].summary" id="careers[${index}].summary" />
            </div>
        </div>
    `;
    list.appendChild(row);
}

function removeCareer(button) {
    const entry = button.closest(".career-entry");
    if (entry) entry.remove();
}

window.addEventListener("DOMContentLoaded", () => {
    const selected = document.querySelector('input[name="experienceType"]:checked');
    toggleCareer(selected?.value === "EXP");
    addCareerRow(); // 기본 1개 경력 입력칸 추가
});
