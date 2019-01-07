package dao;

import domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {

    List<Product> findNewProducts()throws SQLException;

    List<Product> findHotProducts()throws SQLException;

    Product findProductByPid(String pid) throws SQLException;

    int findTotalNum(String cid)throws SQLException;

    List<Product> findProductWithCidAndPage(String cid, int startIndex, int pageSize)throws SQLException;

}
