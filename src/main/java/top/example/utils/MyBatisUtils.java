package top.example.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class MyBatisUtils {

    public static SqlSessionFactory sqlSessionFactory;
    public static SqlSession sqlSession;

    static {
        Reader reader;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSession = sqlSessionFactory.openSession(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlSession getSqlSession(){
        return sqlSession;
    }


    public static Object executeQuery(Function<SqlSession, Object> func) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return func.apply(sqlSession);
    }

    /**
     * 执行增、删、改操作
     * @param func
     * @return
     */
    public static Object executeUpdate(Function<SqlSession, Object> func) {
        SqlSession sqlSession = null;
        try {
            Object obj = func.apply(sqlSession);
            sqlSession.commit();
            return obj;
        } catch (Exception e) {
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }
        return null;
    }


}
