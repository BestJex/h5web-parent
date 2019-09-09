package com.xmbl.h5.web.game.dao;

import com.xmbl.h5.web.game.entity.WxTranspondRecord;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


@Repository
public class WxTranspondRecordDao extends EntityMongoLoginDaoImpl<WxTranspondRecord> {


    public WxTranspondRecord findLasted() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(0));
        query.limit(1);
        return this.getMongoTemplate().findOne(query,WxTranspondRecord.class);
    }

    public void updateRecordExpire(WxTranspondRecord wxTranspondRecord) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(wxTranspondRecord.getId()));
        Update update = new Update();
        update.set("status",1);
        this.getMongoTemplate().updateFirst(query,update,WxTranspondRecord.class);
    }

    public void saveRecord(WxTranspondRecord record) {
        this.getMongoTemplate().save(record);
    }
}
