package com.birdcomics.GestioneMagazzino.Service;

import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.ScaffaliBean;

import java.sql.SQLException;
import java.util.List;

public interface MagazzinoService {
    List<ScaffaliBean> getScaffaleMagazzino(String email) throws SQLException;
	List<MagazzinoBean> getAllMagazzini() throws SQLException;
}
