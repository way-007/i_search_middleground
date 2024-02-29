<template>
  <a-empty v-if="userList.length <= 0" :image="simpleImage" />
  <div id="content">
    <a-card
      hoverable
      style="
        width: 300px;
        float: left;
        margin-right: 120px;
        margin-left: -100px;
        margin-top: -100px;
      "
      v-for="(items, index) in props.userList"
      :key="index"
    >
      <template #cover>
        <img alt="example" :src="gege" />
      </template>
      <template #actions>
        <setting-outlined key="setting" />
        <edit-outlined key="edit" />
        <ellipsis-outlined key="ellipsis" />
      </template>
      <a-card-meta :title="items.userName"></a-card-meta>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { withDefaults, defineProps } from "vue";
import gege from "../../assets/gege.png";
import { Empty } from "ant-design-vue";

interface userListType {
  userList: any[];
}

const props = withDefaults(defineProps<userListType>(), {
  userList: [],
});

const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;
</script>

<style scoped></style>
