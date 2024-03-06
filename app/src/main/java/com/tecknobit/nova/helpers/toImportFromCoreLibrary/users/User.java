package com.tecknobit.nova.helpers.toImportFromCoreLibrary.users;

import java.util.UUID;

public class User extends PublicUser {

    public static final String USER_KEY = "user";

    public static final String USER_IDENTIFIER_KEY = "userId";

    public static final String TOKEN_KEY = "token";

    public static final String PASSWORD_KEY = "password";

    private final String token;

    private final String password;

    // TODO: 06/03/2024 TO REMOVE
    public User(String name, String surname) {
        super(name, surname);
        this.token = UUID.randomUUID().toString().replace("-", "");
        this.password = UUID.randomUUID().toString().replace("-", "");
    }

    public User(String id, String name, String surname, String email, String profilePicUrl,
                String token, String password) {
        super(id, name, surname, email, profilePicUrl);
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

}
