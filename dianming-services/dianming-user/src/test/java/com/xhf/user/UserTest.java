package com.xhf.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.xhf.common.redis.utils.RedisConstant;
import com.xhf.file.service.OSSService;
import com.xhf.model.user.dtos.UserDto;
import com.xhf.model.user.entity.ChatHistoryEntity;
import com.xhf.model.user.entity.StudentEntity;
import com.xhf.user.config.TtlQueueConfig;
import com.xhf.user.service.ChatHistoryService;
import com.xhf.user.service.StudentService;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

//@SpringBootTest
//@RunWith(SpringRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
    @Autowired
    private OSSService ossService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Test
    public void test2() {
        List<Long> idList = studentService.list().stream().map(StudentEntity::getId).collect(Collectors.toList());
        Random random = new Random();
        ArrayList<ChatHistoryEntity> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            ChatHistoryEntity entity = new ChatHistoryEntity();
            entity.setContent("测试bigdata" + i);
            int i1 = random.nextInt(idList.size() - 1) + 1;
            int i2 = random.nextInt(idList.size() - 1) + 1;
            while (i2 == i1) {
                i2 = random.nextInt(idList.size() - 1) + 1;
            }
            String s = String.valueOf(idList.get(i1));
            String s1 = String.valueOf(idList.get(i2));
            entity.setFromUserId(s);
            entity.setToUserId(s1);
            entity.setMark(RedisConstant.getChatMark(Long.valueOf(s), Long.valueOf(s1)));
            list.add(entity);
        }
        chatHistoryService.saveBatch(list);
        // 多线程上传
        List<List<ChatHistoryEntity>> partition = Lists.partition(list, 1000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (List<ChatHistoryEntity> part : partition) {
            executorService.execute(() -> chatHistoryService.saveBatch(part, 1000));
        }
        executorService.shutdown();
    }

    @Test
    public void test() throws InterruptedException {
        List<StudentEntity> list = studentService.list();
        // 读取所有的头像数据
        List<List<StudentEntity>> partition = Lists.partition(list, 8);
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < partition.size(); i++) {
            int finalI = i;
            Thread thread = new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    // 遍历list
                    for (int i1 = 0; i1 < partition.get(finalI).size(); i1++) {
                        // 1. 下载url
                        String url = partition.get(finalI).get(i1).getSportrait();
                        // 截取url, 获取名字
                        String fileName = extractFileNameFromUrl(url);
                        InputStream inputStream = downloadImageAsStream(url);
                        // 2. 将文件存储到云端
                        String newURL = ossService.uploadFile(inputStream, fileName);
                        System.out.println(newURL);
                        // 3. 修改对象
                        partition.get(finalI).get(i1).setSportrait(newURL);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        // 等待线程结束
        for (Thread thread : threads) {
            System.out.println("abab");
            thread.join();
        }
        // 4.存储数据库
        partition.forEach(e -> {
            studentService.updateBatchById(e);
        });
    }
    public static InputStream downloadImageAsStream(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        return new BufferedInputStream(connection.getInputStream()) {
            @Override
            public void close() throws IOException {
                super.close();
                connection.disconnect();
            }
        };
    }

    public static String extractFileNameFromUrl(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            return url.substring(lastSlashIndex + 1);
        } else {
            return null; // URL 不包含斜杠或斜杠位于末尾
        }
    }

    @Data
    class A{
        private String A;
        public String getC() {
            return "abab";
        }
    }
    @Data
    class B extends A{
        private String B;
    }


    @Test
    public void test3() throws JsonProcessingException {
        UserDto b = new UserDto();
        String s = objectMapper.writeValueAsString(b);

        System.out.println(s);
    }

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void test4() throws Exception {
        A a = new A();
        a.setA("a");
        System.out.println(objectMapper.writeValueAsString(a));
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test5() {
        rabbitTemplate.convertAndSend(TtlQueueConfig.X_CHANGE, "XB", "hello rabbitMQ");
    }
}
