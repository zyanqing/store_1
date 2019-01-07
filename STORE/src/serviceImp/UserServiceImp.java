package serviceImp;

import dao.UserDao;
import daoImp.UserDaoImp;
import domain.User;
import service.UserService;
import utils.MailUtils;

import java.sql.SQLException;

public class UserServiceImp implements UserService {
    @Override
    public void userRegist(User user) throws SQLException {


        try {
            UserDao userdao = new UserDaoImp();
            userdao.userRegist(user);

            MailUtils.sendMail(user.getEmail(), user.getCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User userActive(String code) throws SQLException {

        UserDao userDao = new UserDaoImp();

        return userDao.userActive(code);
    }

    @Override
    public void updateUser(User user) throws SQLException {
        UserDao userDao = new UserDaoImp();

        userDao.updateUser(user);
    }

    @Override
    public User userLogin(User user) throws SQLException {
        UserDao userDao = new UserDaoImp();
        return userDao.userLogin(user);
    }
}
