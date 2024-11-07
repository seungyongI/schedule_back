<script setup>
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import dayjs from 'dayjs';
import ScheduleForm from '@/components/ScheduleForm.vue';
import DiaryForm from '@/components/DiaryForm.vue';
import ScheduleDayForm from '@/components/ScheduleDayForm.vue';
import { BASE_URL } from '@/config';
import YearlyCalendar from '@/components/YearlyCalendar.vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

const schedules = ref([]); // 현재 월의 일정 데이터를 저장
const now = ref(dayjs());
const columns = ref([]);
const groupColumns = ref([]);
const holidays = ref([]);
const countryCode = 'KR';

// Nager.Date API에서 공휴일 데이터를 가져오는 함수
const fetchHolidays = async () => {
  const year = now.value.format('YYYY');
  try {
    const response = await axios.get(`https://date.nager.at/api/v3/publicholidays/${year}/${countryCode}`);
    holidays.value = response.data;
    console.log(`공휴일 (${year}): `, holidays.value);
  } catch (error) {
    console.error('Failed to fetch holidays:', error);
  }
};

watch(
  () => now.value.format('YYYY'), // now의 연도 부분만 추적
  fetchHolidays,
);

// @<@ 일정 띄우기 @>@

const MonthlySchedules = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/schedule/1/${now.value.format('YYYY')}/${now.value.format('MM')}`);
    schedules.value = response.data;
  } catch (error) {
    console.error('Failed to show monthly schedules:', error);
  }
};

// 컴포넌트가 로드될 때 일정 데이터를 가져옴+다이어리도
onMounted(() => {
  fetchHolidays();
  MonthlySchedules();
  fetchDiaryEntriesForMonth();
});

// 달이 바뀔 때마다 새 데이터를 불러오도록 watch 사용
watch(now, () => {
  MonthlySchedules();
  fetchDiaryEntriesForMonth();
});

// 날짜에 해당하는 일정을 필터링하는 함수
const getSchedulesForDate = date => {
  return schedules.value.filter(schedule => dayjs(schedule.start).isSame(date, 'day')).slice(0, 3);
};

// @<@ 일기 띄우기 @>@
// 매달 다이어리 데이터를 불러오는 함수
const diaryEntries = ref([]);
const fetchDiaryEntriesForMonth = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/diary/1/${now.value.format('YYYY')}/${now.value.format('MM')}`);
    const diaries = response.data;
    diaryEntries.value = diaries.map(diary => dayjs(diary.date).format('YYYY-MM-DD'));
  } catch (error) {
    console.error('Failed to fetch diary entries:', error);
  }
};

// 날짜에 해당하는 일기를 확인하는 함수
const isDiaryEntry = date => {
  return diaryEntries.value.includes(date.format('YYYY-MM-DD'));
};

const selectDate = ref(null);
const isFlipped = ref(false);

const isScheduleFormVisible = ref(false);
const isDiaryFormVisible = ref(false);
const isYearlyView = ref(false); // 기본값: 일반 달력

const flipBack = () => {
  isFlipped.value = false;
  isScheduleFormVisible.value = false;
  isDiaryFormVisible.value = false;
  MonthlySchedules();
  fetchDiaryEntriesForMonth();
};

const subMonth = () => {
  now.value = dayjs(now.value).subtract(1, 'month');
};
const addMonth = () => {
  now.value = dayjs(now.value).add(1, 'month');
};
const selectDateFn = date => {
  selectDate.value = dayjs(date).format('YYYY-MM-DD');
  isFlipped.value = true;
};
const goToday = () => {
  now.value = dayjs();
};

const showScheduleForm = () => {
  isScheduleFormVisible.value = true;
  isDiaryFormVisible.value = false;
};

const showDiaryForm = () => {
  isDiaryFormVisible.value = true;
  isScheduleFormVisible.value = false;
};

// 스케줄 폼 닫기
const closeScheduleForm = () => {
  isScheduleFormVisible.value = false;
  isDiaryFormVisible.value = false;
};

