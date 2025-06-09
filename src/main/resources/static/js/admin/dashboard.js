document.addEventListener("DOMContentLoaded", () => {
    // Chart.js 설정
    Chart.register(ChartDataLabels);

    const userTypeLabels = window.userTypeLabels;
    const resumeJobLabels = window.resumeJobLabels;

    const userTypeDataByPeriod = window.userTypeDataByPeriod;
    const resumeJobDataByPeriod = window.resumeJobDataByPeriod;

    const userCtx = document.getElementById("userTypePieChart").getContext("2d");
    const resumeCtx = document.getElementById("resumeJobPieChart").getContext("2d");

    const chartOptions = {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
            legend: {position: 'bottom'},
            tooltip: {enabled: false},
            datalabels: {
                color: '#fff',
                formatter: (value, context) => {
                    const label = context.chart.data.labels[context.dataIndex];
                    return `${label}\n${value}`;
                },
                font: {
                    size: 16,
                    weight: 'bold'
                }
            }
        }
    };

    const userChart = new Chart(userCtx, {
        type: 'pie',
        data: {
            labels: userTypeLabels,
            datasets: [{
                data: userTypeDataByPeriod.total,
                backgroundColor: ['#fc7b1e', '#4e67a7']
            }]
        },
        options: chartOptions
    });

    const resumeChart = new Chart(resumeCtx, {
        type: 'pie',
        data: {
            labels: resumeJobLabels,
            datasets: [{
                data: resumeJobDataByPeriod.total,
                backgroundColor: ['#fc7b1e', '#4e67a7']
            }]
        },
        options: chartOptions
    });

    function updateChart(chartType, period) {
        if (chartType === "user") {
            userChart.data.datasets[0].data = userTypeDataByPeriod[period];
            userChart.update();
        } else if (chartType === "resume") {
            resumeChart.data.datasets[0].data = resumeJobDataByPeriod[period];
            resumeChart.update();
        }
    }

    const periodButtons = document.querySelectorAll(".chart-sync-tabs .card-period-tab");
    const allCards = document.querySelectorAll(".stats-grid .card");

    periodButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            const period = btn.getAttribute("data-period");

            periodButtons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            allCards.forEach(card => {
                card.querySelectorAll("li").forEach(li => li.classList.remove("active-value"));
                const target = card.querySelector(`li[data-period="${period}"]`);
                if (target) target.classList.add("active-value");
            });

            // ✅ 이 부분이 꼭 필요
            updateChart("user", period);
            updateChart("resume", period);
        });
    });

    // 페이지 로드시: 전체 <p> 강조
    allCards.forEach(card => {
        const totalP = card.querySelector('li[data-period="total"]');
        if (totalP) totalP.classList.add("active-value");
    });
    updateChart("user", "total");
    updateChart("resume", "total");
});
