<template>
  <div id="content">
    <a-list
      :grid="{ gutter: 16, xs: 1, sm: 2, md: 4, lg: 4, xl: 6, xxl: 3, xxxl: 2 }"
      :data-source="vedioList"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <a-card>
            <p v-html="item.title"></p>
            <a :href="item.arcurl">
              <img
                :width="150"
                :height="100"
                :src="item.pic"
                referrerpolicy="no-referrer"
              />
            </a>
          </a-card>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { withDefaults, defineProps } from "vue";
import gege from "../../assets/gege.png";

interface PictureType {
  vedioList: any[];
}

const props = withDefaults(defineProps<PictureType>(), {
  vedioList: [],
});
</script>

<style scoped></style>
