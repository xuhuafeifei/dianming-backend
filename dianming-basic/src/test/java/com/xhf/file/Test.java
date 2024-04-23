package com.xhf.file;

import com.xhf.file.service.OSSService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class Test {
    @Autowired
    private OSSService ossService;

    @org.junit.Test
    public void test1() throws FileNotFoundException {
        File file = new File("C:\\Users\\25080\\Pictures\\22.jpg");
        String s = ossService.uploadFile(new FileInputStream(file), "22.jpg");
        System.out.println(s);
        log.info("" + s);
    }

    @org.junit.Test
    public void test2() throws FileNotFoundException {
        byte[] bytes = ossService.downLoadFile("22.jpg");
        log.info("" + bytes);
    }

    @org.junit.Test
    public void test3() {
        ossService.delete("22.jpg");
    }

}
