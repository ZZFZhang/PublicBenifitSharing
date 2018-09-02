package com.publicbenifitsharing.android.util;

import android.text.TextUtils;

import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.StringUtils;
import com.publicbenifitsharing.android.entityclass.Dynamic;
import com.publicbenifitsharing.android.entityclass.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBService {

    private Connection conn=null; //打开数据库对象
    private PreparedStatement ps=null;//操作整合sql语句的对象
    private ResultSet rs=null;//查询结果的集合

    //DBService 对象
    public static DBService dbService=null;

    // 构造方法，私有化
    private DBService(){

    }


    //获取MySQL数据库单例类对象
    public static DBService getDbService(){
        if(dbService==null){
            dbService=new DBService();
        }
        return dbService;
    }

    //获取dynamic条数
    public int getDynamicNumber(){
        int number=0;
        String sql="SELECT COUNT(*) FROM dynamics";
        conn= MySQLDBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                if (ps != null) {
                    rs=ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            number=rs.getInt("COUNT(*)");
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        MySQLDBOpenHelper.closeAll(conn,ps,rs);//关闭数据库连接
        return number;
    }
    //获取project条数
    public int getProjectNumber(){
        int number=0;
        String sql="SELECT COUNT(*) FROM projects";
        conn= MySQLDBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                if (ps != null) {
                    rs=ps.executeQuery();
                    if (rs!=null){
                        while (rs.next()){
                            number=rs.getInt("COUNT(*)");
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        MySQLDBOpenHelper.closeAll(conn,ps,rs);//关闭数据库连接
        return number;
    }

    // 获取dynamic数据
    public List<Dynamic> getDynamicData(){
        //结果存放集合
        List<Dynamic> list=new ArrayList<Dynamic>();
        //MySQL 语句
        String sql="select * from dynamics order by id desc";//以id为关键字降序查询
        //获取链接数据库对象
        conn= MySQLDBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            Dynamic dynamic=new Dynamic();
                            dynamic.setId(rs.getInt("id"));
                            dynamic.setUserName(rs.getString("userName"));
                            dynamic.setHeadIconUrl(rs.getString("headIconUrl"));
                            dynamic.setImageUrl(rs.getString("imageUrl"));
                            dynamic.setContentTitle(rs.getString("content"));
                            dynamic.setReleaseDate(rs.getString("releaseDate"));
                            dynamic.setReleaseTime(rs.getString("releaseTime"));
                            list.add(dynamic);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySQLDBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }
    //获取project数据
    public List<Project> getProjectData(){
        //结果存放集合
        List<Project> list=new ArrayList<Project>();
        //MySQL 语句
        String sql="select * from projects order by id desc";//以id为关键字降序查询
        //获取链接数据库对象
        conn= MySQLDBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            Project project=new Project();
                            project.setId(rs.getInt("id"));
                            project.setUserName(rs.getString("userName"));
                            project.setImageUrl(rs.getString("imageUrl"));
                            project.setTitle(rs.getString("title"));
                            project.setContentText(rs.getString("content"));
                            project.setReleaseTime(rs.getString("releaseTime"));
                            list.add(project);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySQLDBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }
    //获取我的dynamic
    public List<Dynamic> getMyDynamicData(String userName){
        //结果存放集合
        List<Dynamic> list=new ArrayList<Dynamic>();
        //MySQL 语句
        String sql="select * from dynamics where userName='"+userName+"' order by id desc";//以id为关键字降序查询
        //获取链接数据库对象
        conn= MySQLDBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            Dynamic dynamic=new Dynamic();
                            dynamic.setId(rs.getInt("id"));
                            dynamic.setUserName(rs.getString("userName"));
                            dynamic.setHeadIconUrl(rs.getString("headIconUrl"));
                            dynamic.setImageUrl(rs.getString("imageUrl"));
                            dynamic.setContentTitle(rs.getString("content"));
                            dynamic.setReleaseDate(rs.getString("releaseDate"));
                            dynamic.setReleaseTime(rs.getString("releaseTime"));
                            list.add(dynamic);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySQLDBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }
    //获取我的project数据
    public List<Project> getMyProjectData(String userName){
        //结果存放集合
        List<Project> list=new ArrayList<Project>();
        //MySQL 语句
        String sql="select * from projects where userName='"+userName+"' order by id desc";//以id为关键字降序查询
        //获取链接数据库对象
        conn= MySQLDBOpenHelper.getConn();
        try {
            if(conn!=null&&(!conn.isClosed())){
                ps= (PreparedStatement) conn.prepareStatement(sql);
                if(ps!=null){
                    rs= ps.executeQuery();
                    if(rs!=null){
                        while(rs.next()){
                            Project project=new Project();
                            project.setId(rs.getInt("id"));
                            project.setUserName(rs.getString("userName"));
                            project.setImageUrl(rs.getString("imageUrl"));
                            project.setTitle(rs.getString("title"));
                            project.setContentText(rs.getString("content"));
                            project.setReleaseTime(rs.getString("releaseTime"));
                            list.add(project);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySQLDBOpenHelper.closeAll(conn,ps,rs);//关闭相关操作
        return list;
    }

    // 上传dynamic数据
    public int uploadDynamicData(Dynamic dynamic){
        int result=-1;
        if(dynamic!=null){
            //获取链接数据库对象
            conn= MySQLDBOpenHelper.getConn();
            //MySQL 语句
            String sql="INSERT INTO dynamics (userName,headIconUrl,imageUrl,content,releaseDate,releaseTime) VALUES (?,?,?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    String userName=dynamic.getUserName();
                    String headIconUrl=dynamic.getHeadIconUrl();
                    String imageUrl=dynamic.getImageUrl();
                    String content=dynamic.getContentTitle();
                    String releaseDate=dynamic.getReleaseDate();
                    String releaseTime=dynamic.getReleaseTime();
                    ps.setString(1,userName);//第一个参数
                    ps.setString(2,headIconUrl);//第二个参数
                    ps.setString(3,imageUrl);//第三个参数
                    ps.setString(4,content);//第四个参数
                    ps.setString(5,releaseDate);//第五个参数
                    ps.setString(6,releaseTime);//第六个参数
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MySQLDBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }
    // 上传project数据
    public int uploadProjectData(Project project){
        int result=-1;
        if(project!=null){
            //获取链接数据库对象
            conn= MySQLDBOpenHelper.getConn();
            //MySQL 语句
            String sql="INSERT INTO projects(userName,title,imageUrl,content,releaseTime) VALUES (?,?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    String userName=project.getUserName();
                    String title=project.getTitle();
                    String imageUrl=project.getImageUrl();
                    String content=project.getContentText();
                    String releaseTime=project.getReleaseTime();
                    ps.setString(1,userName);//第一个参数
                    ps.setString(2,title);//第二个参数
                    ps.setString(3,imageUrl);//第三个参数
                    ps.setString(4,content);//第四个参数
                    ps.setString(5,releaseTime);//第五个参数
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MySQLDBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    //删除我的dynamic
    public int deleteMyDynamicData(String userName){
        int result=-1;
        if(userName!=null){
            //获取链接数据库对象
            conn= MySQLDBOpenHelper.getConn();
            //MySQL 语句
            String sql="delete from dynamics where userName=?";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1, userName);
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MySQLDBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }
    //删除我的project
    public int deleteMyProjectData(String userName){
        int result=-1;
        if(userName!=null){
            //获取链接数据库对象
            conn= MySQLDBOpenHelper.getConn();
            //MySQL 语句
            String sql="delete from projects where userName=?";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1, userName);
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MySQLDBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }

    /*// 修改数据
    public int updateUserData(String phone){
        int result=-1;
        if(!TextUtils.isEmpty(phone)){
            //获取链接数据库对象
            conn= MySQLDBOpenHelper.getConn();
            //MySQL 语句
            String sql="update user set state=? where phone=?";
            try {
                boolean closed=conn.isClosed();
                if(conn!=null&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1,"1");//第一个参数state 一定要和上面SQL语句字段顺序一致

                    ps.setString(2,phone);//第二个参数 phone 一定要和上面SQL语句字段顺序一致

                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MySQLDBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }


    // 插入数据
    public int insertUserData(List<User> list){
        int result=-1;
        if((list!=null)&&(list.size()>0)){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            //MySQL 语句
            String sql="INSERT INTO user (name,phone,content,state) VALUES (?,?,?,?)";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    for(User user:list){
                        ps= (PreparedStatement) conn.prepareStatement(sql);
                        String name=user.getName();
                        String phone=user.getPhone();
                        String content=user.getContent();
                        String state=user.getState();
                        ps.setString(1,name);//第一个参数 name 规则同上
                        ps.setString(2,phone);//第二个参数 phone 规则同上
                        ps.setString(3,content);//第三个参数 content 规则同上
                        ps.setString(4,state);//第四个参数 state 规则同上
                        result=ps.executeUpdate();//返回1 执行成功
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }


    // 删除数据
    public int delUserData(String phone){
        int result=-1;
        if((!StringUtils.isEmpty(phone))&&(PhoneNumberUtils.isMobileNumber(phone))){
            //获取链接数据库对象
            conn= DBOpenHelper.getConn();
            //MySQL 语句
            String sql="delete from user where phone=?";
            try {
                boolean closed=conn.isClosed();
                if((conn!=null)&&(!closed)){
                    ps= (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1, phone);
                    result=ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn,ps);//关闭相关操作
        return result;
    }*/

}
