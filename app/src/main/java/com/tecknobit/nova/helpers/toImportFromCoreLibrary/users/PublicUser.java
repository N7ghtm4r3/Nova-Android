package com.tecknobit.nova.helpers.toImportFromCoreLibrary.users;

import com.tecknobit.nova.helpers.toImportFromCoreLibrary.NovaItem;

import java.util.UUID;

public class PublicUser extends NovaItem {

    public static final String PUBLIC_USER_KEY = "publicUser";

    public static final String PUBLIC_USER_IDENTIFIER_KEY = "publicUserId";

    public static final String NAME_KEY = "name";

    public static final String SURNAME_KEY = "surname";

    public static final String EMAIL_KEY = "email";

    public static final String PROFILE_PIC_URL_KEY = "profilePicUrl";

    private final String name;

    private final String surname;

    private final String email;

    private final String profilePicUrl;

    // TODO: 25/02/2024 TO REMOVE
    public PublicUser(String name, String surname) {
        this(UUID.randomUUID().toString().replace("-", ""),
                name, surname, name + surname + "@gmail.com",
                "https://t3.ftcdn.net/jpg/05/58/61/32/360_F_558613274_Z1zbjnHZKjpnTvvsjfZzYXk2TIeUl54a.jpg");
    }

    public PublicUser(String id, String name, String surname, String email, String profilePicUrl) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

}
