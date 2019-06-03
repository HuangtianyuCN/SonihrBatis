<!-- TOC -->

- [框架复习（三）：不如写个MyBatis？](#框架复习三不如写个mybatis)
    - [项目来源](#项目来源)
    - [MyBatis框架概论](#mybatis框架概论)
        - [JDBC如何演化到MyBatis](#jdbc如何演化到mybatis)
            - [问题与解决思路](#问题与解决思路)
            - [Mybatis功能概述](#mybatis功能概述)
    - [MyBatis流程概述](#mybatis流程概述)
    - [快速实现一个MyBatis核心功能](#快速实现一个mybatis核心功能)
    - [getMapper流程](#getmapper流程)
        - [getMapper主要流程](#getmapper主要流程)
        - [可配置的数据源](#可配置的数据源)
        - [](#)
    - [四大组件](#四大组件)

<!-- /TOC -->

# 框架复习（三）：不如写个MyBatis？
## 项目来源
https://github.com/HuangtianyuCN/SonihrBatis
## MyBatis框架概论
### JDBC如何演化到MyBatis
- JDBC查询分析
    - 加载JDBC驱动
    - 建立并获取数据库连接
    - 创建JDBC statement对象
    - sql语句传入参数
    - 执行sql语句并获得查询结果
    - 对查询结果进行转换处理并将结果返回
    - 释放资源，关闭resultset，statement，connection。
- 存在哪些问题？
#### 问题与解决思路
- 连接的获取和释放
1. 由上可见，JDBC每次查询或其他数据库操作都需要频繁的开启和关闭。要知道，每次连接数据库相当于一次socket通信，因此我们可以通过数据库连接池来解决，即复用数据库连接，当调用connection.close()时，并不关闭连接，而是放回连接池中等待复用，这边和线程池是类似的。本项目采用**Druid连接池，QuickStart中会提及。**
2. 连接池多种多样，可能采用DBCP，也可能用Druid，因此需要通过**DataSource进行解耦**，我们同一从DataSource中获取数据库连接，至于DataSource中配置的是什么连接池实现，用户不必关心。**实现了ORM框架与连接池的解耦**
- SQL统一存储
3. 在不同的类中进行数据库操作时，不同的类中都散落着SQL语句。我们希望sql语句和具体的java类之间进行解耦，即sql语句单独放在xml配置文件中，或者单独放在某一个java类中。**这一步要求我们思考，如何从配置文件中读SQL，Dom4J在一旁摩拳擦掌**
- 传入参数映射和动态sql
4. 即ORM，对象关系映射。传入sql语句中的参数可以是一个对象，sql语句的返回值也可以包装成一个对象。
5. 所谓动态sql，比如select * from t_dept where name = ? and age = ?，有时候用户只想查询name = huang，age随意的，或者age=10，name随意的。那你怎么解决呢？方法1.在添加两个方法，select语句中只有name和age，但是不够优雅，如果你的表有20列，那你排列组合得多少种情况？方法2.如果只有name或只有age的话，就自动生成只有age和name的语句。怎么实现了呢？通过xml标签，\<if\>等。
- 结果映射和结果缓存
6. JDBC的resultSet也太麻烦了，所以要把这里封装起来。返回的除了Boolean外，查询结果还可能是Bean，List，Map。这些值都需要两点**1.返回什么类型。2.需要返回的数据结构和执行结果的映射，即ResultSet和结果的转换。**
7. 可以考虑将结果缓存以提升性能。**key值是sql语句和传入参数联合，如果遇到sql和参数相同的，即返回缓存值。**
. 解决sql语句重复问题
8. 可以将重复的sql块独立出来，在其他sql块中引用它。
- 逆向工程和插件
9. 手写sql语句还是太多了，对于单表而言是否可以自动创建sql语句。
10. 对于sql语句生成的前后，可否用拦截器实现插件。

#### Mybatis功能概述
1. Mapper接口。mapper.xml对应一个mapper接口，\<mapper\>节点中的\<select\>等标签的id对应接口方法，标签内内容对应sql语句。Mybatis通过SqlSession.get(XXXMapper.class)，通过动态代理生成一个
mapper实例，将connection和statement写sql，获取resultSet等操作织入其中，最后返回list，map，bean或者boolean。**当然，其实MyBatis底层有自己已经封装了一层的select，update等方法。**
![](https://upload-images.jianshu.io/upload_images/2062729-e41b12da7b1b27cd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1000/format/webp =70%x)
2. 数据处理层。主要用处是**参数映射，动态sql生成**。着重说一下参数映射，包括查询阶段将javaBean转化成JDBC类型数据和将查询结果resultset的jdbcType转化成javaBean。
3. 架构支撑层。1.事务管理机制。2.连接池机制。3.缓存机制。
4. 引导层。核心组件：
    - SqlSession，顶层API，交给用户，表示一个和数据库的会话，通过其完成增删查改操作。
    - Executor，MyBaits调度核心，负责SQL语句的生成和查询缓存的维护
    - StatementHandler，封装JDBC Statement操作，负责对JDBC statement设置参数
    - ParamterHandler，负责将用户传递的参数转换成JDBC Statement所需要的参数
    - ResultSetHandler，将JDBC返回的ResultSet结果集对象转换成list类型的集合
    - TypeHandler，负责java数据类型和jdbc数据类型之间的映射和转换
    - MappedStatement，其中维护者select|update|delete|insert节点的封装
    - SqlSource，根据用户传递的parameterObject动态生成SQL语句并封装到BoundSql中
    - BoundSql，动态生成的SQL语句以及相应参数信息
    - Configuration，类似于ApllicationContext，读取配置文件并保存为一个配置类
![](https://upload-images.jianshu.io/upload_images/2062729-a2f20529d6d908a5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/905/format/webp =80%x)

## MyBatis流程概述
1. MyBatis流程图
![](http://img.sonihr.com/e1961348-fb37-4652-ad4d-7609b6f5a82a.jpg)

    下面将结合代码具体分析。

2. MyBatis具体代码分析
- SqlSessionFactoryBuilder根据XML文件流，或者Configuration类实例build出一个SqlSessionFactory。
- SqlSessionFactory.openSession()相当于从连接池中获取了一个connection,创建Executor实例，创建事务实例。

    ```java
    DefaultSqlSessionFactory.class

    private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
        Transaction tx = null;

        DefaultSqlSession var8;
        try {
            Environment environment = this.configuration.getEnvironment();
            TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);
            tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
            Executor executor = this.configuration.newExecutor(tx, execType);
            var8 = new DefaultSqlSession(this.configuration, executor, autoCommit);
        } catch (Exception var12) {
            this.closeTransaction(tx);
            throw ExceptionFactory.wrapException("Error opening session.  Cause: " + var12, var12);
        } finally {
            ErrorContext.instance().reset();
        }

        return var8;
    }
    ```
- 此时我们只是获得一条connection，session.getMapper(XxxMapper.class)时才进行创建代理实例的过程，后面会介绍。SqlSession.getMapper实际上托付给Configuration去做。

    ```java
    public <T> T getMapper(Class<T> type) {
        return this.configuration.getMapper(type, this);
    }
    ```
    
    Configuration交给自己的成员变量mapperRegistry去做。这个成员变量是Map再封装之后的，持有configuration实例和Map<Class<?>, MapperProxyFactory<?>> knownMappers，正如xml文件中写的那样，每个mappee.xml中都有一个namespace，这个namespace就是Class<?>,而后者是对这个接口进行代理的工厂MapperProxyFactory实例，其中封装了被代理接口和缓存。这个knownMappers应该是初始化configuration的时候就已经处理完毕的。

    ```java
    MapperRegistry.class
    
    private final Configuration config;
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap();
    ```

    类似于Spring中的getBean方法，MyBatis中用getMapper的方式进行创建。下面代码可以看出，先根据class类型获取代理类工厂，去工厂中newInstance。注意这里是没有Spring中的单例多例的，只要你getMapper，框架就会给你newInstance一个全新的被代理实例。

    ```java
    MapperRegistry.class
    
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory)this.knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        } else {
            try {
                return mapperProxyFactory.newInstance(sqlSession);
            } catch (Exception var5) {
                throw new BindingException("Error getting mapper instance. Cause: " + var5, var5);
            }
        }
    }
    ```

    newInstance()中做了什么呢？

    ```java
    MapperProxyFactory.class
    
    protected T newInstance(MapperProxy<T> mapperProxy) {
        return Proxy.newProxyInstance(this.mapperInterface.getClassLoader(), new Class[]{this.mapperInterface}, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy(sqlSession, this.mapperInterface, this.methodCache);
        return this.newInstance(mapperProxy);
    }
    ```

    其实我们知道，Proxy.newProxyInstance()需要三个参数，类加载器，被代理接口和InvocationHnadler，**什么？不知道？快去补习基础。**其中InvocationHandler掌管着invoke方法，正是这个方法中实现了对被代理实例的代码增强（或者叫做代理代码）。那我们就要着重看这个InvocationHandler里面到底有什么，特别是他的invoke方法。

    ```java
    MapperProxy.class

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ...省略...
        MapperMethod mapperMethod = this.cachedMapperMethod(method);
        return mapperMethod.execute(this.sqlSession, args);
    }
    ```
    
    invoke方法中，重点是调用了mapperMethod.execute()。这个mapperMethod就是:**被代理接口A，A中有方法a()，代理类实例((A)proxyA).a()中的这个a，就是method,而mapperMethod就是method被包装了一层。**换而言之，(session.getMapper(XxxMapper)).interfaceMethod()时，都在走mapperMethod.execute()这个方法。

- 下面我们来看mapperMethod.execute这个方法。

    ```java
    MapperMethod.class

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result;
        Object param;
        switch(this.command.getType()) {
        ...省略...
        case SELECT:
        ...省略...
        param = this.method.convertArgsToSqlCommandParam(args);
        result = sqlSession.selectOne(this.command.getName(), param);
        ...省略...
        }
        ...省略...
    }
    ```
    这个方法做了两件事，1.对参数用参数解析器转化为JDBCType的参数，这边不是重点。2.执行sqlSession.selectOne()，当然我删去了一些代码，为讲清楚，只讲selectOne()即可，其他都是大同小异的。**又回到最初的起点，呆呆地望着镜子前。** sqlSession又见面了，发现了么？sqlSession先是把getMapper交给configuration做，然后自己还能执行类似selecOne，update之类的命令，这是因为sqlSession是暴露给用户的接口，如果用户要用传统方式，就可以直接调用selectOne之类的方法，比如`        Employee employee =  session.selectOne("mybatisDemo.dao.EmployeeMapper.getEmpById",1);`如果用户想用mapper.xml和mapper接口的方法，就getMapper获得代理实例然后调用接口方法即可。**所以本质上，所有跟JDBC打交道的还是sqlsession的select、update等方法**

    现在还都是表面功夫，直到sqlSession.selectOne才开始真正的辉煌旅程。**小结一下，目前我们看到的MyBatis组件包括SqlSessionFactoryBuilder、SqlSessionFactory、SqlSession。还未看到的有，Executor，ParameterHandler，StatementHandler，ResultSetHandler。这几个部件都会在之后出现。**

- 下面来分析session.selectOne()。selectOne内部调用的还是selectList，因此直接看SqlSession的实现类DefaultSqlSession中的方法。可以发现，**Executor组件终于出现了**，而这个组件才是真正执行query()方法的组件。SqlSession真的是领导，getMapper交给config做，select等脏活累活又交给Executor完成。Executor.query的入参有什么？被代理方法参数parameter，ms用于动态sql的。

    ```java
    DefaultSqlSession.class

    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        List var5;
        ...省略...
        MappedStatement ms = this.configuration.getMappedStatement(statement);
        var5 = this.executor.query(ms, this.wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
        ...省略...
        return var5;
    }
    ```
    下面去看query方法,在Executor的一个抽象实现类，其实也就是模板类BaseExecutor中。

    ```java
    BaseExecutor.class

    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        BoundSql boundSql = ms.getBoundSql(parameter);
        CacheKey key = this.createCacheKey(ms, parameter, rowBounds, boundSql);
        return this.query(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }
    ```
    BoundSql就是动态sql，key是将sql语句，入参组合起来作为缓存参数，即：如果sql语句相同且参数一样，那可以认为两个sql语句会返回同样的结果（缓存未失效的情况下）。query方法中进一步调用doQuery方法，这个方法在BaseExecutor中只给出抽象方法，交给子类去继承实现。这个子类就是SimpleExecutor。

    ```java
    SimpleExecutor.class

    public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        Statement stmt = null;

        List var9;
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this.wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
            stmt = this.prepareStatement(handler, ms.getStatementLog());
            var9 = handler.query(stmt, resultHandler);
        } finally {
            this.closeStatement(stmt);
        }

        return var9;
    }
    ```

    这里出现了**StatementHandler**这个组件，先别急着点进newStatementHandler()方法，先看一下StatementHandler接口，发现这个接口有`ParameterHandler getParameterHandler();`方法和`<E> List<E> query(Statement var1, ResultHandler var2)`。这时候，**ParmeterHandler**和**ResultHandler**两大组件也出现了。所以这三个组件的关系是，StatementHandler中需要通过ParamterHandler处理参数，然后将结果通过ResultHandler处理成要求的JavaBean、Map、List后输出。

    **小结一下：SqlSession将查询等任务交给Executor接口实现类完成，Executor内有StatementHandler，StatementHandler内有ParameterHandler和ResultHandler，分别进行参数处理和结果处理。**

- 还没讲newStatementHandler()这个方法呢，为什么要现在讲？

    ```java
    Configuration.class

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler = (ParameterHandler)this.interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql) {
        ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        ResultSetHandler resultSetHandler = (ResultSetHandler)this.interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
        StatementHandler statementHandler = (StatementHandler)this.interceptorChain.pluginAll(statementHandler);
        return statementHandler;
    }
    ```

    值得注意的是interceptorChain，拦截器链，这里的拦截器链通过pluginAll对几个Handler进行织入。织入的是什么代码呢？是你写的拦截器代码。回忆一下MyBatis写拦截器代码的时候要指定哪些呢？1.要指定针对Exector，ParameterHandler，StatementHandler，或者ResultHandler进行拦截2.要指定针对什么方法拦截。针对拦截器这一部分的原理，建议阅读
    > https://www.jianshu.com/p/b82d0a95b2f3

    ```java
    @Intercepts({@Signature(type= Executor.class, method = "update", args = {MappedStatement.class,Object.class})})
    public class ExamplePlugin implements Interceptor {
        public Object intercept(Invocation invocation) throws Throwable {
            return invocation.proceed();
        }
        public Object plugin(Object target) {
            return Plugin.wrap(target, this);
        }
        public void setProperties(Properties properties) {
        }
    }
    ```
    这边拦截器和前面Mapper接口都是用到了动态代理。前面Mapper接口是通过动态代理技术代理Mapper接口，实现了即使我们不用谢Mapper接口实现类仍然可以调用Mapper接口内方法，因为MyBatis帮我们内部实现了一个代理类实例。这边的拦截器说的是，对于3个Handler和1个Exector接口，他们的所有接口方法都可以被拦截。所以这里拦截器所拦截的只能是这4个接口的实现类。这个拦截器主要的思路就是，被代理的实例叫target，然后把这个实例代理后，返回一个代理类实例叫proxyTarget，然后把这个proxyTarget再赋值给target，然后target再被其他代理，结果就是代理类代理代理类代理代理类。。。。层层包裹。当你调用最外层代理类实例时会从外向内一层一层执行前增强代码，然后再从内向外一层一层执行后增强代码。

## 快速实现一个MyBatis核心功能
1. DepartmentMapper.xml和DepartmentMapper接口均已完成，是如何完成departmentMapper.getDeptById(1)之后就直接获得Department对象的呢？主要是以下的核心代码：

    ```java
    @Test
    public void testDynamicProxy(){
        Object proxy = Proxy.newProxyInstance(CoreFunction.class.getClassLoader(),
                new Class[]{DepartmentMapper.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String sql = "select id,dept_name departmentName from t_dept where id = " + args[0];
                        Connection connection = DBUtil.getConnection();
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(sql);
                        while(resultSet.next()){
                            int id = resultSet.getInt(1);
                            String departmentName = resultSet.getString("departmentName");
                            Map<String,Object> map = new HashMap<>();
                            map.put("id",id);
                            map.put("departmentName",departmentName);
                            Department department = new Department();
                            BeanUtils.populate(department,map);
                            return department;
                        }
                        return null;
                    }
                });
        Department department = ((DepartmentMapper)proxy).getDeptById(1);
        System.out.println(department);
    }
    ```
    核心思路就是这样：用一个动态代理来代理这个接口，然后在invoke中获得数据库连接，调用statement，封装参数，执行sql后将resultSet封装成一个javaBean返回。

2. 除了核心代码，还有什么问题要解决？1.sql语句要从xml中获取。2.装配参数要进行转化，从javaBean转化成JDBCType。3.采用连接池，并且可以配置数据源。4.resultSet封装成javaBean。5.用拦截器实现插件。6.缓存功能。7.实现多表查询。8.如何和Spring相结合。
3. 针对以上提出解决方案。
    1. Dom4j技术获取。
    2. BeanUtils实现Bean向Map的转换，即可获得对象对应属性与属性值之间关系。在xml中比如：`select * from t_employee where id = #{id}`#{xxx}中xxx即为map的key，获取值后填充。如果是#{}，则采用PreparedStatement填充占位符？，如果是${}用Statement,以字符串拼接形式完成sql。
    3. 
    4. 可以通过ResultSetMetaData获取列名，列名和JavaBean的参数名相对应。
    5. 利用动态代理，对4大组件进代理，在InvocationHandler的invoke方法中调用Interceptor实现类，Interceptor实现类中有intercept(Invocation)方法，invocation.proceed调用被代理实例方法，intercept中其他代码为代理代码。
    6. 这边可以用grauva实现，或者自己手写一个LRU，然后用定时任务队列实现超时失效。
    7. 目前没考虑好。
    8. 整体写完后重构。

## getMapper流程
### getMapper主要流程

### 可配置的数据源

### 

## 四大组件

