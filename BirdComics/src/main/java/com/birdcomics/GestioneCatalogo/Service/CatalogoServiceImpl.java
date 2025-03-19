package com.birdcomics.GestioneCatalogo.Service;

import java.sql.SQLException;
import java.util.List;
import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Dao.ProductServiceDAO;

public class CatalogoServiceImpl implements CatalogoService {

	private ProductServiceDAO productServiceDAO;

	public CatalogoServiceImpl() {
		this.productServiceDAO = new ProductServiceDAO();
	}

	@Override
	public List<ProductBean> visualizzaCatalogo() throws SQLException {
		return productServiceDAO.getAllProducts();
	}

	@Override
	public ProductBean visualizzaDettagli(String productId) throws SQLException {
		return productServiceDAO.getProductsByID(productId);
	}

	@Override
	public String addFumetto(String name, String description, float price, String imagePath, String[] genres) throws SQLException {
		// Chiamata al DAO per aggiungere il prodotto
		return productServiceDAO.addProduct(name, description, price, imagePath, genres);
	}

	@Override
	public List<ProductBean> ricercaID(String search) throws SQLException {
		return productServiceDAO.searchAllProductsGestore(search);  // Chiamata al DAO per cercare i prodotti
	}

	@Override
	public List<ProductBean> ricercaGenere(String type) throws SQLException {
		return productServiceDAO.getAllProductsByType(type);  // Recupera prodotti per tipo
	}

	@Override
	public List<ProductBean> ricercaTitolo(String search) throws SQLException {
		return productServiceDAO.searchAllProducts(search);  // Cerca i prodotti dal DAO
	}

	@Override
	public String rmFumetto(String productId) throws SQLException {
		return productServiceDAO.removeProduct(productId);  // Invoca il DAO per rimuovere il prodotto
	}
}



