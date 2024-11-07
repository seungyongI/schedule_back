<template>
  <header>
    <div class="nav_wrapper">
      <!-- 수정된 부분: authStore에서 profile 값을 직접 가져옴 -->
      <Profile :isLoggedIn="authStore.isLoggedIn" :userName="authStore.userName" :profile="authStore.profile" />
      <nav class="menu-grid">
        <RouterLink to="/" class="menu-item" active-class="active">
          <FontAwesomeIcon class="fa-icon" :icon="faCalendarAlt" />
          Calendar
        </RouterLink>
        <RouterLink to="/diary" class="menu-item" active-class="active">
          <FontAwesomeIcon class="fa-icon" :icon="faBook" />
          Diary
        </RouterLink>

        <RouterLink to="/teams" class="menu-item" active-class="active">
          <FontAwesomeIcon class="fa-icon" :icon="faUsers" />
          Teams
        </RouterLink>

        <RouterLink to="/setting" class="menu-item" active-class="active">
          <FontAwesomeIcon class="fa-icon" :icon="faCog" />
          Setting
        </RouterLink>

        <!-- 로그인 상태에 따라 Sign In / Sign Out 버튼 표시 -->
        <button v-if="!authStore.isLoggedIn" class="menu-item sign-in" @click="showModal = true">
          <FontAwesomeIcon class="fa-icon" :icon="faSignInAlt" />
          Sign In
        </button>

        <!-- 로그아웃 버튼 -->
        <button v-else class="menu-item sign-in" @click="handleLogout">
          <FontAwesomeIcon class="fa-icon" :icon="faSignOutAlt" />
          Sign Out
        </button>
      </nav>

      <!-- Modal 컴포넌트 -->
      <Modal :show="showModal" @close="showModal = false" />
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Profile from './ProfileSide.vue';
import Modal from './MoDal.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faCalendarAlt, faBook, faUsers, faCog, faSignInAlt, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore'; // Pinia store import

// Pinia store 사용
const authStore = useAuthStore();
const router = useRouter();

// 모달 표시 상태 관리
const showModal = ref(false); // 기본값 false로 시작

// 로그아웃 처리 함수
const handleLogout = async () => {
  await authStore.logout(); // Pinia 스토어에서 로그아웃 처리
  router.push('/'); // 로그아웃 후 로그인 페이지로 이동
};

// Pinia store의 상태가 복원되었는지 확인
onMounted(() => {
  if (!authStore.isLoggedIn) {
    authStore.restoreLogin();
  }
});
</script>

<style scoped>
.nav_wrapper {
  display: flex;
  flex-direction: column; /* 세로 방향으로 정렬 */
  align-items: flex-start; /* 왼쪽 정렬 */
  padding: 5vh 0 0 17vw; /* vh와 vw 단위로 패딩 설정 */
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 2개의 열 */
  grid-template-rows: repeat(3, 10vh); /* vh 단위로 행 높이 설정 */
  gap: 0;
  width: 12vw; /* 전체 너비를 vw 단위로 설정 */
  margin-top: 2vh; /* 프로필과 메뉴 간의 간격 */
}

/* 각 메뉴 항목의 스타일 */
.menu-item {
  padding: 1.5rem 1rem; /* rem 단위로 패딩 설정 */
  background-color: var(--menu-item-background-color); /* 변수 사용 */
  border: 1px solid #ccc;
  text-align: center;
  cursor: pointer;
  font-size: 1rem; /* rem 단위로 폰트 크기 설정 */
  text-decoration: none;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: all 0.3s ease;
  border-radius: 0;
  color: var(--menu-item-text-color); /* 텍스트 색상 변수 사용 */
}

/* 아이콘과 텍스트 간격 */
.menu-item .fa-icon {
  margin-bottom: 0.5rem; /* rem 단위로 간격 설정 */
}

/* 활성화된 메뉴 (클릭 후 유지되는 상태) */
.active {
  background-color: var(--menu-item-active-background-color); /* 변수 사용 */
  color: var(--menu-item-active-text-color); /* 텍스트 색상 변수 */
  transform: scale(1.1); /* 버튼을 살짝 키워서 강조 */
  z-index: 1; /* 다른 버튼들 위로 올라옴 */
  border-color: var(--menu-item-active-border-color); /* 테두리 색상 변수 */
  border-radius: 0.8rem; /* 둥근 모서리 */
}

/* Calendar 메뉴 */
.menu-item:nth-child(1) {
  border-top-left-radius: 1rem; /* 좌측 상단 모서리 둥글게 */
}

/* Diary 메뉴 */
.menu-item:nth-child(2) {
  border-top-right-radius: 1rem; /* 우측 상단 모서리 둥글게 */
}

/* Sign In / Sign Out 버튼 */
.sign-in {
  grid-column: span 2; /* 마지막 줄에서 두 칸 병합 */
  border-bottom-right-radius: 1rem;
  border-bottom-left-radius: 1rem;
  color: var(--menu-item-text-color); /* 텍스트 색상 변수 사용 */
  color: var(--menu-text-color); /* 테마 색상 유지 */
}

/* 반응형 디자인을 위한 미디어 쿼리 */
@media (max-width: 768px) {
  .menu-grid {
    grid-template-columns: 1fr; /* 작은 화면에서는 1열로 변경 */
    width: 80vw; /* 너비를 화면 전체에 맞추기 */
    grid-template-rows: repeat(5, 8vh); /* 높이를 5행으로 변경 */
  }

  .menu-item {
    font-size: 0.9rem; /* 작은 화면에서 폰트 크기를 줄임 */
    padding: 1rem 0.5rem; /* 패딩을 줄여서 작은 화면에서도 보기 좋게 설정 */
  }
}
</style>
