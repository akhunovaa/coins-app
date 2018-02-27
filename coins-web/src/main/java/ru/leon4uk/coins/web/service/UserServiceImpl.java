package ru.leon4uk.coins.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.leon4uk.coins.web.dao.UserDAO;
import ru.leon4uk.coins.web.domain.User;

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
    @Transactional
    public void removeUser(Integer id) {
        userDAO.removeUser(id);
    }
}
