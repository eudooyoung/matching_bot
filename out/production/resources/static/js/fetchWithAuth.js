// fetchWithAuth.js
window.fetchWithAuth = async function fetchWithAuth(url, options = {}, retry = true) {
    try {
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
            const refreshResponse = await fetch('/auth/refresh', {
                method: 'POST',
                credentials: 'include'
            });

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
