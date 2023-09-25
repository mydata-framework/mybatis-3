package com.my.demo01;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * 源码剖析-1-Mybatis初始化是如何解析的全局配置文件?
 * <p>
 * 解析第一步, 怎么读取配置文件的?
 * <p>
 * flag: p-step-1.
 */
public class Demo2 {

  public static void main(String[] args) throws IOException {
    //p-step-1.0001 解析第一步, 怎么读取配置文件的?
    //p-step-1.0002 进入到 Resources 中
    //p-step-1.0004 接下来我们就去进去看看 Resources.getResourceAsReader("mybatis-config.xml"); 这个方法是去怎么执行的

    //p-step-1.0013
    //这里就获取到了返回的流文件, 当然这里由于是asReader就被封装了一层变为了Reader
    //这一步到这里就结束了, 这一步主要就是封装了类加载器和类加载器读取资源的过程的封装;
    //这一步配置文件并没有被解析, 仅仅是获取到了配置文件内容流对象,
    //所以要看配置文件是如何配解析的我们还要继续往下看
    Reader resourceAsReader = Resources.getResourceAsReader("mybatis-config.xml");

    //p-step-1.0014
    //这里看到了创建了一个SqlSessionFactoryBuilder对象, 并把前面获取的资源对象放入到build方法中, 经过build方法返回了SqlSessionFactory对象,
    //所以对mybatis-config.xml的解析就应当是在build方法中被解析的,因为我们应该先去看看SqlSessionFactory类的内部, 可以看到其提供了getConfiguration方法, 说明配置都封装在了Configuration中了
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resourceAsReader);

    //p-step-1.0063
    //这里获取到核心API层对象SqlSession
    SqlSession sqlSession = sqlSessionFactory.openSession();
    Map<String, Object> map = new HashMap<>();
    map.put("id", 73);
    //p-step-1.0068
    //这里开始使用sqlSession执行查询了
    //这里我们应该知道 sqlSession 中包含了有哪些内容
    //1 sqlSession中有Configuration, Configuration中有mappedStatements
    //2 sqlSession中有执行器 Executor, 而Executor有tx对象, tx对象中包含了数据源和事务参数
    Object entity = sqlSession.selectOne("UserMapper.selectById", map);
    //p-step-1.0094 执行到这里结束
    System.out.println(entity);
    sqlSession.close();

    sqlSession.rollback();

    //可以尝试再次相同查询, 会走缓存
    //Object entity2 = sqlSession.selectOne("UserMapper.selectById", map);

    System.out.println(entity);
  }
}
