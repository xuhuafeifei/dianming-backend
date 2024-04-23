/*
 Navicat Premium Data Transfer

 Source Server         : 我得腾讯云
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 175.178.108.46:3306
 Source Schema         : dianming

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 01/10/2023 16:22:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_history
-- ----------------------------
DROP TABLE IF EXISTS `chat_history`;
CREATE TABLE `chat_history`  (
  `chat_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `from_user_id` bigint(20) NULL DEFAULT NULL COMMENT '消息来源',
  `to_user_id` bigint(20) NULL DEFAULT NULL COMMENT '消息去向',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '聊天内容',
  `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标记，小id_大id',
  PRIMARY KEY (`chat_id`) USING BTREE,
  INDEX `消息来源id`(`from_user_id`) USING BTREE,
  INDEX `消息去向id`(`to_user_id`) USING BTREE,
  CONSTRAINT `消息去向id` FOREIGN KEY (`to_user_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `消息来源id` FOREIGN KEY (`from_user_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天历史记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_history
-- ----------------------------
INSERT INTO `chat_history` VALUES (64, 1565, NULL, '1', '1_1565');
INSERT INTO `chat_history` VALUES (65, 1565, NULL, '1', '1_1565');
INSERT INTO `chat_history` VALUES (66, 1565, NULL, '2', '1_1565');
INSERT INTO `chat_history` VALUES (67, 1565, NULL, '1', '1_1565');
INSERT INTO `chat_history` VALUES (68, 1565, NULL, '1', '1_1565');
INSERT INTO `chat_history` VALUES (69, 1, 1565, '1', '1_1565');
INSERT INTO `chat_history` VALUES (70, 1565, 1, '飞哥不鸽', '1_1565');
INSERT INTO `chat_history` VALUES (71, 1, 1565, '测试', '1_1565');
INSERT INTO `chat_history` VALUES (72, 1, 1565, '再次测试', '1_1565');
INSERT INTO `chat_history` VALUES (73, 1, 1565, '我靠，哪个孙子上的锁', '1_1565');
INSERT INTO `chat_history` VALUES (74, 1, 1565, '别再乱锁了', '1_1565');
INSERT INTO `chat_history` VALUES (75, 1565, 1, '又不是我锁定的，老哥', '1_1565');
INSERT INTO `chat_history` VALUES (76, 1, 1565, '知道了知道了', '1_1565');
INSERT INTO `chat_history` VALUES (77, 1565, 1, '哎，真累啊', '1_1565');
INSERT INTO `chat_history` VALUES (78, 1565, 1, '你好', '1_1565');
INSERT INTO `chat_history` VALUES (79, 1, 1565, '我不好', '1_1565');
INSERT INTO `chat_history` VALUES (80, 1565, 1, '12222', '1_1565');
INSERT INTO `chat_history` VALUES (81, 1565, 1, '1', '1_1565');
INSERT INTO `chat_history` VALUES (82, 1565, 1, '1', '1_1565');
INSERT INTO `chat_history` VALUES (83, 1565, 1, '你好', '1_1565');
INSERT INTO `chat_history` VALUES (84, 1, 1565, '我不好', '1_1565');
INSERT INTO `chat_history` VALUES (85, 1565, 1, '我知道你很好', '1_1565');

-- ----------------------------
-- Table structure for exam_repo
-- ----------------------------
DROP TABLE IF EXISTS `exam_repo`;
CREATE TABLE `exam_repo`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `exam_id` bigint(20) NULL DEFAULT NULL COMMENT '考试ID',
  `repo_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '题库名称',
  `repo_id` bigint(20) NULL DEFAULT NULL COMMENT '题库ID',
  `radio_count` int(11) NULL DEFAULT 0 COMMENT '单选题数量',
  `radio_score` int(11) NULL DEFAULT 0 COMMENT '单选题分数',
  `multi_count` int(11) NULL DEFAULT 0 COMMENT '多选题数量',
  `multi_score` int(11) NULL DEFAULT 0 COMMENT '多选题分数',
  `judge_count` int(11) NULL DEFAULT 0 COMMENT '判断题数量',
  `judge_score` int(11) NULL DEFAULT 0 COMMENT '判断题分数',
  `saq_count` int(11) NULL DEFAULT 0 COMMENT '简答题数量',
  `saq_score` int(11) NULL DEFAULT 0 COMMENT '简答题分数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `exam_repo_id`(`exam_id`, `repo_id`) USING BTREE,
  INDEX `rule_id`(`exam_id`) USING BTREE,
  INDEX `repo_id`(`repo_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试题库' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_repo
-- ----------------------------
INSERT INTO `exam_repo` VALUES (1, NULL, 'Linux题库', 1, 0, 0, 0, 0, 0, 0, 0, 0);

-- ----------------------------
-- Table structure for mq_message_info
-- ----------------------------
DROP TABLE IF EXISTS `mq_message_info`;
CREATE TABLE `mq_message_info`  (
  `mq_msg_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送消息',
  `error_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
  `retry_num` int(11) NULL DEFAULT 0 COMMENT '重试次数',
  `exchange_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交换机名称',
  `routing_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由key',
  `publish_start_time` datetime NULL DEFAULT NULL COMMENT '发布开始时间',
  `publish_end_time` datetime NULL DEFAULT NULL COMMENT '发布结束时间',
  `publish_status` tinyint(1) NULL DEFAULT NULL COMMENT '消息发布状态 1:解决 /0:未解决',
  `consumer_status` tinyint(1) NULL DEFAULT NULL COMMENT '消息消费状态 1:解决 /0:未解决',
  PRIMARY KEY (`mq_msg_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'MQ信息发布存储，记录发送给mq所有信息状态' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mq_message_info
-- ----------------------------
INSERT INTO `mq_message_info` VALUES ('1708396808507977729', 'CHAT_FACE_TO_FACE_1_1533', 'channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange \'chat_exchangeabab\' in vhost \'/\', class-id=60, method-id=40)', 0, 'chat_exchangeabab', 'chat_routing_key', '2023-10-01 16:21:54', NULL, 0, NULL);
INSERT INTO `mq_message_info` VALUES ('1708396863461748737', 'CHAT_FACE_TO_FACE_1_1533', 'channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange \'chat_exchangeabab\' in vhost \'/\', class-id=60, method-id=40)', 0, 'chat_exchangeabab', 'chat_routing_key', '2023-10-01 16:22:07', NULL, 0, NULL);
INSERT INTO `mq_message_info` VALUES ('1708396878267641858', 'CHAT_FACE_TO_FACE_1_1533', 'channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange \'chat_exchangeabab\' in vhost \'/\', class-id=60, method-id=40)', 0, 'chat_exchangeabab', 'chat_routing_key', '2023-10-01 16:22:11', NULL, 0, NULL);

-- ----------------------------
-- Table structure for mq_waring_info
-- ----------------------------
DROP TABLE IF EXISTS `mq_waring_info`;
CREATE TABLE `mq_waring_info`  (
  `mq_waring_id` int(11) NOT NULL AUTO_INCREMENT,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送的消息',
  `waring_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`mq_waring_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'MQ报警信息存储' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mq_waring_info
-- ----------------------------

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项',
  `image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `qu_id` bigint(20) NULL DEFAULT NULL COMMENT '关联问题id',
  `correct` tinyint(1) NULL DEFAULT NULL COMMENT '正确选项',
  `analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '答案分析',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1692018078067924998 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of options
-- ----------------------------
INSERT INTO `options` VALUES (83, 'mkdir', '', 26, 0, '');
INSERT INTO `options` VALUES (84, 'move', '', 26, 0, '');
INSERT INTO `options` VALUES (85, 'mv', '', 26, 0, '');
INSERT INTO `options` VALUES (86, 'rm', '', 26, 1, '');
INSERT INTO `options` VALUES (87, 'gzip', '', 27, 0, '');
INSERT INTO `options` VALUES (88, 'tar', '', 27, 1, '');
INSERT INTO `options` VALUES (89, 'dump', '', 27, 0, '');
INSERT INTO `options` VALUES (90, 'dd', '', 27, 0, '');
INSERT INTO `options` VALUES (91, 'df', '', 28, 1, '');
INSERT INTO `options` VALUES (92, 'du', '', 28, 0, '');
INSERT INTO `options` VALUES (93, 'ls', '', 28, 0, '');
INSERT INTO `options` VALUES (94, 'mount', '', 28, 0, '');
INSERT INTO `options` VALUES (95, '/etc/shadow', '', 29, 1, '');
INSERT INTO `options` VALUES (96, '/etc/passwd', '', 29, 0, '');
INSERT INTO `options` VALUES (97, '/etc/group', '', 29, 0, '');
INSERT INTO `options` VALUES (98, '/etc/profile', '', 29, 0, '');
INSERT INTO `options` VALUES (99, 'r- 可读', '', 30, 0, '');
INSERT INTO `options` VALUES (100, 'w- 可写', '', 30, 0, '');
INSERT INTO `options` VALUES (101, 'x- 可执行', '', 30, 1, '');
INSERT INTO `options` VALUES (102, '都不是', '', 30, 0, '');
INSERT INTO `options` VALUES (103, 'cp', '', 31, 0, '');
INSERT INTO `options` VALUES (104, 'rmdir', '', 31, 0, '');
INSERT INTO `options` VALUES (105, 'mv', '', 31, 1, '');
INSERT INTO `options` VALUES (106, 'rename', '', 31, 0, '');
INSERT INTO `options` VALUES (107, '[abcde]', '', 32, 0, '');
INSERT INTO `options` VALUES (108, '?', '', 32, 0, '');
INSERT INTO `options` VALUES (109, '*', '', 32, 1, '');
INSERT INTO `options` VALUES (110, '[!a-e]', '', 32, 0, '');
INSERT INTO `options` VALUES (111, '开源', '', 33, 0, '');
INSERT INTO `options` VALUES (112, '模块化', '', 33, 0, '');
INSERT INTO `options` VALUES (113, '安全性好', '', 33, 0, '');
INSERT INTO `options` VALUES (114, '收费', '', 33, 1, '');
INSERT INTO `options` VALUES (115, 'cp', '', 34, 0, '');
INSERT INTO `options` VALUES (116, 'rmdir', '', 34, 0, '');
INSERT INTO `options` VALUES (117, 'mv', '', 34, 0, '');
INSERT INTO `options` VALUES (118, 'rename', '', 34, 1, '');
INSERT INTO `options` VALUES (119, 'rwx', '', 35, 1, '');
INSERT INTO `options` VALUES (120, 'xrw', '', 35, 0, '');
INSERT INTO `options` VALUES (121, 'rdx', '', 35, 0, '');
INSERT INTO `options` VALUES (122, 'srw', '', 35, 0, '');
INSERT INTO `options` VALUES (123, '/lib', '', 36, 0, '');
INSERT INTO `options` VALUES (124, '/dev', '', 36, 0, '');
INSERT INTO `options` VALUES (125, '/proc', '', 36, 0, '');
INSERT INTO `options` VALUES (126, '/etc', '', 36, 1, '');
INSERT INTO `options` VALUES (127, 'chown', '', 37, 0, '');
INSERT INTO `options` VALUES (128, 'chgrp', '', 37, 0, '');
INSERT INTO `options` VALUES (129, 'chmod', '', 37, 1, '');
INSERT INTO `options` VALUES (130, '以上都不对', '', 37, 0, '');
INSERT INTO `options` VALUES (131, 'FAT/NTFS', '', 38, 0, '');
INSERT INTO `options` VALUES (132, 'FAT/SWAP', '', 38, 0, '');
INSERT INTO `options` VALUES (133, 'NTFS/SWAP', '', 38, 0, '');
INSERT INTO `options` VALUES (134, 'SWAP/根分区', '', 38, 1, '');
INSERT INTO `options` VALUES (135, 'root', '', 39, 1, '');
INSERT INTO `options` VALUES (136, 'guest', '', 39, 0, '');
INSERT INTO `options` VALUES (137, 'administrator', '', 39, 0, '');
INSERT INTO `options` VALUES (138, 'supervistor', '', 39, 0, '');
INSERT INTO `options` VALUES (139, 'del/tmp/*', '', 40, 0, '');
INSERT INTO `options` VALUES (140, 'rm -rf /tmp', '', 40, 1, '无');
INSERT INTO `options` VALUES (141, 'rm -Ra /tmp/*', '', 40, 0, '');
INSERT INTO `options` VALUES (142, 'rm –rf /tmp/*', '', 40, 0, '');
INSERT INTO `options` VALUES (143, 'shadow', '', 41, 0, '');
INSERT INTO `options` VALUES (144, 'group', '', 41, 0, '');
INSERT INTO `options` VALUES (145, 'passwd', '', 41, 1, '');
INSERT INTO `options` VALUES (146, 'Gshadow', '', 41, 0, '');
INSERT INTO `options` VALUES (147, '3', '', 42, 0, '');
INSERT INTO `options` VALUES (148, '6', '', 42, 1, '');
INSERT INTO `options` VALUES (149, '1', '', 42, 0, '');
INSERT INTO `options` VALUES (150, '12', '', 42, 0, '');
INSERT INTO `options` VALUES (151, 'Ctrl -C', '', 43, 1, '');
INSERT INTO `options` VALUES (152, 'Ctrl -F', '', 43, 0, '');
INSERT INTO `options` VALUES (153, 'Ctrl -B', '', 43, 0, '');
INSERT INTO `options` VALUES (154, 'Ctrl -D', '', 43, 0, '');
INSERT INTO `options` VALUES (155, 'fileB 也随之被删除', '', 44, 0, '');
INSERT INTO `options` VALUES (156, 'fileB 仍存在，但是属于无效文件', '', 44, 1, '');
INSERT INTO `options` VALUES (157, '因为 fileB 未被删除，所以 fileA 会被系统自动重新建立', '', 44, 0, '');
INSERT INTO `options` VALUES (158, 'fileB 会随 fileA 的删除而被系统自动删除', '', 44, 0, '');
INSERT INTO `options` VALUES (159, 'grep', '', 45, 1, '');
INSERT INTO `options` VALUES (160, 'gzip', '', 45, 0, '');
INSERT INTO `options` VALUES (161, 'find', '', 45, 0, '');
INSERT INTO `options` VALUES (162, 'sort', '', 45, 0, '');
INSERT INTO `options` VALUES (163, '&', '', 46, 1, '');
INSERT INTO `options` VALUES (164, '@', '', 46, 0, '');
INSERT INTO `options` VALUES (165, '#', '', 46, 0, '');
INSERT INTO `options` VALUES (166, '$', '', 46, 0, '');
INSERT INTO `options` VALUES (167, 'rwxr--rw-', '', 47, 1, '');
INSERT INTO `options` VALUES (168, 'rw-r--r--', '', 47, 0, '');
INSERT INTO `options` VALUES (169, '--xr—rwx', '', 47, 0, '');
INSERT INTO `options` VALUES (170, 'rwxr--r—', '', 47, 0, '');
INSERT INTO `options` VALUES (171, 'cat', '', 48, 0, '');
INSERT INTO `options` VALUES (172, 'more', '', 48, 0, '');
INSERT INTO `options` VALUES (173, 'less', '', 48, 1, '');
INSERT INTO `options` VALUES (174, 'menu', '', 48, 0, '');
INSERT INTO `options` VALUES (175, 'ping', '', 49, 0, '');
INSERT INTO `options` VALUES (176, 'ipconfig', '', 49, 0, '');
INSERT INTO `options` VALUES (177, 'winipcfg', '', 49, 0, '');
INSERT INTO `options` VALUES (178, 'ifconfig', '', 49, 1, '');
INSERT INTO `options` VALUES (179, 'pwd', '', 50, 0, '');
INSERT INTO `options` VALUES (180, 'newpwd', '', 50, 0, '');
INSERT INTO `options` VALUES (181, 'passwd', '', 50, 1, '');
INSERT INTO `options` VALUES (182, 'password', '', 50, 0, '');
INSERT INTO `options` VALUES (183, 'compress', '', 51, 0, '');
INSERT INTO `options` VALUES (184, 'gzip', '', 51, 0, '');
INSERT INTO `options` VALUES (185, 'bzip2', '', 51, 0, '');
INSERT INTO `options` VALUES (186, 'tar', '', 51, 1, '');
INSERT INTO `options` VALUES (187, '/root', '', 52, 0, '');
INSERT INTO `options` VALUES (188, '/bin', '', 52, 0, '');
INSERT INTO `options` VALUES (189, '/dev', '', 52, 0, '');
INSERT INTO `options` VALUES (190, '/boot', '', 52, 1, '');
INSERT INTO `options` VALUES (191, 'del /tmp/*', '', 53, 0, '');
INSERT INTO `options` VALUES (192, 'rm -rf /tmp', '', 53, 0, '');
INSERT INTO `options` VALUES (193, 'rm -Ra /tmp/*', '', 53, 0, '');
INSERT INTO `options` VALUES (194, 'rm –rf /tmp/*', '', 53, 1, '');
INSERT INTO `options` VALUES (195, 'pwd', '', 54, 1, '');
INSERT INTO `options` VALUES (196, 'cd', '', 54, 0, '');
INSERT INTO `options` VALUES (197, 'who', '', 54, 0, '');
INSERT INTO `options` VALUES (198, 'ls', '', 54, 0, '');
INSERT INTO `options` VALUES (199, 'a', NULL, 56, 1, '');
INSERT INTO `options` VALUES (200, 'b', NULL, 56, 0, '');
INSERT INTO `options` VALUES (201, 'u', NULL, 56, 0, '');
INSERT INTO `options` VALUES (202, 'x', NULL, 56, 0, '');
INSERT INTO `options` VALUES (203, ':q', NULL, 57, 0, '');
INSERT INTO `options` VALUES (204, ':w', NULL, 57, 0, '');
INSERT INTO `options` VALUES (205, ':wq', NULL, 57, 0, '');
INSERT INTO `options` VALUES (206, ':q!', NULL, 57, 1, '');
INSERT INTO `options` VALUES (207, 'super', NULL, 58, 0, '');
INSERT INTO `options` VALUES (208, 'passwd', NULL, 58, 0, '');
INSERT INTO `options` VALUES (209, 'tar', NULL, 58, 0, '');
INSERT INTO `options` VALUES (210, 'su', NULL, 58, 1, '');
INSERT INTO `options` VALUES (211, '命令行的每个选项', NULL, 59, 0, '');
INSERT INTO `options` VALUES (212, '是否真的删除', NULL, 59, 1, '');
INSERT INTO `options` VALUES (213, '是否有写的权限', NULL, 59, 0, '');
INSERT INTO `options` VALUES (214, '文件的位置', NULL, 59, 0, '');
INSERT INTO `options` VALUES (215, 'fstab文件只能描述属于linux的文件系统', NULL, 60, 0, '');
INSERT INTO `options` VALUES (216, 'CD_ROM和软盘必须是自动加载的', NULL, 60, 0, '');
INSERT INTO `options` VALUES (217, 'fstab文件中描述的文件系统不能被卸载', NULL, 60, 0, '');
INSERT INTO `options` VALUES (218, '启动时按fstab文件描述内容加载文件系统', NULL, 60, 1, '');
INSERT INTO `options` VALUES (219, 'SAS', NULL, 61, 0, '');
INSERT INTO `options` VALUES (220, 'SATA', NULL, 61, 0, '');
INSERT INTO `options` VALUES (221, 'PATA', NULL, 61, 1, '');
INSERT INTO `options` VALUES (222, 'SCSI', NULL, 61, 0, '');
INSERT INTO `options` VALUES (223, 'ext2', NULL, 62, 0, '');
INSERT INTO `options` VALUES (224, 'ext10', NULL, 62, 1, '');
INSERT INTO `options` VALUES (225, 'ext3', NULL, 62, 0, '');
INSERT INTO `options` VALUES (226, 'ext4', NULL, 62, 0, '');
INSERT INTO `options` VALUES (227, '1', NULL, 63, 0, '');
INSERT INTO `options` VALUES (228, '6', NULL, 63, 1, '');
INSERT INTO `options` VALUES (229, '10', NULL, 63, 0, '');
INSERT INTO `options` VALUES (230, '2', NULL, 63, 0, '');
INSERT INTO `options` VALUES (231, 'mount', NULL, 64, 1, '');
INSERT INTO `options` VALUES (232, 'unmount', NULL, 64, 0, '');
INSERT INTO `options` VALUES (233, 'mkfs.ext{2,3,4}', NULL, 64, 0, '');
INSERT INTO `options` VALUES (234, 'mkfs.xfs', NULL, 64, 0, '');
INSERT INTO `options` VALUES (235, 'mount', NULL, 65, 1, '');
INSERT INTO `options` VALUES (236, 'unmount', NULL, 65, 0, '');
INSERT INTO `options` VALUES (237, 'mkfs.ext{2,3,4}', NULL, 65, 0, '');
INSERT INTO `options` VALUES (238, 'mkfs.xfs', NULL, 65, 0, '');
INSERT INTO `options` VALUES (239, '物理卷(PV)', NULL, 66, 1, '');
INSERT INTO `options` VALUES (240, '卷组(VG)', NULL, 66, 1, '');
INSERT INTO `options` VALUES (241, '逻辑卷(LV)', NULL, 66, 1, '');
INSERT INTO `options` VALUES (242, '物理区域(PE)', NULL, 66, 1, '');
INSERT INTO `options` VALUES (243, '4MB', NULL, 67, 1, '');
INSERT INTO `options` VALUES (244, '0.1MB', NULL, 67, 0, '');
INSERT INTO `options` VALUES (245, '100MB', NULL, 67, 0, '');
INSERT INTO `options` VALUES (246, '500MB', NULL, 67, 0, '');
INSERT INTO `options` VALUES (247, 'pwd', NULL, 68, 0, '');
INSERT INTO `options` VALUES (248, 'whoami', NULL, 68, 1, '');
INSERT INTO `options` VALUES (249, 'ls', NULL, 68, 0, '');
INSERT INTO `options` VALUES (250, 'ps', NULL, 68, 0, '');
INSERT INTO `options` VALUES (251, 'cat /etc/passwd', NULL, 69, 1, '');
INSERT INTO `options` VALUES (252, 'cat /etc/shadow', NULL, 69, 0, '');
INSERT INTO `options` VALUES (253, 'cat /etc/group', NULL, 69, 0, '');
INSERT INTO `options` VALUES (254, 'ls -l /home', NULL, 69, 0, '');
INSERT INTO `options` VALUES (255, 'vgcreate', NULL, 70, 0, '');
INSERT INTO `options` VALUES (256, 'vgdisplay', NULL, 70, 1, '');
INSERT INTO `options` VALUES (257, 'vgs', NULL, 70, 0, '');
INSERT INTO `options` VALUES (258, 'vgremove', NULL, 70, 0, '');
INSERT INTO `options` VALUES (259, 'mkfs', NULL, 71, 1, '');
INSERT INTO `options` VALUES (260, 'mount', NULL, 71, 0, '');
INSERT INTO `options` VALUES (261, 'lsblk', NULL, 71, 0, '');
INSERT INTO `options` VALUES (262, 'df', NULL, 71, 0, '');
INSERT INTO `options` VALUES (263, 'lsblk', NULL, 72, 0, '');
INSERT INTO `options` VALUES (264, 'umount', NULL, 72, 0, '');
INSERT INTO `options` VALUES (265, 'df', NULL, 72, 0, '');
INSERT INTO `options` VALUES (266, 'mount', NULL, 72, 1, '');
INSERT INTO `options` VALUES (267, 'mkfs', NULL, 73, 1, '');
INSERT INTO `options` VALUES (268, 'mount', NULL, 73, 0, '');
INSERT INTO `options` VALUES (269, 'umount', NULL, 73, 0, '');
INSERT INTO `options` VALUES (270, 'fdisk', NULL, 73, 0, '');
INSERT INTO `options` VALUES (271, 'lvcreate', NULL, 74, 1, '');
INSERT INTO `options` VALUES (272, 'lvdisplay', NULL, 74, 0, '');
INSERT INTO `options` VALUES (273, 'lvremove', NULL, 74, 0, '');
INSERT INTO `options` VALUES (274, 'lvs', NULL, 74, 0, '');
INSERT INTO `options` VALUES (275, 'lvextend', NULL, 75, 1, '');
INSERT INTO `options` VALUES (276, 'lvreduce', NULL, 75, 0, '');
INSERT INTO `options` VALUES (277, 'lvcreate', NULL, 75, 0, '');
INSERT INTO `options` VALUES (278, 'lvdisplay', NULL, 75, 0, '');
INSERT INTO `options` VALUES (279, 'vgcreate', NULL, 76, 0, '');
INSERT INTO `options` VALUES (280, 'vgdisplay', NULL, 76, 1, '');
INSERT INTO `options` VALUES (281, 'vgs', NULL, 76, 0, '');
INSERT INTO `options` VALUES (282, 'vgremove', NULL, 76, 0, '');
INSERT INTO `options` VALUES (1692018078067924994, '1', NULL, 1692018078005010434, 1, '');
INSERT INTO `options` VALUES (1692018078067924995, '2', NULL, 1692018078005010434, 0, '');
INSERT INTO `options` VALUES (1692018078067924996, '3', NULL, 1692018078005010434, 0, '');
INSERT INTO `options` VALUES (1692018078067924997, '4', NULL, 1692018078005010434, 0, '');

-- ----------------------------
-- Table structure for qu_repo
-- ----------------------------
DROP TABLE IF EXISTS `qu_repo`;
CREATE TABLE `qu_repo`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `qu_id` bigint(20) NULL DEFAULT NULL COMMENT '试题',
  `repo_id` bigint(20) NULL DEFAULT NULL COMMENT '归属题库',
  `qu_type` int(11) NULL DEFAULT 0 COMMENT '题目类型',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `qu_id`(`qu_id`) USING BTREE,
  INDEX `repo_id`(`repo_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1692018078067924994 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '试题题库关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qu_repo
-- ----------------------------
INSERT INTO `qu_repo` VALUES (23, 26, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (24, 27, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (25, 28, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (26, 29, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (27, 30, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (28, 31, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (29, 32, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (30, 33, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (31, 34, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (32, 35, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (33, 36, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (34, 37, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (35, 38, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (36, 39, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (37, 40, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (38, 41, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (39, 42, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (40, 43, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (41, 44, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (42, 45, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (43, 46, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (44, 47, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (45, 48, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (46, 49, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (47, 50, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (48, 51, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (49, 52, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (50, 53, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (51, 54, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (52, 55, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (53, 56, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (54, 57, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (55, 58, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (56, 59, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (57, 56, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (58, 57, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (59, 58, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (60, 59, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (61, 60, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (62, 61, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (63, 62, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (64, 63, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (65, 64, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (66, 65, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (67, 66, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (68, 67, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (69, 68, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (70, 69, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (71, 70, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (72, 71, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (73, 72, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (74, 73, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (75, 74, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (76, 75, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (77, 76, 1, 0, 0);
INSERT INTO `qu_repo` VALUES (1692018078067924993, 1692018078005010434, 1, 0, 0);

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '问题',
  `qu_type` int(11) NULL DEFAULT NULL COMMENT '类型',
  `right_num` int(11) NULL DEFAULT NULL COMMENT '回答正确次数',
  `analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '题目分析',
  `image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '题目备注',
  `level` int(11) NULL DEFAULT NULL COMMENT '难易程度',
  `draw_num` int(11) NULL DEFAULT NULL COMMENT '抽中次数',
  `choose` tinyint(1) NULL DEFAULT NULL COMMENT '今天是否被抽中',
  `chapter` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '章节',
  `status` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 1 COMMENT '题目是否有效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1692018078005010435 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES (26, '删除文件命令为', 1, NULL, '', '', '2023-04-04 11:40:00', NULL, NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (27, '对文件进行归档的命令为（ ）', 1, NULL, '', '', '2023-04-04 11:42:03', NULL, NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (28, '下面哪条命令可被用来显示已安装文件系统的占用磁盘空间（ ）', 1, NULL, '', '', '2023-04-04 11:44:06', NULL, NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (29, '在（ ）文件中存放有用户密码的相关信息', 1, NULL, '', '', '2023-04-04 11:46:50', '2023-04-13 10:52:05', NULL, 1, 0, 0, NULL, 0);
INSERT INTO `questions` VALUES (30, 'Linux三种权限中只允许进入目录的权限是（ ）', 1, NULL, '', '', '2023-04-04 11:50:29', NULL, NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (31, '可实现移动文件的命令是', 1, NULL, '', '', '2023-04-04 11:51:44', NULL, NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (32, '下面通配符可匹配多个任意字符的通配符是（ ）', 1, NULL, '', '', '2023-04-04 11:53:25', NULL, NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (33, '以下不是 Linux的特点的是（ ）', 1, NULL, '', '', '2023-04-04 11:54:41', NULL, NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (34, '可实现对文件进行重命名的命令是（ ）', 1, NULL, '', '', '2023-04-04 15:49:29', '2023-04-05 23:19:06', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (35, '文件权限读、写、执行的三种标识字母依次是（ ）', 1, NULL, '', '', '2023-04-04 15:56:10', '2023-04-05 23:20:19', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (36, '在 Linux 目录结构中（ ）目录用来存放系统配置文件', 1, NULL, '', '', '2023-04-04 15:57:54', '2023-04-05 23:20:45', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (37, '下列更改文件命令中，只能更改目标文件的权限的命令是', 1, NULL, '', '', '2023-04-04 15:58:47', '2023-04-05 23:21:02', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (38, '在创建 Linux 分区时，一定要创建（ ）两个分区', 1, NULL, '', '', '2023-04-04 16:00:56', '2023-04-05 23:22:59', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (39, '在 Linux 系统中，系统默认的（ ）用户对整个系统拥有完全的控制权', 1, NULL, '', '', '2023-04-04 16:02:21', '2023-04-05 23:23:10', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (40, '如何删除一个非空子目录 /tmp（ ）', 1, 0, '', '', '2023-04-04 16:06:58', '2023-04-05 23:23:48', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (41, '存放用户帐号的文件是（ ）', 1, NULL, '', '', '2023-04-04 16:07:53', '2023-04-05 23:24:17', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (42, '当运行在多用户模式下时，用 Ctrl+ALT+F* 可以切换多少虚拟用户终端（ ）', 1, NULL, '', '', '2023-04-04 16:09:52', '2023-04-05 23:24:30', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (43, '按下（ ）键能终止当前运行的命令', 1, NULL, '', '', '2023-04-04 16:10:59', '2023-04-05 23:25:09', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (44, '假设文件 fileA 的符号链接为 fileB，那么删除 fileA 后，下面的描述正确的是（ ）', 1, NULL, '', '', '2023-04-04 16:12:05', '2023-04-05 23:25:53', NULL, 1, 5, 0, NULL, 1);
INSERT INTO `questions` VALUES (45, '在给定文件中查找与设定条件相符字符串的命令为（ ）', 1, NULL, '', '', '2023-04-04 16:13:24', '2023-04-05 23:26:43', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (46, '从后台启动进程，应在命令的结尾加上符号（ ）', 1, NULL, '', '', '2023-04-04 16:14:16', '2023-04-05 23:27:04', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (47, '如果执行命令chmod 746 file.txt，那么该文件的权限是（ ）', 1, NULL, '', '', '2023-04-04 16:15:38', '2023-04-05 23:27:43', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (48, 'Linux 有三个查看文件的命令，若希望在查看文件内容过程中可以用光标上下移动来查看文件内容，应使用命令（ ）', 1, NULL, '', '', '2023-04-04 16:16:23', '2023-04-05 23:29:32', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (49, '在 Linux 系统中，一般用（ ）命令来查看网络接口的状态', 1, NULL, '', '', '2023-04-04 16:17:23', '2023-04-05 23:29:51', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (50, '下列那一个指令可以设定使用者的密码（ ） ', 1, NULL, '', '', '2023-04-04 16:19:14', '2023-04-05 23:30:24', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (51, '下 列 那 一 个 不 是 压 缩 指 令 （ ） ', 1, NULL, '', '', '2023-04-04 16:20:06', '2023-04-05 23:30:51', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (52, '下面哪个系统目录中存放了系统引导、启动时使用的一些文件和目录（ ）', 1, NULL, '', '', '2023-04-04 16:20:56', '2023-04-05 23:31:10', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (53, '如何删除目录/tmp下的所有文件及子目录（ ）', 1, NULL, '', '', '2023-04-04 16:22:21', '2023-04-05 23:35:15', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (54, '怎样显示当前目录（ ）', 1, NULL, '', '', '2023-04-04 16:23:04', '2023-04-05 23:35:25', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (56, '在ps命令中什么参数是用来显示所有用户的进程的？', 1, NULL, '', '', '2023-04-13 10:19:33', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (57, '在vi中退出不保存的命令是？', 1, NULL, '', '', '2023-04-13 10:23:55', NULL, NULL, 1, 5, 1, NULL, 1);
INSERT INTO `questions` VALUES (58, '哪个命令可以将普通用户转换成超级用户', 1, NULL, '', '', '2023-04-13 10:26:05', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (59, '用 “rm -i”,系统会提示什么来让你确认', 1, NULL, '', '', '2023-04-13 10:27:42', NULL, NULL, 1, 5, 1, NULL, 1);
INSERT INTO `questions` VALUES (60, '下列关于 /etc/fstab 文件描述，正确的是 D', 1, NULL, '', '', '2023-04-13 10:31:01', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (61, '以下哪种硬盘接口基本被淘汰', 1, NULL, '', '', '2023-04-13 10:42:16', NULL, NULL, 1, 5, 1, NULL, 1);
INSERT INTO `questions` VALUES (62, '以下哪个不是Linux的标准文件系统', 1, NULL, '', '', '2023-04-13 10:46:25', NULL, NULL, 1, 5, 1, NULL, 1);
INSERT INTO `questions` VALUES (63, '/etc/fstab文件中，每一行包含几列信息', 1, NULL, '', '', '2023-04-13 10:49:19', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (64, '以下哪个是挂在文件系统工具', 1, NULL, '', '', '2023-04-20 01:30:52', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (66, '以下哪个是LVM基本术语（多选）', 2, NULL, '', '', '2023-04-20 01:35:38', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (67, '物理区域(PE)是物理卷(PV)划分的基本单元。PE的大小可在创建物理卷时指定，请问默认多少MB', 1, NULL, '', '', '2023-04-20 01:39:09', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (68, '在 Linux 中，如何查看当前登录的用户？', 1, NULL, '', '', '2023-04-20 01:50:05', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (69, '在 Linux 中，如何查看当前系统中所有的用户账户？', 1, NULL, '', '', '2023-04-20 01:53:55', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (70, '在Linux系统中，如何查看卷组的详细信息，例如物理卷的数量和逻辑卷的数量？', 1, NULL, '', '', '2023-04-20 01:54:55', '2023-04-20 02:24:28', NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (71, '以下哪个命令可以将一个空的硬盘分区为一个新的文件系统？', 1, NULL, '', '', '2023-04-20 02:29:09', NULL, NULL, 1, 5, 1, NULL, 1);
INSERT INTO `questions` VALUES (72, '在Linux系统中，如何列出所有已经挂载的文件系统？', 1, NULL, '', '', '2023-04-20 02:30:01', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (73, '在Linux系统中，如何将一个分区格式化为指定的文件系统？', 1, NULL, '', '', '2023-04-20 02:31:27', NULL, NULL, 1, 5, 1, NULL, 1);
INSERT INTO `questions` VALUES (74, '在Linux系统中，如何创建一个新的逻辑卷？', 1, NULL, '', '', '2023-04-20 02:32:44', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (75, '在Linux系统中，如何扩展一个逻辑卷的容量？', 1, NULL, '', '', '2023-04-20 02:34:01', NULL, NULL, 1, 5, 1, NULL, 1);
INSERT INTO `questions` VALUES (76, '在Linux系统中，如何查看卷组的详细信息，例如物理卷的数量和逻辑卷的数量？', 1, NULL, '', '', '2023-04-20 02:35:06', NULL, NULL, 1, 4, 0, NULL, 1);
INSERT INTO `questions` VALUES (1692018078005010434, '测试', 1, 0, '测试', '', '2023-08-17 11:38:41', NULL, NULL, 1, 2, 1, NULL, 1);

-- ----------------------------
-- Table structure for sign_up
-- ----------------------------
DROP TABLE IF EXISTS `sign_up`;
CREATE TABLE `sign_up`  (
  `sign_id` bigint(20) NOT NULL COMMENT '主键id',
  `stu_sno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学生学号',
  `sem_id` bigint(20) NULL DEFAULT NULL COMMENT '关联学期课程名称',
  `sign_time` datetime NULL DEFAULT NULL COMMENT '签到时间',
  PRIMARY KEY (`sign_id`) USING BTREE,
  INDEX `sem_id`(`sem_id`) USING BTREE,
  INDEX `stu_sno`(`stu_sno`) USING BTREE,
  CONSTRAINT `sign_up_ibfk_1` FOREIGN KEY (`sem_id`) REFERENCES `term_schedule` (`semester_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sign_up_ibfk_2` FOREIGN KEY (`stu_sno`) REFERENCES `student` (`sno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '签到表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sign_up
-- ----------------------------
INSERT INTO `sign_up` VALUES (0, '2021003177', 1692171453233328130, '2023-08-18 10:18:54');
INSERT INTO `sign_up` VALUES (1692478300657963010, '2021003177', 0, '2023-08-18 18:07:26');
INSERT INTO `sign_up` VALUES (1692484622585561089, '2021003177', 0, '2023-08-18 18:32:34');
INSERT INTO `sign_up` VALUES (1692730542061420546, '2021003177', 0, '2023-08-19 10:49:45');
INSERT INTO `sign_up` VALUES (1692730712530518017, '2021003177', 0, '2023-08-19 10:50:26');
INSERT INTO `sign_up` VALUES (1692732129852948482, '2021003177', 0, '2023-08-19 10:56:04');
INSERT INTO `sign_up` VALUES (1692733314324398082, '2021003177', 0, '2023-08-19 11:00:46');
INSERT INTO `sign_up` VALUES (1692733321916088321, '2021003177', 0, '2023-08-19 11:00:48');
INSERT INTO `sign_up` VALUES (1692733326773092354, '2021003177', 0, '2023-08-19 11:00:49');
INSERT INTO `sign_up` VALUES (1692733328861855745, '2021003177', 0, '2023-08-19 11:00:50');
INSERT INTO `sign_up` VALUES (1692733331084836866, '2021003177', 0, '2023-08-19 11:00:50');
INSERT INTO `sign_up` VALUES (1692733335216226306, '2021003177', 0, '2023-08-19 11:00:51');
INSERT INTO `sign_up` VALUES (1692733335744708609, '2021003177', 0, '2023-08-19 11:00:51');
INSERT INTO `sign_up` VALUES (1692734958659346434, '2021003177', 0, '2023-08-19 11:07:18');
INSERT INTO `sign_up` VALUES (1692734970059464705, '2021003177', 0, '2023-08-19 11:07:21');
INSERT INTO `sign_up` VALUES (1692734976019570690, '2021003177', 0, '2023-08-19 11:07:22');
INSERT INTO `sign_up` VALUES (1692734981967093761, '2021003177', 0, '2023-08-19 11:07:24');
INSERT INTO `sign_up` VALUES (1692736032770248706, '2021003177', 0, '2023-08-19 11:11:34');
INSERT INTO `sign_up` VALUES (1692736080518205442, '2021003177', 0, '2023-08-19 11:11:46');
INSERT INTO `sign_up` VALUES (1692765470354464769, '2021003177', 0, '2023-08-19 13:08:33');
INSERT INTO `sign_up` VALUES (1692765777922777090, '2021003178', 0, '2023-08-19 13:09:46');
INSERT INTO `sign_up` VALUES (1692766223022317569, '2021007151', 0, '2023-08-19 13:11:32');
INSERT INTO `sign_up` VALUES (1692766639889997826, '2021003177', 0, '2023-08-19 13:13:12');
INSERT INTO `sign_up` VALUES (1692773280618254337, '2021003177', 0, '2023-08-19 13:39:35');
INSERT INTO `sign_up` VALUES (1692773308686536705, '2021003178', 0, '2023-08-19 13:39:42');
INSERT INTO `sign_up` VALUES (1692773866575106050, '2021003178', 0, '2023-08-19 13:41:55');
INSERT INTO `sign_up` VALUES (1692774686418931714, '2021003177', 0, '2023-08-19 13:45:10');
INSERT INTO `sign_up` VALUES (1692775741647192066, '2021003177', 0, '2023-08-19 13:49:22');
INSERT INTO `sign_up` VALUES (1692775921306009602, '2021003177', 0, '2023-08-19 13:50:05');
INSERT INTO `sign_up` VALUES (1692776298629791746, '2021003178', 0, '2023-08-19 13:51:35');
INSERT INTO `sign_up` VALUES (1692777829978570754, '2021003178', 0, '2023-08-19 13:57:40');
INSERT INTO `sign_up` VALUES (1692782310094950401, '2021003177', 0, '2023-08-19 14:15:28');
INSERT INTO `sign_up` VALUES (1692783911954173954, '2021003177', 0, '2023-08-19 14:21:50');
INSERT INTO `sign_up` VALUES (1692786979043221505, '2021003177', 0, '2023-08-19 14:34:01');
INSERT INTO `sign_up` VALUES (1692788526267387905, '2021003177', 0, '2023-08-19 14:40:10');
INSERT INTO `sign_up` VALUES (1692789787947622402, '2021003177', 0, '2023-08-19 14:45:11');
INSERT INTO `sign_up` VALUES (1693081742761979905, '2021003177', 0, '2023-08-20 10:05:18');
INSERT INTO `sign_up` VALUES (1693090204581531649, '2021003177', 0, '2023-08-20 10:38:55');
INSERT INTO `sign_up` VALUES (1693094376429228033, '2021003177', 0, '2023-08-20 10:55:30');
INSERT INTO `sign_up` VALUES (1707022444197773313, '2021003177', 0, '2023-09-27 21:20:40');
INSERT INTO `sign_up` VALUES (1707286928199536642, '2021003177', 0, '2023-09-28 14:51:38');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sclass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级',
  `sno` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学号',
  `sname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `label` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '个性标签',
  `absent` int(11) NULL DEFAULT 0 COMMENT '缺勤次数',
  `sportrait` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '头像',
  `lock_time` datetime NULL DEFAULT NULL COMMENT '锁定时间',
  `locked` tinyint(1) NULL DEFAULT 0 COMMENT '是否被锁定',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '保密' COMMENT '性别',
  `phone_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '保密' COMMENT '手机号',
  `interest` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '兴趣爱好',
  `register_time` date NULL DEFAULT NULL COMMENT '注册日期',
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sno`(`sno`) USING BTREE COMMENT '学号'
) ENGINE = InnoDB AUTO_INCREMENT = 1569 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '数科2102班', '2021003177', '徐华飞', 'idea啊，idea你不要走啊，没你我可怎么写代码（悲）【idea是用来写Java代码的软件】', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/IMG20230924174337.jpg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (2, '数科2102班', '2021002094', '高鹏', '心向远方自朝阳', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021002094-gaopeng.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (19, '数科2103班', '2021007096', '马晨茹', 'shine', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007096-machenru.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (23, '数科2101班', '2021003511', '刘瑞', '不开心就多喝开水', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021003511-liurui.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (27, '数科2103班', '2021000018', '贾浩轩', '一箫一剑走江湖，千古情仇酒一壶! 两脚踏变尘世路，以天为盖地为庐', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021000018-jiahaoxuan.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (35, '数科2101班', '2021003443', '崔鹏亮', 'Love the life you live.Live the life you love', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021003443-cuipengliang.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (37, '数科2101班', '2021007441', '徐炜', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007441-xuwei.jpeg', '2023-05-02 22:26:59', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (41, '数科2103班', '2021007094', '陈子钰', '你也想起舞嘛', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007094-chenziyu.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (43, '数科2104班', '2021007137', '王圆圆', '你浅浅的微笑就像乌梅子酱', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007137-wangyuanyuan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (49, '数科2102班', '2021007075', '张泰松', '为人低调，但很可靠！', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007075-zhangtaisong.jpeg', '2023-05-11 18:21:19', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (63, '数科2102班', '2021007070', '王英巧', '并不是星星会发光，而是发光的成了星星！', 1, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007070-wangyingqiao.jpeg', '2025-07-01 20:19:54', 1, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1172, '数科2101班', '2021001438', '安欣锐', '今天我也是个帅哥呢~', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021001438-anxinrui.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1173, '数科2104班', '2021007148', '刘茁远', '拼搏百天，我要上太原理工大学！', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007148-liuzhuoyuan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1174, '数科2103班', '2021007119', '谢林钊', 'ikun', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007119-xielinzhao.jpeg', '2023-05-01 18:16:06', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1175, '数科2102班', '2021007071', '刘百超', '.', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007071-liubaichao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1176, '数科2104班', '2021007125', '王博', '希望早点快进到退休', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007125-wangbo.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1177, '数科2101班', '2021001495', '巴永恒', '世界以痛吻我，要我报之以歌', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021001495-bayongheng.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1178, '数科2103班', '2021003112', '马浚凯', '很内向，上课不敢听讲', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021003112-majunkai.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1179, '数科2104班', '2021007147', '肖鑫宇', '你现在的气质里,藏着你走过的路,读过的书和爱过的人。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007147-xiaoxinyu.jpeg', '2023-06-01 00:12:00', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1180, '数科2102班', '2021007081', '吕皓恒', '做零的使者，非壹的传人', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007081-lühaoheng.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1181, '数科2104班', '2021007129', '梁晓娟', '开摆', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007129-liangxiaojuan.jpeg', '2023-06-09 00:20:17', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1182, '数科2104班', '2021007145', '曾佳雯', '好爱上课哇(￣へ￣)', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007145-cengjiawen.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1183, '数科2104班', '2021007146', '刘倩', '。。。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007146-liuqian.jpeg', '2023-06-10 00:25:37', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1184, '数科2101班', '2021007040', '武薇', '欲买桂花同载酒，终不似，少年游', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007040-wuwei.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1185, '数科2101班', '2021007039', '张蕊', '早上好', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007039-zhangrui.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1186, '数科2101班', '2021007038', '张艺之', '我是正义呆呆', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007038-zhangyizhi.jpeg', '2023-06-17 00:34:29', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1187, '数科2102班', '2021007078', '杨甲伟', '梅狸猫的杨甲伟', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007078-yangjiawei.jpeg', '2023-04-30 22:32:55', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1188, '数科2104班', '2021002651', '王家齐', '好好学Linux', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021002651-wangjiaqi.jpeg', '2023-06-01 00:17:35', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1189, '数科2104班', '2021007139', '滕天任', 'W-fovik_Y5', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007139-tengtianren.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1190, '数科2101班', '2021007037', '胡芷毓', '学习新思想，争做新青年', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007037-huzhiyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1191, '数科2103班', '2021007101', '贾星宇', '因为相信，所以坚持；因为坚持，所以看见。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007101-jiaxingyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1192, '数科2101班', '2021007058', '肖木', '走好自己的路', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007058-xiaomu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1193, '数科2101班', '2021007054', '刘雅琪', '糟糕糟糕，有请下一位~', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007054-liuyaqi.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1195, '数科2101班', '2021007048', '刘学松', '天天快乐', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007048-liuxuesong.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1196, '数科2103班', '2021007113', '舒展', '🥺', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007113-shuzhan.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1197, '数科2103班', '2021004238', '李同璐', '不喜欢榴莲披萨的大学生不是一个好的懒癌晚期患者', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021004238-litonglu.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1198, '数科2101班', '2021007044', '王钰焘', '11', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007044-wangyudao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1199, '数科2104班', '2021007135', '常建辉', '无', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007135-changjianhui.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1200, '数科2104班', '2021007132', '李琛', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007132-lichen.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1201, '数科2104班', '2021007131', '张宇轩', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007131-zhangyuxuan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1202, '数科2102班', '2021007067', '刘旭娇', '好事总是在下个转弯', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007067-liuxujiao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1203, '数科2101班', '2021007061', '刘凡皓', '有空多读书，没事早睡觉', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007061-liufanhao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1204, '数科2102班', '2021007083', '郑力华', '。。。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007083-zhenglihua.png', '2023-04-26 23:34:48', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1205, '数科2101班', '2021007053', '梁佳钰', '一颗狗尾草', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007053-liangjiayu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1206, '数科2101班', '2021007047', '刘嘉昊', '长风破浪会有时，直挂云帆济沧海。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007047-liujiahao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1207, '数科2101班', '2021007045', '张涛', '安警官，新年快乐', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007045-zhangtao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1208, '数科2102班', '2021007065', '柳子乐', '年年岁岁不挂科，朝朝暮暮有帅哥', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007065-liuzile.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1209, '数科2101班', '2021007059', '邓继红', '越努力，越幸运', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007059-dengjihong.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1210, '数科2101班', '2021007042', '丁少杰', 'Goat  Messi', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007042-dingshaojie.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1211, '数科2103班', '2021007103', '李世祥', '我爱张浩铭', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007103-lishixiang.jpeg', '2023-04-26 07:44:19', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1213, '数科2103班', '2021007104', '王振华', '做自己想做的事情', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007104-wangzhenhua.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1214, '数科2104班', '2021007128', '余艳', '空', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007128-yuyan.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1215, '数科2103班', '2021007100', '孙浩宇', '抬头仰望北斗星，低头思念毛泽东', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007100-sunhaoyu.jpeg', '2023-04-26 08:52:03', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1216, '数科2102班', '2021007068', '刘星宇', '苍生皆平庸，蝼蚁望晴空', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007068-liuxingyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1217, '数科2104班', '2021007134', '田臻', 'hello world', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007134-tianzhen.jpeg', '2023-06-01 00:09:49', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1218, '数科2102班', '2021007064', '李佳璇', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007064-lijiaxuan.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1219, '数科2102班', '2021007073', '薛凯允', '薛凯允来自山西运城 特长乒乓球🏓️爱好 打csgo.', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007073-xuekaiyun.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1220, '数科2102班', '2021007074', '李泽林', '', 1, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007074-lizelin.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1221, '数科2103班', '2021007118', '徐驭', '🇺🇦🇺🇦🇺🇦', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007118-xuyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1222, '数科2103班', '2021007110', '徐稼馨', '上帝给予我吃货的属性，却没有给我土豪的身份', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007110-xujiaxin.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1223, '数科2103班', '2021007122', '徐建喜', '我总是一个人在练习一个人', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007122-xujianxi.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1224, '数科2101班', '2021007062', '张骐轩', '大家好，我是练习时长两年半的张徐坤，我平时酷爱唱，跳，rap，篮球，接下来我给大家表演一段舞蹈，只因你太美……', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007062-zhangqixuan.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1225, '数科2103班', '2021007109', '郑佳蕊', '超级无敌旋风霹雳美少女', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007109-zhengjiarui.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1227, '数科2102班', '2021007091', '程嘉伟', '我们是两个即将在除夕夜放飞理想的有志青年', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007091-chengjiawei.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1228, '数科2103班', '2021007102', '兰学渊', '+1', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007102-lanxueyuan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1229, '数科2101班', '2021007034', '陈佳丽', 'Bazinga', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007034-chenjiālì.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1230, '数科2104班', '2021004175', '孙乐乐', '生如逆旅，一苇以航', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021004175-sunlele.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1231, '数科2102班', '2021007063', '孙铋程', '少年易老学难成，一寸光阴不可轻', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007063-sunbicheng.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1232, '数科2102班', '2021001181', '毛睿君', '2006年时代周刊年度风云人物\r\n2008年感动中国年度人物特别奖\r\n我没逗你，你可以百度', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021001181-maoruijun.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1233, '数科2102班', '2021005055', '万力', '哈哈哈哈哈哈哈', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021005055-wanli.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1234, '数科2102班', '2021003521', '赵巧霞', '努力进阶', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021003521-zhaoqiaoxia.jpeg', '2023-06-01 00:14:59', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1235, '数科2102班', '2021000004', '杨云鑫', '要永远做一个勇敢的女孩鸭~', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021000004-yangyunxin.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1236, '数科2102班', '2021007069', '张钟月', '言必行，行必果', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007069-zhangzhongyue.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1237, '数科2103班', '2021007115', '孙睿哲', '别点我别点我', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007115-sunruizhe.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1238, '数科2102班', '2021007082', '吴相熙', '成分复杂。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007082-wuxiangxi.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1239, '数科2102班', '2021007077', '孙国良', '杨甲伟是我好大儿', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007077-sunguoliang.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1240, '数科2103班', '2021007097', '白晨茜', '答题被抽到了，我一点也不痛苦(╥_╥)', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007097-baichenqian.jpeg', '2023-06-01 18:17:23', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1241, '数科2102班', '2021007072', '武耀阳', '无', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007072-wuyaoyang.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1242, '数科2102班', '2021007080', '曹倍宁', '那年我双手插兜，不知道什么叫做对手', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007080-caobeining.jpeg', '2023-06-16 00:32:21', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1243, '数科2101班', '2021007041', '袁龙', '。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007041-yuanlong.png', '2023-07-12 00:27:41', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1244, '数科2101班', '2021007043', '付佳豪', '无', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007043-fujiahao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1245, '数科2101班', '2021007035', '霍璟琦', '一路风霜与热望', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007035-huojingqi.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1246, '数科2101班', '2021007036', '梁欣宇', 'hi', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007036-liangxinyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1247, '数科2101班', '2021007033', '刘辰宣', '略略略', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007033-liuchenxuan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1248, '数科2101班', '2021007050', '徐跃', '早上好(•̀⌄•́)', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007050-xuyue.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1249, '数科2101班', '2021007046', '柳国栋', '没有什么是两年半的努力无法解决的，如果有，那就再来两年半。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007046-liuguodong.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1250, '数科2102班', '2021007086', '何佳骆', '公的', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007086-hejialuo.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1251, '数科2101班', '2021007056', '曾庆阳', 'nothing to say at all', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007056-cengqingyang.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1252, '数科2102班', '2021007076', '武宇恒', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007076-wuyuheng.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1253, '数科2102班', '2021007088', '李俊杰', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007088-lijunjie.png', '2023-04-26 23:35:12', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1254, '数科2102班', '2021007084', '王亚楠', '喜欢听说唱。Girls have to go into the world and make up their own minds about things.', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007084-wangyanan.jpeg', '2023-07-01 18:17:55', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1255, '数科2102班', '2021007087', '杨景森', '惠一中大头狗', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007087-yangjingsen.jpeg', '2023-06-09 00:18:41', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1256, '数科2102班', '2021007089', '黎明波', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007089-limingbo.jpeg', '2023-06-24 00:35:24', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1257, '数科2101班', '2021007060', '李旻骏', 'Veni Vidi Vici', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007060-liminjun.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1258, '数科2102班', '2021007090', '巴怡琦', '……', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007090-bayiqi.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1259, '数科2101班', '2021007057', '李嘉文', '。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007057-lijiawen.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1260, '数科2101班', '2021007052', '刘夏', '勇士总冠军', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007052-liuxia.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1261, '数科2103班', '2021007093', '李鑫睿', '无', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007093-lixinrui.jpeg', '2023-06-01 00:10:11', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1262, '数科2102班', '2021007092', '吕宗焘', 'cancanneed', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007092-lüzongdao.jpeg', '2023-04-26 07:43:43', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1263, '数科2104班', '2021007141', '俞果', '6', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007141-yuguo.jpeg', '2023-07-08 00:36:04', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1264, '数科2102班', '2021007085', '付静远', '哈喽哈喽', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007085-fujingyuan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1265, '数科2104班', '2021007143', '韩雨彤', '大家好！我是数科2104韩雨彤，来自山东。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007143-hanyutong.jpeg', '2023-05-03 21:38:04', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1266, '数科2103班', '2021007117', '农莹', '普通路过的大学生，特长是画画(>▽<)~', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007117-nongying.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1267, '数科2104班', '2021007126', '李敏娟', '芜湖~', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007126-liminjuan.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1268, '数科2103班', '2021007098', '李沂桐', '1.如果是回答问题的话，就请我后排右边第二个替我回答吧😄2.我不是喂，我有名字3.我是609寝室的，舍友是：李敏，白晨茜，郑佳蕊，农莹', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007098-liyitong.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1269, '数科2103班', '2021007105', '赵瑞哲', '相信科学', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007105-zhaoruizhe.jpeg', '2023-09-16 00:32:49', 1, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1270, '数科2103班', '2021007099', '李敏', '一个喜欢手工艺类的女生', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007099-limin.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1271, '数科2103班', '2021007120', '高馨语', '阿巴阿巴', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007120-gaoxinyu.png', '2023-06-16 00:22:11', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1272, '数科2103班', '2021007112', '邱宇', '行到水穷处， 坐看云起时', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007112-qiuyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1273, '数科2104班', '2021007127', '张荣荣', '啦啦啦啦啦啦啦', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007127-zhangrongrong.png', '2023-06-09 00:24:46', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1274, '数科2104班', '2021007124', '赵晨淼', '生活每天都一样，你要积极又向上', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007124-zhaochenmiao.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1275, '数科2104班', '2021007136', '张承浩', '张承浩', 1, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007136-zhangchenghao.png', '2023-06-01 18:18:54', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1276, '数科2104班', '2021007142', '吴昊', '啥也不是', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007142-wuhao.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1277, '数科2103班', '2021007095', '张洋', '魈爱我，我爱魈，我是魈的大宝贝', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007095-zhangyang.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1278, '数科2104班', '2021007151', '杨智鑫', '朝诺重工', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007151-yangzhixin.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1279, '数科2104班', '2021002772', '常红媛', '热爱漫无边际 生活自有分寸', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021002772-changhongyuan.png', '2023-06-17 00:33:51', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1280, '数科2104班', '2021007152', '杨嘉源', '..', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007152-yangjiayuan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1281, '数科2104班', '2021000444', '卢俊羽', '👻👻👻👻👻', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021000444-lujunyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1282, '数科2104班', '2021007150', '张嘉蕾', '数科2104张嘉蕾', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007150-zhangjialei.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1283, '数科2104班', '2021007133', '司宇飞', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007133-siyufei.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1284, '数科2104班', '2021003815', '罗震宇', '大家好。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021003815-luozhenyu.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1285, '数科2103班', '2021003724', '张云重', '春来不是读书天，夏日炎炎正好眠，秋有蚊虫冬又冷，背起书包过大年。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021003724-zhangyunzhong.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1286, '数科2103班', '2021002567', '杨权', '孤舟蓑笠翁，独钓寒江雪', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021002567-yangquan.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1300, '数科2101班', '2021007055', '王一城', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007055-wangyicheng.png', '2023-04-26 07:44:48', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1301, '数科2103班', '2021007108', '赵亮', '', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021007108-zhaoliang.jpeg', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1302, '数科2104班', '2021000817', '马靖超', '用心，用情，用力，用命', 0, 'http://82.156.215.40:9000/dian-xhf/2021000817-majingchao.jpeg', '2023-07-22 00:36:42', 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1560, '数科2101班', '2021005866', '赵宇杰', '2413412', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/2021005866-zhaoyujie.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1561, '无', '无', '郑老师', '公共安全大数据研究所是实施太原理工大学“人工智能学科建设”的核心团队.所长：郑文研究员，太原理工大学引进人才，山西省“三晋英才”，山西省通信学会理事，山西省《网络安全和大数据信息技术标准化技术委员会》委员，长治医学院特聘教授。', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/%E6%97%A0-zhenglaoshi.jpeg', '2035-09-01 17:15:02', 1, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1562, '数科2102班', '20220000', '测试啊啊啊', '测试abab', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/20220000-ceshiaaa.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1563, '数科2102班', '20210011', '喜羊羊', NULL, 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/20210011-xiyangyang.jpeg.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1564, '数科2102班', '20210012', '懒羊羊', NULL, 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/20210012-lanyangyang.jpg.png', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1565, '数科2102班', '2021003178', '测试学生账号', '测试', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/QQ图片20230819130111.gif', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1566, '无', '没有', '测试老师账号', '测试abab2222333', 0, 'https://feigebuge.oss-cn-beijing.aliyuncs.com/QQ%E5%9B%BE%E7%89%8720230819130111.gif', NULL, 0, NULL, NULL, '保密', '保密', NULL, '2023-09-27', NULL);
INSERT INTO `student` VALUES (1568, '数科2102班', '2022006004', '郭芸杉', '', 0, '', NULL, 0, NULL, NULL, '保密', '保密', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for term_schedule
-- ----------------------------
DROP TABLE IF EXISTS `term_schedule`;
CREATE TABLE `term_schedule`  (
  `semester_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '主键',
  `semester` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学期',
  `course` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程',
  `class_number` int(11) NULL DEFAULT NULL COMMENT '课堂次序，第几节课',
  `sign_start_time` datetime NULL DEFAULT NULL COMMENT '签到开启时间',
  `sign_last_time` int(11) NULL DEFAULT NULL COMMENT '签到持续时间',
  `finished` tinyint(1) NULL DEFAULT 0 COMMENT '签到是否结束',
  `count` int(11) NULL DEFAULT NULL COMMENT '签到次数',
  PRIMARY KEY (`semester_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学期课程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of term_schedule
-- ----------------------------
INSERT INTO `term_schedule` VALUES (0, '2023学期', 'Linux', 1, '2023-09-29 09:43:30', 5, 0, 1);
INSERT INTO `term_schedule` VALUES (1692171453233328130, '2023学期', 'Linux', 2, '2023-08-08 22:11:42', 5, 0, 0);
INSERT INTO `term_schedule` VALUES (1692171582224953345, '2023学期', 'Linux', 3, '2023-08-17 14:45:09', 5, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334153506656258, '2023学期', 'Linux', 4, '2023-08-20 13:13:01', 5, 0, 1);
INSERT INTO `term_schedule` VALUES (1692334272486477826, '2023学期', 'Linux', 5, '2023-08-20 13:13:18', 5, 0, 1);
INSERT INTO `term_schedule` VALUES (1692334290723311617, '2023学期', 'Linux', 6, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334305793445889, '2023学期', 'Linux', 7, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334320951660545, '2023学期', 'Linux', 8, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334333916254209, '2023学期', 'Linux', 9, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334355370119170, '2023学期', 'Linux', 10, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334376987561986, '2023学期', 'Linux', 11, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334485917831170, '2023学期', 'Linux', 12, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334498903396354, '2023学期', 'Linux', 13, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334515357650945, '2023学期', 'Linux', 14, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334528011866114, '2023学期', 'Linux', 15, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334541983092737, '2023学期', 'Linux', 16, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334554930909185, '2023学期', 'Linux', 17, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334566976950274, '2023学期', 'Linux', 18, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1692334587701006337, '2023学期', 'Linux', 19, NULL, NULL, 0, 0);
INSERT INTO `term_schedule` VALUES (1707246674075729921, '2023学期', 'Linux', 20, NULL, 5, 0, 0);

-- ----------------------------
-- Table structure for term_sign_start_history_time
-- ----------------------------
DROP TABLE IF EXISTS `term_sign_start_history_time`;
CREATE TABLE `term_sign_start_history_time`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sem_id` bigint(20) NULL DEFAULT NULL COMMENT '学期课程id',
  `start_time` datetime NULL DEFAULT NULL COMMENT '签到开启时间',
  `count` int(11) NULL DEFAULT NULL COMMENT '课程签到次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '记录课程签到历史开始时间' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of term_sign_start_history_time
-- ----------------------------
INSERT INTO `term_sign_start_history_time` VALUES (1, 0, '2023-08-17 14:45:09', 0);
INSERT INTO `term_sign_start_history_time` VALUES (2, 0, '2023-08-17 14:45:09', 1);
INSERT INTO `term_sign_start_history_time` VALUES (3, 0, '2023-08-20 12:29:53', 2);
INSERT INTO `term_sign_start_history_time` VALUES (4, 1692334153506656258, '2023-08-20 12:59:08', 0);
INSERT INTO `term_sign_start_history_time` VALUES (5, 1692334153506656258, '2023-08-20 13:13:01', 2);
INSERT INTO `term_sign_start_history_time` VALUES (6, 1692334272486477826, '2023-08-20 13:13:18', 1);
INSERT INTO `term_sign_start_history_time` VALUES (7, 0, '2023-09-27 21:20:21', 4);
INSERT INTO `term_sign_start_history_time` VALUES (8, 0, '2023-09-28 12:12:24', 5);
INSERT INTO `term_sign_start_history_time` VALUES (9, 0, '2023-09-28 12:13:03', 6);
INSERT INTO `term_sign_start_history_time` VALUES (10, 0, '2023-09-28 12:13:08', 7);
INSERT INTO `term_sign_start_history_time` VALUES (11, 0, '2023-09-28 14:51:22', 8);
INSERT INTO `term_sign_start_history_time` VALUES (12, 0, '2023-09-29 09:43:30', 9);

-- ----------------------------
-- Table structure for title
-- ----------------------------
DROP TABLE IF EXISTS `title`;
CREATE TABLE `title`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子标题',
  `title_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子标题级别（只能是1级子标题，2级子标题，3级子标题）',
  `father_title_id` bigint(20) NULL DEFAULT NULL COMMENT '父标题id，1级子标题就是他自己',
  `subject_id` bigint(20) NULL DEFAULT NULL COMMENT '学科id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `father_title_id`(`father_title_id`) USING BTREE,
  INDEX `subject_id`(`subject_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标题，内含有 ‘学科’，‘子标题’， ‘子标题类型’， ‘子标题指向的父标题’' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of title
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL,
  `uname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `passwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `roles` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '飞哥不鸽', 'ff492c758dbab8499d71f28d5a22ee0d', 'user');
INSERT INTO `user` VALUES (2, 'gaopeng', 'f8884230780a2f71f0dfd911d63e4f73', 'user');
INSERT INTO `user` VALUES (19, 'machenru', 'a50fb3b75b7a00ad351471c13d20188d', 'user');
INSERT INTO `user` VALUES (23, 'liurui', '7b022e747374c7c4a3eda502abac28ab', 'user');
INSERT INTO `user` VALUES (27, 'jiahaoxuan', 'b1a67a8e7ee95e01df41b8fd0741652f', 'user');
INSERT INTO `user` VALUES (35, 'cuipengliang', 'f323a8064cedaf0fb572c71cc156b3ec', 'user');
INSERT INTO `user` VALUES (37, 'xuwei', 'c062f4050b72637452ffeb29fb944270', 'user');
INSERT INTO `user` VALUES (41, 'chenziyu', '3ce5704a6e59d7a4e2cad489b437cb43', 'user');
INSERT INTO `user` VALUES (43, 'wangyuanyuan', 'fa11a4e337417a1a083d18b04957dfd8', 'user');
INSERT INTO `user` VALUES (49, 'zhangtaisong', 'c884d2b76aabf40ef5fede8ab187251d', 'user');
INSERT INTO `user` VALUES (63, 'wangyingqiao', 'f15547708ee947abf08885c51481be3e', 'user');
INSERT INTO `user` VALUES (1172, 'anxinrui', 'b7c1b3f4985173ac163a651194fdb915', 'user');
INSERT INTO `user` VALUES (1173, 'liuzhuoyuan', '08dd52a5e46137c29981b80adc5c0f57', 'user');
INSERT INTO `user` VALUES (1174, 'xielinzhao', '89aaabacb0f9bddd52dc061d7425f8b2', 'user');
INSERT INTO `user` VALUES (1175, 'liubaichao', '205d5a614a354f428f82319dacaad3e6', 'user');
INSERT INTO `user` VALUES (1176, 'wangbo', 'df73939a52a5a37e0894f5ae5239b21d', 'user');
INSERT INTO `user` VALUES (1177, 'bayongheng', 'a476d28ed7ab55b132bbf02239c7b5eb', 'user');
INSERT INTO `user` VALUES (1178, 'majunkai', '11d695cb97bb475926131a7799a81aca', 'user');
INSERT INTO `user` VALUES (1179, 'xiaoxinyu', '46c636d6f92120044a28f91211a4c41e', 'user');
INSERT INTO `user` VALUES (1180, 'lvhaoheng', '8853c03a946889332db3494c1fd20876', 'user');
INSERT INTO `user` VALUES (1181, 'liangxiaojuan', '215d8b18fe813c90a8e553c5aa590c05', 'user');
INSERT INTO `user` VALUES (1182, 'zengjiawen', '830351c42d2511044b45bf3a10513c7f', 'user');
INSERT INTO `user` VALUES (1183, 'liuqian', '3e99e9bdd5681e1c29e70f8b6bbe2e07', 'user');
INSERT INTO `user` VALUES (1184, 'wuwei', 'a1c6d95af914310a8c557b7604aec9eb', 'user');
INSERT INTO `user` VALUES (1185, 'zhangrui', 'adaa84ea647d34baf01f5c073ecb602e', 'user');
INSERT INTO `user` VALUES (1186, 'zhangyizhi', '6f405bd65dd80606b67c55da1630c719', 'user');
INSERT INTO `user` VALUES (1187, 'yangjiawei', '1b1f4cb6409f98be5ebeb3b36e59fb34', 'user');
INSERT INTO `user` VALUES (1188, 'wangjiaqi', '22b849d555b310e51fe38cad71199e0e', 'user');
INSERT INTO `user` VALUES (1189, 'tengtianren', 'c35c65270f89a0afcb72fbcf42b2e1ce', 'user');
INSERT INTO `user` VALUES (1190, 'huzhiyu', '75b9ba11a700b47bb60cd8edd9009c46', 'user');
INSERT INTO `user` VALUES (1191, 'jiaxingyu', '9cb4a901d605f085ff1ebc918ee0c7b6', 'user');
INSERT INTO `user` VALUES (1192, 'xiaomu', '0da1afdbfc09f41a2240c41519b9a2cb', 'user');
INSERT INTO `user` VALUES (1193, 'liuyaqi', 'ed493dd90b97642323ac49f817631d7a', 'user');
INSERT INTO `user` VALUES (1195, 'liuxuesong', '982906f425c573c1c162bbb683e930c5', 'user');
INSERT INTO `user` VALUES (1196, 'shuzhan', '698b147cdc109849e353afd671689f3d', 'user');
INSERT INTO `user` VALUES (1197, 'litonglu', '33996ae1ddb917450322e318b5ead9ab', 'user');
INSERT INTO `user` VALUES (1198, 'wangyutao', '8d23de1b060b1bb52b68b4d903c6a5d5', 'user');
INSERT INTO `user` VALUES (1199, 'changjianhui', '61084f8bee29dad7b1846d84a428f2ea', 'user');
INSERT INTO `user` VALUES (1200, 'lichen', 'acc1ab386b9e09bbfa38dbd4a66be33d', 'user');
INSERT INTO `user` VALUES (1201, 'zhangyuxuan', 'e1fd7f4be8e81aa8af08b561d90f735f', 'user');
INSERT INTO `user` VALUES (1202, 'liuxujiao', '421b7242caafe1949069f185534b2020', 'user');
INSERT INTO `user` VALUES (1203, 'liufanhao', 'dae4f24d2bc6e2c4b52e74b51fb1577b', 'user');
INSERT INTO `user` VALUES (1204, 'zhenglihua', '9117ce052d00b057cc170e21b96c0bac', 'user');
INSERT INTO `user` VALUES (1205, 'liangjiayu', 'b8cd4fc5518ec4bf6b0a0d41b53fa07c', 'user');
INSERT INTO `user` VALUES (1206, 'liujiahao', '809a49f222596456f57c5754cd0f5205', 'user');
INSERT INTO `user` VALUES (1207, 'zhangtao', '4daf10688a642327a619ec905848c818', 'user');
INSERT INTO `user` VALUES (1208, 'liuzile', 'fb5f1129a404423e926fb1351a29b3d1', 'user');
INSERT INTO `user` VALUES (1209, 'dengjihong', '4b20c420db08742863d8626ba7d2ad9b', 'user');
INSERT INTO `user` VALUES (1210, 'dingshaojie', '132987bc38b37ed563b0598c347a090a', 'user');
INSERT INTO `user` VALUES (1211, 'lishixiang', '3bca7df4fb4169d6365089fc0f18c8d6', 'user');
INSERT INTO `user` VALUES (1213, 'wangzhenhua', 'b2f3a0e12de5c818a073665d0fc8ba03', 'user');
INSERT INTO `user` VALUES (1214, 'yuyan', '105461bd67a26a3266376d8a48c648f9', 'user');
INSERT INTO `user` VALUES (1215, 'sunhaoyu', 'a3ba23ea847a155584ac0bd99a89e8f5', 'user');
INSERT INTO `user` VALUES (1216, 'liuxingyu', '0c4193dbe290faef4413beac844f7206', 'user');
INSERT INTO `user` VALUES (1217, 'tianzhen', '76c885cd70d8de27fd0951ab7852f813', 'user');
INSERT INTO `user` VALUES (1218, 'lijiaxuan', '33240f73395d54784539c09da3737c23', 'user');
INSERT INTO `user` VALUES (1219, 'xuekaiyun', '3d3e165b876fda8886e9a122d0b51c2a', 'user');
INSERT INTO `user` VALUES (1220, 'lizelin', '734c579ad29bdbf7d3005b3a6652b0c9', 'user');
INSERT INTO `user` VALUES (1221, 'xuyu', '08cc1e3ad8e0c2b5e02583d341227107', 'user');
INSERT INTO `user` VALUES (1222, 'xujiazuo', 'f7f2b55ea3a79d7d0a4c3fd74f869807', 'user');
INSERT INTO `user` VALUES (1223, 'xujianxi', '3c2be27086142c733e13fa081bac253e', 'user');
INSERT INTO `user` VALUES (1224, 'zhangqixuan', 'c20c30ca253d0bb5c0055fea8067613d', 'user');
INSERT INTO `user` VALUES (1225, 'zhengjiarui', 'c3a94d625b7437cca17d67e61268387e', 'user');
INSERT INTO `user` VALUES (1227, 'chengjiawei', 'f2f066c839e27a6a0f1225bd18988e53', 'user');
INSERT INTO `user` VALUES (1228, 'lanxueyuan', 'f38d9982155bc137b4a91acc74f01174', 'user');
INSERT INTO `user` VALUES (1229, 'chenjiali', '4e24fa0a158e7bffcb9c183880511c4a', 'user');
INSERT INTO `user` VALUES (1230, 'sunlele', 'd1dbd0aa98128c063f45a5ccfdece0d1', 'user');
INSERT INTO `user` VALUES (1231, 'sunbicheng', 'df2a9a08eafc6b90975cc521087f84f2', 'user');
INSERT INTO `user` VALUES (1232, 'maoruijun', 'f4e9ec45c5b1d0f41101f4321f691754', 'user');
INSERT INTO `user` VALUES (1233, 'wanli', '06d2cb52f328b16dc9145fa948e8bfdf', 'user');
INSERT INTO `user` VALUES (1234, 'zhaoqiaoxia', '752b887c8017b5f156c07bd8cd86fe8c', 'user');
INSERT INTO `user` VALUES (1235, 'yangyunxin', 'c85ef47aeb2ca5a64b8df7b407281ab3', 'user');
INSERT INTO `user` VALUES (1236, 'zhangzhongyue', 'cb48c1fd9bb240881a8a270264f0a225', 'user');
INSERT INTO `user` VALUES (1237, 'sunruizhe', '8954ad479bb22e2371d4e787fa3585d0', 'user');
INSERT INTO `user` VALUES (1238, 'wuxiangxi', 'f083249a57734b7505aac1bfe96b0b3e', 'user');
INSERT INTO `user` VALUES (1239, 'sunguoliang', 'ab22c515d692890425efc517b9638b26', 'user');
INSERT INTO `user` VALUES (1240, 'baichengxi', 'e4cc1ec1030ef99381bc9064ccb19d65', 'user');
INSERT INTO `user` VALUES (1241, 'wuyaoyang', '2a890188c05e9545e6c675bdfb2f57b0', 'user');
INSERT INTO `user` VALUES (1242, 'caobeining', '2b1c5ae46eef97aac1714c03f38631cc', 'user');
INSERT INTO `user` VALUES (1243, 'yuanlong', 'b53040b885c401f61cbfb1588d6523e7', 'user');
INSERT INTO `user` VALUES (1244, 'fujiahao', 'a877cc0d1b4e4eac42b7e48a63169396', 'user');
INSERT INTO `user` VALUES (1245, 'huojingqi', 'e5086ba35c28fef2815016d3558391c4', 'user');
INSERT INTO `user` VALUES (1246, 'liangxinyu', 'c1d76307856ac8be46d8772c4b400232', 'user');
INSERT INTO `user` VALUES (1247, 'liuchenxuan', '7bea8e1dd2946e9ccd5bd3f1b5403c65', 'user');
INSERT INTO `user` VALUES (1248, 'xuyue', 'f52d1f0fd85692b1132f13d143e24165', 'user');
INSERT INTO `user` VALUES (1249, 'liuguodong', 'bfe3e82e60f46f3dbcd47c9d3221d512', 'user');
INSERT INTO `user` VALUES (1250, 'hejialuo', 'ba9d7b117adecdd164202b1c1d405bd7', 'user');
INSERT INTO `user` VALUES (1251, 'zengqingyang', 'a063a2cd8a20dca728aca2585ff8761b', 'user');
INSERT INTO `user` VALUES (1252, 'wuyuheng', 'd447bb49f630d70b4d0da875469263e4', 'user');
INSERT INTO `user` VALUES (1253, 'lijunjie', 'de08490f2216b00c3a5f45272500049b', 'user');
INSERT INTO `user` VALUES (1254, 'wangyanan', '1247ddfa17e6822ec3cf7ab254609206', 'user');
INSERT INTO `user` VALUES (1255, 'yangjingsen', 'e0995d9970d616ca269110ae92920133', 'user');
INSERT INTO `user` VALUES (1256, 'limingbo', 'df8824e82a299b17254968385a1cfc8b', 'user');
INSERT INTO `user` VALUES (1257, 'liminjun', '9c51c9482118641ff2b1b46f6ddb85c8', 'user');
INSERT INTO `user` VALUES (1258, 'bayiqi', 'ad57bca4d778ee0c47f3f6bdd4c15420', 'user');
INSERT INTO `user` VALUES (1259, 'lijiawen', '2459c59ea3f53bf8b12fdef007076954', 'user');
INSERT INTO `user` VALUES (1260, 'liuxia', '29dd1194422218703e806acdc72138c4', 'user');
INSERT INTO `user` VALUES (1261, 'lixinrui', 'ad702010073a70c6e9a4fc57854cfbac', 'user');
INSERT INTO `user` VALUES (1262, 'lvzongtao', 'a67e530d70ad770478aba9134ce5253d', 'user');
INSERT INTO `user` VALUES (1263, 'yuguo', 'd36a51c303ba566d0af69f5d1728b0a9', 'user');
INSERT INTO `user` VALUES (1264, 'fujingyuan', '1f17d702ebbd34ccf24b78212eec868f', 'user');
INSERT INTO `user` VALUES (1265, 'hanyutong', 'a0937e7139682d95f3adfe2fa328ffb7', 'user');
INSERT INTO `user` VALUES (1266, 'nongying', '780fee9c73dc1b24ed695988c83f3fe1', 'user');
INSERT INTO `user` VALUES (1267, 'liminjuan', '8c3591e64c73c987143da50d05fd5925', 'user');
INSERT INTO `user` VALUES (1268, 'liyitong', 'f0c02518e7b9b84358f1a51a79985f6b', 'user');
INSERT INTO `user` VALUES (1269, 'zhaoruizhe', '6ffd2b47921f3d7c0d3e08dd8968f6ec', 'user');
INSERT INTO `user` VALUES (1270, 'limin', '28b13fa328ade1be0bde4d0941669385', 'user');
INSERT INTO `user` VALUES (1271, 'gaoxinyu', '3b43e58169eae45066c3393cf6a108f7', 'user');
INSERT INTO `user` VALUES (1272, 'qiuyu', 'aefbb67d6d1ab0cd782d7f7dcafc579b', 'user');
INSERT INTO `user` VALUES (1273, 'zhangrongrong', 'e6dd630e76117a1781f38ce5d3f511d9', 'user');
INSERT INTO `user` VALUES (1274, 'zhaochenmiao', '5114f86d0f72775deb9b6ea99b224f40', 'user');
INSERT INTO `user` VALUES (1275, 'zhangchenghao', '60fd74649fd4ae33b101c4052ed6f4b8', 'user');
INSERT INTO `user` VALUES (1276, 'wuhao', '19318180a4b0c4ed8fdca218f93d1052', 'user');
INSERT INTO `user` VALUES (1277, 'zhangyang', 'cbd57b3d19005193e6796fe5a199836d', 'user');
INSERT INTO `user` VALUES (1278, 'yangzhixin', '103c107d8b2a4459e648a70e04b3da6e', 'user');
INSERT INTO `user` VALUES (1279, 'changhongyuan', 'd8d37609ec6be255835d7c381b782b80', 'user');
INSERT INTO `user` VALUES (1280, 'yangjiayuan', 'ffd829608b519b19de746ca2f7e22875', 'user');
INSERT INTO `user` VALUES (1281, 'lujunyu', '48fd35ec7e4fbe241ea10c34f6ca2cf9', 'user');
INSERT INTO `user` VALUES (1282, 'zhangjialei', 'c31361a7fbc3913f0f9b828eb644d7e1', 'user');
INSERT INTO `user` VALUES (1283, 'siyufei', 'e9341af85930d9bebbd2035664bcfac1', 'user');
INSERT INTO `user` VALUES (1284, 'luozhenyu', '02ff365155d7345513de9b19090ccff8', 'user');
INSERT INTO `user` VALUES (1285, 'zhangyunzhong', '8d97115e78951c455ca085f44061beca', 'user');
INSERT INTO `user` VALUES (1286, 'yangquan', 'd7bab4918f15276d86544b2801369c09', 'user');
INSERT INTO `user` VALUES (1300, 'wangyicheng', 'cb374e9e69ff1b2b5b3c83c4515428f4', 'user');
INSERT INTO `user` VALUES (1301, 'zhaoliang', 'be44a612c0fdaf27ae69c6a118bc2579', 'user');
INSERT INTO `user` VALUES (1302, 'majingchao', 'f0e1568bed50984458cbf4c88e00be37', 'user');
INSERT INTO `user` VALUES (1560, 'zhaoyujie', 'd5ddff3e39939eac38c058f15f92d430', 'user');
INSERT INTO `user` VALUES (1561, 'bigdata', 'ff492c758dbab8499d71f28d5a22ee0d', 'admin');
INSERT INTO `user` VALUES (1562, 'ceshi', 'e2a4c06b95bc58e76971e78d6974e9b5', 'user');
INSERT INTO `user` VALUES (1565, 'ceshixuesheng', 'ff492c758dbab8499d71f28d5a22ee0d', 'user');
INSERT INTO `user` VALUES (1566, 'ceshilaoshi', 'ff492c758dbab8499d71f28d5a22ee0d', 'admin');
INSERT INTO `user` VALUES (1568, 'guoyunshan', 'ff492c758dbab8499d71f28d5a22ee0d', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
