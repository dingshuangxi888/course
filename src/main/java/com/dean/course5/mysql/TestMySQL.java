package com.dean.course5.mysql;

/**
 * Created by dean on 15/1/4.
 */
public class TestMySQL {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        long userId = System.currentTimeMillis();
        System.out.println("userId: " + userId);
        User user = new User();
        user.setUserId(userId);
        user.setUserName("Dean");

        System.out.println("======== save user =========");
        userDao.save(user);


        System.out.println("======== query user =========");
        User user2 = userDao.queryById(userId);

        System.out.println("userId: " + user2.getUserId() + "; userName: " + user2.getUserName());
    }
}
