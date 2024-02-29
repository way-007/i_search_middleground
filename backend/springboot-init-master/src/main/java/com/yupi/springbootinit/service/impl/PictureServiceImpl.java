package com.yupi.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.entity.Images;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import com.yupi.springbootinit.exception.ThrowUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Images> searchPicture(PictureQueryRequest pictureQueryRequest) {
        long pageSize = pictureQueryRequest.getPageSize();
        long pageNum = pictureQueryRequest.getCurrent();

        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        long index = (pageNum - 1) * pageSize;
        String str = pictureQueryRequest.getSearchText();
        String url = String.format("https://cn.bing.com/images/search?q=%s&form=HDRSC2&first=%s",str, index);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "没有请求到任何数据");
        }
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

            // 限制取出的数据条数
            if(ImageList.size() >= pageSize) {
                break;
            }
        }
        // 将list数据对象转换为page类型的分页返回对象
        Page<Images> imagesPage = new Page<>(pageNum, pageSize);
        imagesPage.setRecords(ImageList);
        return imagesPage;
    }
}
