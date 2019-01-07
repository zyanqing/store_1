package serviceImp;

import dao.CategoryDao;
import daoImp.CategoryDaoImp;
import domain.Category;
import service.CategoryService;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImp implements CategoryService {

    @Override
    public List<Category> findAllCats() throws SQLException {

        CategoryDao categoryDao = new CategoryDaoImp();

        return categoryDao.findAllCats();
    }
}
