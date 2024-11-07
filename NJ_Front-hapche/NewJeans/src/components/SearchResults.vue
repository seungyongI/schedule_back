<template>
  <div class="wrapper">
    <h1>검색 결과</h1>
    <p v-if="loading">Loading...</p>
    <p v-if="error">{{ error }}</p>

    <div v-if="results.length > 0 && !loading && !error">
      <div v-for="item in results" :key="item.idx">
        <h3>{{ item.type === 'SCHEDULE' ? '일정' : '일기' }}: {{ item.title }}</h3>

        <!-- 일정 데이터 표시 -->
        <template v-if="item.type === 'SCHEDULE'">
          <p>시작 날짜: {{ formatDate(item.startDate) }}</p>
          <p>내용: {{ item.content }}</p>
        </template>

        <!-- 일기 데이터 표시 -->
        <template v-else-if="item.type === 'DIARY'">
          <p>날짜: {{ formatDate(item.date) }}</p>
          <p>카테고리: {{ item.category }}</p>
        </template>
      </div>
    </div>

    <div v-else-if="!loading && !error">
      <p>검색 결과가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { BASE_URL } from '@/config';

const route = useRoute();
const results = ref([]);
const loading = ref(false);
const error = ref(null);

// 날짜 포맷 함수
const formatDate = date => {
  return date ? new Date(date).toLocaleDateString() : '';
};

// 컴포넌트가 마운트될 때 검색 요청
onMounted(async () => {
  const query = route.query.query || '';
  const filterType = route.query.filterType || 'ALL';

  loading.value = true;
  error.value = null;

  try {
    const response = await axios.get(`${BASE_URL}/home/search`, {
      params: { query, filterType },
    });
    results.value = response.data;
  } catch (err) {
    console.error('검색 오류:', err);
    error.value = '검색 중 오류가 발생했습니다. 다시 시도해 주세요.';
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.wrapper {
  background-color: white;
  border-radius: 10px;
  perspective: 1000px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: 20px 50px;
  min-height: 500px;
  width: 65%;
  margin: 50px auto;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}
</style>
