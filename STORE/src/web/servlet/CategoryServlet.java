package web.servlet;

import domain.Category;
import net.sf.json.JSONArray;
import service.CategoryService;
import serviceImp.CategoryServiceImp;
import web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryServlet extends BaseServlet {

    public String findAllcats(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        CategoryService categoryService = new CategoryServiceImp();

        List<Category> cats = categoryService.findAllCats();

        response.setContentType("application/json;charset=utf-8");

        String jsonStr = JSONArray.fromObject(cats).toString();

        response.getWriter().println(jsonStr);

        return null;
    }

}
