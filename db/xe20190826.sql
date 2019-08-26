-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: 47.101.53.114    Database: xero
-- ------------------------------------------------------
-- Server version	5.7.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `etsy_account_bind`
--

DROP TABLE IF EXISTS `etsy_account_bind`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etsy_account_bind` (
  `bind_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '账户绑定关系标识',
  `app_account` varchar(100) DEFAULT NULL COMMENT 'xe app账户名',
  `etsy_account` varchar(100) DEFAULT NULL COMMENT 'etsy账户名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`bind_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='app账户与etsy账户绑定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etsy_account_bind`
--

LOCK TABLES `etsy_account_bind` WRITE;
/*!40000 ALTER TABLE `etsy_account_bind` DISABLE KEYS */;
INSERT INTO `etsy_account_bind` VALUES (1,'jenks.guo@dataseek.info','etsy@dataseek.info',NULL);
/*!40000 ALTER TABLE `etsy_account_bind` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etsy_developer_detail`
--

DROP TABLE IF EXISTS `etsy_developer_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etsy_developer_detail` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录标识',
  `developer_account` varchar(100) DEFAULT NULL COMMENT '开发者帐号',
  `consumer_key` varchar(100) DEFAULT NULL COMMENT 'consumer key',
  `consumer_secret` varchar(100) DEFAULT NULL COMMENT 'consumer secrect',
  `request_token_url` varchar(200) DEFAULT NULL,
  `authorize_url` varchar(200) DEFAULT NULL,
  `access_token_url` varchar(200) DEFAULT NULL,
  `callback_url` varchar(500) DEFAULT NULL COMMENT '应用回调地址',
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='etsy开发者信息管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etsy_developer_detail`
--

LOCK TABLES `etsy_developer_detail` WRITE;
/*!40000 ALTER TABLE `etsy_developer_detail` DISABLE KEYS */;
INSERT INTO `etsy_developer_detail` VALUES (1,'etsy@dataseek.info','78qwl864ty5269f469svn6md','xcalhsuznj','https://openapi.etsy.com/v2/oauth/request_token?scope=transactions_r%20listings_r%20shops_rw%20profile_r%20billing_r','https://www.etsy.com/oauth/signin','https://openapi.etsy.com/v2/oauth/access_token','http://127.0.0.1:12006/test/etsy/token_apply');
/*!40000 ALTER TABLE `etsy_developer_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etsy_token_admin`
--

DROP TABLE IF EXISTS `etsy_token_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etsy_token_admin` (
  `admin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_account` varchar(100) DEFAULT NULL COMMENT 'app与etsy关联id',
  `request_token` varchar(100) DEFAULT NULL,
  `request_secret` varchar(100) DEFAULT NULL,
  `access_token` varchar(100) DEFAULT NULL,
  `access_secret` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='etsy用户Token管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etsy_token_admin`
--

LOCK TABLES `etsy_token_admin` WRITE;
/*!40000 ALTER TABLE `etsy_token_admin` DISABLE KEYS */;
INSERT INTO `etsy_token_admin` VALUES (2,'jenks','3f4c8a4185cae8b8f899d98158c7d1','4adec1af11','eca5c9e7ec45d8dcf3462d15323ca2','7d66830ced','2019-08-26 23:09:47');
/*!40000 ALTER TABLE `etsy_token_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_table`
--

DROP TABLE IF EXISTS `test_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_table` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_table`
--

LOCK TABLES `test_table` WRITE;
/*!40000 ALTER TABLE `test_table` DISABLE KEYS */;
INSERT INTO `test_table` VALUES (1,'taosm'),(2,'weigang');
/*!40000 ALTER TABLE `test_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xe_user`
--

DROP TABLE IF EXISTS `xe_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xe_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `first_name` varchar(30) DEFAULT NULL COMMENT 'first_name',
  `last_name` varchar(30) DEFAULT NULL COMMENT 'last_name',
  `email` varchar(40) DEFAULT NULL COMMENT 'email',
  `password` varchar(50) DEFAULT NULL COMMENT 'password',
  `active` varchar(2) DEFAULT NULL COMMENT 'Y:激活 N:未激活',
  `user_id` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='user info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xe_user`
--

LOCK TABLES `xe_user` WRITE;
/*!40000 ALTER TABLE `xe_user` DISABLE KEYS */;
INSERT INTO `xe_user` VALUES (18,'gei','wang','2222@ss.com','1111','N','1000001'),(19,'gei','wang','2222@ss.com','1111','N','1000002'),(20,'gei','wang','2222@ss.com','1111','N','1000003'),(21,'gei','wang','2222@ss.com','1111','N','1000004'),(22,'gei','wang','2222@ss.com','tZxnvxlqR1gZHkL3ZnDOug==','N','201908201629511001');
/*!40000 ALTER TABLE `xe_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xero_account_bind`
--

DROP TABLE IF EXISTS `xero_account_bind`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xero_account_bind` (
  `bind_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_account` varchar(100) DEFAULT NULL,
  `xero_account` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`bind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xero_account_bind`
--

LOCK TABLES `xero_account_bind` WRITE;
/*!40000 ALTER TABLE `xero_account_bind` DISABLE KEYS */;
/*!40000 ALTER TABLE `xero_account_bind` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xero_developer_detail`
--

DROP TABLE IF EXISTS `xero_developer_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `xero_developer_detail` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  `developer_account` varchar(100) DEFAULT NULL,
  `consumer_key` varchar(100) DEFAULT NULL,
  `consumer_secret` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='xero开发者管理信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xero_developer_detail`
--

LOCK TABLES `xero_developer_detail` WRITE;
/*!40000 ALTER TABLE `xero_developer_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `xero_developer_detail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-26 23:15:27
