package web.servlet;

import domain.PageModel;
import domain.Product;
import service.ProductService;
import serviceImp.ProductServiceImp;
import web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductServlet extends BaseServlet {

    public String findProductByPid(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String pid = request.getParameter("pid");

        ProductServiceImp productServiceImp = new ProductServiceImp();

        Product product = productServiceImp.findProductByPid(pid);

        System.out.println(product);

        request.setAttribute("pro", product);

        return "/jsp/product_info.jsp";
    }

    public String findProductWithCidAndPage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String cid = request.getParameter("cid");

        int num = Integer.parseInt(request.getParameter("num"));

        ProductService productService = new ProductServiceImp();
        PageModel pm = productService.findProductWithCidAndPage(cid,num);

        request.setAttribute("page",pm);

        return "/jsp/product_list.jsp";
    }

}





















