<template>
  <div>
    <h2>월별 통계</h2>
    <div class="header">
      <button @click="goToPreviousMonth">〈</button>
      <span>{{ displayDate }}</span>
      <button @click="goToNextMonth">〉</button>
    </div>
    <canvas ref="combinedChartCanvas" id="combined-chart-month"></canvas>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, LineController, LineElement, PointElement, LinearScale, Title, CategoryScale, BarController, BarElement } from 'chart.js';
import { BASE_URL } from '@/config';

Chart.register(LineController, LineElement, PointElement, LinearScale, Title, CategoryScale, BarController, BarElement);

export default {
  name: 'CombinedChartMonth',
  data() {
    const today = new Date();
    return {
      combinedChart: null,
      currentYear: today.getFullYear(),
      currentMonth: today.getMonth() + 1,
      colors: {
        DAILY: '#ff6384',
        GROWTH: '#36a2eb',
        TRIP: '#ffce56',
        EXERCISE: '#4bc0c0',
        ETC: '#9966ff'
      },
      isLoading: false,
    };
  },
  computed: {
    displayDate() {
      return `${this.currentYear}년 ${this.currentMonth}월`;
    },
  },
  async mounted() {
    await this.loadChartData();
  },
  methods: {
    async loadChartData() {
      if (this.isLoading) return;
      this.isLoading = true;

      try {
        const canvasElement = this.$refs.combinedChartCanvas;
        if (!canvasElement) {
          console.error("Canvas element not found");
          return;
        }

        if (this.combinedChart) {
          this.combinedChart.destroy();
          this.combinedChart = null;
        }

        const response = await axios.get(`${BASE_URL}/statistics/month`, {
          params: { userIdx: 1, year: this.currentYear, month: this.currentMonth },
        });

        const data = response.data || [];
        const daysInMonth = new Date(this.currentYear, this.currentMonth, 0).getDate();
        const dayLabels = Array.from({ length: daysInMonth }, (_, i) => `${i + 1}`);
        const categoryData = {
          DAILY: Array(daysInMonth).fill(0),
          GROWTH: Array(daysInMonth).fill(0),
          TRIP: Array(daysInMonth).fill(0),
          EXERCISE: Array(daysInMonth).fill(0),
          ETC: Array(daysInMonth).fill(0),
        };
        const totalPercentages = Array(daysInMonth).fill(0);

        for (const entry of data) {
          const day = entry.date ? parseInt(entry.date.split('-')[2], 10) - 1 : null;
          if (day !== null) {
            for (const [category, percentage] of Object.entries(entry.categoryPercentageMap || {})) {
              if (categoryData[category]) {
                categoryData[category][day] = percentage;
              }
            }
            totalPercentages[day] = entry.totalPercentage;
          }
        }

        const maxCount = daysInMonth;

        this.combinedChart = new Chart(canvasElement.getContext("2d"), {
          type: 'bar',
          data: {
            labels: dayLabels,
            datasets: [
              {
                label: '전체 작성 비율 (%) - 꺾은선',
                data: totalPercentages.map(count => (count / maxCount) * 100),
                borderColor: '#3b82f6',
                fill: false,
                type: 'line',
                yAxisID: 'y2',
                tension: 0.4,
                pointRadius: totalPercentages.map((value, index, arr) =>
                  index === 0 || value !== arr[index - 1] ? 5 : 0
                ),
                pointBackgroundColor: totalPercentages.map((value, index, arr) =>
                  index === 0 || value !== arr[index - 1] ? '#3b82f6' : 'transparent'
                ),
              },
              ...Object.keys(categoryData).map(category => ({
                label: category,
                data: categoryData[category],
                backgroundColor: this.colors[category],
                type: 'bar',
                stack: 'stacked',
              })),
            ],
          },
          options: {
            responsive: true,
            scales: {
              x: {
                ticks: {
                  autoSkip: false,
                  maxRotation: 0,
                },
              },
              y: {
                beginAtZero: true,
                max: 100,
                stacked: true,
                title: {
                  display: true,
                  text: '비율 (%)',
                },
              },
              y2: {
                beginAtZero: true,
                max: maxCount,
                position: 'right',
                grid: {
                  drawOnChartArea: false,
                },
              },
            },
          },
        });
      } catch (error) {
        console.error("Error loading data:", error);
      } finally {
        this.isLoading = false;
      }
    },
    goToPreviousMonth() {
      if (this.currentMonth === 1) {
        this.currentMonth = 12;
        this.currentYear -= 1;
      } else {
        this.currentMonth -= 1;
      }
      this.loadChartData();
    },
    goToNextMonth() {
      if (this.currentMonth === 12) {
        this.currentMonth = 1;
        this.currentYear += 1;
      } else {
        this.currentMonth += 1;
      }
      this.loadChartData();
    },
  },
};
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  font-size: 18px;
}

.header button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  padding: 0 15px;
}

canvas {
  width: 100%;
  max-width: 800px;
  height: 400px;
}
</style>
