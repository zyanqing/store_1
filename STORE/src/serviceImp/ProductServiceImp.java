package serviceImp;

import dao.ProductDao;
import daoImp.ProductDaoImp;
import domain.PageModel;
import domain.Product;
import org.junit.Test;
import service.ProductService;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImp implements ProductService {

    ProductDao ProductDao=new ProductDaoImp();

    @Override
    public List<Product> findNewProducts() throws SQLException {
       return ProductDao.findNewProducts();
    }

    @Override
    public List<Product> findHotProducts() throws SQLException {
        return ProductDao.findHotProducts();
    }

    @Override
    public Product findProductByPid(String pid) throws SQLException {
        return ProductDao.findProductByPid(pid);
    }

    @Override
    public PageModel findProductWithCidAndPage(String cid, int curNum) throws SQLException {


        int total = ProductDao.findTotalNum(cid);

        PageModel pm=new PageModel(curNum, total, 12);
        //2_关联集合 SELECT * FROM product WHERE cid=1 LIMIT (当前页-1)*5,5;
        List<Product> list=ProductDao.findProductWithCidAndPage(cid,pm.getStartIndex(),pm.getPageSize());
        pm.setList(list);
        //3_关联url
        pm.setUrl("ProductServlet?method=findProductWithCidAndPage&cid="+cid);
        return pm;
    }

    @Test
    public void test() throws Exception{
         System.out.println(findProductByPid("7"));
    }

}
