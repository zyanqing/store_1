package daoImp;

import dao.CategoryDao;
import domain.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import utils.JDBCUtils;

import java.sql.SQLException;
import java.util.List;

public class CategoryDaoImp implements CategoryDao {

    @Override
    public List<Category> findAllCats() throws SQLException {

        QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());

        String sql = "select * from category";

        return qr.query(sql,new BeanListHandler<Category>(Category.class));

    }
}
