-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 初始化admin表数据

INSERT INTO `sys_admin` VALUES (1, 'banana', '53ee8da4e538f332cfca95f120d53e1a', 'https://shopzz.oss-cn-guangzhou.aliyuncs.com/other/a1.jpg', '香蕉', 0, '18888888888', 'whoiszxl@gmail.com', '', 0, '2022-12-22 12:12:12', 90071992547409944, 1, 0, 'banana', 'banana', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_admin` VALUES (2, 'apple', '53ee8da4e538f332cfca95f120d53e1a', 'https://shopzz.oss-cn-guangzhou.aliyuncs.com/other/a1.jpg', '苹果', 1, '18888888888', 'whoiszxl@gmail.com', '', 0, '2022-12-22 12:12:12', 1, 1, 0, 'banana', 'banana', '2029-11-29 16:07:56', '2023-03-08 16:08:31');

INSERT INTO `sys_admin_role` VALUES (1, 1, 1);
INSERT INTO `sys_admin_role` VALUES (2, 1, 2);

INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, NULL, 0, 1, 1, 'system', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (2, '管理员管理', 1, 2, '/system/admin', NULL, NULL, 0, 1, 1, 'system:admin:list', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (3, '管理员新增', 2, 3, '', NULL, NULL, 0, 1, 1, 'system:admin:add', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (4, '管理员修改', 2, 3, NULL, NULL, NULL, 0, 1, 1, 'system:admin:update', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (5, '管理员删除', 2, 3, NULL, NULL, NULL, 0, 1, 1, 'system:admin:delete', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (6, '管理员导出', 2, 3, NULL, NULL, NULL, 0, 1, 1, 'system:admin:export', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (7, '管理员密码重置', 2, 3, NULL, NULL, NULL, 0, 1, 1, 'system:admin:reset-password', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (8, '管理员角色分配', 2, 3, NULL, NULL, NULL, 0, 1, 1, 'system:admin:update-role', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (10001, '管理员状态切换', 2, 3, NULL, NULL, NULL, 0, 1, 1, 'system:admin:switch-status', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (9, '角色管理', 1, 2, '/system/role', NULL, NULL, 0, 1, 1, 'system:role:list', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (10, '角色新增', 9, 1, NULL, NULL, NULL, 0, 1, 1, 'system:role:add', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (11, '角色修改', 9, 1, NULL, NULL, NULL, 0, 1, 1, 'system:role:update', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (12, '角色删除', 9, 1, NULL, NULL, NULL, 0, 1, 1, 'system:role:delete', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (13, '角色导出', 9, 1, NULL, NULL, NULL, 0, 1, 1, 'system:role:export', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (14, '菜单管理', 1, 1, '/system/menu', NULL, NULL, 0, 1, 1, 'system:menu:list', NULL, NULL, 1, 1, 0, '', '', NULL, '2023-03-05 11:55:51');
INSERT INTO `sys_menu` VALUES (15, '菜单新增', 14, 1, NULL, NULL, NULL, 0, 1, 1, 'system:menu:add', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (16, '菜单修改', 14, 1, NULL, NULL, NULL, 0, 1, 1, 'system:menu:update', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (17, '菜单删除', 14, 1, NULL, NULL, NULL, 0, 1, 1, 'system:menu:delete', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');
INSERT INTO `sys_menu` VALUES (18, '菜单导出', 14, 1, NULL, NULL, NULL, 0, 1, 1, 'system:menu:export', NULL, NULL, 1, 1, 0, '', '', '2023-11-08 16:07:45', '2023-03-08 16:08:30');

INSERT INTO `sys_menu` VALUES (10004, '实战管理', 0, 1, 'ops', NULL, NULL, 0, 1, 0, 'ops', 'cloud', 999, 1, 1, 0, '', '', '2023-03-11 01:46:22', '2023-03-11 02:27:38');
INSERT INTO `sys_menu` VALUES (10005, '项目管理', 10004, 2, 'ops-project', 'ops-project', NULL, 0, 1, 1, 'ops:project:list', 'apps', 999, 1, 1, 0, '', '', '2023-03-11 01:47:34', '2023-03-11 02:27:41');
INSERT INTO `sys_menu` VALUES (10006, '项目新增', 10005, 3, NULL, NULL, NULL, 1, 1, 1, 'ops:project:add', NULL, 999, 1, 1, 0, '', '', '2023-03-11 01:48:12', '2023-03-11 01:48:12');
INSERT INTO `sys_menu` VALUES (10007, '项目修改', 10005, 3, NULL, NULL, NULL, 1, 1, 1, 'ops:project:update', NULL, 999, 1, 1, 0, '', '', '2023-03-11 01:48:34', '2023-03-11 01:48:34');
INSERT INTO `sys_menu` VALUES (10008, '项目删除', 10005, 3, NULL, NULL, NULL, 1, 1, 1, 'ops:project:delete', NULL, 999, 1, 1, 0, '', '', '2023-03-11 01:48:51', '2023-03-11 01:48:51');
INSERT INTO `sys_menu` VALUES (10009, '项目导出', 10005, 3, '', '', '', 1, 1, 1, 'ops:project:export', '', 999, 1, 1, 0, '', '', '2023-03-11 01:49:16', '2023-03-11 01:52:02');

INSERT INTO `sys_menu` VALUES (10010, '实例管理', 10004, 2, 'ops-server', 'ops-server', NULL, 0, 1, 1, 'ops:server:list', 'apps', 999, 1, 1, 0, '', '', '2023-03-11 01:47:34', '2023-03-11 02:27:41');
INSERT INTO `sys_menu` VALUES (10011, '实例新增', 10010, 3, NULL, NULL, NULL, 1, 1, 1, 'ops:server:add', NULL, 999, 1, 1, 0, '', '', '2023-03-11 01:48:12', '2023-03-11 01:48:12');
INSERT INTO `sys_menu` VALUES (10012, '实例修改', 10010, 3, NULL, NULL, NULL, 1, 1, 1, 'ops:server:update', NULL, 999, 1, 1, 0, '', '', '2023-03-11 01:48:34', '2023-03-11 01:48:34');
INSERT INTO `sys_menu` VALUES (10013, '实例删除', 10010, 3, NULL, NULL, NULL, 1, 1, 1, 'ops:server:delete', NULL, 999, 1, 1, 0, '', '', '2023-03-11 01:48:51', '2023-03-11 01:48:51');
INSERT INTO `sys_menu` VALUES (10014, '实例导出', 10010, 3, '', '', '', 1, 1, 1, 'ops:server:export', '', 999, 1, 1, 0, '', '', '2023-03-11 01:49:16', '2023-03-11 01:52:02');


INSERT INTO `sys_role` VALUES (1, 'admin', 'admin', '超级管理员', 1, 1, 0, '', '', '2023-03-04 15:45:04', '2023-03-04 15:45:12');
INSERT INTO `sys_role` VALUES (2, 'dev', 'dev', '开发', 1, 1, 0, '', '', '2023-03-04 15:45:26', '2023-03-04 15:45:26');
INSERT INTO `sys_role` VALUES (3, 'test', 'test', '测试', 1, 1, 0, '', '', '2023-03-04 15:45:37', '2023-03-04 15:45:38');
INSERT INTO `sys_role` VALUES (4, 'devops', 'devops', '运维', 1, 1, 0, '', '', '2023-03-04 15:46:02', '2023-03-04 15:46:08');
INSERT INTO `sys_role` VALUES (5, 'pm', 'pm', '产品', 1, 1, 0, '', '', '2023-03-04 15:46:41', '2023-03-04 15:46:41');

INSERT INTO `sys_role_menu` VALUES (1, 2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2, 2);
INSERT INTO `sys_role_menu` VALUES (3, 2, 3);
INSERT INTO `sys_role_menu` VALUES (4, 2, 4);
INSERT INTO `sys_role_menu` VALUES (5, 2, 5);
INSERT INTO `sys_role_menu` VALUES (6, 2, 6);
