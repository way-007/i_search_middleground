<template>
  <div id="content">
    <!-- 顶部搜索栏 -->
    <a-input-search
      style="width: 70%; margin-left: 200px; margin-bottom: 30px"
      v-model:value="searchParams.text"
      placeholder="input search text"
      enter-button
      @search="onSearch"
    />
    <br />
    <!-- tab标签栏 -->
    <a-tabs v-model:activeKey="activeKey" key="" @change="typeChange">
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
      </a-tab-pane>
      <a-tab-pane key="post" tab="文章" force-render>
        <ContextList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <ImgList :picture-list="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="vedio" tab="视频">
        <VedioList :vedio-list="vedioList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
// 01. 搜索按钮回调
import { onMounted, ref, watchEffect } from "vue";
import UserList from "@/views/Content/UserList.vue";
import ContextList from "@/views/Content/ContextList.vue";
import ImgList from "@/views/Content/ImgList.vue";
import { useRoute, useRouter } from "vue-router";
import request from "../plugins/request.js";
import { message } from "ant-design-vue";
import VedioList from "@/views/Content/VedioList.vue";

const router = useRouter();
const route = useRoute();
const activeKey = ref(route.params.category);

// 01. 创建子组件传递需要的数据参数
const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);
const vedioList = ref([]);

// 02. 初始化搜索的参数 主要是size和page参数
const initParams = {
  type: activeKey,
  text: "",
  pageSize: 10,
  pageNum: 1,
};

// 03. 将初始化参数变为响应式
const searchParams = ref(initParams);

// 06. 监听url的变化  将双向数据传递改为单向数据传递
watchEffect(() => {
  searchParams.value = {
    // 默认传递初始值  当里面的值发生改变的时候  默认传递初始值，然后进行赋值text
    ...initParams,
    text: route.query.text as string, // 目的就是为了将url里面query参数同步到输入框中
    // type: activeKey, // 这里的值一定要使用activekey否则就不是最新的值
  } as any;
});

// 页面加载的时候调用 页面刷新之后会自动发送请求获取数据
onMounted(() => {
  loadData(searchParams.value);
});
// 04. 创建一个函数去请求三个接口的数据需要传递不同的参数
// 04-1. 三个接口请求数据
// const loadData = async (params: any) => {
//   const searchQuery = {
//     ...params,
//     searchText: params.text,
//   };
//   let result = await request.post("/post/list/page/vo", searchQuery);
//   postList.value = result.records;
//   const userQuery = {
//     ...params,
//     userName: params.text,
//   };
//   result = await request.post("/user/list/page/vo", userQuery);
//   userList.value = result.records;
//   result = await request.post("/picture/list", searchQuery);
//   pictureList.value = result.records;
// };

// 04-2. 一个接口请求数据
const loadData = async (params: any) => {
  const { type } = params;
  if (!type) {
    message.error("请求类别不存在");
    return;
  }
  const searchParams = {
    ...params,
    category: params.type,
    searchText: params.text,
  };
  const result = await request.post("/search/list", searchParams);
  if (type === "user") userList.value = result.dataList;
  if (type === "post") postList.value = result.dataList;
  if (type === "picture") pictureList.value = result.dataList;
  if (type === "vedio") vedioList.value = result.dataList;
};

// 05. 点击搜索按钮之后url加上搜索栏的所有数据  并发送请求获取数据
const onSearch = async (val: string) => {
  // console.log(val);
  router.push({
    query: searchParams.value,
  });
  loadData(searchParams.value);
};

// 07. tab栏切换的回调
const typeChange = (val: string) => {
  router.push({
    path: `/${val}`,
    query: searchParams.value,
  });
  loadData(searchParams.value);
};
</script>

<style scoped></style>
