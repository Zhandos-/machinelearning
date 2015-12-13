package kz.zhandos.machinelearning.mybatis;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import kz.zhandos.machinelearning.chapter4.SqLiteDbUpdate;

public class SearchDAO {
  private final static Logger log = Logger.getLogger(SearchDAO.class);
  private static SqlSessionFactory sqlSessionFactory;
  private static SqlSession session = null;



  public SearchDAO() {
    try {
      SqLiteDbUpdate.createDB(false);
    } catch (Exception e) {
      log.error("", e);
    }
  }

  static {
    String resource = "config.xml";
    InputStream inputStream;
    try {
      inputStream = SearchDAO.class.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } catch (Exception e) {
      log.error("", e);
    }
  }

  public static SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }



  public static SearchMapper getMapper() {
    SqlSession session = getSqlSessionFactory().openSession();
    SearchDAO.session = session;
    SearchMapper searchMapper = session.getMapper(SearchMapper.class);
    return searchMapper;
  }

  public static void commitChanges() {
    session.commit();
  }

  public static void closeConection() {
    session.close();
  }

  public static void main(String args[]) {
    getMapper().update("update databasechangeloglock set LOCKED=0 where id=1");
    commitChanges();
    System.out.println(getMapper().getAllFromTable("databasechangeloglock"));
    closeConection();
  }
}
