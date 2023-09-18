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
 * 快速验证源码环境是否搭建完毕, 看是否可以执行成功, 执行成功表示环境搭建完毕
 */
public class Demo1 {

  public static void main(String[] args) throws IOException {
    Reader resourceAsReader = Resources.getResourceAsReader("mybatis-config.xml");

    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(resourceAsReader);

    SqlSession sqlSession = sqlSessionFactory.openSession();
    Map<String, Object> map = new HashMap<>();
    map.put("id", 73);
    Object entity = sqlSession.selectOne("UserMapper.selectById", map);
    System.out.println(entity);
  }
}
