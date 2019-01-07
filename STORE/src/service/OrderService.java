package service;

import domain.Order;
import domain.PageModel;
import domain.User;

import java.util.List;


public interface OrderService {

	void saveOrder(Order order);

	PageModel findOrdersByUidWithPage(User user, int curNum)throws Exception;

	Order findOrderByOid(String oid)throws Exception;

	void updateOrder(Order order)throws Exception;
	List<Order> findAllOrders()throws Exception;
	List<Order> findAllOrdersWithState(String state)throws Exception;;
}
