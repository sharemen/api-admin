/*
 Navicat Premium Data Transfer

 Source Server         : dev-database
 Source Server Type    : PostgreSQL
 Source Server Version : 90501
 Source Host           : 172.17.36.33:21769
 Source Catalog        : srv_directory
 Source Schema         : apiadmin

 Target Server Type    : PostgreSQL
 Target Server Version : 90501
 File Encoding         : 65001

 Date: 22/04/2020 09:13:55
*/


-- ----------------------------
-- Sequence structure for api_biz_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "apiadmin"."api_biz_id_seq";
CREATE SEQUENCE "apiadmin"."api_biz_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 2;

-- ----------------------------
-- Sequence structure for api_history_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "apiadmin"."api_history_id_seq";
CREATE SEQUENCE "apiadmin"."api_history_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 2;

-- ----------------------------
-- Table structure for api_biz
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_biz";
CREATE TABLE "apiadmin"."api_biz" (
  "id" int8 NOT NULL DEFAULT nextval('api_biz_id_seq'::regclass),
  "biz_name" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_index" int8 DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_biz"."id" IS '主键自增：api_biz_id_seq';
COMMENT ON COLUMN "apiadmin"."api_biz"."biz_name" IS '业务线名称';
COMMENT ON COLUMN "apiadmin"."api_biz"."order_index" IS '排序';

-- ----------------------------
-- Table structure for api_datatype
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_datatype";
CREATE TABLE "apiadmin"."api_datatype" (
  "id" int8 NOT NULL DEFAULT nextval('api_datatype_id_seq'::regclass),
  "name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "about" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "biz_id" int8 DEFAULT NULL,
  "owner" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_datatype"."id" IS '自增主键：api_datatype_id_seq';
COMMENT ON COLUMN "apiadmin"."api_datatype"."name" IS '数据类型名称';
COMMENT ON COLUMN "apiadmin"."api_datatype"."about" IS '数据类型描述';
COMMENT ON COLUMN "apiadmin"."api_datatype"."biz_id" IS '业务线ID，为0表示公共';
COMMENT ON COLUMN "apiadmin"."api_datatype"."owner" IS '负责人';

-- ----------------------------
-- Table structure for api_datatype_fileds
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_datatype_fileds";
CREATE TABLE "apiadmin"."api_datatype_fileds" (
  "id" int8 NOT NULL DEFAULT nextval('api_datatype_fileds_id_seq'::regclass),
  "parent_datatype_id" int8 DEFAULT NULL,
  "field_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "field_about" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "field_datatype_id" int8 DEFAULT NULL,
  "field_type" int4 DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_datatype_fileds"."id" IS '自增主键：api_datatype_id_seq';
COMMENT ON COLUMN "apiadmin"."api_datatype_fileds"."parent_datatype_id" IS '所属，数据类型ID';
COMMENT ON COLUMN "apiadmin"."api_datatype_fileds"."field_name" IS '字段名称';
COMMENT ON COLUMN "apiadmin"."api_datatype_fileds"."field_about" IS '字段描述';
COMMENT ON COLUMN "apiadmin"."api_datatype_fileds"."field_datatype_id" IS '字段数据类型ID';
COMMENT ON COLUMN "apiadmin"."api_datatype_fileds"."field_type" IS '字段形式：0=默认、1=数组';

-- ----------------------------
-- Table structure for api_document
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_document";
CREATE TABLE "apiadmin"."api_document" (
  "id" int8 NOT NULL DEFAULT nextval('api_document_id_seq'::regclass),
  "project_id" int8 DEFAULT NULL,
  "group_id" int8 DEFAULT NULL,
  "name" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "status" int4 DEFAULT 0,
  "star_level" int4 DEFAULT NULL,
  "request_url" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "request_method" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "request_headers" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "query_params" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "response_params" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "response_datatype_id" int8 DEFAULT 0,
  "success_resp_type" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "success_resp_example" varchar COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "fail_resp_type" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "fail_resp_example" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "remark" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "add_time" timestamp(6) NOT NULL DEFAULT NULL,
  "update_time" timestamp(6) NOT NULL DEFAULT NULL,
  "request_example" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "need_resources" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "wiki_id" int8 DEFAULT NULL,
  "response_remark" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "creat_user" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL,
  "update_user" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL,
  "request_remark" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_document"."id" IS '自增主键：api_document_id_seq';
COMMENT ON COLUMN "apiadmin"."api_document"."project_id" IS '项目ID';
COMMENT ON COLUMN "apiadmin"."api_document"."group_id" IS '分组ID';
COMMENT ON COLUMN "apiadmin"."api_document"."name" IS '接口名称';
COMMENT ON COLUMN "apiadmin"."api_document"."status" IS '状态：0-启用、1-维护、2-废弃';
COMMENT ON COLUMN "apiadmin"."api_document"."star_level" IS '星标等级：0-普通接口、1-一星接口';
COMMENT ON COLUMN "apiadmin"."api_document"."request_url" IS 'Request URL：相对地址';
COMMENT ON COLUMN "apiadmin"."api_document"."request_method" IS 'Request Method：如POST、GET';
COMMENT ON COLUMN "apiadmin"."api_document"."request_headers" IS 'Request Headers：Map-JSON格式字符串';
COMMENT ON COLUMN "apiadmin"."api_document"."query_params" IS 'Query String Parameters：VO-JSON格式字符串';
COMMENT ON COLUMN "apiadmin"."api_document"."response_params" IS 'Response Parameters：VO-JSON格式字符串';
COMMENT ON COLUMN "apiadmin"."api_document"."response_datatype_id" IS '响应数据类型ID';
COMMENT ON COLUMN "apiadmin"."api_document"."success_resp_type" IS 'Response Content-type：成功接口，如JSON、XML、HTML、TEXT、JSONP';
COMMENT ON COLUMN "apiadmin"."api_document"."success_resp_example" IS 'Response Content：成功接口';
COMMENT ON COLUMN "apiadmin"."api_document"."fail_resp_type" IS 'Response Content-type：失败接口';
COMMENT ON COLUMN "apiadmin"."api_document"."fail_resp_example" IS 'Response Content：失败接口';
COMMENT ON COLUMN "apiadmin"."api_document"."remark" IS '备注';
COMMENT ON COLUMN "apiadmin"."api_document"."add_time" IS '创建时间';
COMMENT ON COLUMN "apiadmin"."api_document"."update_time" IS '更新时间';
COMMENT ON COLUMN "apiadmin"."api_document"."request_example" IS '请求示例';
COMMENT ON COLUMN "apiadmin"."api_document"."need_resources" IS '依赖资源：VO-JSON格式字符串';
COMMENT ON COLUMN "apiadmin"."api_document"."wiki_id" IS '对应在wiki系统中该接口doc对应的页面id';
COMMENT ON COLUMN "apiadmin"."api_document"."response_remark" IS '响应备注：描述json格式的细节等';
COMMENT ON COLUMN "apiadmin"."api_document"."creat_user" IS '创建者';
COMMENT ON COLUMN "apiadmin"."api_document"."update_user" IS '最后修改人';
COMMENT ON COLUMN "apiadmin"."api_document"."request_remark" IS '参数备注：描述json格式的细节等';

-- ----------------------------
-- Table structure for api_group
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_group";
CREATE TABLE "apiadmin"."api_group" (
  "id" int8 NOT NULL DEFAULT nextval('api_group_id_seq'::regclass),
  "project_id" int8 DEFAULT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "order_index" int8 DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_group"."id" IS '自增主键：api_group_id_seq';
COMMENT ON COLUMN "apiadmin"."api_group"."project_id" IS '所属，项目ID';
COMMENT ON COLUMN "apiadmin"."api_group"."name" IS '分组名称';
COMMENT ON COLUMN "apiadmin"."api_group"."order_index" IS '分组排序';

-- ----------------------------
-- Table structure for api_history
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_history";
CREATE TABLE "apiadmin"."api_history" (
  "id" int8 NOT NULL DEFAULT nextval('api_history_id_seq'::regclass),
  "oper_user" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "operate" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "time" timestamp(6) NOT NULL DEFAULT NULL::timestamp without time zone,
  "document_id" int8 NOT NULL DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_history"."id" IS '主键：api_history_id_seq';
COMMENT ON COLUMN "apiadmin"."api_history"."oper_user" IS '操作人';
COMMENT ON COLUMN "apiadmin"."api_history"."operate" IS '操作';
COMMENT ON COLUMN "apiadmin"."api_history"."time" IS '操作时间';
COMMENT ON COLUMN "apiadmin"."api_history"."document_id" IS 'api id';

-- ----------------------------
-- Table structure for api_mock
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_mock";
CREATE TABLE "apiadmin"."api_mock" (
  "id" int8 NOT NULL DEFAULT nextval('api_mock_id_seq'::regclass),
  "document_id" int8 DEFAULT NULL,
  "uuid" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "resp_type" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "resp_example" varchar(26000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "req_uri" varchar(2000) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "isdefault" int2 DEFAULT NULL,
  "creat_time" timestamp(6) DEFAULT NULL,
  "update_time" timestamp(6) DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_mock"."id" IS '自增主键：api_mock_id_seq';
COMMENT ON COLUMN "apiadmin"."api_mock"."document_id" IS '所属，项目ID';
COMMENT ON COLUMN "apiadmin"."api_mock"."uuid" IS '分组名称';
COMMENT ON COLUMN "apiadmin"."api_mock"."resp_type" IS '分组排序';
COMMENT ON COLUMN "apiadmin"."api_mock"."resp_example" IS '分组排序';
COMMENT ON COLUMN "apiadmin"."api_mock"."req_uri" IS 'mock对应的api uri';
COMMENT ON COLUMN "apiadmin"."api_mock"."isdefault" IS '是否为默认：0否 ,1是';
COMMENT ON COLUMN "apiadmin"."api_mock"."creat_time" IS '创建时间';
COMMENT ON COLUMN "apiadmin"."api_mock"."update_time" IS '最后修改时间';

-- ----------------------------
-- Table structure for api_project
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_project";
CREATE TABLE "apiadmin"."api_project" (
  "id" int8 NOT NULL DEFAULT nextval('api_project_id_seq'::regclass),
  "name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "description" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "base_url_product" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "base_url_ppe" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "base_url_qa" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "biz_id" int8 DEFAULT NULL,
  "base_url_dev" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_project"."id" IS '自增主键：api_project_id_seq';
COMMENT ON COLUMN "apiadmin"."api_project"."name" IS '项目名称';
COMMENT ON COLUMN "apiadmin"."api_project"."description" IS '项目描述';
COMMENT ON COLUMN "apiadmin"."api_project"."base_url_product" IS '根地址：线上环境';
COMMENT ON COLUMN "apiadmin"."api_project"."base_url_ppe" IS '根地址：预发布环境';
COMMENT ON COLUMN "apiadmin"."api_project"."base_url_qa" IS '根地址：测试环境';
COMMENT ON COLUMN "apiadmin"."api_project"."biz_id" IS '业务线ID';
COMMENT ON COLUMN "apiadmin"."api_project"."base_url_dev" IS '根地址：开发环境';

-- ----------------------------
-- Table structure for api_test_history
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_test_history";
CREATE TABLE "apiadmin"."api_test_history" (
  "id" int8 NOT NULL DEFAULT nextval('api_test_history_id_seq'::regclass),
  "document_id" int8 DEFAULT NULL,
  "request_url" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "request_method" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "request_headers" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "query_params" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "resp_type" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "add_time" timestamp(6) DEFAULT NULL,
  "update_time" timestamp(6) DEFAULT NULL
)
;
COMMENT ON COLUMN "apiadmin"."api_test_history"."id" IS '自增主键：api_test_history_id_seq';
COMMENT ON COLUMN "apiadmin"."api_test_history"."document_id" IS '接口ID';
COMMENT ON COLUMN "apiadmin"."api_test_history"."request_url" IS 'Request URL：绝对地址';
COMMENT ON COLUMN "apiadmin"."api_test_history"."request_method" IS 'Request Method：如POST、GET';
COMMENT ON COLUMN "apiadmin"."api_test_history"."request_headers" IS 'Request Headers：Map-JSON格式字符串';
COMMENT ON COLUMN "apiadmin"."api_test_history"."query_params" IS 'Query String Parameters：VO-JSON格式字符串';
COMMENT ON COLUMN "apiadmin"."api_test_history"."resp_type" IS 'Response Content-type：如JSON';
COMMENT ON COLUMN "apiadmin"."api_test_history"."add_time" IS '创建时间';
COMMENT ON COLUMN "apiadmin"."api_test_history"."update_time" IS '更新时间';

-- ----------------------------
-- Table structure for api_user
-- ----------------------------
DROP TABLE IF EXISTS "apiadmin"."api_user";
CREATE TABLE "apiadmin"."api_user" (
  "id" int8 NOT NULL DEFAULT nextval('api_user_id_seq'::regclass),
  "username" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "password" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "type" int4 DEFAULT NULL,
  "permission_biz" varchar(4000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "apiadmin"."api_user"."id" IS '自增主键：api_user_id_seq';
COMMENT ON COLUMN "apiadmin"."api_user"."username" IS '账号';
COMMENT ON COLUMN "apiadmin"."api_user"."password" IS '密码';
COMMENT ON COLUMN "apiadmin"."api_user"."type" IS '用户类型：0-普通用户、1-超级管理员';
COMMENT ON COLUMN "apiadmin"."api_user"."permission_biz" IS '业务线权限，多个逗号分隔';

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
SELECT setval('"apiadmin"."api_biz_id_seq"', 9, true);
SELECT setval('"apiadmin"."api_history_id_seq"', 37, true);

-- ----------------------------
-- Primary Key structure for table api_biz
-- ----------------------------
ALTER TABLE "apiadmin"."api_biz" ADD CONSTRAINT "api_biz_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_datatype
-- ----------------------------
ALTER TABLE "apiadmin"."api_datatype" ADD CONSTRAINT "api_datatype_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_datatype_fileds
-- ----------------------------
ALTER TABLE "apiadmin"."api_datatype_fileds" ADD CONSTRAINT "api_datatype_fileds_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_document
-- ----------------------------
ALTER TABLE "apiadmin"."api_document" ADD CONSTRAINT "api_document_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_group
-- ----------------------------
ALTER TABLE "apiadmin"."api_group" ADD CONSTRAINT "api_group_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_history
-- ----------------------------
ALTER TABLE "apiadmin"."api_history" ADD CONSTRAINT "api_history_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_mock
-- ----------------------------
ALTER TABLE "apiadmin"."api_mock" ADD CONSTRAINT "api_mock_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_project
-- ----------------------------
ALTER TABLE "apiadmin"."api_project" ADD CONSTRAINT "api_project_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_test_history
-- ----------------------------
ALTER TABLE "apiadmin"."api_test_history" ADD CONSTRAINT "api_test_history_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table api_user
-- ----------------------------
ALTER TABLE "apiadmin"."api_user" ADD CONSTRAINT "api_user_pkey" PRIMARY KEY ("id");
