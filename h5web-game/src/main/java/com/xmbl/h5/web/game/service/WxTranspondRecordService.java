package com.xmbl.h5.web.game.service;

import com.xmbl.h5.web.game.entity.WxTranspondRecord;

public interface WxTranspondRecordService {

    WxTranspondRecord findLasted();

    void updateRecordExpire(WxTranspondRecord wxTranspondRecord);

    void saveRecord(WxTranspondRecord record);
}