const weeksInMonth = ref(6); // 기본 6주로 설정 (최대 6주)

watch(
  now,
  () => {
    columns.value = [];
    groupColumns.value = [];
    const startday = dayjs(now.value).startOf('month');
    const lastday = dayjs(now.value).endOf('month');
    const startdayOfWeek = startday.get('day');
    const lastdayOfWeek = lastday.get('day');

    // 주차 수 계산: 시작일과 끝일의 요일에 따라 달의 주차를 계산
    const totalDays = lastday.diff(startday, 'day') + 1;
    const daysFromLastMonth = startdayOfWeek;
    const daysFromNextMonth = 6 - lastdayOfWeek;
    const totalCells = totalDays + daysFromLastMonth + daysFromNextMonth;
    weeksInMonth.value = Math.ceil(totalCells / 7); // 몇 주인지 계산

    // 이전 달의 날짜 추가
    for (let i = 1; i <= startdayOfWeek; i++) {
      columns.value.unshift(dayjs(startday).subtract(i, 'day'));
    }

    // 해당 월의 날짜 추가
    for (let i = 0; i < lastday.get('date'); i++) {
      columns.value.push(dayjs(startday).add(i, 'day'));
    }

    // 다음 달의 날짜 추가
    for (let i = 1; i <= 6 - lastdayOfWeek; i++) {
      columns.value.push(dayjs(lastday).add(i, 'day'));
    }

    for (let i = 0; i < columns.value.length; i += 7) {
      groupColumns.value.push(columns.value.slice(i, i + 7));
    }
  },
  {
    immediate: true,
    deep: true,
  },
);

//문자열 색상을 hex 값으로 변환
const colorList = [
  { value: 'PINK', color: '#ff7f7f' },
  { value: 'ORANGE', color: '#ff9933' },
  { value: 'YELLOW', color: '#ffe066' },
  { value: 'BLUE', color: '#4da6ff' },
  { value: 'GREEN', color: '#5cd65c' },
  { value: 'VIOLET', color: '#b366ff' },
  { value: 'GRAY', color: '#a6a6a6' },
];

const getHexColor = value => {
  const colorItem = colorList.find(item => item.value === value);
  return colorItem ? colorItem.color : '#000000'; // 기본값: 검정색
};

// 색깔 바꾸기
const hexToRgba = (hex, opacity) => {
  if (hex.startsWith('#')) {
    const r = parseInt(hex.slice(1, 3), 16); // 빨강
    const g = parseInt(hex.slice(3, 5), 16); // 초록
    const b = parseInt(hex.slice(5, 7), 16); // 파랑
    return `rgba(${r}, ${g}, ${b}, ${opacity})`;
  }
  return hex;
};

// 연도, 월 이동 선택항목
const selectedYear = ref(now.value.year());
const selectedMonth = ref(now.value.month() + 1);

const yearsRange = Array.from({ length: 20 }, (_, i) => dayjs().year() - 10 + i); // 현재 연도를 기준으로 10년 전후
const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

const onYearChange = () => {
  now.value = dayjs(`${selectedYear.value}-${selectedMonth.value}-01`);
};

const onMonthChange = () => {
  now.value = dayjs(`${selectedYear.value}-${selectedMonth.value}-01`);
};

function speakAllSchedules() {
  const parent = event.target.parentElement;

// 부모 요소 내에서 모든 형제 schedule-title 요소 가져오기
const scheduleElements = parent.querySelectorAll('.schedule-title');
const scheduleTexts = Array.from(scheduleElements).map((el) => el.textContent.trim());

// 텍스트들을 하나의 문자열로 합치기
const combinedText = scheduleTexts.join(', ');
speakText(combinedText);
}

function speakText(text) {
  const speech = new SpeechSynthesisUtterance(text);
  speech.lang = 'ko-KR'; // 원하는 언어 설정
  window.speechSynthesis.speak(speech);
}
// 일기 북마크
</script>

