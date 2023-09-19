/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/**
 * Builds {@link SqlSession} instances.
 *
 * @author Clinton Begin
 */
public class SqlSessionFactoryBuilder {

  /**
   * @param reader
   * @return
   *
   * p-step-1.0015 我们进入 build 方法, 看到是个重载方法, 我们进入重载方法
   */
  public SqlSessionFactory build(Reader reader) {
    return build(reader, null, null);
  }

  public SqlSessionFactory build(Reader reader, String environment) {
    return build(reader, environment, null);
  }

  public SqlSessionFactory build(Reader reader, Properties properties) {
    return build(reader, null, properties);
  }

  /**
   * @param reader
   * @param environment
   * @param properties
   * @return
   *
   * p-step-1.0016
   * 进入重载方法,
   * reader 是前面获取到的配置资源文件流对象,environment,properties则分别是null;
   * 这里第一步是创建了一个解析器 XMLConfigBuilder parser, 传入了 reader 流对象, 我们进去看看
   *
   */
  public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
      //p-step-1.0019
      //上面行就相当于 XMLConfigBuilder parser 这样对象中已经包含了一个 document 对象, 并且内部包含一个空配置对象,
      //这里 XMLConfigBuilder 调用了自己的 parse() 方法进行解析 得到了一个 Configuration 对象
      //我们进去看看是如何将document对象转换为一个Configuration对象的, 所以xml解析参数的工作在这里面, 相当于将xml中的参数存到了Configuration中
      //p-step-1.0026
      //根据追踪发现, 解析工作还是在这个XMLConfigBuilder的parse()方法中进行的, 我们已经知道其内部维护了一个document和一个空配置对象, 我们进去看看实现
      Configuration configuration = parser.parse();

      //p-step-1.0023
      //这里获取到的configuration就是已经xml中的配置参数全都放到了Configuration中,
      //接下来就看 SqlSessionFactoryBuilder 的这个build方法是如何根据configuration配置对象转换为sqlSessionFactory
      //那么这个过程也就是配置的解析的主要过程了
      SqlSessionFactory sqlSessionFactory = build(configuration);

      return sqlSessionFactory;
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        reader.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  public SqlSessionFactory build(InputStream inputStream) {
    return build(inputStream, null, null);
  }

  public SqlSessionFactory build(InputStream inputStream, String environment) {
    return build(inputStream, environment, null);
  }

  public SqlSessionFactory build(InputStream inputStream, Properties properties) {
    return build(inputStream, null, properties);
  }

  public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        inputStream.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  /**
   * @param config
   * @return
   *
   * p-step-1.0024
   * 这里就new 了一个DefaultSqlSessionFactory对象, 传入了一个配置对象,
   * 可以想到, 这个内部是做了解析过程, 我们跟进去看看
   */
  public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
  }

}
