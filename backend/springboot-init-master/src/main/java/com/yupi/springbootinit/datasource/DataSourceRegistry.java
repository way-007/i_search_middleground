package com.yupi.springbootinit.datasource;

import com.yupi.springbootinit.model.enums.SearchCategoryEnum;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 02. 注册数据源 使用map集合
 */

@Component
public class DataSourceRegistry {

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private UserServiceDataSource userServiceDataSource;

    @Resource
    private PostServiceDataSource postServiceDataSource;

    @Resource
    private VedioDataSource vedioDataSource;


    private Map<String, DataSource<T>> dataSourceMap;


    // 用于标记一个方法在对象创建并完成了依赖注入后被自动调用  然后再注册数据源初始化map
    // 这里的数据源不是userService 别搞错了
    @PostConstruct
    public void initData() {
        dataSourceMap = new HashMap(){{
           put(SearchCategoryEnum.PICTURE.getValue(), pictureDataSource);
           put(SearchCategoryEnum.USER.getValue(), userServiceDataSource);
           put(SearchCategoryEnum.POST.getValue(), postServiceDataSource);
           put(SearchCategoryEnum.VEDIO.getValue(), vedioDataSource);
        }};
    }

    public DataSource getDataSourceByType(String type) {
        if(type == null) {
            return null;
        }
        return dataSourceMap.get(type);
    }

}
