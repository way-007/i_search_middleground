package com.yupi.springbootinit.model.dto.search;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 聚合搜索请求参数
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchAllQueryRequest extends PageRequest implements Serializable {

    /**
     * 聚合搜索的参数  只需要输入一个关键字进行查询
     */
    private String searchText;

    /**
     * 根据传递过来的tab标签分类的类型查询数据
     */
    private String category;


    private static final long serialVersionUID = 1L;
}