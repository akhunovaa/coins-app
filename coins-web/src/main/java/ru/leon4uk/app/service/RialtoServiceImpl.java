package ru.leon4uk.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.leon4uk.app.dao.RialtoDAO;
import ru.leon4uk.app.domain.Rialto;

import java.util.List;

@Service
public class RialtoServiceImpl implements RialtoService{

    @Autowired
    private RialtoDAO currencyPairDAO;

    @Override
    public List<Rialto> listRialto() {
        return currencyPairDAO.listRialto();
    }
}
