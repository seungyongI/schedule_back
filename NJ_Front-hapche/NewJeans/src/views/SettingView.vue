<template>
  <div class="settings-container">
    <!-- 프로필 섹션 -->
    <div class="profile-section">
      <div class="profile-image-container">
        <img :src="profileImage && profileImage !== 'undefined' ? profileImage : defaultProfileImage" alt="Profile Image" class="profile-image" />
        <button @click="openFilePicker" class="change-img-btn">프로필 사진 변경</button>
        <input type="file" ref="fileInput" @change="handleFileChange" style="display: none" accept="image/*" />
      </div>
      <div class="profile-details">
        <div class="name-edit">
          <h1 v-if="!isEditingName" class="profile-name">{{ userName }}</h1>
          <input v-else v-model="newUserName" class="name-input" placeholder="새 닉네임 입력" />
          <button @click="isEditingName ? saveUserName() : startEditingName()" class="edit-btn">
            {{ isEditingName ? '저장' : '닉네임 변경' }}
          </button>
        </div>

        <p class="profile-email">{{ email }}</p>

        <div class="delete-section">
          <div class="delete-container">
            <div class="delete-text-container">
              <button @click="deleteAccount" class="delete-btn">내 계정 삭제</button>
              <p class="delete-description">계정을 영구적으로 삭제하고 모든 워크 스페이스에서 액세스 권한을 제거합니다.</p>
            </div>
            <span class="delete-arrow">›</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 테마 선택 섹션 -->
    <div class="theme-section">
      <div class="theme-header">
        <h4>Theme</h4>
        <p>{{ selectedTheme }}</p>
      </div>
      <div class="theme-options">
        <label v-for="(theme, index) in themes" :key="index" class="theme-box">
          <input type="radio" name="theme" :value="theme.value" v-model="selectedTheme" class="radio-input" @change="applyTheme" />
          <div class="theme-preview">
            <img :src="theme.icon" alt="Theme Icon" class="theme-icon" />
          </div>
        </label>
      </div>
    </div>

    <!-- 통계 보기 버튼 -->
    <button class="statistics-btn" @click="showStatistics">통계 보기</button>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import defaultProfileImage from '@/assets/profile2.jpg';
import axios from 'axios';
import { BASE_URL } from '@/config';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const userName = computed(() => authStore.userName);
const email = computed(() => authStore.email);

const profileImage = computed(() => authStore.profile || defaultProfileImage); // 기본 이미지 적용

const isEditingName = ref(false);
const newUserName = ref(userName.value);

const startEditingName = () => {
  isEditingName.value = true;
  newUserName.value = userName.value;
};

const checkAuthStoreLoaded = () => {
  if (!authStore.idx || !authStore.email) {
    console.warn('authStore에 idx 또는 email이 없습니다.');
    return false;
  }
  return true;
};

const saveUserName = async () => {
  if (!checkAuthStoreLoaded()) return;

  // 닉네임에서 모든 공백 제거
  const sanitizedUserName = newUserName.value.replace(/\s/g, '');

  // 공백이 포함된 경우 저장 불가
  if (!sanitizedUserName || sanitizedUserName === '') {
    alert('닉네임에 공백을 포함할 수 없습니다.');
    return;
  }

  try {
    await axios.put(`${BASE_URL}/user/updateUserName/${authStore.idx}`, { userName: sanitizedUserName });

    authStore.userName = sanitizedUserName;

    isEditingName.value = false;
  } catch (error) {
    console.error('닉네임 저장 중 오류:', error);

    if (error.response && error.response.data) {
      try {
        console.error('서버 응답:', JSON.stringify(error.response.data, null, 2).slice(0, 1000));
      } catch (stringifyError) {
        console.error('에러 응답을 문자열로 변환하는 중 오류 발생:', stringifyError);
      }
    }
  }
};

const themes = [
  { value: 'Light', label: 'Light Theme', backgroundColor: '#f5f5f5', icon: 'src/assets/white_icon.jpg' },
  { value: 'Dark', label: 'Dark Theme', backgroundColor: '#242424', icon: 'src/assets/dark_icon.jpg' },
  { value: 'Pink', label: 'Pink Theme', backgroundImage: 'url("src/assets/flowers-3435886_1920.jpg")', icon: 'src/assets/flowers_icon.jpg' },
  { value: 'Sky', label: 'Sky Theme', backgroundImage: 'url("src/assets/sky-5534319_1920.jpg")', icon: 'src/assets/sky_icon.jpg' },
];

const selectedTheme = ref(themes[0].value);

const fileInput = ref(null);

const openFilePicker = () => fileInput.value.click();

const handleFileChange = async event => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      authStore.profile = reader.result;
      uploadProfileImage(file);
    };
    reader.readAsDataURL(file);
  }
};

const uploadProfileImage = async file => {
  if (!checkAuthStoreLoaded()) return;

  const formData = new FormData();

  formData.append('imageFiles', file);

  try {
    const response = await axios.post(`${BASE_URL}/user/updateProfileImage/${authStore.idx}`, formData);
    if (response.data.profileImageUrl) {
      authStore.profile = response.data.profileImageUrl;
      localStorage.setItem('profile', response.data.profileImageUrl);
    }
  } catch (error) {
    console.error('프로필 이미지 업로드 중 오류:', error);
  }
};

