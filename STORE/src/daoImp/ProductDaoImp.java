package daoImp;

import dao.ProductDao;
import domain.Product;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtils;

import java.sql.SQLException;
import java.util.List;

public class ProductDaoImp implements ProductDao {

    @Override
    public List<Product> findNewProducts() throws SQLException {

        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "SELECT * FROM product WHERE pflag=0 ORDER BY pdate DESC LIMIT 0 ,9";

        return qr.query(sql, new BeanListHandler<Product>(Product.class));
    }

    @Override
    public List<Product> findHotProducts() throws SQLException {
        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "SELECT * FROM product WHERE pflag=0 AND is_hot=1 ORDER BY pdate DESC LIMIT 0 ,9";

        return qr.query(sql, new BeanListHandler<Product>(Product.class));
    }

    @Override
    public Product findProductByPid(String pid) throws SQLException {

        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "select * from product where pid=?";

        return qr.query(sql, new BeanHandler<Product>(Product.class), pid);
    }

    @Override
    public int findTotalNum(String cid) throws SQLException {

        String sql="select count(*) from product where cid=?";
        QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
        Long num=(Long)qr.query(sql, new ScalarHandler(),cid);
        return num.intValue();
    }

    @Override
    public List<Product> findProductWithCidAndPage(String cid, int startIndex, int pageSize) throws SQLException {

        String sql="SELECT * FROM product WHERE cid=? LIMIT ?,?";
        QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
        return qr.query(sql, new BeanListHandler<Product>(Product.class),cid,startIndex,pageSize);
    }

}



















