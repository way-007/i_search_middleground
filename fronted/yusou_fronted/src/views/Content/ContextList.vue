<template>
  <div id="content" style="margin: 0">
    <a-list item-layout="horizontal" :data-source="props.postList">
      <template #renderItem="{ item }">
        <a-list-item>
          <a-list-item-meta :description="item.content">
            <template #title>
              <a href="https://www.antdv.com/">{{ item.title }}</a>
            </template>
            <template #avatar>
              <a-avatar :src="gege" />
            </template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { withDefaults, defineProps } from "vue";
import gege from "../../assets/gege.png";

interface propsType {
  postList: any[];
}

const props = withDefaults(defineProps<propsType>(), {
  postList: [],
});
</script>

<style scoped></style>
