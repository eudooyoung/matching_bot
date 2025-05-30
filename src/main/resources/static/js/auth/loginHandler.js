function isLoginPage(url) {
    return url.includes("/admin/login") || url.includes("/auth/login");
}

function storePreviousUrl() {
    const previousUrl = document.referrer;
    if (previousUrl && !isLoginPage(previousUrl)) {
        sessionStorage.setItem("beforeLoginUrl", previousUrl);
    }
}

async function handleLogin(emailId, passwordId, errorMsgId, role) {
    const email = document.getElementById(emailId).value;
    const password = document.getElementById(passwordId).value;

    try {
        const response = await fetch("/api/v1/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password, role })
        });

        if (response.ok) {
            const redirectUrl = sessionStorage.getItem("beforeLoginUrl") || "/main";
            sessionStorage.removeItem("beforeLoginUrl");
            window.location.href = redirectUrl;
        } else {
            const errorJson = await response.json();
            const msg = errorJson.message || "로그인 중 오류가 발생했습니다.";
            document.getElementById(errorMsgId).innerText = "로그인 실패: " + msg;
        }
    } catch (err) {
        console.error("로그인 중 오류:", err);
        document.getElementById(errorMsgId).innerText = "로그인 중 오류가 발생했습니다.";
    }
}

document.addEventListener("DOMContentLoaded", storePreviousUrl);
