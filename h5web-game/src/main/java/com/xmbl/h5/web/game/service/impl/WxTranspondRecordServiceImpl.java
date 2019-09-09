package com.xmbl.h5.web.game.service.impl;

import com.xmbl.h5.web.game.dao.WxTranspondRecordDao;
import com.xmbl.h5.web.game.entity.WxTranspondRecord;
import com.xmbl.h5.web.game.service.WxTranspondRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile --
 * @Version 1.0
 * @Description
 */
@Service
public class WxTranspondRecordServiceImpl implements WxTranspondRecordService {

    @Autowired
    private WxTranspondRecordDao wxTranspondRecordDao;

    @Override
    public WxTranspondRecord findLasted() {
        return wxTranspondRecordDao.findLasted();
    }

    @Override
    public void updateRecordExpire(WxTranspondRecord wxTranspondRecord) {
        wxTranspondRecordDao.updateRecordExpire(wxTranspondRecord);
    }

    @Override
    public void saveRecord(WxTranspondRecord record) {
        wxTranspondRecordDao.saveRecord(record);
    }
}
