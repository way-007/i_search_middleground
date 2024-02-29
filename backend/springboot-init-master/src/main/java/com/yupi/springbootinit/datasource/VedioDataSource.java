package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.dto.vedio.VedioQueryRequest;
import com.yupi.springbootinit.model.entity.Images;
import com.yupi.springbootinit.model.vo.VedioVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.VedioService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VedioDataSource implements DataSource<VedioVO> {

    @Resource
    private VedioService vedioService;

    @Override
    public Page<VedioVO> doSearch(String searchText, int pageSize, int pageNum) {
        VedioQueryRequest vedioQueryRequest = new VedioQueryRequest();
        vedioQueryRequest.setSearchText(searchText);
        vedioQueryRequest.setPageSize(pageSize);
        vedioQueryRequest.setCurrent(pageNum);
        Page<VedioVO> vedioVOPage = vedioService.searchVedio(vedioQueryRequest);
        return vedioVOPage;
    }
}
