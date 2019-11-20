package com.uncle.controller.mongo.service.impl;

import com.uncle.controller.mongo.dao.ManMongoDAO;
import com.uncle.controller.mongo.service.ManMongoService;
import com.uncle.controller.mongo.bo.Man;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨戬
 * @className ManMongoServiceImpl
 * @email uncle.yeung.bo@gmail.com
 * @date 19-10-17 14:27
 */
@Service
@Slf4j
public class ManMongoServiceImpl implements ManMongoService {

    @Resource
    private ManMongoDAO manMongoDAO;

    @Override
    public void insert(Man man) {
        manMongoDAO.insert(man);
        log.info("新增man数据成功{}", man);
    }

    @Override
    public Man selectByName(String name) {
        log.info("根据name查询man：{}", name);
        return manMongoDAO.selectByName(name);
    }

    @Override
    public Man selectById(String id) {
        return manMongoDAO.selectById(id);
    }

    @Override
    public List<Man> listAll() {
        return manMongoDAO.listAll();
    }
}
