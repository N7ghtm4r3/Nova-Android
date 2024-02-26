package com.tecknobit.nova.helpers.toImportFromCoreLibrary;

import java.util.List;
import java.util.UUID;

public class Project extends NovaItem {

    public static final String PROJECT_KEY = "project";

    public static final String PROJECT_IDENTIFIER_KEY = "id";

    public static final String LOGO_URL_KEY = "logoUrl";

    public static final String PROJECT_NAME_KEY = "name";

    public static final String WORKING_PROGRESS_VERSION_KEY = "workingProgressVersion";

    public static final String PROJECT_RELEASES_KEY = "projectReleases";

    private final String logoUrl;

    private final String name;

    private final String workingProgressVersion;

    private final List<Release> releases;

    // TODO: 25/02/2024 TO REMOVE
    public Project(String name) {
        this(name, "1.0.0");
    }

    // TODO: 25/02/2024 TO REMOVE
    public Project(String name, String workingProgressVersion) {
        this(UUID.randomUUID().toString().replace("-", ""),
                "https://t3.ftcdn.net/jpg/05/69/72/02/360_F_569720237_58rhoQoMjxyB0QCeXQK0OVUA0qNogTmq.jpg",
                name, workingProgressVersion, List.of(new Release("1.0.1", Release.ReleaseStatus.Approved),
                        new Release("1.0.0", Release.ReleaseStatus.New)));
    }

    public Project(String id, String logoUrl, String name, String workingProgressVersion,
                   List<Release> releases) {
        super(id);
        this.logoUrl = logoUrl;
        this.name = name;
        this.workingProgressVersion = workingProgressVersion;
        this.releases = releases;
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
        if(workingProgressVersion == null)
            return null;
        if(workingProgressVersion.startsWith("v. "))
            return workingProgressVersion;
        return "v. " + workingProgressVersion;
    }

    public List<Release> getReleases() {
        return releases;
    }

}
