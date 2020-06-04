package ru.indoornav.authentificate;

import org.apache.commons.lang3.StringUtils;
import ru.indoornav.User;
import ru.indoornav.dao.implementation.IndoorNavDAO;
import ru.indoornav.lambdaworks.crypto.SCryptUtil;

public class AuthenticationDetails
{
    public String username;

    public char[] password;

    public boolean isSuperuser = false;

    private String token;

    private PBKDF2Hasher hasher;


    public AuthenticationDetails(final String username, final String password)
    {
        this.username = username;
        this.password = password.toCharArray();
    }

    public AuthenticationDetails(final String usertype, PBKDF2Hasher hasher) {
        this.hasher = hasher;
        if(usertype.equals("superuser")) {
            isSuperuser = true;
            IndoorNavDAO indoorNavDAO = new IndoorNavDAO();
            UserService userService = indoorNavDAO.getSuperuserCredentials();

            this.username = String.valueOf(userService.getUserId());
            this.token = userService.getUserToken();

        } else if(usertype.equals("user")) {
            isSuperuser = false;

        }
    }



    public String getToken() {
        return this.token;
    }

    public void checkUser(int tabelId) {
        IndoorNavDAO indoorNavDAO = new IndoorNavDAO();
        UserService user  = indoorNavDAO.getUserCredentials(tabelId);
        System.out.println("Returned ID: " + user.getUserId());
        System.out.println("Returnted token: " + user.getUserToken());
        this.username = String.valueOf(user.getUserId());
        this.token = user.getUserToken();

    }

    public boolean authentificate(String submittedUsername, String submittedPass) {
        IndoorNavDAO indoorNavDAO = new IndoorNavDAO();
        UserService userService = indoorNavDAO.getUserCredentials(Integer.parseInt(submittedUsername));
        return StringUtils.equals(submittedUsername, String.valueOf(userService.getUserId())) && SCryptUtil.check(submittedPass, userService.getUserToken());
    }
}