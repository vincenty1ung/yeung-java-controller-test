package com.uncle.controller.controller;

import com.uncle.components.monitor.message.api.facade.MonitorMessageFacade;
import com.uncle.core.UncleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨戬
 * @email yangb@email.com
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Resource
    MonitorMessageFacade messageFacade;


    @GetMapping("/test/test.do")
    public void test() throws UncleException {
        log.error("这是error的");
        log.info("这是info的");
        log.debug("这是debug的");
        log.trace("这是trace的");
        log.warn("这是warn的");
        messageFacade.testMessageConsumer();
    }

}
