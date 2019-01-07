package service;

import domain.PageModel;
import domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    List<Product> findNewProducts() throws SQLException;

    List<Product> findHotProducts() throws SQLException;

    Product findProductByPid(String pid) throws SQLException;

    PageModel findProductWithCidAndPage(String cid, int curNum) throws SQLException;

}
