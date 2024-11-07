<template>
  <div class="profile">
    <div class="profile-container" v-if="isLoggedIn">
      <!-- 프로필 이미지와 사용자명을 함께 표시 -->
      <img :src="profile || defaultProfileImage" alt="Profile Picture" class="profile-image" />
      <p>Welcome, {{ userName }}!</p>
    </div>
    <div class="profile-container" v-else>
      <img :src="defaultProfileImage" alt="Default Profile Picture" class="profile-image" />
      <div class="welcome-text">
        <p class="f1">Welcome!</p>
        <p class="f2">로그인 후 이용해주세요</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useAuthStore } from '@/stores/authStore'; // Pinia 스토어 가져오기
import defaultProfileImage from '@/assets/profile2.jpg'; // 기본 프로필 이미지 경로 지정

const authStore = useAuthStore(); // Pinia 스토어 사용

// 반응형으로 값을 가져오도록 computed 사용
const isLoggedIn = computed(() => authStore.isLoggedIn);
const userName = computed(() => authStore.userName);
const profile = computed(() => authStore.profile || defaultProfileImage); // 프로필 이미지 상태를 반응형으로 가져옴

// localStorage 값 확인
console.log('Token in localStorage:', localStorage.getItem('token'));
console.log('UserName in localStorage:', localStorage.getItem('userName'));
console.log('Profile in localStorage:', localStorage.getItem('profile'));

// Pinia 상태 확인
console.log('Pinia Token:', authStore.token);
console.log('Pinia UserName:', authStore.userName);
console.log('Pinia Profile:', authStore.profile);
</script>

<style scoped>
.profile {
  display: flex;
  flex-direction: column; /* 세로 방향으로 정렬 */
  align-items: flex-start; /* 왼쪽 정렬 */
}

.profile-container {
  width: 230px;
  margin: 0 auto;
  display: flex;
  flex-direction: column; /* 세로 방향으로 정렬 */
  align-items: center; /* 수평 정렬 */
  margin-bottom: 10px; /* 각 프로필 블록 간의 간격 */
}

.profile-image {
  border-radius: 50%; /* 프로필 이미지를 둥글게 */
  width: 100px; /* 이미지 너비 */
  height: 100px; /* 이미지 높이 */
  border: 1px solid #ccc; /* 선으로 된 원의 색상 및 두께 */
  padding: 5px; /* 이미지와 테두리 간의 간격 */
  box-sizing: border-box; /* padding을 포함하여 크기를 계산 */
}

.welcome-text {
  text-align: center; /* 텍스트 중앙 정렬 */
}
.f1 {
  font-weight: 600;
}
.f2 {
  color: #ccc;
}
</style>
