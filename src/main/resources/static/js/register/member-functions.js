document.addEventListener("DOMContentLoaded", () => {

  // 전체 약관 동의 체크 연동
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

  // 전화번호 입력 숫자만 허용
  document.querySelectorAll(".phone-segment").forEach(input => {
    input.addEventListener("input", e => {
      e.target.value = e.target.value.replace(/[^0-9]/g, "");
    });
  });

  // 비밀번호 유효성 검사
  function validatePassword(password) {
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+=-]).{8,16}$/;
    return regex.test(password);
  }

  const userForm = document.getElementById("userForm");
  if (userForm) {
    userForm.addEventListener("submit", function (e) {
      const passwordInput = document.getElementById("password");
      const password = passwordInput?.value || "";

      if (!validatePassword(password)) {
        e.preventDefault();
        alert("비밀번호는 8~16자이며, 영문자/숫자/특수문자를 모두 포함해야 합니다.");
        passwordInput.focus();
      }
    });
  }
});

