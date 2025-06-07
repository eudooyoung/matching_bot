import { termsContent } from '/js/register/terms-member.js';

function openAgreementModal(type) {
  const modal = document.getElementById("agreementModal");
  const title = document.getElementById("modalTitle");
  const content = document.getElementById("modalContent");

  if (termsContent[type]) {
    title.textContent = termsContent[type].title;
    content.innerHTML = termsContent[type].content;
  }

  modal.style.display = "flex";
}

function closeModal() {
  document.getElementById("agreementModal").style.display = "none";
}

window.openAgreementModal = openAgreementModal;
window.closeModal = closeModal;

window.addEventListener("click", function (e) {
  const modal = document.getElementById("agreementModal");
  if (e.target === modal) {
    closeModal();
  }
});