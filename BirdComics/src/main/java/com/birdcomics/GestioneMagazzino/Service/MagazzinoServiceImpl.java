package com.birdcomics.GestioneMagazzino.Service;

import com.birdcomics.Bean.MagazzinoBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.MagazzinoDao;
import com.birdcomics.Dao.ScaffaleDao;

import java.sql.SQLException;
import java.util.List;

public class MagazzinoServiceImpl implements MagazzinoService {

    private ScaffaleDao scaffaleDao;
	private MagazzinoDao magazzinoDao;

    public MagazzinoServiceImpl() {
        this.scaffaleDao = new ScaffaleDao();
        this.magazzinoDao = new MagazzinoDao();
    }

    @Override
    public List<ScaffaliBean> getScaffaleMagazzino(UserBean user) throws SQLException {
        return scaffaleDao.getScaffaleMagazzino(user.getMagazzino().getNome());
    }
    
    @Override
    public List<MagazzinoBean> getAllMagazzini() throws SQLException {
        return magazzinoDao.getMagazzini();
    }
}
