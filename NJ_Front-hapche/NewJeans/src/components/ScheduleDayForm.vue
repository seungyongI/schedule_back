<template>
  <div>
    <!-- 일정 및 일기 조회 섹션 -->
    <div class="day-form" v-if="showDayView">
      <!-- 스케줄 섹션 -->
      <div class="schedule-section">
        <div v-if="schedules.length > 0">
          <div v-for="(schedule, index) in schedules" :key="index" class="schedule-item" :style="{ borderColor: schedule.color }" @click="toggleScheduleExpand(index)">
            <div class="title-container">
              <h4 v-if="editIndex !== index">{{ schedule.title }}</h4>
              <input v-else v-model="editData.title" class="input-field" placeholder="Enter Title" @click.stop />
              <p class="date">{{ schedule.date }}</p>
            </div>

            <transition name="slide-fade">
              <div v-show="isScheduleExpanded[index]" class="expanded-content">
                <hr class="divider" />
                <p v-if="editIndex !== index"><strong>Time:</strong> {{ schedule.time }}</p>
                <input v-else v-model="editData.time" class="input-field" placeholder="Enter Time" @click.stop />
                <p v-if="editIndex !== index"><strong>Repeat:</strong> {{ schedule.repeat }}</p>
                <input v-else v-model="editData.repeat" class="input-field" placeholder="Enter Repeat Frequency" @click.stop />

                <hr class="divider" />
                <p v-if="editIndex !== index">{{ schedule.content }}</p>
                <textarea v-else v-model="editData.content" class="input-field textarea-field" placeholder="Enter Content" @click.stop></textarea>

                <hr class="divider" />
                <p v-show="editIndex !== index"><strong>Address:</strong></p>
                <div v-if="isScheduleExpanded[index]" class="map-container">
                  <KakaoMapView :latitude="schedule.latitude" :longitude="schedule.longitude" :key="schedule.id" />
                </div>

                <!-- 이미지 관리 섹션 -->
                <div class="schedule-images">
                  <div v-for="(imageUrl, imgIndex) in schedule.images" :key="imgIndex" class="image-container">
                    <img :src="`${BASE_URL}${imageUrl}`" alt="Schedule Image" style="width: 150px; margin: 5px" />
                  </div>
                </div>

                <div class="button-group">
                  <button @click.stop="startEdit('schedule', index)" v-if="editIndex !== index">Edit</button>
                  <button @click.stop="deleteSchedule(index, 'deleteOnlyThis')" v-if="editIndex !== index">Delete</button>

                  <div v-if="editIndex === index">
                    <button @click.stop="saveScheduleEdit('schedule', index)">Save</button>
                    <button @click.stop="cancelEdit">Cancel</button>
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>
        <div v-else>
          <p>해당 날짜에 등록된 일정이 없습니다.</p>
        </div>
      </div>

      <!-- 다이어리 섹션 -->
      <div class="diary-section">
        <div v-if="diaries.length > 0">
          <div v-for="(diary, index) in diaries" :key="index" class="diary-item" @click="toggleDiaryExpand(index)">
            <div class="title-container">
              <h4 v-if="editIndex !== index">{{ diary.title }}</h4>
              <input v-else v-model="editData.title" class="input-field" placeholder="Enter Title" @click.stop />

              <p class="category">{{ diary.category }}</p>
            </div>

            <transition name="slide-fade">
              <div v-show="isDiaryExpanded[index]" class="expanded-content">
                <hr class="divider" />
                <p v-if="editIndex !== index"><strong>Date:</strong> {{ diary.date }}</p>
                <input v-else v-model="editData.date" class="input-field" placeholder="Enter Date" type="date" @click.stop />
                <hr class="divider" />
                <p v-if="editIndex !== index">{{ diary.content }}</p>
                <textarea v-else v-model="editData.content" class="input-field textarea-field" placeholder="Enter Content" @click.stop></textarea>

                <!-- <div class="diary-images">
                  <div v-for="(imageUrl, imgIndex) in diary.images" :key="imgIndex" class="image-container">
                    <img :src="`${BASE_URL}${imageUrl}`" alt="Diary Image" style="width: 150px; margin: 5px" />
                  </div>
                </div> -->

                <!-- 이미지 관리 섹션 -->
                <div v-if="editIndex === index" class="diary-images">
                  <div v-for="(imageUrl, imgIndex) in editData.images" :key="imgIndex" class="image-container">
                    <img :src="isNewImage(imageUrl) ? imageUrl : `${BASE_URL}${imageUrl}`" alt="Diary Image" style="width: 150px; margin: 5px" />
                    <button class="delete-btn" @click.stop="removeImage(imgIndex, imageUrl)">X</button>
                  </div>
                  <input type="file" @change="onFileChange" multiple accept="image/*" />
                </div>

                <div v-else class="diary-images">
                  <div v-for="(imageUrl, imgIndex) in diary.images" :key="imgIndex" class="image-container">
                    <img :src="`${BASE_URL}${imageUrl}`" alt="Diary Image" style="width: 150px; margin: 5px" />
                  </div>
                </div>
                <div class="button-group">
                  <button @click.stop="startEdit('diary', index)" v-if="editIndex !== index">Edit</button>
                  <button @click.stop="deleteDiary(index)">Delete</button>

                  <div v-if="editIndex === index">
                    <button @click.stop="saveDiaryEdit('diary', index)">Save</button>
                    <button @click.stop="cancelEdit">Cancel</button>
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>
        <div v-else>
          <p>해당 날짜에 작성된 일기가 없습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted, nextTick } from 'vue';
