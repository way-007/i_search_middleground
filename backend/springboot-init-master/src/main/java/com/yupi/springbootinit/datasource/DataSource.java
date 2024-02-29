package com.yupi.springbootinit.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

/**
 * 01. 定义需要接入数据的接口规范
 */
@Service
public interface DataSource<T> {
    Page<T> doSearch(String searchText, int pageSize, int pageNum);
}
