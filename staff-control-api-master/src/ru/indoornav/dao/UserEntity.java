package ru.indoornav.dao;

import ru.indoornav.authentificate.PBKDF2Hasher;
import ru.indoornav.lambdaworks.crypto.SCryptUtil;

public class UserEntity {

    private String firstName;
    private String lastName;
    private String password;
    private int tabelId;
    private String workerPosition;

    public UserEntity(String firstName, String lastName, String password, String tabelId, String workerPosition) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.tabelId = Integer.parseInt(tabelId);
        this.workerPosition = workerPosition;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public String getToken() {

        String token = SCryptUtil.scrypt(password, 16, 16, 16);
        return token;
    }

    public int getTabelId() {
        return this.tabelId;
    }

    public String getWorkerPosition() {
        return this.workerPosition;
    }
}
