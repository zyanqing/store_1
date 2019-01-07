package web.servlet;

import YOP.YOPService;
import YOP.YOPServiceImp;
import domain.*;
import serviceImp.OrderServiceImp;
import utils.UUIDUtils;
import utils.YeepayService;
import web.base.BaseServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class OrderServlet extends BaseServlet {

    YOPService YOPService = new YOPServiceImp();

    service.OrderService OrderService = new OrderServiceImp();

    public String saveOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute("loginUser");

        if (user == null) {
            request.setAttribute("msg", "请登录");
            return "/jsp/info.jsp";
        }


        Cart cart = (Cart) request.getSession().getAttribute("cart");
        Collection<CartItem> cartItems = cart.getCartItems();

        Order order = new Order();
        order.setOrderTime(new Date());
        order.setState(1);
        order.setOid(UUIDUtils.getId());
        order.setTotal(cart.getTotal());
        order.setUser(user);


        for (CartItem item : cartItems) {

            OrderItem orderItem = new OrderItem();
            orderItem.setItemid(UUIDUtils.getId());
            orderItem.setProduct(item.getProduct());
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getNum());
            orderItem.setTotal(item.getSubTotal());

            order.getList().add(orderItem);
        }

        OrderService.saveOrder(order);

        //清空购物车
        cart.clearCart();

        request.setAttribute("order", order);
        return "/jsp/order_info.jsp";
    }

    //findOrdersByUidWithPage
    public String findOrdersByUidWithPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取数据
        int curNum = Integer.parseInt(request.getParameter("num"));
        User user = (User) request.getSession().getAttribute("loginUser");
        //调用业务层功能,返回PageModel对象(1_分页参数2_所有订单3_url)
        PageModel pm = OrderService.findOrdersByUidWithPage(user, curNum);
        //将PageModel对象放入request,转发到order_list.jsp页面
        request.setAttribute("page", pm);
        return "/jsp/order_list.jsp";
    }

    //findOrderByOid
    public String findOrderByOid(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //服务端获取oid,根据oid查询订单(订单上携带当前订单上所有的订单项以及商品),
        String oid = request.getParameter("oid");
        Order order = OrderService.findOrderByOid(oid);
        //将订单放入request内
        request.setAttribute("order", order);
        //转发到订单详情页面order_info.jsp
        return "/jsp/order_info.jsp";
    }

    //payOrder
    public String payOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //服务端获取到收货人姓名,电话,地址,订单编号,银行信息
        String name = request.getParameter("name");
        String telephone = request.getParameter("telephone");
        String address = request.getParameter("address");
        String oid = request.getParameter("oid");
        String pd_FrpId = request.getParameter("pd_FrpId");
        //根据获取订单编号获取订单信息,更新订单上的收货人姓名,电话,地址,
        Order order = OrderService.findOrderByOid(oid);
        order.setName(name);
        order.setTelephone(telephone);
        order.setAddress(address);
        //更新数据库中当前订单上收货人相关信
        OrderService.updateOrder(order);
        //调用相关的支付代码

        String goodsName = "小米手机";
        String goodsDesc = "为发烧而生";
        String goodsParamExt = "{\"goodsName\":\"" + goodsName + "\",\"goodsDesc\":\"" + goodsDesc + "\"}";


        String token = YOPService.createOrder(oid, String.valueOf(order.getTotal()), "http://119.23.41.164:8080/store/OrderServlet?method=callBack", "http://119.23.41.164:8080/store/OrderServlet?method=callBackForPage", goodsParamExt);

        User user = (User) request.getSession().getAttribute("loginUser");

        String payURL = YOPService.paymentOrderByCashier(token, user.getUid());
        response.sendRedirect(payURL);

//        YOPService.paymentOrderByAPI(token,user.getUid());

        return null;
    }


    public String callBackForPage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //localhost:8082/store/OrderServlet?method=callBack&merchantNo=10000466938&
        // parentMerchantNo=10000466938&orderId=A00DEE60930A45769F821E9D28347A01&
        // sign=QqdoRioI_PXTauohLdr_xcrdeWCflQehIGc6JXn28xQWevzJAIngDreD1t_n6NebEKhAGsmj448D3xgsUEQCDA6aKVdbeoWYitG2GZIAJDJ57pJaDx5Y_IY417tkC9A6sLWkDZel66Sdi-lVH2_N0q8Ykq5ZP6r58UKhosMCjLwzjhaHrOxGOe7w-uj78bAqiT_gV75yNsuvvy9PXpFsvCWo8rEVupMtA9PAJ81qIcUuP-LTEi3Ma1UcDAwZ2ZO2GNQ9qlhO1TtKDQFzgbQqlYjFqZMs28-5ZJ2GeLGUNOuXqMZe6Ua8F36MntU4WG6hwSO8Rnc6AohvWpn61qhNKQ$SHA256
        String merchantNo = request.getParameter("merchantNo");
        String parentMerchantNo = request.getParameter("parentMerchantNo");
        String orderId = request.getParameter("orderId");
        String sign = request.getParameter("sign");

        Map<String, String> responseParams = new HashMap<String, String>();
        responseParams.put("merchantNo", merchantNo);
        responseParams.put("parentMerchantNo", parentMerchantNo);
        responseParams.put("orderId", orderId);
        responseParams.put("sign", sign);

        if (YeepayService.verifyCallback(responseParams)) {

            service.OrderService OrderService = new OrderServiceImp();
            Order order = OrderService.findOrderByOid(orderId);
            order.setState(2);
            OrderService.updateOrder(order);
            request.setAttribute("msg", "支付成功！订单号：" + orderId);
            return "/jsp/info.jsp";

        } else {
            request.setAttribute("msg", "数据被串改");
            return "/jsp/info.jsp";
        }

    }

    public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {


        String responseStr = request.getParameter("response");
        System.out.println(responseStr);

        Map<String, String> responseMap = YOPService.decrypt(responseStr);

        System.out.println(responseMap);

        String orderId = responseMap.get("orderId");
        String uniqueOrderNo = responseMap.get("uniqueOrderNo");

        service.OrderService OrderService = new OrderServiceImp();
        Order order = OrderService.findOrderByOid(orderId);
        if (order.getState() == 1){
            order.setState(2);
            order.setUniqueOrderNo(uniqueOrderNo);
            OrderService.updateOrder(order);
        }

        return null;
    }


    public String refund(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String oid = request.getParameter("oid");

        Order order = OrderService.findOrderByOid(oid);
        order.setRefundRequestId(order.getOid() + order.getUniqueOrderNo());
        order.setState(4);
        OrderService.updateOrder(order);

        Map<String, String> result = YOPService.refund(order);

        String status = result.get("status");
        String message = result.get("message");

        if (status == null || status.equals("")) {
            request.setAttribute("msg", message + "- 可能是由于同一订单发起多次退款所造成的，请联系客服！");
        } else {
            order.setRefundStatus(status);
            request.setAttribute("msg", "申请退款成功，款项将会原路返回，到账时间或退款状态的更新可能会有延迟！");
        }
        OrderService.updateOrder(order);
        return "/jsp/info.jsp";


    }

    public String refundCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {


        String responseStr = request.getParameter("response");

        Map<String, String> responseMap = YeepayService.parseResponse(responseStr);

        String orderId = responseMap.get("orderId");
        String status = responseMap.get("status");


        Order order = OrderService.findOrderByOid(orderId);

        if (status == "SUCCESS") {
            order.setState(4);
        }
        order.setRefundStatus(status);
        OrderService.updateOrder(order);

        return null;
    }

}
