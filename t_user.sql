/*
Navicat MySQL Data Transfer

Source Server         : localhosy_mysql
Source Server Version : 50634
Source Host           : localhost:3306
Source Database       : webrtc_demo

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2019-05-02 10:32:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(32) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  `pwd` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('28255BDED0244B799B64157D27FF4478', 'test2', 'yang4', '123456');
INSERT INTO `t_user` VALUES ('9FB097BE9DF647DFB73D7077F782B079', '往事如风', 'yang3', '123456');
INSERT INTO `t_user` VALUES ('A82E63B61EC24EDF97ADCB1EFD0A61BC', '杨', 'yang', '123456');
INSERT INTO `t_user` VALUES ('CEF3F8992ECC4E0DA18E442694750B1E', '为往圣继绝学', 'yang2', '123456');
