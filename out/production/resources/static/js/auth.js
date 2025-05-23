// auth.js
(function () {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    const req = new XMLHttpRequest();
    req.open("GET", "/main", false); // 동기 요청
    req.setRequestHeader("Authorization", "Bearer " + accessToken);
    req.send();

    if (req.status === 200) {
      window.location.href = "/main";
    } else {
      alert("인증 실패");
    }
  } else {
    window.location.href = "/main";
  }
})();
