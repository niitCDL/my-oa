/*
Navicat MySQL Data Transfer

Source Server         : CDL
Source Server Version : 50738
Source Host           : localhost:3306
Source Database       : oa

Target Server Type    : MYSQL
Target Server Version : 50738
File Encoding         : 65001

Date: 2023-03-03 15:18:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `adm_department`
-- ----------------------------
DROP TABLE IF EXISTS `adm_department`;
CREATE TABLE `adm_department` (
  `department_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(32) NOT NULL,
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of adm_department
-- ----------------------------
INSERT INTO `adm_department` VALUES ('1', '总裁办');
INSERT INTO `adm_department` VALUES ('2', '研发部');
INSERT INTO `adm_department` VALUES ('3', '市场部');

-- ----------------------------
-- Table structure for `adm_employee`
-- ----------------------------
DROP TABLE IF EXISTS `adm_employee`;
CREATE TABLE `adm_employee` (
  `employee_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `department_id` bigint(20) NOT NULL,
  `title` varchar(32) NOT NULL,
  `level` int(11) NOT NULL,
  `path` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`employee_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of adm_employee
-- ----------------------------
INSERT INTO `adm_employee` VALUES ('1', '张晓涛', '1', '总经理', '8', 'images/2.jpg');
INSERT INTO `adm_employee` VALUES ('2', '齐紫陌', '2', '部门经理', '7', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('3', '王美美', '2', '高级研发工程师', '6', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('4', '宋彩妮', '2', '研发工程师', '5', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('5', '欧阳峰', '2', '初级研发工程师', '4', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('6', '张世豪', '3', '部门经理', '7', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('7', '王子豪', '3', '大客户经理', '6', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('8', '段峰', '3', '客户经理', '5', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('9', '章雪峰', '3', '客户经理', '4', 'images/default.jpg');
INSERT INTO `adm_employee` VALUES ('10', '李莉', '3', '见习客户经理', '3', 'images/default.jpg');

-- ----------------------------
-- Table structure for `adm_leave_form`
-- ----------------------------
DROP TABLE IF EXISTS `adm_leave_form`;
CREATE TABLE `adm_leave_form` (
  `form_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '请假单编号',
  `employee_id` bigint(20) NOT NULL COMMENT '员工编号',
  `form_type` int(11) NOT NULL COMMENT '请假类型 1-事假 2-病假 3-工伤假 4-婚假 5-产假 6-丧假',
  `start_time` datetime NOT NULL COMMENT '请假起始时间',
  `end_time` datetime NOT NULL COMMENT '请假结束时间',
  `reason` varchar(128) NOT NULL COMMENT '请假事由',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `state` varchar(32) NOT NULL COMMENT 'processing-正在审批 approved-审批已通过 refused-审批被驳回',
  PRIMARY KEY (`form_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of adm_leave_form
-- ----------------------------
INSERT INTO `adm_leave_form` VALUES ('1', '1', '1', '2023-03-03 00:00:00', '2023-03-10 00:00:00', '我是总经理', '2023-03-03 15:13:32', 'processing');

-- ----------------------------
-- Table structure for `adm_process_flow`
-- ----------------------------
DROP TABLE IF EXISTS `adm_process_flow`;
CREATE TABLE `adm_process_flow` (
  `process_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '处理任务编号',
  `form_id` bigint(20) NOT NULL COMMENT '表单编号',
  `operator_id` bigint(20) NOT NULL COMMENT '经办人编号',
  `action` varchar(32) NOT NULL COMMENT 'apply-申请  audit-审批',
  `result` varchar(32) DEFAULT NULL COMMENT 'approved-同意 refused-驳回',
  `reason` varchar(255) DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `audit_time` datetime DEFAULT NULL COMMENT '审批时间',
  `order_no` int(11) NOT NULL COMMENT '任务序号',
  `state` varchar(32) NOT NULL COMMENT 'ready-准备 process-正在处理 complete-处理完成 cancel-取消',
  `is_last` int(11) NOT NULL COMMENT '是否最后节点,0-否 1-是',
  PRIMARY KEY (`process_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of adm_process_flow
-- ----------------------------
INSERT INTO `adm_process_flow` VALUES ('1', '2', '1', 'apply', null, null, '2023-03-03 15:13:32', null, '1', 'complete', '0');
INSERT INTO `adm_process_flow` VALUES ('2', '2', '1', 'audit', null, '我是总经理', '2023-03-03 15:13:32', null, '2', 'complete', '1');

-- ----------------------------
-- Table structure for `sys_node`
-- ----------------------------
DROP TABLE IF EXISTS `sys_node`;
CREATE TABLE `sys_node` (
  `node_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '节点编号',
  `node_type` int(11) NOT NULL COMMENT '节点类型 1-模块 2-功能',
  `node_name` varchar(32) NOT NULL COMMENT '节点名称',
  `url` varchar(255) DEFAULT NULL COMMENT '功能地址',
  `node_code` int(11) NOT NULL COMMENT '节点编码,用于排序',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级节点编号',
  `icon` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`node_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_node
-- ----------------------------
INSERT INTO `sys_node` VALUES ('1', '1', '行政审批', null, '1000000', null, 'Edit');
INSERT INTO `sys_node` VALUES ('2', '2', '通知公告', '/notice.html', '1000001', '1', 'Bell');
INSERT INTO `sys_node` VALUES ('3', '2', '请假申请', '/leave_form.html', '1000002', '1', 'Tickets');
INSERT INTO `sys_node` VALUES ('4', '2', '请假审批', '/audit.html', '1000003', '1', 'CircleCheck');
INSERT INTO `sys_node` VALUES ('5', '1', '系统设置', null, '2000000', null, 'Setting');
INSERT INTO `sys_node` VALUES ('6', '1', '仪表盘', '/dashboard.html', '0', null, 'Grid');
INSERT INTO `sys_node` VALUES ('7', '2', '数据导出', '/export.html', '2000001', '5', 'Folder');
INSERT INTO `sys_node` VALUES ('8', '2', '系统重置', '/reset.html', '2000002', '5', 'Lock');

-- ----------------------------
-- Table structure for `sys_notice`
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `receiver_id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES ('1', '3', '您的请假申请[2023-03-02-00时-2023-03-03-00时]已提交,请等待上级审批.', '2023-02-27 10:12:44');
INSERT INTO `sys_notice` VALUES ('2', '2', '高级研发工程师-王美美提起请假申请[2023-03-02-00时-2023-03-03-00时],请尽快审批', '2023-02-27 10:12:44');
INSERT INTO `sys_notice` VALUES ('3', '3', '您的请假申请[2023-03-02-00时-2023-03-03-00时]部门经理齐紫陌已批准,审批意见:好好休息,审批流程已结束', '2023-02-27 10:13:24');
INSERT INTO `sys_notice` VALUES ('4', '2', '高级研发工程师-王美美提起请假申请[2023-03-02-00时-2023-03-03-00时]您已批准,审批意见:好好休息,审批流程已结束', '2023-02-27 10:13:24');
INSERT INTO `sys_notice` VALUES ('5', '3', '您的请假申请[2023-02-27-00时-2023-03-10-00时]已提交,请等待上级审批.', '2023-02-27 10:13:57');
INSERT INTO `sys_notice` VALUES ('6', '2', '高级研发工程师-王美美提起请假申请[2023-02-27-00时-2023-03-10-00时],请尽快审批', '2023-02-27 10:13:57');
INSERT INTO `sys_notice` VALUES ('7', '3', '您的请假申请[2023-02-27-00时-2023-03-10-00时]部门经理齐紫陌已批准,审批意见:ok ,请继续等待上级审批', '2023-02-27 10:14:10');
INSERT INTO `sys_notice` VALUES ('8', '1', '高级研发工程师-王美美提起请假申请[2023-02-27-00时-2023-03-10-00时],请尽快审批', '2023-02-27 10:14:10');
INSERT INTO `sys_notice` VALUES ('9', '2', '高级研发工程师-王美美提起请假申请[2023-02-27-00时-2023-03-10-00时]您已批准,审批意见:ok,申请转至上级领导继续审批', '2023-02-27 10:14:10');
INSERT INTO `sys_notice` VALUES ('10', '3', '您的请假申请[2023-02-27-00时-2023-03-10-00时]总经理张晓涛已批准,审批意见:ok,审批流程已结束', '2023-02-27 10:14:31');
INSERT INTO `sys_notice` VALUES ('11', '1', '高级研发工程师-王美美提起请假申请[2023-02-27-00时-2023-03-10-00时]您已批准,审批意见:ok,审批流程已结束', '2023-02-27 10:14:31');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_description` varchar(32) NOT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '业务岗角色');
INSERT INTO `sys_role` VALUES ('2', '管理岗角色');

-- ----------------------------
-- Table structure for `sys_role_node`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_node`;
CREATE TABLE `sys_role_node` (
  `rn_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `node_id` bigint(20) NOT NULL,
  PRIMARY KEY (`rn_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role_node
-- ----------------------------
INSERT INTO `sys_role_node` VALUES ('1', '1', '1');
INSERT INTO `sys_role_node` VALUES ('2', '1', '2');
INSERT INTO `sys_role_node` VALUES ('3', '1', '3');
INSERT INTO `sys_role_node` VALUES ('4', '2', '1');
INSERT INTO `sys_role_node` VALUES ('5', '2', '2');
INSERT INTO `sys_role_node` VALUES ('6', '2', '3');
INSERT INTO `sys_role_node` VALUES ('7', '2', '5');
INSERT INTO `sys_role_node` VALUES ('8', '2', '6');
INSERT INTO `sys_role_node` VALUES ('9', '2', '7');
INSERT INTO `sys_role_node` VALUES ('10', '2', '4');
INSERT INTO `sys_role_node` VALUES ('11', '2', '8');

-- ----------------------------
-- Table structure for `sys_role_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `ru_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`ru_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('1', '2', '1');
INSERT INTO `sys_role_user` VALUES ('2', '2', '2');
INSERT INTO `sys_role_user` VALUES ('3', '1', '3');
INSERT INTO `sys_role_user` VALUES ('4', '1', '4');
INSERT INTO `sys_role_user` VALUES ('5', '1', '5');
INSERT INTO `sys_role_user` VALUES ('6', '2', '6');
INSERT INTO `sys_role_user` VALUES ('7', '1', '7');
INSERT INTO `sys_role_user` VALUES ('8', '1', '8');
INSERT INTO `sys_role_user` VALUES ('9', '1', '9');
INSERT INTO `sys_role_user` VALUES ('10', '1', '10');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `employee_id` bigint(20) NOT NULL COMMENT '员工编号',
  `salt` int(11) NOT NULL COMMENT '盐值',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'm8', '6d2822348384052195363684ae531e38', '1', '188');
INSERT INTO `sys_user` VALUES ('2', 't7', '6d2822348384052195363684ae531e38', '2', '188');
INSERT INTO `sys_user` VALUES ('3', 't6', '6d2822348384052195363684ae531e38', '3', '188');
INSERT INTO `sys_user` VALUES ('4', 't5', '11f04f04054772bc1a8fdc41e70c7977', '4', '191');
INSERT INTO `sys_user` VALUES ('5', 't4', '8d7713848189a8d5c224f94f65d18b06', '5', '192');
INSERT INTO `sys_user` VALUES ('6', 's7', '044214e86e07d96c97de79a2222188cd', '6', '193');
INSERT INTO `sys_user` VALUES ('7', 's6', 'ecbd2f592ee65838328236d06ce35252', '7', '194');
INSERT INTO `sys_user` VALUES ('8', 's5', '846ecc83bba8fe420adc38b39f897201', '8', '195');
INSERT INTO `sys_user` VALUES ('9', 's4', 'c1e523cd2daa02f6cf4b98b2f26585fd', '9', '196');
INSERT INTO `sys_user` VALUES ('10', 's3', '89e89f369e07634fbb2286efffb9492b', '10', '197');
INSERT INTO `sys_user` VALUES ('11', 'sa', '6d2822348384052195363684ae531e38', '11', '188');