<template>
  <div v-if="isYearlyView">
    <YearlyCalendar @toMonthlyView="isYearlyView = false" />
  </div>
  <div
    v-else
    class="calendar-wrapper"
    :style="{
      height: weeksInMonth === 5 ? '700px' : Math.max(weeksInMonth * 150, 500) + 'px',
    }"
  >
    <!-- 달력이 뒤집힌 상태에 따라 조건부 렌더링 -->
    <div class="calendar-container" :class="{ flipped: isFlipped }">
      <!-- 달력 앞면 영역 -->
      <div class="vv front">
        <h1 class="Calender-title">
          <button @click="goToday" class="Today-button">Today</button>
          <!-- <button @click="$router.push('/yearlyCalendar')" class="Yealy-button">Yeary</button> -->
          <button @click="isYearlyView = true" class="Yealy-button">Yeary</button>
          <button @click="subMonth()" class="B-Month-button">
            <font-awesome-icon :icon="['fas', 'angle-left']" />
          </button>
          <div class="YMYM">
            <span class="year">{{ now.format('YYYY') }}</span>
            <span class="month">{{ now.format('MMMM') }}</span>
          </div>
          <button @click="addMonth()" class="A-Month-button">
            <font-awesome-icon :icon="['fas', 'angle-right']" />
          </button>

          <div class="YMselecter">
            <!-- 연도 선택 드롭다운 -->
            <select v-model="selectedYear" @change="onYearChange">
              <option v-for="year in yearsRange" :key="year" :value="year">{{ year }}</option>
            </select>

            <!-- 월 선택 드롭다운 -->
            <select v-model="selectedMonth" @change="onMonthChange">
              <option v-for="(month, index) in months" :key="index" :value="index + 1">{{ month }}</option>
            </select>
          </div>
        </h1>
        <div class="DOWgrid">
          <div class="Sun">Sun</div>
          <div class="Dayss">Mon</div>
          <div class="Dayss">Tue</div>
          <div class="Dayss">Wed</div>
          <div class="Dayss">Thur</div>
          <div class="Dayss">Fri</div>
          <div class="Sat">Sat</div>
        </div>
        <div class="CALgrid" v-for="group in groupColumns" :key="group.length">
          <div
            @click="selectDateFn(column)"
            v-for="(column, index) in group"
            :key="column.format('YYYY-MM-DD')"
            class="Datecell"
            :class="{
              sundays: index % 7 == 0,
              saturdays: index % 7 == 6,
              notthisMdays: !column.isSame(now, 'month'),
              today: column.isSame(dayjs(), 'day'),
            }"
          >
            <!-- 일기북마크 -->
            <font-awesome-icon v-if="isDiaryEntry(column)" icon="bookmark" class="bookmark-icon" />

            <template v-for="holiday in holidays" :key="holiday">
              <div v-if="holiday.date == column.format('YYYY-MM-DD')" class="holiday-name">
                {{ holiday.localName }}
              </div>
            </template>
            <div class="date-number">{{ column.get('date') }}</div>
            
            <div v-if="getSchedulesForDate(column) && Object.keys(getSchedulesForDate(column)).length > 0" 
              class="icon" 
              @click.stop="speakAllSchedules">
              🔊
            </div>
            <!-- 일정표시창 -->
            <div
              v-for="schedule in getSchedulesForDate(column)"
              :key="schedule.id"
              :style="{
                backgroundColor: hexToRgba(getHexColor(schedule.color), 0.3), // 투명한 배경색
                border: `1px solid ${getHexColor(schedule.color)}`, // 테두리 색상
              }"
              class="schedule-title"
            >
              {{ schedule.title }}
            </div>
          </div>
        </div>
      </div>

      <!-- 뒤집힌 화면에서 일정 및 다이어리 버튼, 폼 렌더링 -->
      <div class="flipped-content">
        <div class="button-group">
          <button class="schedule-btn" @click="showScheduleForm">Schedule</button>
          <button class="flip-back-btn" @click="flipBack">&orarr;</button>
          <!-- ㄴ 달력 다시 뒤집기 버튼 -->
          <button class="diary-btn" @click="showDiaryForm">Diary</button>
        </div>

        <!-- ScheduleForm 컴포넌트 렌더링 -->
        <div v-if="isScheduleFormVisible" class="form-container">
          <ScheduleForm :selectedDate="selectDate" @closeForm="closeScheduleForm" />
        </div>

        <!-- DiaryForm 컴포넌트 렌더링 -->
        <div v-if="isDiaryFormVisible" class="form-container">
          <DiaryForm :selectedDate="selectDate" @closeForm="closeScheduleForm" />
        </div>

        <div v-show="!isScheduleFormVisible && !isDiaryFormVisible">
          <ScheduleDayForm :selectedDate="selectDate" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 달력의 전체적인 구조 */
