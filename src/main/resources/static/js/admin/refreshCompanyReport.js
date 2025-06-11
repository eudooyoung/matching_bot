function refreshCompanyReport(companyId) {
    if (!confirm("해당 기업의 리포트를 다시 생성하시겠습니까?")) return;

    fetchWithAuth(`/api/v1/admin/company/${companyId}/refresh-report`, {
        method: "POST"
    }).then(res => {
        if (!res.ok) return res.text().then(msg => { throw new Error(msg); });
        alert("리포트가 생성되었습니다.");
        location.reload();
    }).catch(err => {
        alert("리포트 생성 실패: " + err.message);
    });
}
