package com.xhf.common.redis.utils;

public interface RedisConstant {
    String SEMID = "SEMID_";

    String TOPIC = "TOPIC_";

    String FUTURE = "FUTURE_";

    String USER = "USER_";

    String REQUEST_ID = "REQUEST_ID_";

    String SIGN = "SIGN";

    String CHAT = "CHAT_FACE_TO_FACE_";

    String CHAT_LOCK = "CHAT_LOCK_";

    /**
     * 获取聊天信息存储redis的key
     * @param id 消息来源id
     * @param toUserId 消息去向id
     * @return
     */
    static String getChatKey(Long id, Long toUserId) {
        return RedisConstant.CHAT + getChatKeySuffix(id, toUserId);
    }

    static String getChatKeySuffix(Long id, Long toUserId) {
        return id < toUserId ? id + "_" +  toUserId : toUserId + "_" + id;
    }

    static String getChatMark(Long id, Long toUserId) {
        return id < toUserId ? id + "_" +  toUserId : toUserId + "_" + id;
    }

    static String getChatKeySuffix(String json) {
        return json.replace(CHAT, "");
    }

    static String getChatLockKey(Long id, Long toUserId) {
        return RedisConstant.CHAT_LOCK + getChatKeySuffix(id, toUserId);
    }

    static String getChatLockKey(String json) {
        return RedisConstant.CHAT_LOCK + getChatKeySuffix(json);
    }
}
