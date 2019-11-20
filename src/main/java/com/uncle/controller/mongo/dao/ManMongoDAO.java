package com.uncle.controller.mongo.dao;

import com.uncle.controller.mongo.bo.Man;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨戬
 * @className ManMongoDAO
 * @email uncle.yeung.bo@gmail.com
 * @date 19-10-17 14:18
 */
@Repository
public class ManMongoDAO {
    public static final String COLLECTION_MAN = "man";

    @Resource
    private MongoTemplate mongoTemplate;


    public void insert(Man man) {
        mongoTemplate.insert(man, COLLECTION_MAN);
    }

    public Man selectByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Man.class);
    }
    public Man selectById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Man.class);
    }

    public List<Man> listAll() {
        return mongoTemplate.findAll(Man.class);
    }
}
