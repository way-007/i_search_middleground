package com.yupi.springbootinit.job.once;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.esdao.PostEsDao;
import com.yupi.springbootinit.model.dto.post.PostEsDTO;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全量同步帖子到 es
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 取消注释开启任务  不注释的化会每次springboot项目启动的时候都会执行一次  所以执行一次之后就要及时的注释掉
//@Component
@Slf4j
public class FetchGetPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 01. 根据接口获取数据
        String json = "{\"current\": 2, \"pageSize\": 10, \"sortField\": \"createTime\", \"sortOrder\": \"descend\", \"category\": \"文章\",\"reviewStatus\": 1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
//        System.out.println(result);
        // 02. 将json数据转为对象格式  创建一个专门的对象来转（需要所有字段的时候）   创建一个map对象（只需要部分字段时）
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        // 03. 取出data里面的recores，数据需要强转为json格式  debug可以看出
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> listPost = new ArrayList<>();
        // 04. 遍历里面的数据然后写入到Post表里面
        for (Object record : records) {
            JSONObject tempData = (JSONObject) record;
            // 创建一个post
            Post post = new Post();
            post.setTitle(tempData.getStr("title"));
            post.setContent(tempData.getStr("description"));
            // 先将其转换为一个json格式的数组  因为它云本就是一个数组格式，然后转为列表格式
            JSONArray tagTemp = (JSONArray) tempData.get("tags");
            // 转为一个string格式的列表  []
            List<String> tagList = tagTemp.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            listPost.add(post);
        }
//        System.out.println(listPost);
        // 05. 数据入库
        boolean res = postService.saveBatch(listPost);
        if(res) {
            log.info("获取用户数据成功" + listPost.size() + "条数据");
        }else {
            log.error("获取用户数据失败");
        }
    }
}
