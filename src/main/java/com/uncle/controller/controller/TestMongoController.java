package com.uncle.controller.controller;

import com.uncle.controller.mongo.bo.Man;
import com.uncle.controller.mongo.service.ManMongoService;
import com.uncle.core.UncleException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨戬
 * @className MongoTestController
 * @email uncle.yeung.bo@gmail.com
 * @date 19-10-17 14:30
 */
@RestController
@RequestMapping("/test/mongo")
public class TestMongoController {
    @Resource
    private ManMongoService manMongoService;

    /**
     * Mongo-Test-增量man数据
     *
     * @param man json->man
     * @throws UncleException 系统异常
     */
    @PostMapping("/save/man")
    public void saveMan(@RequestBody Man man) throws UncleException {
        manMongoService.insert(man);
    }

    /**
     * Mongo-Test-根据name查询man数据
     *
     * @param name 名字
     * @return json->man
     * @throws UncleException 系统异常
     */
    @GetMapping("/select/man/name")
    public Man selectManByName(String name) throws UncleException {
        return manMongoService.selectByName(name);
    }

    /**
     * Mongo-Test-根据id查询man数据
     *
     * @param id 名字
     * @return json->man
     * @throws UncleException 系统异常
     */
    @GetMapping("/select/man/id")
    public Man selectManById(String id) throws UncleException {
        return manMongoService.selectById(id);
    }

    /**
     * Mongo-Test-获取man数据列表
     *
     * @return json->lsit<man>
     * @throws UncleException 系统异常
     */
    @GetMapping("/list/man")
    public List<Man> listAll() throws UncleException {
        return manMongoService.listAll();
    }

}
