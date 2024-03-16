
### **审批流程3.1.1**








当前版本3.1.1
完成主要功能：支持mysql


未来计划：

     1.拓展为图节点， 节点与节点之间充满随机性（可以作为故事章节、支线、主线的引擎工具）

     2.图形画操作，实现良好的人机交互 使用awt开发进行配套支持（闲暇时间过少，上班求生存，作者有心无力）


当前包主要功能：审批工作流程引擎封装

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

yzm.flow.annotation=true/false 
     
     是否启用注解通知，如果启用注解通知，自定义的通知类可以不用实现IProcess接口类，只需要在方法上面添加 @FlowAuditAfter(审核后通知)/ @FlowAuditBefore(审核前通知)/ @FlowAuditEnd(流程审核结束通知)即可
     方法无参，从BusParam上下文中可以取得当前流程审核所有信息     

### 文档地址

语雀:  https://www.yuque.com/weifeng-1btgr/ysxsg0/roo4oe09exd6atqw

### 视频教程
哔哩哔哩: https://b23.tv/3w9nYRc

### 使用方式
  1. 将源码打包，在自己spring-boot项目中引入依赖
    
```
  <dependency>
       <groupId>com.sophony.flow</groupId>
       <artifactId>flow-yzm-starter</artifactId>
       <version>3.1.1</version>
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

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;响应参数说明：

| 字段名         | 字段描述 |
|-------------|------|
| actName     | 流程名称 |
| actNo       | 流程编号 |
| description | 流程描述 |
| id          | 流程id |
| version     | 版本   |

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

##### 任务模板
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;任务模板必须关联流程模板， 一个流程模板能关联多个任务模板， 一个流程模板中的任务模板必须包含 start 编号任务模板和 end 编号任务模板，否则任务流程无法工作

  1. 添加和更新流程任务定义

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actTask/save

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：POST

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：
     
     ![img_2.png](img_2.png)
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求参数说明：


| 字段名         | 字段描述                                  |
|-------------|---------------------------------------|
| backTasks     | array 回退节点模板，                         |
| cond       | 条件 值为 and 或 or 默认为and ，当前节点有多个父节点可以使用 |
| id | 任务id                                  |
| preTaskIds          | array preTaskIds 父节点模板，               |
| processFid     | 流程模板id, 必填                            |
| processfName     | 流程模板名称                                |
| remark     | 任务备注                                  |
| sort     | 任务排序                                  |
| taskName     | 任务名称                                  |
| taskNo     | 任务编号                                  |


  2. 删除流程任务模板节点

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actTask/delete/{id}

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：POST

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：
    ![img_8.png](img_8.png)
  3. 根据流程模板id查询节点

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actTask/list/{actId}

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：GET

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：
    ![img_10.png](img_10.png)
  
  4. 查询详情
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路由：/flow/actTask/{id}

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求方式：GET

     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求示例：
    ![img_9.png](img_9.png)


##### 业务中使用



     在实际业务中，我们的审核同意、审核拒绝走统一的接口，不再走自定义的审核的接口，（特殊情况下可以自己手动控制流程，不走统一审核流程）
     
     IProcess 统一审核时回调，流程开启回到，分为审核前回调、审核后回调、审核结束回调
          --auditAfter （同步调用）审核前回调，默认返回true， 业务类可重写，根据自己的业务来决定当前流程是否可以审核，返回false，当前审核取消
               --入参：ProcessCommonModel
                    --processId:流程Id
                    --taskNode: TaskNode 当前审核的任务节点
                    --businessParams:审核时代入额外参数，可自定义作业
                    --operation：当前审核动作， （审核同意， 审核拒绝， 撤回）
                    --processTemplateId:当前审核流程模板id
          --auditBefore （异步/同步调用）审核后调用， 当前任务节点审核结束后，
               --入参：ProcessCommonModel
                    --processId:流程Id
                    --taskNode: TaskNode 当前审核的任务节点
                    --businessParams:审核时代入额外参数，可自定义作业
                    --operation：当前审核动作， （审核同意， 审核拒绝， 撤回）
                    --finishNode：当前审批完成任务节点
                    --processTemplateId:当前审核流程模板id
          --goEndBack （异步/同步调用）流程结束回调，当前流程结束时的回调
                    --入参：ProcessCommonModel
                         --processId:流程Id
                         --processTemplateId:当前审核流程模板id
          --start(异步/同步调用)流程开启回调
               --入参：ProcessCommonModel
                    --processId:流程Id
                    --processTemplateId:当前审核流程模板id
          当前版本加入了@FLowAsSync注解可放置在以上方法前一行实现异步调用，不加是同步调用。 只对auditBefore和goEndBack以及start有效


&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;构建以下流程:

![img_11.png](img_11.png)


1.创建spring-boot工程

![img_12.png](img_12.png)


2.创建demo ![img_13.png](img_13.png)

```
@Service
public class DemoService implements IProcess{


