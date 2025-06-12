// fetchWithAuth.js
window.fetchWithAuth = async function fetchWithAuth(url, options = {}, retry = true) {
    try {
        // 👇 8081로 리디렉션 처리
        /* if (url.startsWith("/calculate-similarity")) {
             url = "http://localhost:8081" + url;
             console.log("📡 매칭률 요청 URL:", url);  // 여기에 로그 추가
         }*/

        const mergedHeaders = {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        };

        const response = await fetch(url, {
            ...options,
            credentials: 'include',
            headers: mergedHeaders,
        });

        if (response.status === 401 && retry) {
            const refreshResponse = await fetch('/api/v1/auth/refresh', {
                method: 'POST',
                credentials: 'include'
            });

            if (refreshResponse.ok) {
                return window.fetchWithAuth(url, options, false);
            } else {
                alert("세션이 만료되었습니다. 다시 로그인 해주세요.");
                window.location.href = "/auth/login";
            }
        }

        return response;

    } catch (err) {
        console.error("fetchWithAuth 에러:", err);
        throw err;
    }
}
