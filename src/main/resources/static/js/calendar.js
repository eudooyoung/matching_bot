let currentMonth = new Date().getMonth();
const year = new Date().getFullYear();
let jobPostings = [];

async function fetchJobs(month) {
    try {
        const res = await fetch(`/api/calendar?year=${year}&month=${month + 1}`);
        if (!res.ok) throw new Error("채용 공고 불러오기 실패");
        jobPostings = await res.json();
    } catch (error) {
        console.error("채용 공고 데이터를 가져오지 못했습니다:", error);
        jobPostings = [];
    }
    renderCalendar(month);
}

function renderCalendar(month) {
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();
    const datesContainer = document.getElementById("dates");
    const monthYear = document.getElementById("monthYear");

    const today = new Date();

    monthYear.textContent = `${year}년 ${month + 1}월`;
    datesContainer.innerHTML = '';

    for (let i = 0; i < firstDay; i++) {
        const empty = document.createElement("div");
        empty.classList.add("calendar-cell");
        datesContainer.appendChild(empty);
    }

    for (let i = 1; i <= lastDate; i++) {
        const thisDeadline = `${year}-${String(month + 1).padStart(2, '0')}-${String(i).padStart(2, '0')}`;
        const cell = document.createElement("div");
        cell.classList.add("calendar-cell");

        if (
            year === today.getFullYear() &&
            month === today.getMonth() &&
            i === today.getDate()
        ) {
            cell.classList.add("today");
        }

        const dateText = document.createElement("div");
        dateText.className = "date-number";
        dateText.textContent = i;
        cell.appendChild(dateText);

        const postingsToday = jobPostings.filter(post => post.endDate?.slice(0, 10) === thisDeadline);

        postingsToday.forEach(post => {
            const jobTitle = document.createElement("div");
            jobTitle.className = "job-title";

            const deadline = new Date(post.endDate);
            const todayMidnight = new Date(today.getFullYear(), today.getMonth(), today.getDate());
            const diffTime = deadline - todayMidnight;
            const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

            if (diffDays < 0) {
                jobTitle.classList.add("expired");
            } else if (diffDays <= 4) {
                jobTitle.classList.add("closing-soon");
            } else {
                jobTitle.classList.add("default");
            }

            jobTitle.textContent = post.title;
            jobTitle.onclick = () => {
                window.location.href = `/job/${post.id}`;
            };
            cell.appendChild(jobTitle);
        });

        datesContainer.appendChild(cell);
    }
}

function changeMonth(delta) {
    currentMonth += delta;
    if (currentMonth < 0) currentMonth = 11;
    if (currentMonth > 11) currentMonth = 0;
    fetchJobs(currentMonth);
}

fetchJobs(currentMonth);