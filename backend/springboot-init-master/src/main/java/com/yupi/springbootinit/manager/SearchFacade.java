package com.yupi.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.datasource.*;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchAllQueryRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.dto.vedio.VedioQueryRequest;
import com.yupi.springbootinit.model.entity.Images;
import com.yupi.springbootinit.model.enums.SearchCategoryEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.model.vo.VedioVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 门面模式的代码  搜索方法中的
 */
@Component
public class SearchFacade {

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private UserServiceDataSource userServiceDataSource;

    @Resource
    private PostServiceDataSource postServiceDataSource;

    @Resource
    private VedioDataSource vedioDataSource;


    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public SearchVO searchAllList(@RequestBody SearchAllQueryRequest searchAllQueryRequest, HttpServletRequest request) {
        if (searchAllQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        // 1. 校验传递的type参数是否存在，  不存在就默认是查询所有的数据user, post, picture   存在的化就查询对应的数据
        String category = searchAllQueryRequest.getCategory();   // 这里是string类型的值  前端传递过来的  用户  文章  图片
        // 判断用户是否传递了category
        ThrowUtils.throwIf(StringUtils.isBlank(category), ErrorCode.NOT_FOUND_ERROR);
        SearchCategoryEnum enumByValue = SearchCategoryEnum.getEnumByValue(category);
        // 说明用户不进行分类查询
        String searchText = searchAllQueryRequest.getSearchText();
        int pageSize = searchAllQueryRequest.getPageSize();
        int pageNum = searchAllQueryRequest.getCurrent();
        if (enumByValue == null) {
            // 01. 调用接口查询用户数据
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userServiceDataSource.doSearch(searchText, pageSize, pageNum);
            // 02. 调用接口查询帖子数据
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postServiceDataSource.doSearch(searchText, pageSize, pageNum);
            // 03. 调用接口查询图片数据
            PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
            pictureQueryRequest.setSearchText(searchText);
            Page<Images> imagesPage = pictureDataSource.doSearch(searchText, pageSize, pageNum);
            // 04. 调用接口查询视频数据
            VedioQueryRequest vedioQueryRequest = new VedioQueryRequest();
            vedioQueryRequest.setSearchText(searchText);
            Page<VedioVO> vedioVOPage = vedioDataSource.doSearch(searchText, pageSize, pageNum);
            // 04. 将查询结果传递给SearchVO封装类
            SearchVO searchVO = new SearchVO();
            searchVO.setUserList(userVOPage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setImagesList(imagesPage.getRecords());
            searchVO.setVedioList(vedioVOPage.getRecords());
            return searchVO;
        } else {
            // 使用注册模式可以极大的简化这里的代码  原本是很多的switch case
            SearchVO searchVO = new SearchVO();
            // 根据type获取数据
            DataSource<?> dataSourceByType = dataSourceRegistry.getDataSourceByType(category);
            // 调用方法
            Page<?> page = dataSourceByType.doSearch(searchText, pageSize, pageNum);
            // 获取到分页里面的records数据才对
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
