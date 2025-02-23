package com.birdcomics.GestioneMagazzino.Service;

import com.birdcomics.Bean.MagazzinoBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Bean.UserBean;

import java.sql.SQLException;
import java.util.List;

public interface MagazzinoService {
    List<ScaffaliBean> getScaffaleMagazzino(String email) throws SQLException;
	List<MagazzinoBean> getAllMagazzini() throws SQLException;
}
