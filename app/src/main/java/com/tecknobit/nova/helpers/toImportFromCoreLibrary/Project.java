package com.tecknobit.nova.helpers.toImportFromCoreLibrary;

import java.util.UUID;

public class Project {

    private final String id;

    private final String logoUrl;

    private final String name;

    private final String workingProgressVersion;

    // TODO: 25/02/2024 TO REMOVE
    public Project(String name) {
        this(name, "1.0.0");
    }

    // TODO: 25/02/2024 TO REMOVE
    public Project(String name, String workingProgressVersion) {
        this(UUID.randomUUID().toString().replace("-", ""),
                "https://t3.ftcdn.net/jpg/05/69/72/02/360_F_569720237_58rhoQoMjxyB0QCeXQK0OVUA0qNogTmq.jpg",
                name, workingProgressVersion);
    }

    public Project(String id, String logoUrl, String name, String workingProgressVersion) {
        this.id = id;
        this.logoUrl = logoUrl;
        this.name = name;
        this.workingProgressVersion = workingProgressVersion;
    }

    public String getId() {
        return id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getName() {
        return name;
    }

    public String getWorkingProgressVersion() {
        return workingProgressVersion;
    }

    public String getWorkingProgressVersionText() {
        if(workingProgressVersion.startsWith("v. "))
            return workingProgressVersion;
        return "v. " + workingProgressVersion;
    }

}
