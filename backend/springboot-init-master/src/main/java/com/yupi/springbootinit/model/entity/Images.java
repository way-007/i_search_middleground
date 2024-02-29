package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@TableName(value = "user")
@Data
public class Images implements Serializable {

    private String title;

    private String image;

}