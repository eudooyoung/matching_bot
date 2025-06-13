const MAX_KEYWORDS = 15;

function addKeyword(wrapperId, inputName, placeholderText) {
    const wrapper = document.getElementById(wrapperId);
    const count = wrapper.querySelectorAll('.keyword-input').length;

    if (count >= MAX_KEYWORDS) return;

    const plusButton = Array.from(wrapper.querySelectorAll('button')).find(btn => btn.textContent === '+');
    if (plusButton) {
        plusButton.parentElement.remove();
    }

    const inputDiv = document.createElement('div');
    inputDiv.className = 'keyword-input';
    inputDiv.innerHTML = `
        <input type="text" name="${inputName}" placeholder="${placeholderText}" />
        <button type="button" onclick="removeKeyword(this)">-</button>
    `;
    wrapper.appendChild(inputDiv);

    if (wrapper.querySelectorAll('.keyword-input').length < MAX_KEYWORDS) {
        const plusDiv = document.createElement('div');
        plusDiv.className = 'keyword-input';

        const input = document.createElement('input');
        input.type = 'text';
        input.name = inputName;
        input.placeholder = placeholderText;
        input.maxLength = 10;

        const button = document.createElement('button');
        button.type = 'button';
        button.textContent = '+';
        button.addEventListener('click', () => {
            addKeyword(wrapperId, inputName, placeholderText);
        });

        plusDiv.appendChild(input);
        plusDiv.appendChild(button);
        wrapper.appendChild(plusDiv);
    }
}

export function addSkillKeyword() {
    addKeyword('skill-keyword-wrapper', 'skill_keywords[]', '기술 키워드');
}

export function addTraitKeyword() {
    addKeyword('trait-keyword-wrapper', 'trait_keywords[]', '성향 키워드');
}

export function removeKeyword(button) {
    const wrapper = button.closest('.keyword-wrapper');
    const inputDiv = button.parentElement;
    inputDiv.remove();

    const count = wrapper.querySelectorAll('.keyword-input').length;
    const hasPlusButton = Array.from(wrapper.querySelectorAll('button')).some(btn => btn.textContent === '+');

    if (count < 15 && !hasPlusButton) {
        const isSkill = wrapper.id.includes('skill');
        const inputName = isSkill ? 'skill_keywords[]' : 'trait_keywords[]';
        const placeholder = isSkill ? '기술 키워드' : '성향 키워드';

        const plusDiv = document.createElement('div');
        plusDiv.className = 'keyword-input';

        const input = document.createElement('input');
        input.type = 'text';
        input.name = inputName;
        input.placeholder = placeholder;
        input.maxLength = 10;

        const button = document.createElement('button');
        button.type = 'button';
        button.textContent = '+';
        button.addEventListener('click', () => {
            if (isSkill) {
                addSkillKeyword();
            } else {
                addTraitKeyword();
            }
        });

        plusDiv.appendChild(input);
        plusDiv.appendChild(button);
        wrapper.appendChild(plusDiv);
    }
}

export async function extractKeywords(type) {
    const textareaId = type === 'skill' ? 'skillAnswer' : 'traitAnswer';
    const wrapperId = type === 'skill' ? 'skill-keyword-wrapper' : 'trait-keyword-wrapper';
    const inputName = type === 'skill' ? 'skill_keywords[]' : 'trait_keywords[]';

    const text = document.querySelector(`textarea[name="${textareaId}"]`)?.value.trim();
    if (!text) {
        alert("내용을 입력해주세요.");
        return;
    }

    const formData = new FormData();
    formData.append("text", text);

    try {
        const response = await fetch("http://localhost:8081/extract", {
            method: "POST",
            body: formData
        });

        const result = await response.json();
        let keywords = result.keywords || [];
        keywords = keywords.slice(0, 15);

        const wrapper = document.getElementById(wrapperId);
        wrapper.innerHTML = '';

        keywords.forEach(kw => {
            const inputDiv = document.createElement('div');
            inputDiv.className = 'keyword-input';
            inputDiv.innerHTML = `
                <input type="text" name="${inputName}" value="${kw}" maxlength="10"/>
                <button type="button" onclick="removeKeyword(this)">-</button>
            `;
            wrapper.appendChild(inputDiv);
        });

        if (keywords.length < 15) {
            const plusDiv = document.createElement('div');
            plusDiv.className = 'keyword-input';
            plusDiv.innerHTML = `
                <input type="text" name="${inputName}" placeholder="${type === 'skill' ? '기술 키워드' : '성향 키워드'}" maxlength="10"/>
                <button type="button" onclick="${type === 'skill' ? 'addSkillKeyword()' : 'addTraitKeyword()'}">+</button>
            `;
            wrapper.appendChild(plusDiv);
        }

    } catch (err) {
        console.error("키워드 추출 중 오류:", err);
        alert("키워드 추출 실패");
    }
}

// submit 시 keyword들을 hidden input에 합쳐서 넣기
export function attachKeywordSubmitHandler() {
    document.addEventListener("DOMContentLoaded", () => {
        const form = document.querySelector("form");
        if (!form) return;

        // ✅ 초기 키워드 렌더링
        const initKeywordInputs = (wrapperId, inputName, concatValue) => {
            const wrapper = document.getElementById(wrapperId);
            if (!wrapper) return;

            wrapper.innerHTML = "";

            const keywords = concatValue.split(",").filter(k => k);
            keywords.forEach(kw => {
                const div = document.createElement("div");
                div.className = "keyword-input";
                div.innerHTML = `
                    <input type="text" name="${inputName}" value="${kw}" maxlength="10"/>
                    <button type="button" onclick="removeKeyword(this)">-</button>
                `;
                wrapper.appendChild(div);
            });

            if (keywords.length < 15) {
                const plusDiv = document.createElement("div");
                plusDiv.className = "keyword-input";
                plusDiv.innerHTML = `
                    <input type="text" name="${inputName}" placeholder="${inputName.includes("skill") ? "기술 키워드" : "성향 키워드"}" maxlength="10"/>
                    <button type="button" onclick="${inputName.includes("skill") ? "addSkillKeyword()" : "addTraitKeyword()"}">+</button>
                `;
                wrapper.appendChild(plusDiv);
            }
        };

        initKeywordInputs("skill-keyword-wrapper", "skill_keywords[]", document.getElementById("skillKeywordsConcat")?.value || "");
        initKeywordInputs("trait-keyword-wrapper", "trait_keywords[]", document.getElementById("traitKeywordsConcat")?.value || "");

        // ✅ 기존 submit 로직
        form.addEventListener("submit", function () {
            const skillInputs = document.querySelectorAll("#skill-keyword-wrapper input[name='skill_keywords[]']");
            const traitInputs = document.querySelectorAll("#trait-keyword-wrapper input[name='trait_keywords[]']");

            const skillKeywords = Array.from(skillInputs)
                .map(input => input.value.trim())
                .filter(val => val)
                .join(",")
                .slice(0, 200);

            const traitKeywords = Array.from(traitInputs)
                .map(input => input.value.trim())
                .filter(val => val)
                .join(",")
                .slice(0, 200);

            document.getElementById("skillKeywordsConcat").value = skillKeywords;
            document.getElementById("traitKeywordsConcat").value = traitKeywords;
        });
    });
}
