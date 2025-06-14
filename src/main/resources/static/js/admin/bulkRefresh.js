async function bulkRefresh(url) {
    const selected = Array.from(document.querySelectorAll(".check-row:checked")).map(cb => cb.value);

    if (selected.length === 0) {
        alert("추출할 항목을 선택하세요.");
        return;
    }

    if (!confirm("선택한 기업의 평가 보고서를 다시 생성하시겠습니까?")) return;

    try {
        const res = await fetchWithAuth(url, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ids: selected})
        });
        if (!data.success) {
            alert(`일부 실패: 실패한 ID - ${data.failedIds.join(', ')}`);
        } else {
            alert("모든 보고서 생성이 완료되었습니다!");
        }
        location.reload();
    } catch (e) {
        alert(`요청 실패: ${e.message}`);
    }
}
