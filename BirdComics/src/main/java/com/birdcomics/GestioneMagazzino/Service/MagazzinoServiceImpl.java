package com.birdcomics.GestioneMagazzino.Service;

import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Model.Dao.MagazzinoDao;
import com.birdcomics.Model.Dao.ScaffaleDao;
import com.birdcomics.Model.Dao.UserServiceDAO;

import java.sql.SQLException;
import java.util.List;

public class MagazzinoServiceImpl implements MagazzinoService {

    private ScaffaleDao scaffaleDao;
	private MagazzinoDao magazzinoDao;
	private UserServiceDAO userServiceDao;
	

    public MagazzinoServiceImpl() {
        this.scaffaleDao = new ScaffaleDao();
        this.magazzinoDao = new MagazzinoDao();
        this.userServiceDao = new UserServiceDAO();
    }

    @Override
    public List<ScaffaliBean> getScaffaleMagazzino(String email) throws SQLException {
    	UserBean user = userServiceDao.getUserDetails(email);
        return scaffaleDao.getScaffaleMagazzino(user.getMagazzino().getNome());
    }
    
    @Override
    public List<MagazzinoBean> getAllMagazzini() throws SQLException {
        return magazzinoDao.getMagazzini();
    }
}
