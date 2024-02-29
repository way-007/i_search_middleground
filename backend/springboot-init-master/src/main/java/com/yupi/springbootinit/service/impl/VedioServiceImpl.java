package com.yupi.springbootinit.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.dto.vedio.VedioQueryRequest;
import com.yupi.springbootinit.model.entity.Images;
import com.yupi.springbootinit.model.vo.VedioVO;
import com.yupi.springbootinit.service.VedioService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VedioServiceImpl implements VedioService {
    @Override
    public Page<VedioVO> searchVedio(VedioQueryRequest vedioQueryRequest) {
        long pageSize = vedioQueryRequest.getPageSize();
        long pageNum = vedioQueryRequest.getCurrent();

        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        long index = (pageNum - 1) * pageSize;
        String str = vedioQueryRequest.getSearchText();
        String url = String.format("https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=%s", str);
        String url1 = "https://www.bilibili.com/";
        String result = null;
        try {
            HttpCookie cookie = HttpRequest.get(url1).execute().getCookie("buvid3");
            result = HttpRequest.get(url).cookie(cookie).execute().body();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "没有请求到任何数据");
        }
        // 转为一个json对象使用map进行存储
        Map map = JSONUtil.toBean(result, Map.class);
        Map data = (Map) map.get("data");
        JSONArray videoList = (JSONArray) data.get("result");

        List<VedioVO> videoVoList = new ArrayList<>();
        for (Object video : videoList) {
            JSONObject tempVideo = (JSONObject) video;
            VedioVO videoVo = new VedioVO();
            videoVo.setUpic(tempVideo.getStr("upic"));
            videoVo.setAuthor(tempVideo.getStr("author"));
            videoVo.setPubdate(tempVideo.getInt("pubdate"));
            videoVo.setArcurl(tempVideo.getStr("arcurl"));
            videoVo.setPic("http:" + tempVideo.getStr("pic"));
            videoVo.setTitle(tempVideo.getStr("title"));
            videoVo.setDescription(tempVideo.getStr("description"));
            videoVoList.add(videoVo);
            if (videoVoList.size() >= pageSize) {
                break;
            }
        }
        Page<VedioVO> page = new Page<>(pageNum, pageSize);
        page.setRecords(videoVoList);
        return page;
    }
}
