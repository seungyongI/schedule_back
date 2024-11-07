<template>
  <h2>카테고리 통계</h2>
  <canvas id="myPieChart"></canvas>
</template>

<script>
import { PieController, ArcElement, Tooltip, Legend, Chart } from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';
import axios from 'axios';
import { BASE_URL } from '@/config';

Chart.register(PieController, ArcElement, Tooltip, Legend, ChartDataLabels);

export default {
  data() {
    return {
      categoryLabels: ['DAILY', 'GROWTH', 'EXERCISE', 'TRIP', 'ETC'],
      categoryData: [],
    };
  },
  async mounted() {
    try {
      const userIdx = 1; // 적절한 userIdx 설정
      const response = await axios.get(`${BASE_URL}/statistics/category?userIdx=${userIdx}`);
      console.log("Response Data:", response.data);

      // 데이터를 배열로 변환하여 처리
      const statisticsList = Array.isArray(response.data) ? response.data : [];
      this.processData(statisticsList);

      // 데이터 확인
      console.log("Category Data:", this.categoryData);

      const ctx = document.getElementById('myPieChart').getContext('2d');
      new Chart(ctx, {
        type: 'pie',
        data: {
          labels: this.categoryLabels,
          datasets: [
            {
              label: 'Category Statistics',
              data: this.categoryData,
              backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
            },
          ],
        },
        options: {
          plugins: {
            datalabels: {
              color: '#fff',
              formatter: (value, context) => {
                const label = context.chart.data.labels[context.dataIndex];
                return `${label}\n${value.toFixed(1)}%`; // 소수점 첫째 자리까지 표시
              },
              font: {
                weight: 'bold',
                size: 12,
              },
              anchor: 'end', // 레이블 위치 조정
              align: 'start', // 레이블 위치 조정
            },
            legend: {
              display: true,
              position: 'bottom', // 범례 위치 설정
            },
          },
          responsive: true,
          maintainAspectRatio: false,
        },
      });
    } catch (error) {
      console.error("Failed to fetch category statistics:", error);
    }
  },
  methods: {
    processData(statisticsList) {
      // statisticsList가 배열인지 확인하고, 배열인 경우만 데이터 매핑
      if (Array.isArray(statisticsList)) {
        this.categoryData = this.categoryLabels.map((label) => {
          const categoryStat = statisticsList.find(stat => stat.category === label);
          return categoryStat ? categoryStat.percentage : 0;
        });
      } else {
        console.error("Invalid data structure received:", statisticsList);
      }
    },
  },
};
</script>

<style scoped>
canvas {
  max-width: 300px;
  max-height: 300px;
}
</style>
