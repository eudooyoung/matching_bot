async function showLoadingAndRunBulk(url) {
  const selected = Array.from(document.querySelectorAll(".check-row:checked")).map(cb => cb.value);

  if (selected.length === 0) {
    alert("추출할 항목을 선택하세요.");
    return;
  }

  if (!confirm("선택한 기업의 평가 보고서를 생성하시겠습니까?")) return;

  document.getElementById("loadingOverlay").style.display = "flex";

  try {
    const res = await fetchWithAuth(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ids: selected })
    });

    if (!res.ok) throw new Error("서버 오류");
    location.reload();
  } catch (e) {
    alert(`요청 실패: ${e.message}`);
    document.getElementById("loadingOverlay").style.display = "none";
  }
}
