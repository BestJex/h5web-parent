package com.xmbl.h5.web.game.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile --
 * @Version 1.0
 * @Description
 */
@Data
@Document(collection = "h5_wx_transpond_record")
public class WxTranspondRecord {

    @Id
    @Field("_id")
    private String id;

    @Field("ticket")
    private String ticket;

    @Field("access_token")
    private String access_token;

    @Field("expire_time")
    private Long expire_time = 90*60*1000L;

    private Date createTime = new Date();

    private Date expireTime = new Date(System.currentTimeMillis() + expire_time);

    private int status;

    public WxTranspondRecord() {
    }

    public WxTranspondRecord(String ticket, String access_token, int status) {
        this.ticket = ticket;
        this.access_token = access_token;
        this.status = status;
    }
}
