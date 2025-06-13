function isLoginPage(url) {
    return url.includes("/admin/login") || url.includes("/auth/login");
}

function isRegisterPage(url) {
    return url.includes("/member/register") || url.includes("/company/register");
}

function storePreviousUrl() {
    const previousUrl = document.referrer;
    if (previousUrl && !isLoginPage(previousUrl) && !isRegisterPage(previousUrl)) {
        sessionStorage.setItem("beforeLoginUrl", previousUrl);
    }
}

async function handleLogin(emailId, passwordId, errorMsgId, role) {
    const email = document.getElementById(emailId).value;
    const password = document.getElementById(passwordId).value;
    const errorBox = document.getElementById(errorMsgId);

    try {
        const response = await fetch("/api/v1/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password, role })
        });

        if (!response.ok) {
            const errorJson = await response.json();
            const msg = errorJson.message || "로그인 중 오류가 발생했습니다.";
            errorBox.innerText = msg;
            return;
        }

        // ✅ 백엔드에서 내려준 redirect URL을 바로 사용
        const redirectPath = await response.text();
        sessionStorage.removeItem("beforeLoginUrl");
        window.location.href = redirectPath;

    } catch (err) {
        console.error("로그인 중 오류:", err);
        errorBox.innerText = "로그인 중 오류가 발생했습니다.";
    }
}

document.addEventListener("DOMContentLoaded", storePreviousUrl);

