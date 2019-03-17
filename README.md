# blogs
一个简易的博客系统<br/>
**作者**：Jump-Boy (hi_jumpboy@163.com)

## 项目简介
该项目是我之前学习的一个项目，一个较为完整的博客系统，具备了常见的博客相关功能，初衷就是为了解决日常的学习记录与总结。<br/>
该博客系统大概包含了以下功能：用户的登录与注册、系统的权限管理、发表编辑博客、博客查阅、博客删除、博客评论、博客点赞、个人信息页展示、
个人信息的修改、首页信息显示如热门标签、热门用户、热门博客、新发表博客以及所有博客列表，同时会做阅读量、评论量以及点赞量的统计，
还支持全文搜索，可以快速查找出想要的内容。当然还为管理员提供了后台管理系统，用来管理与维护博客系统，如用户管理，博客管理等。<br/>
项目技术：<br/>
前端：Thymeleaf模板引擎、Bootstrap以及jQuery；<br/>
后端：SpringBoot做基础框架、SpringSecurity做授权与认证、SpringData做数据访问层控制，全文检索使用Elasticsearch、数据库使用MySQL。
当然为了便于测试，也可以继承H2作为测试数据库。项目中的图片上传及图片存储展示使用的一款开源的文件服务器，该文件服务器是基于MongoDB
作为文件信息存储，采用SpringBoot完成项目搭建。<br/>
项目管理（打包工具）：Gradle。<br/>
整个项目目前搭建在阿里云上，服务器主机使用 ECS ，项目容器即服务器采用Tomcat。

