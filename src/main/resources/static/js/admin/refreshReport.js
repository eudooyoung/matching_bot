async function refreshReport({id, url, onSuccess}) {
    if (!confirm("기업 평가 보고서를 생성 하시겠습니까?")) return;

    try {
        const response = await fetchWithAuth(`${url}/${id}/refresh-report`, {
            method: "POST",
            credentials: "include", // ✅ 쿠키 기반 인증
        });

        if (response.ok) {
            alert("보고서 생성 요청이 접수되었습니다.");
            onSuccess?.();
        } else if (response.status === 401) {
            alert("로그인이 필요합니다.");
        } else {
            const message = await response.text();
            alert("삭제 실패: " + message);
        }

    } catch (error) {
        console.error("삭제 중 오류 발생:", error);
        alert("네트워크 오류로 기업 평가서 생성을 실패했습니다.");
    }
}