.calendar-wrapper {
  background-color: white;
  border-radius: 10px;
  perspective: 1000px;
  display: flex;
  align-items: flex-start;
  padding-bottom: 90px;
  padding-left: 50px;
  padding-right: 50px;
  min-height: 500px;
  width: 65%;
  padding-top: 30px;
  margin-left: 50px;
  margin-right: 50px;
  margin-bottom: 50px;
  overflow: hidden;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); /* 그림자 추가 */
}

/* 달력 제목과 관련된 스타일들  */

.Calender-title {
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  align-items: center;
  font-size: 2rem;
  font-weight: 500;
  margin-bottom: 1rem;
  position: relative; /* Today 버튼 고정을 위한 상대 위치 */
}

.Today-button {
  background-color: #333;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  cursor: pointer;
  position: absolute; /* 절대 위치 설정 */
  left: 10px; /* 왼쪽 정렬 */
}

.Yealy-button {
  background-color: #333;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  cursor: pointer;
  position: absolute;
  left: 100px;
}

.YMYM {
  width: 120px; /* 고정 너비를 설정하여 월 이름에 상관없이 동일한 너비 유지 */
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.YMselecter {
  position: absolute;
  right: 0%;
  gap: 5px;
}

select {
  padding: 5px;
  border-radius: 5px;
  font-size: 1rem;
  margin-right: 5px;
}

.year,
.month {
  display: block;
  text-align: center;
}

.year {
  font-size: 1.5rem;
}

.month {
  font-size: 1.2rem;
  white-space: nowrap; /* 텍스트가 줄바꿈되지 않도록 설정 */
}
.B-Month-button,
.A-Month-button {
  background-color: white;
  border: none;
  height: 2rem;
  cursor: pointer;
  font-size: 1.25rem;
  padding: 0 50px;
}

/* 플립 애니메이션 */
.calendar-container {
  width: 95%;
  transition: transform 0.8s;
  transform-style: preserve-3d;
  margin: 0 auto;
  height: 100%;
  min-height: 400px;
}

.flipped {
  transform: rotateY(180deg);
}

/* 달력 앞면과 뒷면 */
.vv {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  backface-visibility: hidden;
  min-height: 750px;
}
.flipped-content {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: -20px;
  backface-visibility: hidden;
  min-height: 750px;
}

/* 달력 앞면 */
.vv {
  display: block;
  backface-visibility: hidden;
}

/* 달력 그리드와 날짜 셀 스타일 */
.DOWgrid,
.CALgrid {
  display: grid;
  grid-template-columns: repeat(7, 1fr); /* 7열 그리드 */
  gap: 15px;
  text-align: center;
  margin-bottom: 1rem;
}

.Datecell {
  position: relative;
  background-color: #f7f7f7;
  border-radius: 3px;
  font-size: 1rem;
  cursor: pointer;
  aspect-ratio: 1 / 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  transition:
    background-color 0.2s,
    border 0.2s;
  aspect-ratio: 1 / 1; /* 정사각형 유지 */
  flex-direction: column; /* 세로 배치 */
  align-items: flex-start;
  padding: 5px;
  cursor: pointer;
  overflow: hidden;
}

.date-number {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 1rem;
}

.Datecell:hover {
  background-color: #e1e1e1;
}

/* 요일 스타일 */
.Sun {
  padding: 0.75rem;
  color: red;
  font-weight: 500;
}

.Dayss {
  padding: 0.75rem;
  color: black;
  font-weight: 500;
}

.Sat {
  padding: 0.75rem;
  color: blue;
  font-weight: 500;
}

/* 특수 날짜 스타일 */
.sundays {
  color: #dc2626;
}

.saturdays {
  color: #2563eb;
}

.notthisMdays {
  opacity: 0.35;
}

.today {
  border: 2px solid black;
}

/* flipped-content 스타일 */
.flipped-content {
  transform: rotateY(180deg);
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  padding: 20px;
  height: 100%;
  position: relative;
  max-height: 100%; /* 부모 요소 높이에 맞추어 최대 높이 설정 */
  overflow-y: auto;
}

/* 버튼 그룹 스타일 */
.button-group {
  display: flex;
  justify-content: center; /* 중앙 정렬 */
  align-items: center;
  gap: 20px;
  margin-top: 0;
  position: absolute;
  top: 20px; /* 상단에 위치 */
}

/* 버튼 스타일 수정 */
.schedule-btn,
.diary-btn {
  background-color: #333;
  color: white;
  padding: 0; /* 패딩을 제거해 크기에 영향 미치지 않도록 설정 */
  display: flex; /* 버튼 내부의 텍스트를 중앙에 정렬하기 위한 flexbox */
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  border-radius: 10px;
  cursor: pointer;
  border: none;
  font-size: 1.2rem;
  transition: background-color 0.3s;
  height: 60px;
  width: 120px;
}

.flip-back-btn {
  background-color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  color: black;
  padding: 0; /* 패딩을 제거해 크기에 영향 미치지 않도록 설정 */
  display: flex; /* 버튼 내부의 텍스트를 중앙에 정렬하기 위한 flexbox */
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
  border-radius: 50%;
  cursor: pointer;
  border: none;
  font-size: 1.2rem;
  transition: background-color 0.3s;
  height: 60px;
  width: 60px;
}

.flip-back-btn:hover {
  background-color: #b4b4b4;
}

.schedule-btn:hover,
.diary-btn:hover {
  background-color: #555;
}

/* 폼을 담는 컨테이너 스타일 */
.form-container {
  /* background-color: white;/// */
  padding: 20px;
  border-radius: 8px;
  /* box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); */
  width: 80%;
  max-width: 600px;
}

/* 일정 제목 스타일 */
.schedule-title {
  width: 100%; /* 셀의 너비를 꽉 차게 설정 */
  box-sizing: border-box; /* padding 포함하여 너비를 계산 */
  font-size: 0.8rem;
  color: #3a3a3a;
  padding: 9px 5px; /* 내부 여백 */
  border-radius: 3px;
  margin-top: 4px;
  text-align: left;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  position: relative;
  top: 10px;
  /* height: 15px; */
  line-height: 0px;
}

.holiday-name {
  font-size: 0.7rem; /* 원하는 폰트 크기 */
  color: red; /* 원하는 글자 색상 */
  font-weight: lighter; /* 글자를 굵게 설정 */
  position: absolute;
  display: block; /* 블록 형식으로 배치 (필요 시) */
  top: 14px;
  left: 45px;
}

/* 북마크 아이콘 스타일 추가 */
.bookmark-icon {
  position: absolute;
  top: 2px;
  left: 5px;
  font-size: 0.8rem;
  color: #dfc38c;
  /* z-index: 10; */
}

.icon {
  position: absolute;
  top: 0.5rem;
  opacity: 0.5;
  transition: opacity 0.3s ease;
}

.icon:hover {
  opacity: 1;
}
</style>
