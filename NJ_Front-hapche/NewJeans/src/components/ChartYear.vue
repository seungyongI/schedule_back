<template>
  <div>
    <h2>연별 통계</h2>
    <div class="header">
      <button @click="changeYear(-1)">〈</button>
      <span>{{ currentYear }}년</span>
      <button @click="changeYear(1)">〉</button>
    </div>
    <canvas ref="combinedChartCanvas" id="combined-chart"></canvas>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, LineController, LineElement, PointElement, LinearScale, Title, CategoryScale, BarController, BarElement } from 'chart.js';
import { BASE_URL } from '@/config';

Chart.register(LineController, LineElement, PointElement, LinearScale, Title, CategoryScale, BarController, BarElement);

export default {
  name: 'CombinedChartYear',
  data() {
    return {
      combinedChart: null,
      currentYear: 2024,
      colors: {
        DAILY: '#ff6384',
        GROWTH: '#36a2eb',
        TRIP: '#ffce56',
        EXERCISE: '#4bc0c0',
        ETC: '#9966ff',
      },
      isLoading: false,
    };
  },
  async mounted() {
    await this.fetchData();
  },
  methods: {
    async fetchData() {
      if (this.isLoading) return;
      this.isLoading = true;

      try {
        const canvasElement = this.$refs.combinedChartCanvas;
        if (!canvasElement) return;

        if (this.combinedChart) {
          this.combinedChart.destroy();
          this.combinedChart = null;
        }

        const response = await axios.get(`${BASE_URL}/statistics/year`, {
          params: { userIdx: 1, year: this.currentYear },
        });
        const data = response.data || [];

        const labels = Array.from({ length: 12 }, (_, i) => `${i + 1}월`);
        const categoryData = {
          DAILY: Array(12).fill(0),
          GROWTH: Array(12).fill(0),
          TRIP: Array(12).fill(0),
          EXERCISE: Array(12).fill(0),
          ETC: Array(12).fill(0),
        };

        const cumulativeCounts = Array(12).fill(0);
        let cumulativeCount = 0;

        for (let month = 0; month < 12; month++) {
          const monthData = data.find(d => parseInt(d.year.split(' ')[1]) - 1 === month);
          if (monthData && monthData.totalDiaryCount) {
            cumulativeCount += monthData.totalDiaryCount;
          }
          cumulativeCounts[month] = cumulativeCount;

          if (monthData && monthData.categoryPercentageMap) {
            for (const [category, percentage] of Object.entries(monthData.categoryPercentageMap)) {
              if (categoryData[category]) {
                categoryData[category][month] = percentage;
              }
            }
          }
        }

        const daysInYear = new Date(this.currentYear, 1, 29).getDate() === 29 ? 366 : 365;

        this.combinedChart = new Chart(canvasElement.getContext("2d"), {
  type: 'bar',
  data: {
    labels,
    datasets: [
      {
        label: '누적 일기 작성 비율 (%)',
        type: 'line',
        data: cumulativeCounts.map(count => ((count / daysInYear) * 100).toFixed(1)), // 소수점 첫째 자리까지 표시
        borderColor: '#3b82f6',
        fill: false,
        tension: 0.1,
        pointRadius: cumulativeCounts.map((value, index, arr) =>
          index === 0 || value === 0 || value === arr[index - 1] ? 0 : 5 // 이전과 같거나 0이면 표시하지 않음
        ),
        pointBackgroundColor: cumulativeCounts.map((value, index, arr) =>
          index === 0 || value === 0 || value === arr[index - 1] ? 'transparent' : '#3b82f6'
        ),
      },
      ...Object.keys(categoryData).map(category => ({
        label: category,
        data: categoryData[category],
        backgroundColor: this.colors[category],
        stack: 'stacked',
        datalabels: {
          display: ctx => ctx.dataset.data[ctx.dataIndex] !== 0, // 0인 경우 표시하지 않음
        },
      })),
    ],
  },
  options: {
    responsive: true,
    plugins: {
      datalabels: {
        display: ctx => {
          const { dataIndex, dataset } = ctx;
          const value = dataset.data[dataIndex];
          const previousValue = dataset.data[dataIndex - 1];

          // 막대그래프에서 0인 경우, 꺾은선 그래프에서 이전과 동일한 수치인 경우 라벨 표시하지 않음
          if (dataset.type === 'bar') {
            return value !== 0;
          } else if (dataset.type === 'line') {
            return dataIndex === 0 || value !== previousValue;
          }
          return true;
        },
        formatter: value => (typeof value === 'number' ? value.toFixed(1) : ''), // 숫자인 경우에만 소수점 표시
        color: '#333',
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: '월',
        },
      },
      y: {
        beginAtZero: true,
        max: 100,
        stacked: true,
      },
    },
  },
});

      } catch (error) {
        console.error('Error loading data:', error);
      } finally {
        this.isLoading = false;
      }
    },
    changeYear(direction) {
      this.currentYear += direction;
      this.fetchData();
    },
  },
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: center;
  align-items: center;
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
