<template>
  <div id="graphView">
    <PieChart />
    <LineAndBarChartMonth />
    <LineAndBarChartYear />
  </div>
</template>

<script>
import PieChart from '../components/PieChart.vue';
import LineAndBarChartMonth from '../components/ChartMonth.vue';
import LineAndBarChartYear from '../components/ChartYear.vue';

export default {
  components: {
    PieChart,
    LineAndBarChartMonth,
    LineAndBarChartYear,
  },
};
</script>

<style>
#graphView {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 90%;
  max-width: 1200px;
  gap: 40px;
  margin: 0 auto;
}
</style>
