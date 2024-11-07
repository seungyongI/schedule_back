<template>
  <div v-if="selectedDiary" class="diary-detail">
    <h2>{{ selectedDiary.title }}</h2>
    <p>{{ getCategoryLabel(selectedDiary.category) }} - {{ selectedDiary.date }}</p>
    <p>{{ selectedDiary.content }}</p>

    <!-- 이미지 섹션 -->
    <div v-if="selectedDiary.images && selectedDiary.images.length > 0" class="diary-images">
      <div v-for="(image, index) in selectedDiary.images" :key="index" class="image-container">
        <img :src="image" alt="Diary Image" class="diary-image" />
      </div>
    </div>

    <button @click="goBack">목록으로 돌아가기</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import { BASE_URL } from '@/config';

const router = useRouter();
const route = useRoute();
const selectedDiary = ref(null);
const IMAGE_BASE_URL = 'http://112.222.157.156:10004'; // 고정 이미지 경로

const fetchDiaryDetail = async () => {
  const idx = route.params.idx;
  try {
    const response = await axios.get(`${BASE_URL}/diary/${idx}`);
    const data = response.data;

    // 각 이미지 경로 앞에 고정 URL을 추가
    data.images = data.images.map(image => `${IMAGE_BASE_URL}${image}`);
    selectedDiary.value = data;
  } catch (error) {
    console.error('일기 상세 조회 중 오류 발생:',error.response?.data || error.message);
  }
};

const goBack = () => {
  router.push({ name: 'diary' });
};

const getCategoryLabel = categoryValue => {
  const categories = [
    { label: '전체보기', value: 'ALL' },
    { label: '#일기', value: 'DAILY' },
    { label: '#성장일지', value: 'GROWTH' },
    { label: '#운동', value: 'EXERCISE' },
    { label: '#여행', value: 'TRIP' },
    { label: '#기타', value: 'ETC' },
  ];
  const category = categories.find(cat => cat.value === categoryValue);
  return category ? category.label : '기타';
};

onMounted(() => {
  fetchDiaryDetail();
});
</script>

<style scoped>
.diary-detail {
  background-color: white;
  border-radius: 10px;
  perspective: 1000px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: 20px 50px;
  min-height: 500px;
  width: 65%;
  margin: 2vh 5vh;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

/* 버튼 스타일 */
button {
  background-color: #363636; /* 밝은 파란색 */
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
  transition:
    background-color 0.3s,
    transform 0.2s;
  margin-top: 15px;
  align-self: flex-end; /* 버튼 오른쪽 정렬 */
}

button:hover {
  background-color: #646369; /* 더 어두운 파란색 */
  transform: translateY(-2px); /* 살짝 위로 이동 */
}

/* 이미지 섹션 */
.diary-images {
  margin-top: 25px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); /* 반응형 그리드 */
  gap: 20px; /* 이미지 간격 */
}

.image-container {
  overflow: hidden;
  border-radius: 10px;
  transition: transform 0.3s;
}

.image-container:hover {
  transform: scale(1.05); /* 호버 시 확대 효과 */
}

.diary-image {
  width: 100%;
  height: auto;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  object-fit: cover; /* 이미지 크기에 맞게 잘림 */
}

/* 텍스트 스타일 */
p {
  font-size: 1rem;
  line-height: 1.6;
  color: #333;
  margin-bottom: 10px;
}

h2 {
  font-size: 2rem;
  font-weight: bold;
  color: #444;
  margin-bottom: 15px;
}
</style>
