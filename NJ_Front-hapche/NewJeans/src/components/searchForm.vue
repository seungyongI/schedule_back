<template>
  <div class="wrapper">
    <h1>검색 결과</h1>
    <p v-if="loading">Loading...</p>
    <p v-if="error">{{ error }}</p>

    <div v-if="results.length > 0 && !loading && !error" class="results-container">
      <div v-for="item in results" :key="item.idx" class="result-item" :class="{ schedule: item.type === 'SCHEDULE', diary: item.type === 'DIARY' }">
        <h3>{{ item.type === 'SCHEDULE' ? '일정' : '일기' }}: {{ item.title }}</h3>

        <template v-if="item.type === 'SCHEDULE'">
          <p><strong>시작 날짜:</strong> {{ formatDate(item.startDate) }}</p>
          <p><strong>내용:</strong> {{ item.content }}</p>
        </template>

        <template v-else-if="item.type === 'DIARY'">
          <p><strong>날짜:</strong> {{ formatDate(item.date) }}</p>
          <p><strong>카테고리:</strong> {{ item.category }}</p>
        </template>
      </div>
    </div>

    <div v-else-if="!loading && !error">
      <p>검색 결과가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { BASE_URL } from '@/config';

const route = useRoute();
const router = useRouter();
const results = ref([]);
const loading = ref(false);
const error = ref(null);
const searchQuery = ref(route.query.query || '');
const selectedOption = ref(route.query.filterType || 'ALL');

// 날짜 포맷 함수
const formatDate = date => {
  return date ? new Date(date).toLocaleDateString() : '';
};

// 검색 요청 함수 분리
const fetchResults = async () => {
  loading.value = true;
  error.value = null;

  try {
    const response = await axios.get(`${BASE_URL}/home/search`, {
      params: { query: searchQuery.value, filterType: selectedOption.value },
    });
    results.value = response.data;
  } catch (err) {
    console.error('검색 오류:', err);
    error.value = '검색 중 오류가 발생했습니다. 다시 시도해 주세요.';
  } finally {
    loading.value = false;
  }
};

// 컴포넌트가 마운트될 때 초기 검색
onMounted(fetchResults);

// route.query가 변경될 때마다 검색 실행
watch(
  () => route.query,
  newQuery => {
    searchQuery.value = newQuery.query || '';
    selectedOption.value = newQuery.filterType || 'ALL';
    fetchResults();
  },
);

// 검색 버튼 클릭 시 호출
const goToSearchForm = () => {
  if (!searchQuery.value.trim()) return; // 검색어가 비어있으면 검색 실행하지 않음
  router.push({
    path: '/searchForm',
    query: { query: searchQuery.value, filterType: selectedOption.value },
  });
};
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
  margin: 0 6vh;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.results-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

.result-item {
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s,
    background-color 0.3s;
  cursor: pointer;
}

.result-item:hover {
  transform: translateY(-5px);
  background-color: #f9f9f9;
}

.result-item.schedule {
  border-left: 4px solid #ff9933; /* 일정 구분을 위한 왼쪽 테두리 */
}

.result-item.diary {
  border-left: 4px solid #2196f3; /* 일기 구분을 위한 왼쪽 테두리 */
}

.result-item h3 {
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  color: #333;
}

.result-item p {
  margin: 0.25rem 0;
  color: #555;
}

.result-item p strong {
  color: #333;
}
</style>
