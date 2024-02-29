<template>
  <div id="content">
    <a-image
      v-for="(items, index) in pictureList"
      :key="index"
      :width="200"
      :height="250"
      :src="items.image"
      :fallback="gege"
      style="margin-right: 20px; margin-left: -100px; margin-top: -100px"
    />
  </div>
</template>

<script setup lang="ts">
import { withDefaults, defineProps } from "vue";
import gege from "../../assets/gege.png";

interface PictureType {
  pictureList: any[];
}

const props = withDefaults(defineProps<PictureType>(), {
  pictureList: [],
});
</script>

<style scoped></style>
