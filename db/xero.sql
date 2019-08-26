/*
 Navicat Premium Data Transfer

 Source Server         : xero数据库
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 47.101.53.114:3606
 Source Schema         : xero

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 26/08/2019 17:15:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for etsy_account_bind
-- ----------------------------
DROP TABLE IF EXISTS `etsy_account_bind`;
CREATE TABLE `etsy_account_bind`  (
  `bind_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '账户绑定关系标识',
  `app_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'xe app账户名',
  `etsy_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'etsy账户名',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`bind_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'app账户与etsy账户绑定表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of etsy_account_bind
-- ----------------------------
INSERT INTO `etsy_account_bind` VALUES (1, 'jenks.guo@dataseek.info', 'etsy@dataseek.info', NULL);

-- ----------------------------
-- Table structure for etsy_developer_detail
-- ----------------------------
DROP TABLE IF EXISTS `etsy_developer_detail`;
CREATE TABLE `etsy_developer_detail`  (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录标识',
  `developer_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '开发者帐号',
  `consumer_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'consumer key',
  `consumer_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'consumer secrect',
  `request_token_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `authorize_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `access_token_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `callback_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '应用回调地址',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'etsy开发者信息管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of etsy_developer_detail
-- ----------------------------
INSERT INTO `etsy_developer_detail` VALUES (1, 'etsy@dataseek.info', '78qwl864ty5269f469svn6md', 'xcalhsuznj', 'https://openapi.etsy.com/v2/oauth/request_token?scope=transactions_r%20listings_r%20shops_rw%20profile_r%20billing_r', 'https://www.etsy.com/oauth/signin', 'https://openapi.etsy.com/v2/oauth/access_token', 'www.baidu.com');

-- ----------------------------
-- Table structure for etsy_token_admin
-- ----------------------------
DROP TABLE IF EXISTS `etsy_token_admin`;
CREATE TABLE `etsy_token_admin`  (
  `admin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'app与etsy关联id',
  `request_token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `request_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `access_token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `acccess_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'etsy用户Token管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test_table
-- ----------------------------
DROP TABLE IF EXISTS `test_table`;
CREATE TABLE `test_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_table
-- ----------------------------
INSERT INTO `test_table` VALUES (1, 'taosm');
INSERT INTO `test_table` VALUES (2, 'weigang');

-- ----------------------------
-- Table structure for xe_user
-- ----------------------------
DROP TABLE IF EXISTS `xe_user`;
CREATE TABLE `xe_user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `first_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'first_name',
  `last_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'last_name',
  `email` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'email',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'password',
  `active` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Y:激活 N:未激活',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'user info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xe_user
-- ----------------------------
INSERT INTO `xe_user` VALUES (18, 'gei', 'wang', '2222@ss.com', '1111', 'N', '1000001');
INSERT INTO `xe_user` VALUES (19, 'gei', 'wang', '2222@ss.com', '1111', 'N', '1000002');
INSERT INTO `xe_user` VALUES (20, 'gei', 'wang', '2222@ss.com', '1111', 'N', '1000003');
INSERT INTO `xe_user` VALUES (21, 'gei', 'wang', '2222@ss.com', '1111', 'N', '1000004');
INSERT INTO `xe_user` VALUES (22, 'gei', 'wang', '2222@ss.com', 'tZxnvxlqR1gZHkL3ZnDOug==', 'N', '201908201629511001');

-- ----------------------------
-- Table structure for xero_account_bind
-- ----------------------------
DROP TABLE IF EXISTS `xero_account_bind`;
CREATE TABLE `xero_account_bind`  (
  `bind_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `xero_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`bind_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xero_developer_detail
-- ----------------------------
DROP TABLE IF EXISTS `xero_developer_detail`;
CREATE TABLE `xero_developer_detail`  (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  `developer_account` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `consumer_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `consumer_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'xero开发者管理信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
