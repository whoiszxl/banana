-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 初始化server表结构
CREATE TABLE IF NOT EXISTS `ops_server`(
    `id`                        int(7) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `admin_id`                  int(7) NOT NULL DEFAULT 0 COMMENT '管理员ID',
    `project_id`                int(7) NOT NULL DEFAULT 0 COMMENT '项目ID',
    `server_name`               varchar(32) NOT NULL COMMENT '服务器名称',
    `server_outer_ip`           varchar(256) NOT NULL COMMENT '服务器外网IP',
    `server_inner_ip`           varchar(256) NOT NULL COMMENT '服务器内网IP',
    `server_port`               varchar(256) NOT NULL COMMENT '服务器端口',
    `server_username`           varchar(256) NOT NULL COMMENT '服务器用户名',
    `server_password`           varchar(256) NOT NULL COMMENT '服务器密码',
    `platform_type`             tinyint(2) NOT NULL COMMENT '服务器类型: 1-自建 2-阿里云 3-腾讯云',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `updated_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '服务器表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `ops_project`(
    `id`                        int(7) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      varchar(256) NOT NULL COMMENT '项目名称',
    `description`               varchar(256) NOT NULL COMMENT '项目描述',
    `markdown`                  text NOT NULL COMMENT '服务器外网IP',
    `platform_type`             tinyint(2) NOT NULL COMMENT '服务器类型: 1-自建 2-阿里云 3-腾讯云',
    `platform_params`           json NOT NULL COMMENT '生成服务器平台的参数(json格式)',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `updated_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '项目表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `ops_software`(
    `id`                        int(7) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `software_name`             varchar(32) NOT NULL COMMENT '组件名',
    `software_filename`         varchar(256) NOT NULL COMMENT '组件文件名',
    `software_path`             varchar(256) NOT NULL COMMENT '组件文件路径',
    `install_path`              varchar(256) NOT NULL COMMENT '组件安装路径',
    `env_path`                  varchar(256) NOT NULL COMMENT '环境变量路径',
    `env_content`               varchar(256) NOT NULL COMMENT '环境变量内容',
    `install_script_path`       varchar(256) NOT NULL COMMENT '安装脚本路径',
    `start_script_path`         varchar(256) NOT NULL COMMENT '启动脚本路径',
    `status_script_path`        varchar(256) NOT NULL COMMENT '状态脚本路径',
    `version`                   bigint(20) UNSIGNED NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `updated_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '基础组件表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `ops_software_config`(
    `id`                        int(7) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `software_id`               int(7) NOT NULL COMMENT '主键',
    `software_name`             varchar(32) NOT NULL COMMENT '组件名',
    `config_name`               varchar(256) NOT NULL COMMENT '配置名',
    `config_path`               varchar(256) NOT NULL COMMENT '配置路径',
    `config_template`           text NOT NULL COMMENT '配置模板',
    `config_template_params`    json DEFAULT NULL COMMENT '配置模板参数',
    `version`                   bigint(20) UNSIGNED NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `updated_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '基础组件配置表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `ops_script`(
    `id`                        int(7) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `script_title`              varchar(64) NOT NULL COMMENT '脚本标题',
    `script_name`               varchar(128) NOT NULL COMMENT '脚本名称',
    `script_path`               varchar(255) NOT NULL COMMENT '脚本路径',
    `script_content`            text NOT NULL COMMENT '脚本内容',
    `description`               varchar(255) NOT NULL COMMENT '脚本描述',
    `version`                   bigint(20) UNSIGNED NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `updated_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = 'SH脚本表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `ops_base_config`(
    `id`                        int(7) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `config_key`                varchar(64) NOT NULL COMMENT '组件名',
    `config_value`              varchar(1024) NOT NULL COMMENT '组件文件名',
    `version`                   bigint(20) UNSIGNED NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `updated_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '基础配置表' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `ops_deploy`(
    `id`                        int(7) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `admin_id`                  int(7) NOT NULL COMMENT '管理员ID',
    `deploy_name`               varchar(64) NOT NULL COMMENT '部署名称',
    `description`               varchar(256) NOT NULL COMMENT '部署描述',
    `software_id`               int(7) NOT NULL COMMENT '部署组件ID',
    `server_ids`                varchar(1024) NOT NULL COMMENT '部署服务实例ID集合',
    `deploy_logs`               text NULL DEFAULT NULL COMMENT '部署日志',
    `init_status`               tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态: 0-未初始化 1-已初始化',
    `version`                   bigint(20) UNSIGNED NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态: 1-已创建 2-部署成功 3-部署失败 4-正在部署中',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `updated_by`                varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '服务部署表' ROW_FORMAT = Dynamic;