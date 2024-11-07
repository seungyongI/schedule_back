<template>
  <div class="team-view">
    <!-- 친구 목록 섹션 -->
    <div class="friends-list">
      <div class="friend-requests">
        <h3>친구 목록</h3>
        <div class="request-notification" @click="toggleRequestList">
          <span>친구 요청</span>
          <div v-if="friendRequests.length > 0" class="notification-badge">
            {{ friendRequests.length }}
          </div>
        </div>

        <!-- 친구 요청 목록 (토글) -->
        <transition name="fade">
          <ul v-if="showRequestList" class="request-list">
            <li v-for="request in friendRequests" :key="request.idx" class="request-item">
              <img :src="request.profileImageUrl || defaultProfileImage" alt="프로필 이미지" class="profile-icon">
              <span>{{ request.userName }}</span>
              <button @click="acceptFriendRequest(request.idx)">승인</button>
            </li>
          </ul>
        </transition>

        <!-- 친구 목록 -->
        <ul>
          <li v-for="friend in friends" :key="friend.idx">
            <img :src="friend.profileImageUrl || defaultProfileImage" alt="프로필 이미지" class="profile-icon">
            <span>{{ friend.userName }}</span>
            <span class="friend-email">{{ friend.email }}</span>
          </li>
        </ul>
      </div>
    </div>

    <!-- 공유 일기 생성 섹션 -->
    <div class="shared-diary-creation">
      <h3>공유일기 생성</h3>
      <button @click="createSharedDiary">+</button>
    </div>

    <!-- 공유 일기 섹션 -->
    <div class="diary-content">
      <h3>공유일기</h3>
      <p v-if="!selectedDiary">공유 일기를 선택해주세요</p>
      <div v-if="selectedDiary" class="diary-details">
        <h4>{{ selectedDiary.title }}</h4>
        <p>{{ selectedDiary.content }}</p>
        <small>{{ selectedDiary.date }}</small>
      </div>
    </div>

    <!-- 친구 검색 및 추가 섹션 -->
    <div class="friend-search">
      <h3>친구 검색</h3>
      <input v-model="searchQuery" placeholder="닉네임 입력..." @input="searchFriends">
      <div v-if="searchResults.length > 0" class="search-results">
        <div v-for="user in searchResults" :key="user.idx" class="search-result-item">
          <img :src="user.profileImageUrl || defaultProfileImage" class="profile-icon">
          <span>{{ user.userName }}</span>
          <span class="friend-email">{{ user.email }}</span>
          <button @click="sendFriendRequest(user.idx)">친구 추가</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { BASE_URL } from '@/config';
import defaultProfileImage from '@/assets/profile2.jpg';
import { useAuthStore } from '@/stores/authStore'; // Pinia 스토어 가져오기

const authStore = useAuthStore(); // Pinia 스토어 인스턴스화
const userId = authStore.idx; // authStore에서 idx 가져오기

console.log("userId from authStore:", userId);

const friends = ref([]);
const friendRequests = ref([]);
const searchQuery = ref('');
const searchResults = ref([]);
const selectedDiary = ref(null);
const showRequestList = ref(false);


// 페이지 로드 시 친구 목록과 친구 요청 목록 가져오기
const loadFriends = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/friend/${userId}/list`);
    friends.value = response.data;
  } catch (error) {
    console.error('Failed to load friends:', error);
  }
};

const loadFriendRequests = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/friend/${userId}/requests`);
    friendRequests.value = response.data;
    console.log("Friend Requests Loaded:", friendRequests.value); // 로그 추가
  } catch (error) {
    console.error('Failed to load friend requests:', error);
  }
};

// 친구 추가 요청 보내기
const sendFriendRequest = async (receiverId) => {
  console.log("Requester ID:", userId);  // userId 확인
  console.log("Receiver ID:", receiverId);  // receiverId 확인
  
  try {
    await axios.post(`${BASE_URL}/friend/request`, null, {
      params: {
        requesterId: userId,
        receiverId,
      }
    });
    alert('친구 요청이 성공적으로 전송되었습니다.');
  } catch (error) {
    console.error('Failed to send friend request:', error);
    alert('친구 요청 전송에 실패했습니다.');
  }
};

