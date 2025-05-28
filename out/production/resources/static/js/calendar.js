const year = 2025;
let currentMonth = 4; // 0: Jan, 4: May

const monthYear = document.getElementById('monthYear');
const datesContainer = document.getElementById('dates');

function renderCalendar(month) {
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    monthYear.textContent = `${year}년 ${month + 1}월`;
    datesContainer.innerHTML = '';

    for (let i = 0; i < firstDay; i++) {
        const empty = document.createElement('div');
        empty.classList.add('empty');
        datesContainer.appendChild(empty);
    }

    for (let i = 1; i <= lastDate; i++) {
        const cell = document.createElement('div');
        cell.classList.add('calendar-cell');

        const dateText = document.createElement('div');
        dateText.className = 'date-number';
        dateText.textContent = i;
        cell.appendChild(dateText);

        const thisDate = `${year}-${String(month + 1).padStart(2, '0')}-${String(i).padStart(2, '0')}`;

        const posting = jobPostings.find(post => post.date === thisDate);
        if (posting) {
            const jobTitle = document.createElement('div');
            jobTitle.className = 'job-title';
            jobTitle.textContent = posting.title;
            cell.appendChild(jobTitle);
        }

        datesContainer.appendChild(cell);
    }
}

function changeMonth(delta) {
    currentMonth += delta;
    if (currentMonth < 0) currentMonth = 11;
    if (currentMonth > 11) currentMonth = 0;
    renderCalendar(currentMonth);
}

renderCalendar(currentMonth);
