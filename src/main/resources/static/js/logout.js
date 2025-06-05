async function logout() {
    const refreshToken = localStorage.getItem("refreshToken");

    try {
        await fetch("/api/v1/auth/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${refreshToken}`
            }
        });
    } catch (err) {
        console.warn("로그아웃 서버 처리 실패 (무시 가능):", err);
    }

    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    window.location.href = "/"; // 홈으로 이동
}

window.logout= logout;
