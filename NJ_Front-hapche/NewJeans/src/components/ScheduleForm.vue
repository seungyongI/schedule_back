<template>
  <div class="schedule-form">
    <form @submit.prevent="submitSchedule">
      <div class="form-grid">
        <!-- 제목 -->
        <div class="form-row" style="width: 450px">
          <label for="title">제목</label>
          <input id="title" v-model="title" placeholder="Enter Title" />
        </div>

        <!-- 색깔 선택 (색상 원으로 표시, 라디오 버튼 숨김) -->
        <div class="form-row" style="width: 450px">
          <label>색깔 선택</label>
          <div class="color-options">
            <label v-for="(colorOption, index) in colorList" :key="index" class="color-label">
              <!-- 색상 선택 부분에서만 라디오 버튼 숨김 -->
              <input type="radio" v-model="color" :value="colorOption.value" class="color-radio" />
              <span class="color-circle" :style="{ backgroundColor: colorOption.color }" @click="color = colorOption.value"></span>
            </label>
          </div>
        </div>

        <!-- 시작 날짜, 종료 날짜 -->
        <div class="form-row" style="width: 450px">
          <label for="startdate">시작 날짜</label>
          <input id="startdate" v-model="startdate" type="datetime-local" />
        </div>
        <div class="form-row" style="width: 450px">
          <label for="enddate">종료 날짜</label>
          <input id="enddate" v-model="enddate" type="datetime-local" />
        </div>

         <!-- 반복 설정을 세로로 배치 (라디오 버튼 보이도록) -->
         <div class="form-row" style="width: 450px">
          <label>반복</label>
          <div class="repeat-options">
            <label for="yearly" class="radio-label">
              <input id="yearly" type="radio" v-model="repeat" value="YEARLY" />
              매년
            </label>
            <label for="monthly" class="radio-label">
              <input id="monthly" type="radio" v-model="repeat" value="MONTHLY" />
              매월
            </label>
            <label for="monthly" class="radio-label">
              <input id="monthly" type="radio" v-model="repeat" value="DAILY" />
              매일
            </label>
            <label for="none" class="radio-label">
              <input id="none" type="radio" v-model="repeat" value="NONE" />
              안함
            </label>
          </div>
        </div>

        <!-- 카카오 지도 컴포넌트를 Add Note 위에 배치 -->
        <div class="form-row">
          <label for="location">지도</label>
          <KakaoMap @updateLocation="updateLocation" /> <!-- 위치 업데이트 이벤트 -->
        </div>

        <!-- 내용 입력 -->
        <div class="form-row" style="width: 450px">
          <div class="icon-label">
            <i class="icon-note"></i>
            <label for="content">메모</label>
          </div>
          <textarea id="content" v-model="description" placeholder="Enter your note" class="input-field textarea-field"></textarea>
        </div>

        <!-- 이미지 업로드 -->
        <div class="form-row">
          <label for="image" style="width: 450px">이미지</label>
          <input id="image" type="file" @chnpmange="handleImageUpload" multiple class="input-field" />
        </div>

        <div class="image-preview">
          <div v-for="(image, index) in images" :key="index" class="image-container">
            <img :src="image.url" alt="Preview" />
            <button class="delete-btn" @click="removeImage(index)">X</button>
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
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
import dayjs from 'dayjs';
import KakaoMap from '@/views/KakaoMap.vue';
import { BASE_URL } from '@/config';

const props = defineProps({
  selectedDate: String,
});

const emit = defineEmits(['closeForm']);

const title = ref('');
const color = ref('ORANGE');
const startdate = ref('');
const enddate = ref('');
const location = ref('');
const description = ref('');
const repeat = ref('NONE');
const images = ref([]); // 이미지 파일을 저장

const colorList = [
  { value: 'PINK', color: '#ff7f7f' },
  { value: 'ORANGE', color: '#ff9933' },
  { value: 'YELLOW', color: '#ffe066' },
  { value: 'BLUE', color: '#4da6ff' },
  { value: 'GREEN', color: '#5cd65c' },
  { value: 'VIOLET', color: '#b366ff' },
  { value: 'GRAY', color: '#a6a6a6' },
];

const updateLocation = (coords) => {
  console.log("부모 컴포넌트에서 받은 좌표:", coords);
  location.value = `${coords.lat}, ${coords.lng}`; // 위치를 위도, 경도로 저장
  console.log("위치 업데이트:", location.value); 
};

