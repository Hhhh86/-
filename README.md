# 知文平台
毕设项目。用于大学生的知识的分享交流，在上面你可以针对不同的学科发表不同的知识博客。
技术栈：
 - SpringBoot搭建项目基本模块，Mybatis与数据库交互。

 - 通过定时统计将文章数据缓存到Redis，减少对MySQL的压力。

 - 采用ElasticSearch搜索引擎，针对一些专业配置了IK分词器，实现用户关键字搜索高亮显示。

 - 采用ActiveMQ的点对点模型，编写生产者和消费者对用户评论采取推送并发送邮件通知。
 
 - 引入第三方QQ登录，用户可以直接通过QQ登录到平台后台分享页面。（未完成）

   
