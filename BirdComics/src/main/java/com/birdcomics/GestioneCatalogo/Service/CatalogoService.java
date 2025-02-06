package com.birdcomics.GestioneCatalogo.Service;

import java.sql.SQLException;
import java.util.List;
import com.birdcomics.Bean.ProductBean;

public interface CatalogoService {
    List<ProductBean> getAllProducts() throws SQLException;
    ProductBean getProductById(String productId) throws SQLException;
	String addProduct(String name, String description, float price, String imagePath, String[] genres)throws SQLException;
	List<ProductBean> searchAllProductsGestore(String search) throws SQLException;
	List<ProductBean> getAllProductsByType(String type) throws SQLException;
	List<ProductBean> searchAllProducts(String search) throws SQLException;
	String removeProduct(String productId) throws SQLException;
	//String updateProduct(String productId, String name, String info, float price, String image, String[] genres) throws SQLException;
}
