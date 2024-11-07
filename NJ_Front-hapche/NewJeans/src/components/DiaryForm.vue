<template>
  <div class="diary-form">
    <form @submit.prevent="submitDiary">
      <!-- Title과 Category -->
      <div class="row">
        <div class="title-category-row">
          <div class="title-section">
            <label for="title" style="width: 50px">제목</label>
            <input id="title" v-model="title" class="input-field" />
          </div>

          <div class="category-section">
            <select v-model="category" id="category" class="input-field" style="width: 114px">
              <option value="DAILY">#일기</option>
              <option value="GROWTH">#성장일지</option>
              <option value="EXERCISE">#운동</option>
              <option value="TRIP">#여행</option>
              <option value="ETC">#기타</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Date -->
      <div class="row">
        <label for="date" style="width: 80px">작성일</label>
        <input id="date" v-model="date" type="date" class="input-field" />
      </div>

      <!-- Note -->
      <div class="row">
        <label for="content" style="width: 80px">내용</label>
        <textarea id="content" v-model="content" placeholder="Enter your note" class="input-field textarea-field"></textarea>
      </div>

      <!-- 이미지 미리보기 -->
      <div class="row">
        <label for="image" style="width: 80px">이미지</label>
        <input id="image" type="file" @change="handleImageUpload" multiple class="input-field" />
      </div>

      <div class="image-preview">
        <div v-for="(image, index) in images" :key="index" class="image-container">
          <img :src="image.url" alt="Preview" />
        </div>
      </div>

      <div class="button-row">
        <!-- 저장 버튼 -->
        <button type="submit" class="submit-button">
          <font-awesome-icon :icon="['fas', 'check']" style="font-size: 24px; color: white" />
        </button>

        <!-- 취소 버튼 -->
        <button type="button" @click="cancelForm" class="cancel-button">
          <font-awesome-icon :icon="['fas', 'times']" style="font-size: 24px; color: white" />
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { BASE_URL } from '@/config';

const props = defineProps({
  selectedDate: String,
});

const emit = defineEmits(['closeForm', 'updateList']);

const title = ref('');
const date = ref(props.selectedDate || '');
const content = ref('');
const category = ref('DAILY');
const images = ref([]); // 이미지 리스트

const handleImageUpload = event => {
  const files = Array.from(event.target.files); // 선택한 모든 파일을 배열로 변환
  files.forEach(file => {
    const reader = new FileReader();
    reader.onload = e => {
      images.value.push({ file, url: e.target.result }); // 이미지 파일과 URL을 모두 추가
    };
    reader.readAsDataURL(file);
  });
  event.target.value = ''; // 입력 초기화
};

const submitDiary = async () => {
  
  //제목없으면 얼럿 띄우고 중단
  if (!title.value.trim()) {
    alert("제목을 입력해주세요."); 
    return;
  }

  const diaryRequest = {
    title: title.value,
    date: date.value,
    content: content.value,
    category: category.value, 
    calendarsIdx: 1,
  };

  const formData = new FormData();
  formData.append('diaryRequest', 
  new Blob([JSON.stringify(diaryRequest)], { type: 'application/json' }));

  // 이미지 파일 추가 여부를 확인
  if (images.value.length > 0) {
    images.value.forEach(image => {
      formData.append('imageFiles', image.file); // 이미지 파일 추가
    });
  } else {
    formData.append('imageFiles', new Blob([], { type: 'application/octet-stream' })); // 빈 Blob 추가
  }

  try {
    const response = await axios.post(`${BASE_URL}/diary/create`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    console.log('Diary Submitted Successfully', response.data);
    emit('closeForm'); 
  } catch (error) {
    console.error('Failed to submit diary:', error);
    emit('closeForm');
  }
};

const cancelForm = () => {
  emit('closeForm');
};
</script>

<style scoped>
.diary-form {
  border: 1px solid #ccc;
  margin-top: 10vh;
  padding: 20px;
  border-radius: 10px;
  height: auto;
}

/* 전체 레이아웃 */
.row {
  display: flex;
  flex-direction: column;
  border-bottom: 1px solid #ccc;
  padding: 10px 0;
}

.title-category-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 2;
}

.category-section {
  flex: 1;
  display: flex;
  justify-content: flex-end;
}

.diary-form label {
  margin-bottom: 15px;
  font-weight: bold;
}

.icon-label {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* 입력 필드 스타일 */
.input-field {
  border: none;
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  font-size: 1rem;
}

.textarea-field {
  height: 220px;
  resize: none;
}

/* 이미지 미리보기 */
.image-preview {
  display: flex;
  flex-wrap: wrap; /* 여러 줄로 표시 */
  margin-top: 10px;
}

.image-container {
  margin: 5px;
}

.image-container img {
  width: 100px;
  height: 100px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.button-row {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 30px;
  margin-bottom: 10px;
}

.submit-button,
.cancel-button {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: none;
  font-size: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.submit-button {
  background-color: #343434;
  color: white;
}

.cancel-button {
  background-color: #808080;
  color: white;
}
</style>
