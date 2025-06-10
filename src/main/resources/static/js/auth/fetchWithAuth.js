// fetchWithAuth.js
window.fetchWithAuth = async function fetchWithAuth(url, options = {}, retry = true) {
    try {
        // 👇 8081로 리디렉션 처리
       /* if (url.startsWith("/calculate-similarity")) {
            url = "http://localhost:8081" + url;
            console.log("📡 매칭률 요청 URL:", url);  // 여기에 로그 추가
        }*/

        const defaultOptions = {
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                ...(options.headers || {}) // 사용자가 넘긴 headers 병합
            }
        };

        const response = await fetch(url, {
            ...defaultOptions, // default를 뒤에 두면 credentials가 덮일 수 있어서 이 순서
            ...options,
        });

        if (response.status === 401 && retry) {
            console.warn("401 발생 → api/v1/auth/refresh 요청 시도 중");
            const refreshResponse = await fetch('/api/v1/auth/refresh', {
                method: 'POST',
                credentials: 'include'
            });
            console.log("refresh 응답 상태:", refreshResponse.status);

            if (refreshResponse.ok) {
                return window.fetchWithAuth(url, options, false);
            } else {
                alert("세션이 만료되었습니다. 다시 로그인 해주세요.");
                window.location.href = "/login";
            }
        }

        return response;

    } catch (err) {
        console.error("fetchWithAuth 에러:", err);
        throw err;
    }
}