onMounted(() => {
  if (props.selectedDate) {
    startdate.value = dayjs(props.selectedDate).format('YYYY-MM-DDTHH:mm');
  }
});

// 이미지 업로드 핸들러
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

const submitSchedule = async () => {

  //제목없으면 얼럿 띄우고 중단
  if (!title.value.trim()) {
    alert("제목을 입력해주세요."); 
    return; 
  }

  // scheduleRequest JSON 객체 생성
  const scheduleRequest = {
    title: title.value,
    color: color.value,
    start: startdate.value,
    end: enddate.value,
    location: location.value,
    content: description.value,
    calendarsIdx: 1,
    repeatType: repeat.value,
  };

  // FormData 생성 및 diaryRequest JSON과 이미지 파일 추가
  const formData = new FormData();
  formData.append(
    'scheduleRequest',
    new Blob([JSON.stringify(scheduleRequest)], {
      type: 'application/json',
    }),
  ); // JSON 데이터를 문자열로 변환해 추가

  // 이미지 파일이 선택된 경우 FormData에 추가
  if (images.value.length > 0) {
    images.value.forEach(image => {
      formData.append('imageFiles', image.file); // 이미지 파일 추가
    });
  } else {
    formData.append('imageFiles', new Blob([], { type: 'application/octet-stream' })); // 빈 Blob 추가
  }

  try {
    const response = await axios.post(`${BASE_URL}/schedule/create`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    console.log('Schedule Submitted Successfully', response.data);

    // 기본일정 데이터 생성 후
    const scheduleId = response.data.scheduleId;

    if (repeat.value !== 'NONE') {
      const repeatRequest = {
        sr_type: repeat.value, // DAILY, WEEKLY, MONTHLY, YEARLY 중 하나
        s_idx: scheduleId, // 생성된 일정의 ID
        r_end_date: dayjs(enddate.value).format('YYYY-MM-DD'), // 반복 종료 날짜
      };

      // 반복 일정 데이터를 서버에 전송
      await axios.post(`${BASE_URL}/scheduleRepeat/create`, repeatRequest);

      console.log('Repeat Schedule Submitted Successfully');
    }

    emit('closeForm');
  } catch (error) {
    console.error('Failed to submit Schedule:', error);
    emit('closeForm');
  }
};

const cancelForm = () => {
  emit('closeForm');
};

// 시작일 정하면 자동으로 종료일은 같은날 1시간후로.
watch(startdate, newStartDate => {
  if (newStartDate) {
    const newEndDate = dayjs(newStartDate).add(1, 'hour').format('YYYY-MM-DDTHH:mm');
    enddate.value = newEndDate;
  }
});
</script>

<style scoped>
.schedule-form {
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  max-width: 600px;
  margin-top: 100px;
  padding: 50px 0;
  border: 1px solid #ccc;
}

/* 그리드를 1열로 변경 */
.form-grid {
  display: grid;
  grid-template-columns: 1fr; /* 1열로 배치 */
  grid-gap: 20px;
}

.form-row {
  display: flex;
  flex-direction: column;
  margin: 0 auto;
}

.form-row label {
  margin-bottom: 15px;
  font-weight: bold;
}

input,
select,
textarea {
  padding: 10px;
  font-size: 16px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

textarea {
  height: 80px;
}

/* 색깔 선택 부분 */
.color-options {
  display: flex;
  gap: 20px;
}

.color-circle {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 1px solid #ccc;
  display: inline-block;
  cursor: pointer;
}

/* 색깔 선택 라디오 버튼만 숨김 */
.color-radio {
  display: none;
}

/* 선택된 색깔 원에 스타일 추가 */
input[type='radio']:checked + .color-circle {
  border: 2px solid black;
}

/* 반복 옵션 스타일 (라디오 버튼 보이게) */
.repeat-options {
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.icon-label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.icon-note {
  font-size: 18px;
  color: #343434;
}

.input-field {
  border: 1px solid #ccc;
  padding: 10px;
  font-size: 14px;
  border-radius: 5px;
}

.textarea-field {
  height: 80px;
  resize: none;
}

.button-row {
  display: flex;
  justify-content: center;
  gap: 10px;
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

/* 이미지 미리보기 */
.image-preview {
  display: flex;
  flex-wrap: wrap; /* 여러 줄로 표시 */
  margin-top: 10px;
}

.image-container {
  position: relative;
  margin: 5px;
}

.image-container img {
  width: 100px;
  height: 100px;
  border: 1px solid #ddd;
  border-radius: 8px;
}
</style>
