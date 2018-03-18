package ru.leon4uk.coins.web.dao;

import ru.leon4uk.coins.web.domain.User;

import java.util.List;

public interface UserDAO {

    public void addUser(User user);

    public List<User> listUser();

    public void removeUser(Integer id);
}
