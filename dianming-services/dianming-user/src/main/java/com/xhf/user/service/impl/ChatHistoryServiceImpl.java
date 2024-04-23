package com.xhf.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xhf.common.redis.CacheService;
import com.xhf.common.redis.utils.RedisConstant;
import com.xhf.common.utils.R;
import com.xhf.model.user.entity.ChatHistoryEntity;
import com.xhf.user.dao.ChatHistoryDao;
import com.xhf.user.service.ChatHistoryService;
import com.xhf.utils.common.Constant;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("chatHistoryService")
@Slf4j
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryDao, ChatHistoryEntity> implements ChatHistoryService {
    @Autowired
    private CacheService cacheService;

    @Autowired
    @Qualifier("myRedissonConfig")
    private Redisson redisson;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ChatHistoryEntity> page = this.page(
                new Query<ChatHistoryEntity>().getPage(params),
                new QueryWrapper<ChatHistoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 将redis中, rediskey的value同步至Mysql
     *
     * @param redisKey
     * @return
     */
    @Override
    public R synchronization(String redisKey) {
        String lockKey = RedisConstant.getChatLockKey(redisKey);
        RLock redissonLock = redisson.getLock(lockKey);
        boolean flag = false;
        log.info("=========================redis与Mysql聊天数据备份开始=========================");
        try {
            log.info("加锁:{}", lockKey);
            // 加锁
            redissonLock.lock();
            // 数据同步至MySQL
            List<String> jsonList = cacheService.lRange(redisKey, 0, -1);
            if (jsonList == null || jsonList.size() == 0) {
                return R.ok();
            }
            // json转化为entity
            List<ChatHistoryEntity> chatHistoryEntityList = convertJsonToChatHistoryList(jsonList);
            // 存储数据库
            flag = this.saveBatch(chatHistoryEntityList);
            // 删除redis
            cacheService.delete(redisKey);
        } catch (Exception e) {
            log.error("", e);
            return R.error();
        }finally {
            log.info("解锁:{}", lockKey);
            // 释放锁
            redissonLock.unlock();
        }
        log.info("=========================redis与Mysql 数据备份结束=========================");
        return flag ? R.ok() : R.error();
    }

    /**
     * 查询历史数据
     *
     * @return
     */
    @Override
    public R getHistory(Map<String, Object> params) {
        Long id = Long.valueOf((String) params.get("id"));
        Long toUserId = Long.valueOf((String) params.get("toUserId"));

        String mark = RedisConstant.getChatKeySuffix(id, toUserId);

        IPage<ChatHistoryEntity> page = this.page(
                new Query<ChatHistoryEntity>().getPage(params),
                new LambdaQueryWrapper<ChatHistoryEntity>().
                        orderByDesc(ChatHistoryEntity::getChatId).
                        eq(ChatHistoryEntity::getMark, mark)
        );
        Collections.reverse(page.getRecords());

        return R.ok().put("data", page.getRecords());
    }

    /**
     * 聊天数据初始化, 先查询redis数据, 然后查询MySQL
     * 降序返回数据. 也即最新的数据出现在list下标最大处
     * @param redisKey
     * @return
     */
    @Override
    public R chatInit(String redisKey) {
        log.info("==================聊天信息初始化====================");
        String lockKey = RedisConstant.getChatLockKey(redisKey);
        log.info("lockKey=" + lockKey);
        RLock lock = redisson.getLock(lockKey);

        try {
            lock.lock(); // 获取分布式锁
            // 执行查询操作
            List<String> jsonList = cacheService.lRange(redisKey, 0, -1);
            if (jsonList == null || jsonList.size() == 0) {
                // 查询数据库
                Map<String, Object> params = new HashMap<String, Object>();
                params.put(Constant.PAGE, "1");
                params.put(Constant.LIMIT, "20");

                IPage<ChatHistoryEntity> page = this.page(
                        new Query<ChatHistoryEntity>().getPage(params),
                        new LambdaQueryWrapper<ChatHistoryEntity>().
                                orderByDesc(ChatHistoryEntity::getChatId).
                                eq(ChatHistoryEntity::getMark, RedisConstant.getChatKeySuffix(redisKey))
                );
                Collections.reverse(page.getRecords());
                return R.ok().put("data", page.getRecords());
            }
            // 返回redis数据
            List<ChatHistoryEntity> chatHistoryEntityList = convertJsonToChatHistoryList(jsonList);
            return R.ok().put("data", chatHistoryEntityList);
        } finally {
            lock.unlock(); // 释放分布式锁
            log.info("解锁:{}", lockKey);
            log.info("----------信息初始化结束--------------");
        }
    }

    /**
     * 将jsonList转化为chatList
     * @param jsonList
     * @return
     */
    private List<ChatHistoryEntity> convertJsonToChatHistoryList(List<String> jsonList) {
        return jsonList.stream().map(e -> {
            try {
                return objectMapper.readValue(e, ChatHistoryEntity.class);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }
}