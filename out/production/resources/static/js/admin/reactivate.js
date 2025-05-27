async function reactivateItem({ id, url, onSuccess }) {
    if (!confirm("정말 복구하시겠습니까?")) return;

    try {
        const response = await fetchWithAuth(`${url}/${id}/reactivate`, {
            method: "PATCH",
            credentials: "include"
        });

        if (response.ok) {
            alert("복구되었습니다.");
            onSuccess?.();
        } else {
            alert("복구 실패: " + (await response.text()));
        }
    } catch (err) {
        alert("네트워크 오류로 복구에 실패했습니다.");
        console.error(err);
    }
}
