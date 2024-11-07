<template>
  <div class="diary-view-container">
    <div class="toolbar">
      <div class="category-buttons">
        <button v-for="category in categories" :key="category.value" @click="fetchDiaries(category.value)" :class="{ selected: selectedCategory === category.value }">
          {{ category.label }}
        </button>
      </div>
      <div class="sort-buttons">
        <button @click="sortDiaries('LATEST')" :class="{ selected: sortOrder === 'LATEST' }">최신순</button>
        <button @click="sortDiaries('OLDEST')" :class="{ selected: sortOrder === 'OLDEST' }">오래된순</button>
      </div>
    </div>

    <div v-if="paginatedDiaries.length > 0" class="diary-list">
      <div v-for="(diary, index) in paginatedDiaries" :key="index" class="diary-item" @click="goToDiaryDetail(diary.idx)">
        <h3>{{ diary.title }}</h3>
        <p>{{ getCategoryLabel(diary.category) }}</p>
        <p>{{ diary.date }}</p>
      </div>
    </div>
    <div v-else>
      <p>해당 카테고리의 일기가 없습니다.</p>
    </div>

    <div class="pagination">
      <button v-for="page in totalPages" :key="page" @click="goToPage(page)" :class="{ active: currentPage === page }">
        {{ page }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { BASE_URL } from '@/config';

const router = useRouter();
const diaries = ref([]);
const sortOrder = ref('LATEST');
const selectedCategory = ref('ALL');
const userIdx = 1;
const currentPage = ref(1); // 현재 페이지 번호
const itemsPerPage = 6; // 한 페이지에 보여줄 일기 개수

const categories = [
  { label: '전체보기', value: 'ALL' },
  { label: '#일기', value: 'DAILY' },
  { label: '#성장일지', value: 'GROWTH' },
  { label: '#운동', value: 'EXERCISE' },
  { label: '#여행', value: 'TRIP' },
  { label: '#기타', value: 'ETC' },
];

const fetchDiaries = async category => {
  selectedCategory.value = category;
  let url = category === 'ALL' ? `${BASE_URL}/diary/${userIdx}/ALL` : `${BASE_URL}/diary/${userIdx}/${category}`;
  try {
    const response = await axios.get(url);
    diaries.value = response.data;
    sortOrder.value = 'LATEST';
  } catch (error) {
    console.error('일기 조회 중 오류 발생:', error.response?.data || error.message);
  }
};

const sortDiaries = order => {
  sortOrder.value = order;
};

const sortedDiaries = computed(() => {
  return [...diaries.value].sort((a, b) => {
    return sortOrder.value === 'LATEST' ? new Date(b.date) - new Date(a.date) : new Date(a.date) - new Date(b.date);
  });
});

const paginatedDiaries = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return sortedDiaries.value.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(sortedDiaries.value.length / itemsPerPage);
});

const goToDiaryDetail = idx => {
  router.push({ name: 'DiaryDetail', params: { idx } });
};

const goToPage = page => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

const getCategoryLabel = categoryValue => {
  const category = categories.find(cat => cat.value === categoryValue);
  return category ? category.label : '기타';
};

onMounted(() => {
  fetchDiaries('ALL');
});
</script>

<style scoped>
.diary-item {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.diary-item:hover {
  background-color: #f0f0f0;
  color: #333;
}

.diary-view-container {
  width: 70%;
  /* height: 100%; */
  padding: 20px;
  border-radius: 10px;
  margin: 2vh 5vh;
  background-color: white;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.toolbar {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  margin-top: 50px;
}

.category-buttons {
  display: flex;
  gap: 10px;
  margin-right: 70px;
}

.category-buttons button {
  padding: 10px 20px;
  border: 1px solid #ccc;
  background-color: white;
  color: black;
  border-radius: 20px;
  cursor: pointer;
}

.category-buttons button.selected {
  background-color: black;
  color: white;
}

.sort-buttons {
  display: flex;
  gap: 10px;
}

.sort-buttons button {
  padding: 10px 20px;
  border: 1px solid #ccc;
  background-color: white;
  color: black;
  border-radius: 20px;
  cursor: pointer;
}

.sort-buttons button.selected {
  background-color: black;
  color: white;
}

.diary-list {
  width: 830px;
  margin: 0 auto;
}

.diary-item {
  padding: 15px 0;
  border-bottom: 1px dashed #ccc;
  display: grid;
  grid-template-columns: 1fr 100px 120px; /* 타이틀, 카테고리, 날짜의 고정 크기 설정 */
  align-items: center;
  gap: 20px;
}

.diary-item h3 {
  text-align: left;
  width: 350px;
}

.diary-title {
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.diary-category {
  text-align: right;
  color: gray;
}

.diary-date {
  text-align: right;
  color: gray;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  padding: 10px;
  margin: 0 5px;
  background-color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.pagination button.active {
  background-color: black;
  color: white;
}
</style>
