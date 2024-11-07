import { defineStore } from 'pinia';
import defaultProfileImage from '@/assets/profile2.jpg'; // 기본 프로필 이미지 경로

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isLoggedIn: false,
    accessToken: '',
    userName: '',
    profileImageUrl: '', 
    email: '',
    idx: null,
  }),
  actions: {
    check() {
      console.log(`this.accessToken = ${this.accessToken}`);
      console.log(`this.isLoggedIn = ${this.isLoggedIn}`);
      console.log(`this.userName = ${this.userName}`);
      console.log(`this.profileImageUrl = ${this.profileImageUrl}`);
      console.log(`this.email = ${this.email}`);
      console.log(`this.idx = ${this.idx}`);
    },
    login(token, userName, profileImageUrl, email, idx) {
      this.accessToken = token;
      this.userName = userName;
      this.profileImageUrl = profileImageUrl && profileImageUrl !== 'undefined' ? profileImageUrl : defaultProfileImage; // 기본 이미지 적용
      this.email = email;
      this.idx = idx;
      this.isLoggedIn = true;

      localStorage.setItem('token', token);
      localStorage.setItem('userName', userName);
      localStorage.setItem('profileImageUrl', profileImageUrl || ''); 
      localStorage.setItem('email', email || '');
      localStorage.setItem('idx', idx || '');
    },
    logout() {
      this.accessToken = null;
      this.userName = '';
      this.profileImageUrl = '';
      this.email = '';
      this.idx = null;
      this.isLoggedIn = false;

      localStorage.removeItem('token');
      localStorage.removeItem('userName');
      localStorage.removeItem('profileImageUrl');
      localStorage.removeItem('email');
      localStorage.removeItem('idx');
    },
    restoreLogin() {
      const accessToken = localStorage.getItem('token');
      const userName = localStorage.getItem('userName');
      const profileImageUrl = localStorage.getItem('profileImageUrl');
      const email = localStorage.getItem('email');
      const idx = localStorage.getItem('idx');

      console.log('restoreLogin 호출됨 - 가져온 데이터:', { accessToken, userName, profileImageUrl, email, idx });

      if (accessToken && userName) {
        this.accessToken = accessToken;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl && profileImageUrl !== 'undefined' && profileImageUrl !== '' ? profileImageUrl : defaultProfileImage; // 기본 이미지 적용
        this.email = email || '';
        this.idx = idx ? Number(idx) : null;
        this.isLoggedIn = true;
        console.log('restoreLogin 실행 후 상태:', this.$state); // 최종 상태 확인
      } else {
        console.warn('localStorage에 저장된 로그인 정보가 없습니다.');
      }
    },
    changeProfileImage(newprofileImageUrl) {
      this.profileImageUrl = newprofileImageUrl && newprofileImageUrl !== '' ? newprofileImageUrl : defaultProfileImage; // 기본 이미지 적용
      localStorage.setItem('profileImageUrl', newprofileImageUrl || ''); 
    },
  },
});
