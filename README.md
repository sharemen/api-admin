# api-admin
接口管理系统

本项目是以XXL-API v1.1.1 为基础进行二次开发的
原始项目地址：https://github.com/xuxueli/xxl-api 

在原功能基础之上增加了
1. 独立DEV环境的接口根地址
2. 默认添加guest账号，以支持访客模式访问(只可以运行接口测试，添加mock数据)，用于给业务端，客服人员使用
3. 增加API 请求参数备注，响应结果参数备注，依赖资源 属性
4. 增加API修改历史的记录，显示创建人，最后修改人
5. 增加mock url适配，可以通过/mock/run/{API URL}来这直接访问对应API的默认mock数据（可以指定），方便前端的团队进行mock测试统一配置
6. 增加对于Apollo配置中心的接入支持（配置可选）
7. 增加API自动生成wiki文档的支持 Atlassian Confluence,并可以通过改resources/templates/wiki/api.wikitemplate.ftl来调整生成格式（配置可选）
8. 一些用户体验细节上的调整
