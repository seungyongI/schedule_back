<template>
  <div class="map_wrap">
    <div id="map" style="width: 100%; height: 400px"></div>

    <div id="menu_wrap" class="bg_white">
      <div class="option">
        <form @submit.prevent="searchPlaces">
          키워드 : <input type="text" v-model="keyword" id="keyword" size="15" />
          <button type="submit">검색하기</button>
        </form>
      </div>
      <hr />
      <ul id="placesList"></ul>
      <div id="pagination"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const keyword = ref(''); // 검색어
const map = ref(null); // 지도 객체
const markers = ref([]); // 마커 배열
let infowindow = null; // 인포윈도우 객체

const initMap = () => {
  if (!window.kakao || !window.kakao.maps) {
    const script = document.createElement('script');
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=b0061f834b78c5f38c345878962fc250&libraries=services`;
    script.onload = () => {
      // Kakao Maps API가 로드된 후에 지도 생성
      kakao.maps.load(() => {
        createMap();
      });
    };
    document.head.appendChild(script);
  } else {
    createMap();
  }
};

const createMap = () => {
  const mapContainer = document.getElementById('map');
  const mapOption = {
    center: new kakao.maps.LatLng(37.566826, 126.9786567),
    level: 3,
  };

  map.value = new kakao.maps.Map(mapContainer, mapOption);
  infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
};

const searchPlaces = () => {
  const ps = new kakao.maps.services.Places();
  const searchKeyword = keyword.value.trim();

  if (!searchKeyword) {
    alert('키워드를 입력해주세요!');
    return;
  }

  ps.keywordSearch(searchKeyword, placesSearchCB);
};

const placesSearchCB = (data, status, pagination) => {
  if (status === kakao.maps.services.Status.OK) {
    displayPlaces(data);
    displayPagination(pagination);
  } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
    alert('검색 결과가 존재하지 않습니다.');
  } else if (status === kakao.maps.services.Status.ERROR) {
    alert('검색 결과 중 오류가 발생했습니다.');
  }
};

const displayPlaces = places => {
  const listEl = document.getElementById('placesList');
  const bounds = new kakao.maps.LatLngBounds();
  removeAllChildNods(listEl);
  removeMarker();

  places.forEach((place, i) => {
    const placePosition = new kakao.maps.LatLng(place.y, place.x);
    const marker = addMarker(placePosition, i);

    const itemEl = getListItem(i, place);

    // 마커 및 리스트 항목에 마우스 이벤트 추가
    kakao.maps.event.addListener(marker, 'mouseover', () => {
      displayInfowindow(marker, place.place_name);
    });
    kakao.maps.event.addListener(marker, 'mouseout', () => {
      infowindow.close();
    });

    itemEl.onmouseover = () => {
      displayInfowindow(marker, place.place_name);
    };
    itemEl.onmouseout = () => {
      infowindow.close();
    };

    // 리스트 항목 클릭 이벤트 추가
    itemEl.onclick = () => {
      map.value.setCenter(placePosition); // 해당 장소로 지도 중심 이동
      displayInfowindow(marker, place.place_name); // 해당 장소에 대한 인포윈도우 열기
    };

    listEl.appendChild(itemEl);
    bounds.extend(placePosition);
  });

  map.value.setBounds(bounds);
};

const getListItem = (index, place) => {
  const el = document.createElement('li');
  el.className = 'item';
  const itemStr = `
    <span class="markerbg marker_${index + 1}"></span>
    <div class="info">
      <h5>${place.place_name}</h5>
      ${place.road_address_name ? `<span>${place.road_address_name}</span><span class="jibun gray">${place.address_name}</span>` : `<span>${place.address_name}</span>`}
      <span class="tel">${place.phone}</span>
    </div>`;
  el.innerHTML = itemStr;
  return el;
};

const addMarker = (position, idx) => {
  const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png'; // 마커 이미지 URL
  const imageSize = new kakao.maps.Size(36, 37); // 마커 이미지의 크기
  const imgOptions = {
    spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
    spriteOrigin: new kakao.maps.Point(0, idx * 46 + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
    offset: new kakao.maps.Point(13, 37), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
  };
  const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
  const marker = new kakao.maps.Marker({
    position: position,
    map: map.value,
    image: markerImage, // 이미지 적용
  });

  markers.value.push(marker);
  return marker;
};

const removeMarker = () => {
  markers.value.forEach(marker => marker.setMap(null));
  markers.value = [];
};

const displayInfowindow = (marker, title) => {
  const content = `<div style="padding:5px;z-index:1;">${title}</div>`;
  infowindow.setContent(content);
  infowindow.open(map.value, marker);
};

const removeAllChildNods = el => {
  while (el.hasChildNodes()) {
    el.removeChild(el.lastChild);
  }
};

const displayPagination = pagination => {
  const paginationEl = document.getElementById('pagination');
  removeAllChildNods(paginationEl);

  for (let i = 1; i <= pagination.last; i++) {
    const el = document.createElement('a');
    el.href = '#';
    el.innerHTML = i;
    if (i === pagination.current) {
      el.className = 'on';
    } else {
      el.onclick = () => pagination.gotoPage(i);
    }
    paginationEl.appendChild(el);
  }
};

onMounted(() => {
  initMap();
});
</script>

<style>
.map_wrap {
  position: relative;
  width: 100%;
  height: 500px;
}

#menu_wrap {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 250px;
  margin: 10px 0 30px 10px;
  padding: 5px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.7);
  z-index: 1;
  font-size: 12px;
  border-radius: 10px;
}

#placesList li {
  list-style: none;
  padding: 5px 0;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}

#placesList .item {
  position: relative;
  border-bottom: 1px solid #888;
  overflow: hidden;
  cursor: pointer;
  min-height: 65px;
}

#placesList .item span {
  display: block;
  margin-top: 4px;
}

#placesList .item h5,
#placesList .item .info {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

#placesList .item .info {
  padding: 10px 0 10px 55px;
}

#placesList .info .gray {
  color: #8a8a8a;
}

#placesList .info .jibun {
  padding-left: 26px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png') no-repeat;
}

#placesList .info .tel {
  color: #009900;
}

#placesList .item .markerbg {
  float: left;
  position: absolute;
  width: 36px;
  height: 37px;
  margin: 10px 0 0 10px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png') no-repeat;
}

#placesList .item .marker_1 {
  background-position: 0 -10px;
}
#placesList .item .marker_2 {
  background-position: 0 -56px;
}
#placesList .item .marker_3 {
  background-position: 0 -102px;
}
/* 추가로 marker_4~marker_15까지 마커에 대한 스타일 추가 가능 */
#pagination a {
  margin-right: 5px;
  cursor: pointer;
}

#pagination .on {
  font-weight: bold;
  color: #777;
}
</style>
