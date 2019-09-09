package com.xmbl.h5.web.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.h5.web.common.consts.EMsgCode;
import com.xmbl.h5.web.common.dto.ResponseResult;
import com.xmbl.h5.web.common.logic.AbstractController;
import com.xmbl.h5.web.game.consts.SystemConst;
import com.xmbl.h5.web.game.entity.WxTranspondRecord;
import com.xmbl.h5.web.game.service.WxTranspondRecordService;
import com.xmbl.h5.web.game.util.Base64Util;
import com.xmbl.h5.web.game.util.HttpUtils;
import com.xmbl.h5.web.game.util.WeChatShareUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile 2018-12-12 -- 15:06
 * @Version 1.0
 * @Description H5分享
 */
@Slf4j
@RestController
@RequestMapping("/h5/web")
@CrossOrigin(origins = "*", maxAge = 3600)
public class H5ShareController extends AbstractController {

    @Autowired
    private WxTranspondRecordService wxTranspondRecordService;


    /**
     *
     * @param url base64字符串  decode解码 url地址
     * @return
     */
    @PostMapping("/share")
    public ResponseResult shareWeb(@RequestParam(value = "url",required = false) String url) {
        log.info("请求域中未解码的base64地址为, jsonData, {}",url);
        Map<String, String> headers = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        Map<String, String> resultMap = new HashMap<>();
        String jsapi_ticket = null;
        try {

            String shareUrl = Base64Util.decode(url);
            log.info("分享的 URL地址 ,shareUrl,{}",shareUrl);

            WxTranspondRecord wxTranspondRecord = wxTranspondRecordService.findLasted();
            if (wxTranspondRecord != null && wxTranspondRecord.getExpireTime().getTime() > new Date().getTime()) {
                resultMap = WeChatShareUtil.sign(wxTranspondRecord.getTicket(), shareUrl);
            } else {
                // 1.根据access_token 获取jsapi_ticket
                String accessToken = getAccessToken();
                // HTTP 获取access_token
                String host = "https://api.weixin.qq.com";
                String path = "/cgi-bin/ticket/getticket";
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                headers.put("Accept", "text/plain;charset=utf-8");
                querys.put("type", "jsapi");
                querys.put("access_token", accessToken);
                HttpResponse httpResponse = HttpUtils.doGet(host, path, headers, querys);
                /**请求发送成功，并得到响应**/
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    /**读取服务器返回过来的json字符串数据**/
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    /**把json字符串转换成json对象**/
                    JSONObject jsonResult = JSONObject.parseObject(strResult);
                    jsapi_ticket = jsonResult.getString("ticket");
                }
//                // 移除过期ticket
//                wxTranspondRecordService.updateRecordExpire(wxTranspondRecord);
                // 更新节点
                WxTranspondRecord record = new WxTranspondRecord(jsapi_ticket, accessToken, 0);
                wxTranspondRecordService.saveRecord(record);
                resultMap = WeChatShareUtil.sign(jsapi_ticket, shareUrl);
                log.info("分享结果，resultMap==============,{}", JSONObject.toJSONString(resultMap));
            }
            return successJson(resultMap);
        } catch (Exception e) {
            return errorJson(EMsgCode.share_failure);
        }
    }

    /**
     * 获取access_token（有效期7200秒，开发者必须在自己的服务全局缓存access_token）
     *
     * @return
     */
    private String getAccessToken() {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        String access_token = null;
        try {
            String client_credential = "client_credential";
            String app_id = SystemConst.APPID;
            String app_secret = SystemConst.APPSECRET;
            // HTTP 获取access_token
//            https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
            String host = "https://api.weixin.qq.com";
            String path = "/cgi-bin/token";
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            headers.put("Accept", "text/plain;charset=utf-8");
            querys.put("grant_type", client_credential);
            querys.put("appid", app_id);
            querys.put("secret", app_secret);
            HttpResponse httpResponse = HttpUtils.doGet(host, path, headers, querys);
            /**请求发送成功，并得到响应**/
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                /**把json字符串转换成json对象**/
                JSONObject jsonResult = JSONObject.parseObject(strResult);
                access_token = jsonResult.getString("access_token");
            }
            return access_token;
        } catch (Exception e) {
            log.error("获取access_token失败,errorMsg,{}", e.getMessage());
            return null;
        }
    }


}
