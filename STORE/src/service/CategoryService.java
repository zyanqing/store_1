package service;

import domain.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {

    public List<Category> findAllCats() throws SQLException;

}