import axios from 'axios';
import KakaoMapView from '@/views/KakaoMapView.vue';
import { BASE_URL } from '@/config';

const props = defineProps({
  selectedDate: String,
});

watch(
  () => props.selectedDate,
  async newDate => {
    if (newDate) {
      await fetchDayData(newDate);
    }
  },
  { immediate: true },
);

const schedules = ref([]);
const diaries = ref([]);
const isScheduleExpanded = ref([]);
const isDiaryExpanded = ref([]);
const editIndex = ref(null);
const editData = ref({ title: '', content: '', address: '', time: '', repeat: '', images: [] });

const showDayView = ref(true);

let pollingInterval = null;



const fetchDayData = async selectedDate => {
  const previousExpandedStates = {
    schedules: [...isScheduleExpanded.value],
    diaries: [...isDiaryExpanded.value],
  };

  const [year, month, day] = selectedDate.split('-');
  const idx = 1;

  try {
    const scheduleResponse = await axios.get(`${BASE_URL}/schedule/${idx}/${year}/${month}/${day}`);

    schedules.value = scheduleResponse.data.map(schedule => {
      let latitude = 37.566826; // 기본값 (서울 좌표)
      let longitude = 126.9786567;

      if (schedule.location) {
        const [lat, lng] = schedule.location.split(',').map(coord => parseFloat(coord.trim()));
        latitude = lat || latitude;
        longitude = lng || longitude;
      }

      return {
        ...schedule,
        id: schedule.idx,
        date: `${year}-${month}-${day}`,
        mapUrl: schedule.mapUrl || null,
        time: schedule.start ? `${schedule.start} - ${schedule.end}` : '',
        repeat: schedule.repeat || 'N/A',
        address: schedule.location || 'No address provided',
        latitude, // 분리한 위도
        longitude, // 분리한 경도
        content: schedule.content || 'No details provided',
        images: schedule.images || [],
      };
    });

    isScheduleExpanded.value = schedules.value.map((_, index) => previousExpandedStates.schedules[index] || false);

    const diaryResponse = await axios.get(`${BASE_URL}/diary/${idx}/${year}/${month}/${day}`);
    diaries.value = diaryResponse.data.map(diary => ({
      ...diary,
      id: diary.idx,
      date: `${year}-${month}-${day}`,
      content: diary.content || 'No content available',
      category: diary.category || 'Uncategorized',
      images: diary.images || [],
    }));

    isDiaryExpanded.value = diaries.value.map((_, index) => previousExpandedStates.diaries[index] || false);
  } catch (error) {
    console.error('데이터 조회 실패:', error);
  }
};

