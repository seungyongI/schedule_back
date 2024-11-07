<template>
  <teleport to="body">
    <div class="modal-overlay" @click.self="closeModal" v-if="show">
      <div class="modal-content">
        <h2>Login</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <input type="text" placeholder="Email" v-model="username" required />
          </div>
          <div class="form-group">
            <input type="password" placeholder="Password" v-model="password" required />
          </div>
          <button type="submit" class="login-button">로그인</button>
          <p class="forgot-password-button">간편 계정 등록 및 회원가입</p>
        </form>

        <!-- 소셜 로그인 버튼 추가 -->
        <div class="social-login-buttons">
          <button id="custom-login-btn" @click="kakaoLogin()" class="social-button kakao-button">
            <img src="@/assets/kakao.png" alt="Kakao Icon" class="social-icon" />
            카카오 계정으로 로그인
          </button>
          <button class="social-button google-button">
            <img src="@/assets/google.png" alt="Google Icon" class="social-icon" />
            구글 계정으로 로그인
          </button>
          <button class="social-button naver-button">네이버 계정으로 로그인</button>
        </div>

        <!-- 회원가입 버튼 추가 -->
        <button @click="openSignUp" class="signup-button">회원가입</button>
      </div>
    </div>
  </teleport>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore'; // Pinia store import
import { watchEffect } from 'vue';
import { useRoute } from 'vue-router';
import { BASE_URL } from '@/config';
import axios from 'axios';

// Pinia store 사용
const authStore = useAuthStore();

const router = useRouter(); // useRouter 훅 사용

// props 정의
const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(['close']);

// 로그인 관련 상태
const username = ref('');
const password = ref('');

// 회원가입 페이지 새 창으로 열기
const openSignUp = () => {
  const signUpUrl = router.resolve({ path: '/signupp' }).href;
  window.open(signUpUrl, '_blank'); // 새 창에서 회원가입 페이지 열기
};

// 로그인 처리 함수 (일반 로그인)
const handleLogin = async () => {
  try {
    // 서버로 로그인 요청 보내기
    const response = await axios.post(`${BASE_URL}/auth/login`, {
      email: username.value,
      password: password.value,
    });

    if (response.status === 200) {
      console.log(response.data);
      // 로그인 성공 시 Pinia store에 상태 업데이트
      authStore.login(response.data.accessToken, response.data.userName, response.data.profile, response.data.email, response.data.idx);

      alert('로그인 성공!');
      authStore.check();
      closeModal(); // 모달 닫기
      router.push('/'); // 로그인 후 홈으로 이동
    }
  } catch (error) {
    // 로그인 실패 시 에러 처리
    alert('로그인 실패: ' + (error.response?.data?.message || '로그인에 실패했습니다.'));
    console.error('로그인 오류:', error);
  }
};

// 모달 닫기 함수
const closeModal = () => {
  emit('close'); // 부모 컴포넌트에 모달 닫기 이벤트 전달
};

//카카오 로그인
const route = useRoute();

const kakaoLogin = () => {
  window.Kakao.Auth.authorize({
    redirectUri: 'http://192.168.0.87:5173/kakaologin', //카카오 리다이렉트 URI
  });
};

// 카카오 리다이렉트 후 처리
watchEffect(() => {
  if (route.query.code) {
    axios
      .get('http://192.168.0.17:8080/auth/kakao/login?code=' + route.query.code)
      .then(response => {
        // 서버로부터 받은 토큰을 localStorage에 저장 (로그인 상태 관리)
        const token = response.data.token;
        localStorage.setItem('token', token); // 토큰을 브라우저에 저장
        authStore.login(token, response.data.userName); // Pinia에 로그인 상태 저장
        alert('카카오 로그인 성공!');
        router.push('/'); // 로그인 후 홈으로 리다이렉트
      })
      .catch(error => {
        console.log('카카오 로그인 실패:', error);
        alert('카카오 로그인에 실패했습니다.');
      });
  }
});
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  width: 300px; /* 너비 조정 */
  height: 500px;
  text-align: center;
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
}

.form-group {
  margin-bottom: 15px; /* 입력 필드 간의 여백 */
}

input {
  width: 93%; /* 입력 필드 너비 100% */
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  transition: border-color 0.3s;
}

input:focus {
  border-color: #007bff; /* 포커스 시 경계색 변경 */
  outline: none; /* 기본 포커스 아웃라인 제거 */
}

.login-button {
  background-color: #2c2c2c; /* 버튼 색상 */
  color: white; /* 버튼 글자색 */
  border: none;
  padding: 10px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
  width: 100%; /* 버튼 너비 100% */
}

.login-button:hover {
  background-color: #eaeaea; /* 호버 시 색상 변경 */
}

.forgot-password-button {
  background-color: transparent; /* 배경을 투명하게 */
  color: #2c2c2c; /* 글자색 */
  border: none;
  font-size: 14px;
  margin-top: 30px; /* 위쪽 여백 */
  padding: 0;
}

/* 소셜 로그인 버튼 스타일 */
.social-login-buttons {
  margin-top: 30px; /* 소셜 로그인 버튼 위쪽 여백 */
}

.social-button {
  color: #2c2c2c; /* 글자색 */
  border: none;
  padding: 10px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 16px;
  width: 100%; /* 버튼 너비 100% */
  margin-bottom: 10px; /* 버튼 간의 간격 */
  display: flex;
  align-items: center; /* 아이콘과 텍스트 수직 정렬 */
  justify-content: center;
}

.kakao-button {
  background-color: #fbe400; /* 버튼 배경색 */
}

.naver-button {
  background-color: #3fc754; /* 버튼 배경색 */
  color: white;
}

.google-button {
  background-color: #ffffff; /* 버튼 배경색 */
  border: 1px solid #b0b0b0;
}

.social-icon {
  width: 20px; /* 아이콘 크기 조정 */
  height: 20px; /* 아이콘 크기 조정 */
  margin-right: 8px; /* 아이콘과 텍스트 사이 간격 */
}

.social-button:hover {
  background-color: #eaeaea; /* 호버 시 색상 변경 */
}

/* 회원가입 버튼 스타일 */
.signup-button {
  background-color: #ffffff;
  color: 2c2c2c;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #b0b0b0;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
  width: 100%; /* 버튼 너비 100% */
}

.signup-button:hover {
  background-color: #eaeaea;
}
</style>
