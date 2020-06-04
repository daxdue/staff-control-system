package ru.indoornav.authentificate;

public class UserService {

    private int userId;
    private String userToken;

    public UserService(int userId, String userToken) {
        this.userId = userId;
        this.userToken = userToken;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getUserToken() {
        return this.userToken;
    }
}