// 친구 요청 수락 
const acceptFriendRequest = async (requesterId) => {
  const receiverId = authStore.idx; // 현재 로그인한 사용자의 ID를 receiverId로 설정

  try {
    await axios.post(`${BASE_URL}/friend/accept`, null, {
      params: {
        requesterId,
        receiverId,
      }
    });
    friendRequests.value = friendRequests.value.filter(request => request.idx !== requesterId);
    await loadFriends();
    alert('친구 요청을 수락했습니다.');
  } catch (error) {
    console.error('Failed to accept friend request:', error);
    alert('친구 요청 수락에 실패했습니다.');
  }
};

// 친구 검색
const searchFriends = async () => {
  try {
    if (searchQuery.value.trim()) {
      const response = await axios.get(`${BASE_URL}/friend/search`, {
        params: { userName: searchQuery.value } // userName으로 전달
      });
      searchResults.value = response.data;
    } else {
      searchResults.value = [];
    }
  } catch (error) {
    console.error('Failed to search friends:', error);
  }
};

// 친구 요청 목록 토글
const toggleRequestList = () => {
  showRequestList.value = !showRequestList.value;
  console.log("Show Request List:", showRequestList.value);
};

// 공유 일기 생성
const createSharedDiary = () => {
  alert("공유 일기 생성 기능은 아직 구현되지 않았습니다.");
};

// 컴포넌트가 로드될 때 친구 목록 및 요청 목록 가져오기
onMounted(() => {
  loadFriends();
  loadFriendRequests();
});
</script>

<style scoped>
.team-view {
  display: grid;
  grid-template-columns: 1fr 1fr 2fr 2fr;
  gap: 20px;
  padding: 20px;
  background-color: #f3f4f6;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.friends-list, .shared-diary-creation, .diary-content, .friend-search {
  background-color: #ffffff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.friends-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 15px;
}

.friend-requests {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.request-notification {
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  padding: 10px;
  background-color: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.request-notification:hover {
  background-color: #f3f4f6;
}

.notification-badge {
  background-color: #ef4444;
  color: white;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.9em;
  font-weight: bold;
}

.request-list {
  list-style: none;
  padding: 0;
  margin: 0;
  background-color: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  overflow: hidden;
}

.request-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #e5e7eb;
}

.request-item:last-child {
  border-bottom: none;
}

.profile-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
  border: 1px solid #ddd;
}

.friend-email {
  font-size: 0.85em;
  color: #6b7280;
}

button {
  margin-left: auto;
  background-color: #3b82f6;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 0.85em;
  font-weight: 500;
}

button:hover {
  background-color: #2563eb;
}

.friends-list h3,
.friend-requests h3,
.shared-diary-creation h3,
.diary-content h3,
.friend-search h3 {
  margin-bottom: 12px;
  font-size: 1.2em;
  font-weight: bold;
  color: #111827;
}

.search-results {
  margin-top: 10px;
}

.search-result-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #e5e7eb;
  background-color: #f9fafb;
  border-radius: 6px;
}

.search-result-item:last-child {
  border-bottom: none;
}

.search-result-item:hover {
  background-color: #f3f4f6;
}

.friend-search input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  outline: none;
  transition: border-color 0.3s;
}

.friend-search input:focus {
  border-color: #3b82f6;
}

.diary-content, .shared-diary-creation {
  display: flex;
  flex-direction: column;
  gap: 15px;
  border: 1px solid #e5e7eb;
  padding: 15px;
  border-radius: 8px;
  background-color: #ffffff;
}

.diary-content p, .shared-diary-creation p {
  font-size: 0.9em;
  color: #6b7280;
  text-align: center;
}
</style>
