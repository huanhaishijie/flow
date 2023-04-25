
### **审批流程3.1.0**








当前版本3.1.0
完成主要功能：增加同步调用功能


当前包主要功能：审批工作流程封装

配置属性详细信息如下：

yzm.flow.joinuser=true/false 是否加入业务系统的用户操作信息

    默认为false,开启后需要实现接口com.sophony.flow.worker.base.FlowUserInfo,
    获取业务系统用户信息，之后流程数据操作记录就会出现用户的详细





yzm.flow.rolevalid=true/false 是否加入业务系统的角色权限校验

    默认为false,开启后需要实现com.sophony.flow.worker.base.PermissionValid接口,开启用户对审核节点的权限校验，
    这里是把当前流程所有可审核的节点传输到业务系统，有业务系统根据TaskNode中taskTemplateId任务节点模板id对用户角色的权限
    进行过滤。然后返回将过滤好的TaskNode返回回去
    （用户可能有多个可审核的节点， 但一次只能审核一个节点，所以尽量不要动传输到业务系统TaskNode顺序）

yzm.flow.sqlType=mysql/postgresql 数据库类型 可选 默认postgresql

    数据库类型，做到尽可能方便开发人员，项目启动会自动刷入当前包应用的数据和表

yzm.flow.openCache=true/false 是否开启缓存

    默认开启缓存，审批流程需要大量查表，开启之后减少io开销

yzm.flow.cacheType=redis/H2 缓存类型

    默认的缓存是H2，放在本地磁盘中，也可以使用redis


### 使用方式
  1. 将源码打包，在自己spring-boot项目中引入依赖
    
```
  <dependency>
       <groupId>com.sophony.flow</groupId>
       <artifactId>flow-yzm-starter</artifactId>
       <version>3.0.0</version>
   </dependency>
```
   

   	将流程依赖包引入，然后在项目的application.yml中按照上面配置介绍结合自己所需配置
##### 流程模板


   


  1. 创建或更新流程模板

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：${host}:${port}/**/flow/actProcdef/save,其中的/**可以没有，是根据项目实际配置来的，

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;参数说明：

| 字段名 | 字段描述 |
|-----|------|
|   actName  | 流程名称 |
|  actNo   | 流程编号 |
|  description   | 流程描述 |
|   id  | 流程id |
   
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：POST

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：

   ![img.png](img.png)
  2. 复制流程模板

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actProcdef/copy

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：GET

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：

   ![img_1.png](img_1.png)

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;入参的id是已经生成流程模板id，如果流程模板已经有流程任务模板，本次任务会连同复制

  3. 删除流程模板
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actProcdef/delete

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：GET

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：

  ![img_3.png](img_3.png)
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;根据流程模板id删除流程模板

  4. 获取流程模板详情
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actProcdef/delete

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：GET

   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：
   ![img_5.png](img_5.png)

  5. 获取所有流程
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actProcdef/list

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：GET

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：
     ![img_6.png](img_6.png)

  6. 激活/冻结流程模板
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actProcdef/updateState

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：GET

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：
  
     ![img_7.png](img_7.png)
     在实际业务中只有开启流程时，只有激活的模板才能使用。 模板编号可以重复，但是相同编号模板只能激活一个



   



   


    









