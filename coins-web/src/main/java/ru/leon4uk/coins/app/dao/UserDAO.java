package ru.leon4uk.coins.app.dao;

import ru.leon4uk.coins.app.domain.User;

import java.util.List;

public interface UserDAO {

    public void addUser(User user);

    public List<User> listUser();

    public void removeUser(Integer id);
}