     @Resource
     IProcessService processService;

     //审核前调用 这里process是ProcessCommonModel
     @Override
     public boolean auditBefore(IProcess process){
     
          XXXXXXXX
     }
     
     
     //审核后调用 这里process是ProcessCommonModel
     @Override
     public boolean auditAfter(IProcess process){
     
          XXXXXXXX
     }
     
     //流程结束调用 这里process是ProcessCommonModel
     //@FLowAsSync 异步调用
     @Override
     public void goEndBack(IProcess process) {
          XXXXXXXX
     }
     
     
     //流程开启回调 这里process是ProcessCommonModel
      @Override
      public void start(IProcess process) {
          XXXXX
      }
      
      
      
      //开启流程任务
      public ResultDTO open() {
        //start("流程编号", 实现IProcess的实现类，并且是放置在spring容器管理的bean)
        String processId = processService.start("test1", this);
        return ResultDTO.success(processId);
    }

}

```
以上是原始方案，第二种方案是使用注解方案

3.注解方案(注解模式不能上面的方案混用，要一开始就决定好使用哪种方案)

3.1 开启流程(使用流程编号和监听器构建流程)

注解模式下，当前监听器由当前框架实例化，不需要托管给spring
![img_14.png](img_14.png)

3.2 监听器实现
```
public class DocumentFlowListener {

    private static Logger log = LoggerFactory.getLogger(DocumentFlowListener.class);

    @Autowired
    private Mapper mapper;

    static Map<String, String> map = Maps.newHashMap();

    @FlowAuditBefore(processTemplateIds = {"document_management"})
    boolean before() {
        Object o = BusParam.getInstance().getMap().get(ParamKey.CONTENTKEY);
        log.info("流程开启上下文参数{}", JSON.toJSON(o));
        //TODO 业务逻辑
        ProcessCommonModel processCommonModel = (ProcessCommonModel) o;

        System.out.println("审核前通知");
        return true;
    }


    @FlowAuditStart
    void start() {
          //TODO 流程开启监听
    
    
    }


    @FlowAuditAfter(processTemplateIds = {"document_management"})
    void after() {
        Object o = BusParam.getInstance().getMap().get(ParamKey.CONTENTKEY);
        log.info("流程开启上下文参数{}", JSON.toJSON(o));
        //TODO 审核后消息提醒，下一个任务节点控制等任务处理
        ProcessCommonModel processCommonModel = (ProcessCommonModel) o;
        JSONObject businessParams = JSON.parseObject(processCommonModel.getBusinessParams());
        //..........
    }


    @FlowAuditEnd(processTemplateIds = {"document_management"})
    void end() {
        Object o = BusParam.getInstance().getMap().get(ParamKey.CONTENTKEY);
        log.info("流程开启上下文参数{}", JSON.toJSON(o));
        //TODO 流程结束任务回调，做数据状态完成更新
        ProcessCommonModel processCommonModel = (ProcessCommonModel) o;
        mapper.finishDocumentFlow(processCommonModel.getProcessId());
    }

}



```

4.审批操作流程图
![img_16.png](img_16.png)




### 其它事项

1. 生产环境移除swagger依赖可以使用以下配置
```

<dependency>
            <groupId>com.sophony.flow</groupId>
            <artifactId>flow-yzm-starter</artifactId>
            <version>3.1.1</version>
            <exclusions>
                <exclusion>
                    <groupId>com.github.xiaoymin</groupId>
                    <artifactId>knife4j-spring-boot-starter</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>io.springfox</groupId>
                    <artifactId>springfox-swagger2</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>io.springfox</groupId>
                    <artifactId>springfox-swagger-ui</artifactId>
                </exclusion>
            </exclusions>
</dependency>
        
```







   



   


    









