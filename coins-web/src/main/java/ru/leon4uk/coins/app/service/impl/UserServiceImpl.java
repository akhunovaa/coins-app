package ru.leon4uk.coins.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leon4uk.coins.app.dao.UserDAO;
import ru.leon4uk.coins.app.domain.User;
import ru.leon4uk.coins.app.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public void addUser(User user) {
        userDAO.addUser(user);
    }

    @Override
    public List<User> listUser() {
       return userDAO.listUser();
    }

    @Override
    public void removeUser(Integer id) {
        userDAO.removeUser(id);
    }
}
