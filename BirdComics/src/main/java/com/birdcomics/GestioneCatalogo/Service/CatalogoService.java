package com.birdcomics.GestioneCatalogo.Service;

import java.sql.SQLException;
import java.util.List;
import com.birdcomics.Bean.ProductBean;

public interface CatalogoService {
    List<ProductBean> visualizzaCatalogo() throws SQLException;
    ProductBean visualizzaDettagli(String productId) throws SQLException;
	String addFumetto(String name, String description, float price, String imagePath, String[] genres)throws SQLException;
	List<ProductBean> ricercaID(String search) throws SQLException;
	List<ProductBean> ricercaGenere(String type) throws SQLException;
	List<ProductBean> ricercaTitolo(String search) throws SQLException;
	String rmFumetto(String productId) throws SQLException;
	//String updateProduct(String productId, String name, String info, float price, String image, String[] genres) throws SQLException;
}
