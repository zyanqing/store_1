package web.servlet;

import domain.Cart;
import domain.CartItem;
import service.ProductService;
import serviceImp.ProductServiceImp;
import web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CartServlet extends BaseServlet {

    public String addToCart(HttpServletRequest request, HttpServletResponse response) throws Exception {


        Cart cart = (Cart) request.getSession().getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute("cart", cart);
        }

        ProductService ProductService = new ProductServiceImp();

        int num = Integer.parseInt(request.getParameter("num"));
        String pid = request.getParameter("pid");
        CartItem cartItem = new CartItem();
        cartItem.setProduct(ProductService.findProductByPid(pid));


        cartItem.setNum(num);

        cart.addCart(cartItem);

        response.sendRedirect(request.getContextPath() + "/jsp/cart.jsp");
        return null;
    }

    public String delCartItem(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String pid = request.getParameter("pid");

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        cart.delCart(pid);

        response.sendRedirect(request.getContextPath() + "/jsp/cart.jsp");
        return null;
    }


    public String clearCart(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        cart.clearCart();

        response.sendRedirect(request.getContextPath() + "/jsp/cart.jsp");
        return null;
    }

}































