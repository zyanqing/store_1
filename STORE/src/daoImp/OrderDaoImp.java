package daoImp;

import dao.OrderDao;
import domain.Order;
import domain.OrderItem;
import domain.Product;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class OrderDaoImp implements OrderDao {

	@Override
	public List<Order> findAllOrdersWithState(String state) throws Exception {
		String sql="select * from orders where state = ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Order>(Order.class),state);
		
	}

	@Override
	public List<Order> findAllOrders() throws Exception {
		String sql="select * from orders ";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		return qr.query(sql, new BeanListHandler<Order>(Order.class));
	}

	@Override
	public void updateOrder(Order order) throws Exception {
		String sql="UPDATE orders SET ordertime =? , total =? ,state= ?, address=? ,name=? ,telephone =? ,uniqueOrderNo=?,refundRequestId=?,refundStatus=?  WHERE oid=?";
		Object[] params={order.getOrderTime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUniqueOrderNo(),order.getRefundRequestId(),order.getRefundStatus(),order.getOid()};
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		qr.update(sql,params);
		
	}

	@Override
	public int findTotalRecordsByUid(User user) throws Exception {
		String sql="select count(*) from orders where uid=?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Long num=(Long)qr.query(sql, new ScalarHandler(),user.getUid());
		return num.intValue();
	}

	@Override
	public List<Order> findOrdersByUidWithPage(User user, int startIndex, int pageSize) throws Exception {
		String sql="SELECT * FROM orders WHERE uid =?  ORDER BY ordertime DESC LIMIT  ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		List<Order> list=qr.query(sql, new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);
		
		for(Order order:list){
			sql="SELECT * FROM  orderitem o , product p WHERE o.pid=p.pid  AND oid=?";
			List<Map<String, Object>> list01 = qr.query(sql, new MapListHandler(),order.getOid());
			for(Map<String, Object> map:list01){
				
				//System.out.println(map);
				
				Product product=new Product();
				OrderItem OrderItem=new OrderItem();
				
				try {
					//BeanUtils会自动将map上属于product对象中的数据填充到product对象上
					//BeanUtils会自动将map上属于OrderItem对象中的数据填充到OrderItem对象上
					BeanUtils.populate(product, map);
					BeanUtils.populate(OrderItem, map);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				OrderItem.setProduct(product);
				order.getList().add(OrderItem);
			}
		}
		return list;
	}
	
	
	
	/*public List<Order> findOrdersByUidWithPage02(User user, int startIndex, int pageSize) throws Exception {
		String sql="SELECT * FROM orders WHERE uid =?  ORDER BY ordertime DESC LIMIT  ? , ?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		List<Order> list=qr.query(sql, new BeanListHandler<Order>(Order.class),user.getUid(),startIndex,pageSize);
		
		for(Order order:list){
			sql="select * from orderitem where oid=?";
			List<OrderItem> orderItems=qr.query(sql, new BeanListHandler<OrderItem>(OrderItem.class),order.getOid());
			for(OrderItem orderItem:orderItems){
				sql="select * from product where pid=?";
				qr.query(sql, new BeanListHandler<Product>(Product.class),orderItem.getProduct().getPid())
			}
			
		}
		return list;
	}*/
	
	

	@Override
	public Order findOrderByOid(String oid) throws Exception {
		String sql="select * from orders where oid=?";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		Order order=qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		
		sql="select * from orderitem o,product p where o.pid=p.pid and oid=?";
		List<Map<String, Object>> list = qr.query(sql,new MapListHandler(),oid);
		for(Map<String, Object> map:list){
			Product Product =new Product();
			OrderItem OrderItem=new OrderItem();
			try {
				BeanUtils.populate(Product, map);
				BeanUtils.populate(OrderItem, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			OrderItem.setProduct(Product);
			order.getList().add(OrderItem);
		}
		
		return order;
	}

	@Override
	public void saveOrder(Order order) throws SQLException {
		String sql="INSERT INTO orders VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params={order.getOid(),order.getOrderTime(),order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid(),order.getUniqueOrderNo(),order.getRefundRequestId(),order.getRefundStatus()};
		QueryRunner qr=new QueryRunner();
		qr.update(JDBCUtils.getConnection(),sql,params);
	}

	@Override
	public void saveOrderItem(OrderItem item)  throws SQLException {
		String sql="INSERT INTO orderitem VALUES(?,?,?,?,?)";
		Object[] params={item.getItemid(),item.getQuantity(),item.getTotal(),item.getProduct().getPid(),item.getOrder().getOid()};
		QueryRunner qr=new QueryRunner();
		qr.update(JDBCUtils.getConnection(),sql,params);
	}

	
	
	public static  void testMapListHandler() throws Exception{
		String sql="select * from category";
		QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		List<Map<String, Object>> list = qr.query(sql, new MapListHandler());
		for(Map<String, Object> map:list){
			System.out.println(map);
		}
		
		
	} 
	
	
	public static void main(String[] args) throws Exception {
		//User u=new User();
		//u.setUid("72DE6CD3E5CC476893F3C68B189ABC42");
		//new OrderDaoImp().findOrdersByUidWithPage(u, 0, 1);
		testMapListHandler();
	}
	
	
	
}
