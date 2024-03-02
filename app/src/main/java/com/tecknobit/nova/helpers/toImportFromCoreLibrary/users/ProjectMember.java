package com.tecknobit.nova.helpers.toImportFromCoreLibrary.users;

import com.tecknobit.nova.helpers.toImportFromCoreLibrary.NovaItem;

import java.util.UUID;

public class ProjectMember extends NovaItem {

    private final String name;

    private final String surname;

    private final String email;

    private final String profilePicUrl;

    // TODO: 25/02/2024 TO REMOVE
    public ProjectMember(String name, String surname) {
        this(UUID.randomUUID().toString().replace("-", ""),
                name, surname, name + surname + "@gmail.com",
                "https://t3.ftcdn.net/jpg/05/58/61/32/360_F_558613274_Z1zbjnHZKjpnTvvsjfZzYXk2TIeUl54a.jpg");
    }

    public ProjectMember(String id, String name, String surname, String email, String profilePicUrl) {
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
