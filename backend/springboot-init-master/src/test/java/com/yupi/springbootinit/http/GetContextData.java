package com.yupi.springbootinit.http;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.*;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Images;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.vo.VedioVO;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
public class GetContextData {
    @Resource
    private PostService postService;

    // 根据html获取网站的图片数据
    @Test
    void getPic() throws IOException {
        int current = 1;
        String url = String.format("https://cn.bing.com/images/search?q=小黑子&form=HDRSC2&first=%s", current);
        Document doc = Jsoup.connect(url).get();
        // 01. 获取到内容的class标签  div  不是整个div是单独的每个div   属性之间不能有空格
        Elements elements = doc.select(".iuscp.isv");
        List<Images> ImageList = new ArrayList<>();
        // 02. 获取内容里面的img和title标签  都在iusc里面
        for (Element element : elements) {
            // 获取头像链接地址
            String a = element.select(".iusc").get(0).attr("m");
            // 转为一个json对象
            Map<String, Object> map = JSONUtil.toBean(a, Map.class);
            String imgUrl = (String) map.get("murl");
//            System.out.println(imgUrl);

            // 获取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);

            // 存储到对象中
            Images images = new Images();
            images.setTitle(title);
            images.setImage(imgUrl);
            ImageList.add(images);
        }
    }

    // 根据url测试获取网站数据
    @Test
    void getHttp() {
        // 01. 根据接口获取数据
        String json = "{\"current\": 1, \"pageSize\": 8, \"sortField\": \"createTime\", \"sortOrder\": \"descend\", \"category\": \"文章\",\"reviewStatus\": 1}";
        String json2 = "{\"current\": 2, \"pageSize\": 8, \"sortField\": \"thumbNum\", \"sortOrder\": \"descend\", \"category\": \"项目\", \"tags\": [\"项目\"]}";
//        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String url2 = "https://www.code-nav.cn/api/post/search/page/vo";
        String url3 = "https://bilibili.com";
//        String result = HttpRequest
//                .post(url3)
//                .body(json)
//                .execute()
//                .body();
//        System.out.println(result);

        String url = "https://www.bilibili.com/";
        List<HttpCookie> cookieList = HttpRequest.get(url).execute().getCookies();
        System.out.println(cookieList);
        // 02. 将json数据转为对象格式  创建一个专门的对象来转（需要所有字段的时候）   创建一个map对象（只需要部分字段时）
//        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
//        // 03. 取出data里面的recores，数据需要强转为json格式  debug可以看出
//        JSONObject data = (JSONObject) map.get("data");
//        JSONArray records = (JSONArray) data.get("records");
//        List<Post> listPost = new ArrayList<>();
//        // 04. 遍历里面的数据然后写入到Post表里面
//        for (Object record : records) {
//            JSONObject tempData = (JSONObject) record;
//            // 创建一个post
//            Post post = new Post();
//            post.setTitle(tempData.getStr("title"));
//            post.setContent(tempData.getStr("description"));
//            // 先将其转换为一个json格式的数组  因为它云本就是一个数组格式，然后转为列表格式
//            JSONArray tagTemp = (JSONArray) tempData.get("tags");
//            // 转为一个string格式的列表  []
//            List<String> tagList = tagTemp.toList(String.class);
//            post.setTags(JSONUtil.toJsonStr(tagList));
//            post.setUserId(1L);
//            listPost.add(post);
//        }
//        System.out.println(listPost);
//        // 05. 数据入库
//        boolean res = postService.saveBatch(listPost);
//        Assertions.assertTrue(res);



    }

    @Test
    void vedioTest() {
        String url1 = "https://www.bilibili.com/";
        String searchText = "小黑子";
        String url2 = String.format("https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=%s",searchText);
        // 这里有三个值，取最后一个值才是cookie
        HttpCookie cookie = HttpRequest.get(url1).execute().getCookie("buvid3");

//        String body = null;
//        try {
//            body = retryer.call(() -> HttpRequest.get(url2)
//                    .cookie(cookie)
//                    .execute().body());
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"重试失败");
//        }
        String result = HttpRequest.get(url2).cookie(cookie).execute().body();
        System.out.println(result);

        Map map = JSONUtil.toBean(result, Map.class);
        Map data = (Map)map.get("data");
        JSONArray videoList = (JSONArray) data.get("result");
//        Page<VideoVo> page = new Page<>(pageNum,pageSize);
        List<VedioVO> videoVoList = new ArrayList<>();
        for(Object video:videoList){
            JSONObject tempVideo = (JSONObject)video;
            VedioVO videoVo = new VedioVO();
            videoVo.setUpic(tempVideo.getStr("upic"));
            videoVo.setAuthor(tempVideo.getStr("author"));
            videoVo.setPubdate(tempVideo.getInt("pubdate"));
            videoVo.setArcurl(tempVideo.getStr("arcurl"));
            videoVo.setPic("http:"+tempVideo.getStr("pic"));
            videoVo.setTitle(tempVideo.getStr("title"));
            videoVo.setDescription(tempVideo.getStr("description"));
            videoVoList.add(videoVo);
//            if(videoVoList.size()>=pageSize){
//                break;
//            }
        }
        System.out.println(videoVoList);
//        page.setRecords(videoVoList);
//        return page;
    }
}



