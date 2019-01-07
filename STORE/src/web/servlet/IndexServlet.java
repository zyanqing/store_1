package web.servlet;

import domain.Product;
import serviceImp.ProductServiceImp;
import web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class IndexServlet extends BaseServlet {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ProductServiceImp productServiceImp = new ProductServiceImp();

        List<Product> newProducts = productServiceImp.findNewProducts();

        List<Product> hotProducts = productServiceImp.findHotProducts();

        request.setAttribute("hots", hotProducts);
        request.setAttribute("news", newProducts);

        return "/jsp/index.jsp";
    }
}
