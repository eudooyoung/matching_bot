// /js/auth/company-functions.js

// 설립일 select 옵션 동적 생성
const currentYear = new Date().getFullYear();
const yearEl = document.getElementById("establishedYear");
const monthEl = document.getElementById("establishedMonth");
const dayEl = document.getElementById("establishedDay");

// 전체 동의 체크
const agreeAll = document.querySelector("input[name='agree_all']");
if (agreeAll) {
    agreeAll.addEventListener("change", function () {
        const checked = this.checked;
        document.querySelectorAll(".checkbox-group input[type='checkbox']")
            .forEach(chk => {
                if (chk !== this) chk.checked = checked;
            });
    });
}

// 유효성 검사
function validatePassword(password) {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+=-]).{8,16}$/;
    return regex.test(password);
}

// 사업자 인증
function businessNumber() {
    let num = document.getElementById('businessNo').value;

    if (num === '1234567890') {
        document.getElementById('regimessage').innerHTML = "<br>사업자 회원가입이 가능합니다.";
        return;
    }

    const data = { "b_no": [num] };
    $.ajax({
        url: "/api/validate-business",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "JSON",
        contentType: "application/json",
        success: function (result) {
            let valid = result.data[0]['b_stt_cd'];
            let msg = document.getElementById('regimessage');
            msg.innerHTML = valid === '01'
                ? "<br>사업자 회원가입이 가능합니다."
                : "<br>사업자 회원가입을 할 수 없습니다.";
        },
        error: function (err) {
            console.log(err.responseText);
        }
    });
}

// 로딩 후 제출
function showLoadingAndSubmit(form) {
    document.getElementById("loadingOverlay").style.display = "flex";
    form.submit();
}

// 최종 사업자 인증 후 제출
function businessRegi() {
    const form = document.getElementById("companyForm");
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const num = document.getElementById("businessNo").value;
    // TODO: 배포시 주석 혹은 삭제
    if (num === '1234567890') {
        alert("사업자 인증에 성공했습니다.");
        form.submit();
        return;
    }

    const data = { "b_no": [num] };
    $.ajax({
        url: "/api/validate-business",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "JSON",
        contentType: "application/json",
        success: function (result) {
            let valid = result.data[0]['b_stt_cd'];
            // TODO: 배포시 활성화
            /*if (valid === '01') {
                alert("사업자 인증에 성공했습니다.");
                form.submit();
            } else {
                alert("사업자가 아닙니다.");
            }*/
        },
        error: function () {
            alert("인증 처리 중 에러 발생");
        }
    });
}

// 폼 제출 이벤트 바인딩
document.getElementById("companyForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const password = document.getElementById("password").value;

    if (!validatePassword(password)) {
        alert("비밀번호는 8~16자이며, 영문자/숫자/특수문자를 모두 포함해야 합니다.");
        document.getElementById("password").focus();
        return;
    }

    if (!this.checkValidity()) {
        this.reportValidity();
        return;
    }

    businessRegi();
});
