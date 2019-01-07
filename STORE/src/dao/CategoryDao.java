package dao;

import domain.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {

    public List<Category> findAllCats() throws SQLException;

}
