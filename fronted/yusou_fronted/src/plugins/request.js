import axios from "axios";

// create an axios instance
const request = axios.create({
  baseURL: "http://localhost:8081/api", // url = base url + request url
  timeout: 100000, // request timeout
});
request.defaults.withCredentials = true;

// Add a request interceptor
request.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add a response interceptor
request.interceptors.response.use(
  function (response) {
    // if (response?.data?.code == 401001) {
    //   const redirectUrl = window.location.href;
    //   window.location.href = `/user/login?redirect=${redirectUrl}`;
    // }
    // Do something with response data
    const result = response.data;
    if (result.code === 0) {
      return response.data.data;
    }
    return response.data;
  },
  function (error) {
    // Do something with response error
    return Promise.reject(error);
  }
);

export default request;