* 项目还是挂在阿里云上，博客系统链接奉上（后台管理就不放了）<br/>
[点击访问项目网站](http://www.humh.cn/blogs) <br/>
登录账号：*test*  密码：*test*  当然也可自行注册。

* 项目部署<br/>
可以clone项目后，直接IDE中运行，或者Gradle中依赖了SpringBoot插件，直接bootRun即可。还可以运行打包后生成的jar包，因为SpringBoot
内置了容器。当然，也可以打成war包，部署到外置容器中去，如Tomcat。

**项目设计以及实现上的几个点** ：<br/>
1、项目使用了Thymeleaf模板引擎，来做前端页面的渲染，替代以往的JSP，同时SpringBoot也对Thymeleaf有很好的集成。页面部分还使用
了BootStrap来完成响应式的布局，它提供了很多的样式供使用。另外结合JQuery完成功能控制等等。<br/>
2、后端部分，整个项目是采用SpringBoot做为基础框架，它省去了原先Spring额外的繁琐配置，达到开箱即用的效果。当然，如果需要一些
定制化需求，SpringBoot也支持。<br/>
3、数据库使用的MySQL，当然我们在测试时可以使用H2这种内存数据库，对于测试来说是很方便的，只需要在SpringBoot中做几个简单配置。
对于数据访问操作的编码上，使用Spring Data JPA来代替以往集成MyBatis的方式，Spring Data JPA实现了JPA规范，使得我们在进行数据层
操作时更加简单，这是JPA的特性，通过完全的ORM映射，我们甚至可以不用写任何一句SQL就可以实现。（默认使用了Hibernate）<br/>
4、对于首页的热点信息及关键词搜索功能，使用了全文检索技术，这里使用的Elasticsearch（也可以使用solr），编码上直接使用Spring 
Data es，与JPA一样，相比直接使用es的api来说是非常的方便。<br/>
4、博客系统中如博客编辑或者用户个人信息中头像设置都涉及到了图片上传，这里我们使用了一款开源的文件服务器
（[GitHub地址](https://github.com/waylau/mongodb-file-server)），用来存储我们图片以及图片访问，它还提供了文件数据管理的可视化界面。
这款文件服务器是基于MongoDB + SpringBoot实现的，对于文件信息，它转做一个File文档对象BSON格式（MongoDB非关系型数据库的特性）进行存储。
因为博客系统中需要上传的图片都是小文件，仅几M，所以使用这款文件服务器可行，如果想要存储超过16M的文件可以使用其他文件服务器，如GridFS、
FastDFS等等。<br/>
5、对于权限的控制以及用户认证登录等功能，使用了SpringSecurity这个安全框架，它负责认证与授权，认证如用户登录，授权如对于权限操作
（博客编辑、点赞、评论等等）需要进行登录，后台管理仅允许admin。SpringSecurity有一些核心类，我们只需要按照规范通过编码或者配置文件
去进行权限分配、认证、会话管理以及安全控制如CSRF等等。它也提供了很方便的注解，可以对我们Controller进行细致的权限把控。同时我们在Thymeleaf中也引入了
Security-thymeleaf，在页面编码上我们也可以进行一些侵入式的API操作。<br/>
6、对于注册或者博客内容上，除过数据库中实体字段类型以及索引限制（在实体类字段上加上Hibernate注解控制）还在页面上以及实体上添加了校验，
如实体上引入了一些Bean校验的注解（@Size、@Email等等），保证了后台代码的安全。
7、博客编辑集成了markdown的插件来完成博客内容的编写。

## 项目展示
![后台管理](https://github.com/Jump-Boy/blogs/blob/master/illustration/后台管理.png) <br/>
![新增用户](https://github.com/Jump-Boy/blogs/blob/master/illustration/新增用户.png) <br/>
![首页](https://github.com/Jump-Boy/blogs/blob/master/illustration/首页.png) <br/>
![个人主页](https://github.com/Jump-Boy/blogs/blob/master/illustration/个人主页.png) <br/>
![博客点赞评论](https://github.com/Jump-Boy/blogs/blob/master/illustration/博客点赞评论.png) <br/>
![博客编辑](https://github.com/Jump-Boy/blogs/blob/master/illustration/博客编辑.png) <br/>
![个人设置](https://github.com/Jump-Boy/blogs/blob/master/illustration/个人设置.png) <br/>
![博客详情](https://github.com/Jump-Boy/blogs/blob/master/illustration/博客详情.png) <br/>
![注册失败](https://github.com/Jump-Boy/blogs/blob/master/illustration/注册失败.png) <br/>

## 项目详述

* 模块设计<br/>
下图为整个博客系统的模块设计图：<br/>
 ![博客系统模块](https://github.com/Jump-Boy/blogs/blob/master/illustration/博客系统模块.png)
 
* 版本参数<br/>

|技术|版本号及相关信息|
|:---|:---|
|JDK|1.8|
|Gradle|3.5|
|Spring Boot|1.5.2. RELEASE|
|Thymeleaf|3.0.3. RELEASE|
|Thymeleaf Layout Dialec|2.2.0|
|MySQL Community Server|5.7.17|
|MySQL Workbench|6.3.9|
|Spring Data JPA|1.11.1. RELEASE|
|Hibernate|5.2.8. Final|
|MySQL Connector|J6.0.5|
|H2 Database|1.4.193|
|Elasticsearch|2.4.4|
|Spring Data Elasticsearch|2.1.3. RELEASE|
|JNA|4.3.0|
|Tether1.4.0|http://tether.io/|
|Bootstrapv4.0.0-alpha.6|https://v4-alpha.getbootstrap.com/|
|jquEry3.1.1|http://jquery.com/download/|
|FontAwesome4.7.0|http://fontawesome.io|
|NprogRess0.2.0|http://ricostacruz.com/nprogress/|
|Thinker-md|http://git.oschina.net/benhail/thinker-md|
|jquEryTagsInput1.3.6|http://xoxco.com/projects/code/tagsinput/|
|BootstrapChosen1.0.3|https://github.com/haubek/bootstrap4c-chosen|
|toast2.1.1|http:/www.toastrjs.com/|
|Spring Security|4.2.2. RELEASE|
|Thymeleaf Spring Security|3.0. 2. RELEASE|
|Apache Commons Lang|3.5|
|Markdown parser for the JVM|0.16|
|Mongo DB|3.4.4|
|Embedded MongoDB|2.0.0|


