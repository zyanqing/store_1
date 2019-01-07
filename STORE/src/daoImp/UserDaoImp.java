package daoImp;

import dao.UserDao;
import domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import utils.JDBCUtils;

import java.sql.SQLException;

public class UserDaoImp implements UserDao {

    @Override
    public void userRegist(User user) throws SQLException {

        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {user.getUid(),user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode()};

        qr.update(sql,params);

    }

    @Override
    public User userActive(String code) throws SQLException {

        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "select * from user where code=?";

       return qr.query(sql,new BeanHandler<User>(User.class),code);
    }

    @Override
    public void updateUser(User user) throws SQLException {

        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql="UPDATE USER SET username= ? ,PASSWORD=? ,NAME =? ,email =? ,telephone =? , birthday =?  ,sex =? ,state= ? , CODE = ? WHERE uid=?";
        Object[] params={user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode(),user.getUid()};

        qr.update(sql,params);

    }

    @Override
    public User userLogin(User user) throws SQLException {

        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "select * from user where username=? and password=?";

        return qr.query(sql,new BeanHandler<User>(User.class),user.getUsername(),user.getPassword());
    }


}
