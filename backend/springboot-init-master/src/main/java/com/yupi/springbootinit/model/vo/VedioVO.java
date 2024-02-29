package com.yupi.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class VedioVO implements Serializable {
    // 视频地址
    private String arcurl;
    // 图片
    private String pic;

    private String title;

    private String description;
    // 作者
    private String author;
    // 最新发布 时间
    private Integer pubdate;
    // 用户头像
    private String upic;
    private static final long serialVersionUID = 1L;
}