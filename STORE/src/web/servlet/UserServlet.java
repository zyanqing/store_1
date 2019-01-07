package web.servlet;

import domain.User;
import service.UserService;
import serviceImp.UserServiceImp;
import utils.MyBeanUtils;
import utils.UUIDUtils;
import web.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class UserServlet extends BaseServlet {

    public String registUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return "jsp/register.jsp";
    }

    public String loginUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return "jsp/login.jsp";
    }

    public String userRegist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        Map<String, String[]> params = request.getParameterMap();

        User user = MyBeanUtils.populate(User.class, params);

        user.setUid(UUIDUtils.getId());
        user.setState(0);
        user.setCode(UUIDUtils.getCode());

        UserService uService = new UserServiceImp();
        uService.userRegist(user);
        request.setAttribute("msg", "注册成功，请去邮箱激活");

        return "/jsp/info.jsp";
    }

    public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        String code = request.getParameter("code");

        UserService uService = new UserServiceImp();

        User user = uService.userActive(code);

        String msg = null;

        if (user != null) {
            user.setCode(null);
            user.setState(1);
            uService.updateUser(user);
            request.setAttribute("msg", "用户激活成功请登录");
            return "jsp/login.jsp";
        }
        request.setAttribute("msg", "链接已失效");
        return "jsp/info.jsp";
    }

    public String userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, String[]> params = request.getParameterMap();


        User user = MyBeanUtils.populate(User.class, params);

        UserService userService = new UserServiceImp();

        user = userService.userLogin(user);

        if (user == null) {
            request.setAttribute("msg", "用户名或密码错误");
            return "jsp/login.jsp";
        } else {

            request.getSession().setAttribute("loginUser", user);

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }

        return null;
    }

    public String logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.getSession().setAttribute("loginUser", null);

        return "jsp/index.jsp";
    }

}
