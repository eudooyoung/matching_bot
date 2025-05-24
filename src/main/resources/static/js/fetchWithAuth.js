// fetchWithAuth.js
async function fetchWithAuth(url, options = {}, retry = true) {
  try {
    const response = await fetch(url, {
      ...options,
      credentials: 'include'
    });

    if (response.status === 401 && retry) {
      const refreshResponse = await fetch('/auth/refresh', {
        method: 'POST',
        credentials: 'include'
      });

      if (refreshResponse.ok) {
        return fetchWithAuth(url, options, false);
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
