package com.tecknobit.nova.helpers.toImportFromCoreLibrary;

import com.tecknobit.apimanager.formatters.TimeFormatter;

import java.util.List;
import java.util.UUID;

public class Release extends NovaItem {

    public static final String RELEASE_KEY = "release";
    
    public static final String RELEASE_IDENTIFIER_KEY = "releaseId";
    
    public static final String RELEASE_VERSION_KEY = "releaseVersion";

    public static final String RELEASE_STATUS_KEY = "releaseStatus";

    public static final String RELEASE_NOTES_KEY = "releaseNotes";

    public static final String CREATION_DATE_KEY = "creationDate";

    public static final String APPROBATION_DATE_KEY = "approbationDate";

    public enum ReleaseStatus {

        New("#e88f13"),

        Verifying("#B1AA2D"),

        Rejected("#E24747"),

        Approved("#86b49a"),

        Alpha("#AF6BDC"),

        Beta("#d073b8"),

        Latest("#3A98C7");

        private final String color;

        ReleaseStatus(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

    }


    public enum ReleaseTag {

        Bug("#E24747"),

        Issue("#AF6BDC"),

        LayoutChange("#3A98C7"),

        Tip("#1A50B5");

        private final String color;

        ReleaseTag(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

    }

    private final String releaseVersion;

    private final ReleaseStatus status;

    private final List<ReleaseNote> releaseNotes;

    private final long creationDate;

    private final long approbationDate;

    // TODO: 25/02/2024 TO REMOVE
    public Release(String releaseVersion, ReleaseStatus status) {
        this(UUID.randomUUID().toString().replace("-", ""), "v. " + releaseVersion,
                status, List.of(new ReleaseNote("Release note 1"), new ReleaseNote("" +
                        "Nota un po piu lunga per testare bene il layout e sperarare che sia un bel layout perché non è il mio punto forte e spero di migliorarlo")),
                System.currentTimeMillis(), System.currentTimeMillis());
    }

    public Release(String id, String releaseVersion, ReleaseStatus status, List<ReleaseNote> releaseNotes, long creationDate,
                   long approbationDate) {
        super(id);
        this.releaseVersion = releaseVersion;
        this.status = status;
        this.releaseNotes = releaseNotes;
        this.creationDate = creationDate;
        this.approbationDate = approbationDate;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public ReleaseStatus getStatus() {
        return status;
    }

    public List<ReleaseNote> getReleaseNotes() {
        return releaseNotes;
    }

    public long getCreationTimestamp() {
        return creationDate;
    }

    public String getCreationDate() {
        return TimeFormatter.getStringDate(creationDate);
    }

    public long getApprobationTimestamp() {
        return approbationDate;
    }

    public String getApprobationDate() {
        return TimeFormatter.getStringDate(approbationDate);
    }

}
