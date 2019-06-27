package com.netease.user.controller;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.netease.common.entity.mysql.WechatUser;
import com.netease.repository.mysql.dao.core.WechatUserDao;
import com.netease.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈〉
 *
 * @author baodekang
 * @create 2019/3/19
 */
@RestController
public class UserServiceController implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceController.class);

    @Autowired
    private WechatUserDao wechatUserDao;

    @Autowired
    @Qualifier(value = "stringRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Override
    public String insert(@RequestBody WechatUser wechatUser) {
        Transaction transaction = Cat.newTransaction("UserService", "insert");
        try {
            wechatUserDao.insert(wechatUser);
            if (!redisTemplate.hasKey("demo")) {
                redisTemplate.opsForValue().set("demo", "demo");
            }
            Cat.logEvent("demo", "demo");
            transaction.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            logger.error("Insert wechat user error", e);
            transaction.setStatus(e);
        } finally {
            transaction.complete();
        }
        return "success";
    }

    @Override
    public WechatUser query(@RequestParam("id") Long id) {
        return wechatUserDao.queryById(id);
    }

    @Override
    public String demo() {
        return jdbcUrl;
    }
}