const applyTheme = () => {
  // 모든 테마 클래스를 제거하고 새로운 테마 클래스를 추가
  document.documentElement.classList.remove('Light-theme', 'Dark-theme', 'Pink-theme', 'Sky-theme');
  document.documentElement.classList.add(`${selectedTheme.value}-theme`);

  // 선택된 테마의 스타일을 가져옴
  const selected = themes.find(theme => theme.value === selectedTheme.value);

  if (selected) {
    if (selected.backgroundColor) {
      // 배경색 설정 및 배경 이미지 제거
      document.documentElement.style.backgroundColor = selected.backgroundColor;
      document.documentElement.style.backgroundImage = 'none';
    } else if (selected.backgroundImage) {
      // 배경 이미지를 설정하고 배경색을 제거
      document.documentElement.style.backgroundImage = selected.backgroundImage;
      document.documentElement.style.backgroundColor = 'transparent';
    }
  }

  // MenuBar에도 테마 적용
  document.querySelector('.nav_wrapper').style.backgroundColor = getComputedStyle(document.documentElement).getPropertyValue('--menu-background-color');
  document.querySelectorAll('.menu-item').forEach(item => {
    item.style.color = getComputedStyle(document.documentElement).getPropertyValue('--menu-text-color');
  });

  // 로고 이미지도 테마에 따라 변경
  const logoElement = document.querySelector('.logo img');
  if (logoElement) {
    logoElement.src = getComputedStyle(document.documentElement).getPropertyValue('--logo-image').trim().replace(/["']/g, '');
  }

  // 테마를 localStorage에 저장
  localStorage.setItem('selectedTheme', selectedTheme.value);
};

// 페이지 로드 시 선택된 테마 초기화
onMounted(() => {
  const savedTheme = localStorage.getItem('selectedTheme');
  if (savedTheme) {
    selectedTheme.value = savedTheme;
    applyTheme();
  }
});

const deleteAccount = async () => {
  if (!checkAuthStoreLoaded()) return;

  try {
    const response = await axios.delete(`${BASE_URL}/user/delete`, {
      data: { idx: authStore.idx, email: authStore.email },
      headers: { Authorization: `Bearer ${authStore.accessToken}` },
    });
    authStore.logout();
    alert('계정이 삭제되었습니다.');
  } catch (error) {
    console.error('계정 삭제 중 오류:', error);
    alert('계정을 삭제하는 중 문제가 발생했습니다.');
  }
};

const router = useRouter();
const showStatistics = () => {
  console.log('통계 보기 버튼 클릭됨 - 여기에 백엔드 연동 코드를 추가하세요.');
  router.push('/graphView');
};
</script>

<style scoped>
.settings-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  background-color: var(--background-color);
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  width: 70%;
  height: 75vh;
  margin-left: 5vh;
}

.profile-section {
  display: flex;
  align-items: center;
  gap: 140px;
  margin-bottom: 30px;
  margin-top: 50px;
  margin-right: 50px;
  width: 85vh;
}

.profile-image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 100px;
}

.profile-image {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 20px;
  border: 1px solid var(--border-color);
  padding: 10px;
}

.change-img-btn {
  border: 1px solid var(--border-color);
  color: var(--button-text-color);
  border-radius: 20px;
  padding: 10px 15px;
  cursor: pointer;
  background-color: var(--button-background);
}

.change-img-btn:hover {
  background-color: var(--border-color);
}

.profile-details {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.name-edit {
  display: flex;
  align-items: center;
  gap: 10px;
}

.edit-btn {
  margin-left: 40px;
  margin-top: 10px;
  border: 1px solid var(--border-color);
  color: var(--button-text-color);
  border-radius: 20px;
  padding: 8px 15px;
  cursor: pointer;
  background-color: var(--button-background);
}

.profile-name {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--text-color);
  margin-bottom: 10px;
}

.profile-email {
  font-size: 1.5rem;
  font-weight: 500;
  color: var(--text-color);
  margin-bottom: 20px;
}

.delete-section {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  width: 100%;
}

.delete-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.delete-text-container {
  display: flex;
  flex-direction: column;
}

.delete-btn {
  background: none;
  border: none;
  color: red;
  font-size: 1rem;
  cursor: pointer;
  flex-grow: 1;
  display: flex;
}

.delete-arrow {
  font-size: 1.5rem;
  color: gray;
  margin-left: 70px;
}

.delete-description {
  color: var(--text-color);
  font-size: 0.9rem;
  margin-top: 5px;
}

.theme-section {
  width: 100%;

  position: relative;
  right: 26.5%;
}

.theme-header {
  display: flex;
  justify-content: center;
  align-items: center;
}

.theme-header h4 {
  font-size: 1.2rem;
  font-weight: bold;
  margin-right: 10px;
}

.theme-header p {
  font-size: 1rem;
  color: var(--text-color);
}

.theme-options {
  position: absolute;
  left: 34%;
  width: 32%;
  display: flex;
  gap: 10px;
  justify-content: center;
  flex-wrap: wrap;
}

.theme-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  margin: 5px;
}

.theme-preview {
  width: 50px;
  height: 50px;
  border-radius: 5px;
  border: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-icon {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.radio-input {
  display: none;
}

.radio-input:checked + .theme-preview {
  border: 2px solid green;
}

.statistics-btn {
  position: relative;
  left: 4.5%;

  bottom: 8%;

  border: 1px solid var(--border-color);
  color: var(--button-text-color);
  border-radius: 20px;
  padding: 10px 20px;
  cursor: pointer;
  background-color: var(--button-background);
}

.statistics-btn:hover {
  background-color: var(--border-color);
}
</style>
