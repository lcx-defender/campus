/*
 Navicat Premium Data Transfer

 Source Server         : 字节云服务器数据库
 Source Server Type    : MySQL
 Source Server Version : 90100
 Source Schema         : campus

 Target Server Type    : MySQL
 Target Server Version : 90100
 File Encoding         : 65001

 Date: 16/05/2025 17:33:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dormitory_info
-- ----------------------------
DROP TABLE IF EXISTS `dormitory_info`;
CREATE TABLE `dormitory_info`  (
  `id` bigint(0) NOT NULL,
  `student_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `dormitory_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宿舍楼号',
  `room_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房间号',
  `bed_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '床位号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生宿舍信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dormitory_info
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID,逻辑外键',
  `student_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `student_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `university_id` bigint(0) NULL DEFAULT NULL COMMENT '学校代码',
  `institute_id` bigint(0) NULL DEFAULT NULL COMMENT '学院代码',
  `major_id` bigint(0) NULL DEFAULT NULL COMMENT '专业代码',
  `class_id` bigint(0) NULL DEFAULT NULL COMMENT '班级代码',
  `current_grade` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当前年级',
  `admit_time` datetime(0) NULL DEFAULT NULL COMMENT '入学时间',
  `graduate_time` datetime(0) NULL DEFAULT NULL COMMENT '毕业时间',
  `political_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `ethnic` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '民族',
  `academic_status` int(0) NULL DEFAULT NULL COMMENT '学籍状态（0未注册、1在籍、2保留学籍、3休学、4退学、5毕业、6结业、7肄业） 数据字典',
  `high_school` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '高中',
  `home_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `idx_student_id`(`student_id`) USING BTREE,
  INDEX `idx_student_name`(`student_name`) USING BTREE,
  INDEX `idx_university_id`(`university_id`) USING BTREE,
  INDEX `idx_institute_id`(`institute_id`) USING BTREE,
  INDEX `idx_major_id`(`major_id`) USING BTREE,
  INDEX `idx_class_id`(`class_id`) USING BTREE,
  INDEX `idx_current_grade`(`current_grade`) USING BTREE,
  INDEX `idx_academic_status`(`academic_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '父部门id',
  `level` int(0) NULL DEFAULT NULL,
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '院校官网',
  `status` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '部门状态 0停用,1正常',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '院校表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, 1, '南京工程测试大学', NULL, NULL, NULL, NULL, '1', '0', '', '2025-04-28 13:08:24', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (2, 0, 1, '南京艺术测试学院', NULL, NULL, NULL, NULL, '1', '0', '', '2025-05-09 19:38:58', '', NULL, NULL);
INSERT INTO `sys_dept` VALUES (3, 1, 2, '计算机工程学院', '刘嘻嘻', '', '', NULL, '1', '0', '', '2025-05-14 14:08:44', '', NULL, '');
INSERT INTO `sys_dept` VALUES (4, 3, 3, '计算机科学与技术', '', '', '', NULL, '1', '0', '', NULL, '', NULL, '');
INSERT INTO `sys_dept` VALUES (5, 0, 1, '南京测试大学', '', '', '', NULL, '1', '0', '', NULL, '', NULL, '');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `dict_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（1正常 0停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_info`;
CREATE TABLE `sys_login_info`  (
  `info_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `login_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_login_time`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限标识',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父菜单ID',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `router_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `menu_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `is_cache` int(0) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `query_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(0) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 'system', 1, '', 0, '', 'system', 'M', '0', 0, '', NULL, 1, '', '2025-04-13 23:10:42', '', '2025-04-11 15:39:38', '');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 'monitor', 2, '', 0, '', 'monitor', 'M', '0', 0, '', NULL, 1, '', '2025-04-13 23:10:42', '', '2025-03-07 16:22:19', '');
INSERT INTO `sys_menu` VALUES (3, '校园业务', 'campus', 3, '', 0, '', 'campus', 'M', '0', 0, '', '', 1, '', '2025-04-13 23:10:43', '', NULL, '校园业务管理菜单');
INSERT INTO `sys_menu` VALUES (4, '用户管理', 'user-manage', 1, 'system:user:list', 1, 'system/user/index', 'user', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:42', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (5, '角色管理', 'peoples', 2, 'system:role:list', 1, 'system/role/index', 'role', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:42', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (6, '菜单管理', 'tree-table', 3, 'system:menu:list', 1, 'system/menu/index', 'menu', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:42', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (7, '部门管理', 'tree', 4, 'system:dept:list', 1, 'system/dept/index', 'dept', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:42', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (8, '字典管理', 'dict', 5, 'system:dict:list', 1, 'system/dict/index', 'dict', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:42', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (9, '操作日志', 'form', 1, 'monitor:operatelog:list', 2, 'monitor/operlog/index', 'operlog', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:43', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (10, '登录日志', 'logininfor', 2, 'monitor:logininfo:list', 2, 'monitor/logininfo/index', 'logininfo', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:43', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (11, '在线用户', 'online', 3, 'monitor:online:list', 2, 'monitor/online/index', 'online', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:43', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (12, '教师管理', 'teacher_manage', 1, 'campus:teacher:list', 3, 'campus/teacher/index', 'teacher', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:44', '', NULL, '教师信息管理菜单');
INSERT INTO `sys_menu` VALUES (13, '学生管理', 'student_manage', 2, 'campus:student:list', 3, 'campus/student/index', 'student', 'C', '0', 0, '', '', 1, '', '2025-04-13 23:10:44', '', NULL, '学生信息管理菜单');
INSERT INTO `sys_menu` VALUES (14, '用户新增', '#', 1, 'system:user:add', 4, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (15, '用户注销', '#', 2, 'system:user:remove', 4, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (16, '用户修改', '#', 3, 'system:user:edit', 4, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (17, '用户查询', '#', 4, 'system:user:query', 4, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (18, '重置密码', '#', 5, 'system:user:reset', 4, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (19, '用户封禁', '#', 6, 'system:user:ban', 4, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (20, '用户恢复', '#', 7, 'system:user:recover', 4, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (21, '分配角色', '#', 8, 'system:user:grant', 4, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (22, '角色新增', '#', 1, 'system:role:add', 5, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (23, '角色删除', '#', 2, 'system:role:remove', 5, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (24, '角色修改', '#', 3, 'system:role:edit', 5, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (25, '角色查询', '#', 4, 'system:role:query', 5, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (26, '分配权限', '#', 5, 'system:role:grant', 5, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (27, '解绑用户', '#', 6, 'system:role:unbind', 5, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (28, '菜单新增', '#', 1, 'system:menu:add', 6, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (29, '菜单删除', '#', 2, 'system:menu:remove', 6, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (30, '菜单修改', '#', 3, 'system:menu:edit', 6, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (31, '菜单查询', '#', 4, 'system:menu:query', 6, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (32, '部门新增', '#', 1, 'system:dept:add', 7, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (33, '部门删除', '#', 2, 'system:dept:remove', 7, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (34, '部门修改', '#', 3, 'system:dept:edit', 7, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (35, '部门查询', '#', 4, 'system:dept:query', 7, '', '', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (36, '字典新增', '#', 1, 'system:dict:add', 8, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (37, '字典删除', '#', 2, 'system:dict:remove', 8, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (38, '字典修改', '#', 3, 'system:dict:edit', 8, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (39, '字典查询', '#', 4, 'system:dict:query', 8, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (40, '操作删除', '#', 1, 'monitor:operatelog:remove', 9, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (41, '操作查询', '#', 2, 'monitor:operatelog:list', 9, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (42, '登录删除', '#', 1, 'monitor:logininfo:remove', 10, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (43, '登录查询', '#', 2, 'monitor:logininfo:query', 10, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (44, '在线查询', '#', 1, 'monitor:online:query', 11, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (45, '批量强退', '#', 2, 'monitor:online:batchLogout', 11, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (46, '单条强退', '#', 3, 'monitor:online:forceLogout', 11, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (47, '教师新增', '#', 1, 'campus:teacher:add', 12, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (48, '教师删除', '#', 2, 'campus:teacher:remove', 12, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (49, '教师修改', '#', 3, 'campus:teacher:edit', 12, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (50, '教师查询', '#', 4, 'campus:teacher:query', 12, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (51, '学生新增', '#', 1, 'campus:student:add', 13, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (52, '学生批量', '#', 2, 'campus:student:add', 13, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (53, '学生删除', '#', 3, 'campus:student:remove', 13, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (54, '学生修改', '#', 4, 'campus:student:edit', 13, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (55, '学生查询', '#', 5, 'campus:student:query', 13, '', ' ', 'F', '0', 0, '', '', 1, '', '2025-04-13 23:10:48', '', NULL, '');
INSERT INTO `sys_menu` VALUES (56, '导出学生', '', 0, 'campus:student:export', 13, '', '', 'F', '0', 0, '', '', 1, '', NULL, '', NULL, '');

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log`  (
  `operate_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `business_type` int(0) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除 4查询 5授权 6导出 7导入 8强退）数据字典',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式,PUT,POST,GET,DELETE',
  `operator_type` int(0) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）数据字典',
  `user_id` bigint(0) NOT NULL COMMENT '操作人员ID',
  `operate_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `operate_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `operate_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `operate_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `operate_status` int(0) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(0) NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`operate_id`) USING BTREE,
  INDEX `idx_sys_op_log_bt`(`business_type`) USING BTREE,
  INDEX `idx_sys_op_log_s`(`operate_status`) USING BTREE,
  INDEX `idx_sys_op_log_ot`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用） 数据字典',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '顶级管理员', 'admin', '1', '', '2025-03-07 16:06:33', '', '2025-03-07 16:06:36', NULL);
INSERT INTO `sys_role` VALUES (2, '南京测试大学负责人', 'university_admin', '1', '1', '2025-05-12 00:22:11', '1', '2025-05-12 14:41:14', NULL);
INSERT INTO `sys_role` VALUES (3, '南京测试大学计算机工程学院辅导员', 'counselor', '1', '1', '2025-05-12 14:40:01', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (3, 3);
INSERT INTO `sys_role_menu` VALUES (3, 12);
INSERT INTO `sys_role_menu` VALUES (3, 13);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `identity` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` int(0) NOT NULL COMMENT '用户类型（0系统管理&程序员;1教师;2学生） 数据字典',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码-适应国际化号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）数据字典',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `user_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1封禁 2删除）数据字典',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录地点',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `unique_identity`(`identity`) USING BTREE,
  UNIQUE INDEX `unique_email`(`email`) USING BTREE,
  UNIQUE INDEX `unique_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '360123', 'lcx_defender', 0, '123456@qq.com', '180000000', '0', 'https://greet-freshman.oss-cn-shanghai.aliyuncs.com/upload/avatar/2025/05/13/68236aea2051dbe1227172fb.png', '$2a$10$lyn4VN9u.SNX27iEOaCxX.vftSRQgjBQW7W4XfAk/hVfW40YZIyyq', '0', '127.0.0.1', '内网IP', '', '2025-03-05 22:03:23', '', '2025-05-16 15:59:39', '');
INSERT INTO `sys_user` VALUES (2, '320123', '1688小便当火爆上市', 1, '', '', '1', 'https://greet-freshman.oss-cn-shanghai.aliyuncs.com/default-avatar.png', '$2a$10$nsmKiSdYZHxJy1p3ViXUyu5R/AQSAww5yT8fmV7eEKojhbyq/Swym', '0', '', '', '', '2025-05-09 00:27:17', '', NULL, '');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID,逻辑外键',
  `teacher_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师工号',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `dept_id` bigint(0) NOT NULL COMMENT '部门代码-直接显示最低单位',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职称',
  `office` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '办公室',
  `admit_time` datetime(0) NULL DEFAULT NULL COMMENT '入职时间',
  `position_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位状态（0在职、1停职、2离职） 数据字典',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `idx_teacher_id`(`teacher_id`) USING BTREE,
  INDEX `idx_teacher_name`(`teacher_name`) USING BTREE,
  INDEX `idx_university_id`(`dept_id`) USING BTREE,
  INDEX `idx_title`(`title`) USING BTREE,
  INDEX `idx_position_status`(`position_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教师表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
