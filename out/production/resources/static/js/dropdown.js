function toggleDropdown(button) {
  const wrapper = button.closest('.dropdown-wrapper');
  wrapper.classList.toggle('active');

  // 다른 드롭다운 닫기
  document.querySelectorAll('.dropdown-wrapper').forEach(el => {
    if (el !== wrapper) el.classList.remove('active');
  });
}

// 바깥 클릭 시 드롭다운 닫기
document.addEventListener('click', function (e) {
  const isDropdown = e.target.closest('.dropdown-wrapper');
  if (!isDropdown) {
    document.querySelectorAll('.dropdown-wrapper').forEach(el => el.classList.remove('active'));
  }
});