const startPolling = selectedDate => {
  fetchDayData(selectedDate);
  pollingInterval = setInterval(() => {
    fetchDayData(selectedDate);
  }, 1100);
};

const stopPolling = () => {
  if (pollingInterval) {
    clearInterval(pollingInterval);
  }
};

onMounted(() => {
  if (props.selectedDate) {
    startPolling(props.selectedDate);
  }
});

onUnmounted(() => {
  stopPolling();
});

watch(
  () => props.selectedDate,
  newDate => {
    stopPolling();
    if (newDate) {
      startPolling(newDate);
    }
  },
  { immediate: true },
);

const toggleScheduleExpand = index => {
  if (editIndex.value === index) return;
  isScheduleExpanded.value[index] = !isScheduleExpanded.value[index];
};

const toggleDiaryExpand = index => {
  if (editIndex.value === index) return;
  isDiaryExpanded.value[index] = !isDiaryExpanded.value[index];
};

const startEdit = (type, index) => {
  editIndex.value = index;
  editData.value = type === 'schedule' ? { ...schedules.value[index], images: [...schedules.value[index].images] } : { ...diaries.value[index], images: [...diaries.value[index].images] };
};

const saveDiaryEdit = async (type, index) => {
  if (type !== 'diary') return;

  const diaryToUpdate = diaries.value[index];

  // diaryRequest 객체 생성 (JSON 형식)
  const diaryRequest = {
    idx: diaryToUpdate.id,
    title: editData.value.title,
    date: editData.value.date,
    content: editData.value.content,
    category: diaryToUpdate.category,
    deletedImageList: editData.value.deletedImageList || [], // 삭제할 이미지 ID 목록
  };

  try {
    // FormData 객체 생성
    const formData = new FormData();
    // diaryRequest 객체를 JSON 문자열로 변환하여 추가
    formData.append('diaryRequest', new Blob([JSON.stringify(diaryRequest)], { type: 'application/json' }));

    // <<<<<<< HEAD
    // `editData.value.imageFiles` 배열에 있는 파일 객체를 추가
    if (editData.value.imageFiles) {
      for (let file of editData.value.imageFiles) {
        formData.append('imageFiles', file);
        // =======
        //     // 새로 등록할 이미지를 FormData에 추가
        //     for (let image of editData.value.images) {
        //       if (typeof image === 'object' && image instanceof File) {
        //         // 파일인 경우만 추가
        //         formData.append('imageFiles', image);
        // >>>>>>> sunny
      }
    }

    // API 호출
    const response = await axios.post(`${BASE_URL}/diary/update`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    console.log('Diary updated successfully:', response.data);

    // 성공 시 데이터 업데이트
    Object.assign(diaryToUpdate, editData.value);
  } catch (error) {
    // 오류 로그 출력
    console.error('Error during diary update:', error.response ? error.response.data : error.message);
  } finally {
    // 편집 모드 해제
    editIndex.value = null;
  }
};

const saveScheduleEdit = async (type, index) => {
  if (type !== 'schedule') return;

  const scheduleToUpdate = schedules.value[index];
  const scheduleRequest = {
    idx: scheduleToUpdate.id,
    title: editData.value.title,
    date: scheduleToUpdate.date,
    time: editData.value.time,
    repeat: editData.value.repeat,
    address: editData.value.address,
    content: editData.value.content,
  };

  const formData = new FormData();
  formData.append('scheduleRequest', new Blob([JSON.stringify(scheduleRequest)], { type: 'application/json' }));

  try {
    const response = await axios.post(`${BASE_URL}/schedule/update`, formData);
    console.log('Schedule updated successfully:', response.data);
    Object.assign(scheduleToUpdate, editData.value);
  } catch (error) {
    console.error('Error during schedule update:', error.response ? error.response.data : error.message);
  } finally {
    editIndex.value = null;
  }
};

const cancelEdit = () => {
  editIndex.value = null;
};

const deleteSchedule = async (index, deleteType) => {
  const scheduleId = schedules.value[index].id;
  try {
    const response = await axios.delete(`${BASE_URL}/schedule/delete/${scheduleId}`, {
      params: {
        deleteOnlyThis: deleteType === 'deleteOnlyThis',
        deleteAllRepeats: false,
        deleteAfter: false,
      },
    });
    schedules.value.splice(index, 1);
    console.log('Schedule deleted successfully', + response.data);
  } catch (error) {
    console.error('Failed to delete schedule:', error);
  }
};

const deleteDiary = async index => {
  const diaryId = diaries.value[index].id;
  try {
    await axios.delete(`${BASE_URL}/diary/delete/${diaryId}`);
    diaries.value.splice(index, 1);
    console.log('Diary deleted successfully');
  } catch (error) {
    console.error('Failed to delete diary:', error);
  }
};

const onFileChange = event => {
  const files = event.target.files;

  for (let i = 0; i < files.length; i++) {
    const file = files[i];

    // 파일을 미리 보기 이미지로 표시
    const reader = new FileReader();
    reader.onload = e => {
      // 미리 보기 용도로 base64 URL을 추가
      editData.value.images.push(e.target.result);
    };
    reader.readAsDataURL(file);

    // 서버로 보낼 때는 File 객체를 그대로 유지
    if (!editData.value.imageFiles) {
      editData.value.imageFiles = [];
    }
    editData.value.imageFiles.push(file);
  }

  event.target.value = ''; // 파일 입력 필드를 초기화
};

const removeImage = index => {
  // 이미지 URL을 사용하여 삭제할 수도 있습니다.
  const imageUrl = editData.value.images[index];

  if (!imageUrl) {
    console.error('Image URL is not defined');
    return;
  }

  // deletedImageList에 URL을 추가 (필요시)
  if (!editData.value.deletedImageList) {
    editData.value.deletedImageList = [];
  }
  editData.value.deletedImageList.push(imageUrl);

  // 이미지 리스트에서 삭제
  editData.value.images.splice(index, 1);
};

// 수정시 이미지 문제 해결하기 위해 추가
const isNewImage = (imageUrl) => {
  // 새로운 이미지인지 여부를 판단
  return imageUrl.startsWith('data:image'); // base64 URL은 'data:image'로 시작
};

</script>

<style scoped>
.day-form {
  margin-top: 100px;
  padding: 20px;
  background-color: white;
  border-radius: 10px;
  width: 700px;
}

.schedule-section,
.diary-section {
  margin-bottom: 20px;
}

.schedule-item,
.diary-item {
  border: 2px solid #c7c7c7;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  color: black;
  background-color: transparent;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.title-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.date,
.category {
  font-size: 0.9rem;
  color: gray;
  margin-left: auto;
}

.expanded-content {
  margin-top: 10px;
  padding-top: 10px;
  overflow: hidden;
}

.divider {
  border: none;
  border-top: 1px solid #ccc;
  margin: 10px 0;
}

.button-group {
  display: flex;
  justify-content: center;
  margin-top: 10px;
  margin-right: 0;
}

.button-group button {
  background-color: #343434;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 18px;
  font-size: 0.9rem;
  cursor: pointer;
  transition:
    background-color 0.3s,
    transform 0.2s;
  margin-right: 5px;
}

.button-group button:last-child {
  margin-right: 0;
}

.button-group button:hover {
  background-color: #808080;
  transform: translateY(-2px);
}

.button-group button:active {
  background-color: #004080;
  transform: translateY(0);
}

.map-container {
  margin-top: 10px;
}

.map-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.input-field {
  border: none;
  width: 20%;
  padding: 10px;
  border-radius: 5px;
  font-size: 1rem;
}

.textarea-field {
  height: 100px;
  resize: none;
}
</style>
