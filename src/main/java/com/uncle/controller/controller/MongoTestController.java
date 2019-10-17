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
 * @email yangb@chaosource.com
 * @date 19-10-17 14:30
 */
@RestController
@RequestMapping("/mongo")
public class MongoTestController {
    @Resource
    private ManMongoService manMongoService;

    @PostMapping("/save/man")
    public void saveMan(@RequestBody Man man) throws UncleException {
        manMongoService.insert(man);
    }

    @GetMapping("/select/man")
    public Man selectMan(String name) throws UncleException {
        return manMongoService.selectByName(name);
    }

    @GetMapping("/list/man")
    public List<Man> listAll() throws UncleException {
        return manMongoService.listAll();
    }

}
