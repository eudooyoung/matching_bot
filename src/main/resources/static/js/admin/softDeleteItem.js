// /static/js/common/softDeleteItem.js

async function deactivateItem({ id, url, onSuccess }) {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    try {
        const response = await fetchWithAuth(`${url}/${id}`, {
            method: "DELETE",
            credentials: "include", // ✅ 쿠키 기반 인증
        });

        if (response.ok) {
            alert("삭제되었습니다.");
            onSuccess?.();
        } else if (response.status === 401) {
            alert("로그인이 필요합니다.");
        } else {
            const message = await response.text();
            alert("삭제 실패: " + message);
        }
    } catch (error) {
        console.error("삭제 중 오류 발생:", error);
        alert("네트워크 오류로 삭제에 실패했습니다.");
    }
}
