package com.xhf.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.xhf.common.utils.R;
import com.xhf.file.service.OSSService;
import com.xhf.model.user.dtos.UserDto;
import com.xhf.model.user.entity.StudentEntity;
import com.xhf.model.user.entity.UserEntity;
import com.xhf.user.service.StudentService;
import com.xhf.user.service.UserService;
import com.xhf.utils.common.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.xhf.user.utils.FileNameUtil.getFileName;

/**
 *
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:15:36
 */
@RestController
@RequestMapping("user/student")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private OSSService ossService;

    @Autowired
    private Gson gson;

    /**
     * 列表,查询学生信息
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = studentService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/listAll")
    public R listAll() {
        // 查询未被锁定的用户
        LambdaQueryWrapper<StudentEntity> query = new LambdaQueryWrapper<>();
        query.eq(StudentEntity::isLocked, false);
        List<StudentEntity> list = studentService.list(query);
        Collections.shuffle(list);
        return R.ok().put("data", list);
    }


    /**
     * 根据id查询学生信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        StudentEntity student = studentService.getById(id);
        return R.ok().put("student", student);
    }

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 保存, 修改图片
     */
    @RequestMapping("/save_with_pict")
    @Transactional
    public R save(@RequestParam("student") String studentJSON, @RequestParam("image") MultipartFile image) throws JsonProcessingException {
        // 存储图片
        StudentEntity student = objectMapper.readValue(studentJSON, StudentEntity.class);
        ossService.uploadFile(image, student, getFileName(student));
        if (student.getId() == null) {
            studentService.save(student);
            // 创建user对象
            userService.saveByStu(student);
        }else {
            studentService.updateById(student);
        }
        return R.ok().put("data", student);
    }

    /**
     * 保存, 没有图片
     * @param student
     * @return
     */
    @RequestMapping("/save_without_pict")
    @Transactional
    public R save(@RequestBody StudentEntity student) {
        if (student.getId() == null) {
            studentService.save(student);
            // 创建user对象
            userService.saveByStu(student);
        }else {
            studentService.updateById(student);
        }
        return R.ok().put("data", student);
    }

    /**
     * 修改个人信息, 不包括图片
     */
    @PostMapping("/personal/update_without_pict")
    @Transactional
    public R update(@RequestBody UserDto userDto) {
        StudentEntity stu = userDto.toStu();
        UserEntity user = userDto.toUser();
        studentService.updatePersonal(stu, user);
        return R.ok();
    }

    /**
     * 修改个人信息, 只修改图片
     */
    @PostMapping("/personal/update_with_pict")
    public R update(@RequestParam("image") MultipartFile image, @RequestParam("id") String id) {
        String fileName = getFileName(image);
        // 存储图片
        String pictUrl = ossService.uploadFile(image, fileName);
        // 修改对象
        StudentEntity entity = new StudentEntity();
        entity.setId(Long.valueOf(id));
        entity.setSportrait(pictUrl);
        studentService.updateById(entity);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody StudentEntity student){
        studentService.updateById(student);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        studentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 导出excel
     */
    @PostMapping(value = "/export")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据写出", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        studentService.export(response);
    }

    /**
     * 导入excel
     */
    @PostMapping(value = "/upload/excel")
    public R uploadExcel(@RequestBody List<StudentEntity> studentList) {
        boolean b = studentService.saveBatch(studentList);
        // 保存并创建用户
        if (b) {
            // todo: 创建用户
            userService.saveByStu(studentList);
        }
        return b ? R.ok() : R.error();
    }

    @GetMapping("/lock")
    public R lock(@RequestParam Long id) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(id);
        studentEntity.setLocked(true);
        studentEntity.setLockTime(LocalDateTime.now());
        studentService.updateById(studentEntity);
        return R.ok();
    }

    @PostMapping("/uploadImages")
    public R uploadImages(@RequestParam("images") List<MultipartFile> multipartFileList) {
        return studentService.uploadImages(multipartFileList);
    }

}
