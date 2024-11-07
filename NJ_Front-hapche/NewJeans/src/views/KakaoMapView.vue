<script setup>
import { onMounted, watch, nextTick, ref } from 'vue';

const props = defineProps({
  latitude: Number,
  longitude: Number,
  mapId: String,  // 지도의 고유 ID를 받아오는 props 추가
});

let map;
let marker;
const mapContainer = ref(null); // map 요소에 대한 참조

const initMap = () => {
  if (!mapContainer.value) return;

  const options = {
    center: new kakao.maps.LatLng(props.latitude, props.longitude),
    level: 3,
  };
  map = new kakao.maps.Map(mapContainer.value, options);

  if (marker) {
    marker.setMap(null);
  }

  const markerPosition = new kakao.maps.LatLng(props.latitude, props.longitude);
  marker = new kakao.maps.Marker({
    position: markerPosition,
  });

  marker.setMap(map);
};

onMounted(async () => {
  await nextTick();
  initMap();
});

watch(
  () => [props.latitude, props.longitude],
  async () => {
    await nextTick();
    initMap();
  },
);
</script>

<template>
  <!-- 각 지도 컴포넌트가 mapId를 통해 고유한 ref를 갖도록 설정 -->
  <div :id="props.mapId" ref="mapContainer" style="width: 100%; height: 300px;"></div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 300px;
}
</style>
