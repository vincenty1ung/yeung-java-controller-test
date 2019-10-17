package com.uncle.controller.mongo.test;


import com.alibaba.fastjson.JSON;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨戬
 * @className MongoTest
 * @email yangb@chaosource.com
 * @date 19-10-17 09:52
 */
public class MongoTest {
    public static void main(String[] args) {
        String str = "mongodb://admin:123456@localhost:27017/?authSource=shop";
        MongoClientURI mongoClientURI = new MongoClientURI(str);
        //创建mongoclient
        //MongoClient mongoClient = new MongoClient( "localhost" , 27017);
        //ServerAddress serverAddress = new ServerAddress("localhost",27017);
        //MongoCredential credential = MongoCredential.createCredential("admin", "shop", "123456".toCharArray());
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        MongoDatabase shop = mongoClient.getDatabase("shop");
        DB shopDb = mongoClient.getDB("shop");
        DBCollection man = shopDb.getCollection("man");

        //新增集合文档
        BasicDBObject basicBSONObject = new BasicDBObject("id","6");
        basicBSONObject.append("name","chenjian");
        //WriteResult insert = man.insert(basicBSONObject);

        //查询集合文档
        DBCursor dbObjects = man.find();
        List<Man> mens = new ArrayList<>();
        while (dbObjects.hasNext()) {
            mens.add(JSON.parseObject(dbObjects.next().toString(),Man.class));
        }
        System.out.println("mens = " + mens);
    }
}
