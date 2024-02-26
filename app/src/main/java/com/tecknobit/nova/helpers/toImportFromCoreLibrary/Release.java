package com.tecknobit.nova.helpers.toImportFromCoreLibrary;

import com.tecknobit.apimanager.formatters.TimeFormatter;

import java.util.List;
import java.util.UUID;

public class Release extends NovaItem {

    public static final String RELEASE_KEY = "release";
    
    public static final String RELEASE_IDENTIFIER_KEY = "releaseId";
    
    public static final String RELEASE_VERSION_KEY = "releaseVersion";

    public static final String RELEASE_NOTES_KEY = "releaseNotes";

    public static final String CREATION_DATE_KEY = "creationDate";

    public static final String APPROBATION_DATE_KEY = "approbationDate";

    private final String releaseVersion;

    private final List<ReleaseNote> releaseNotes;

    private final long creationDate;

    private final long approbationDate;

    // TODO: 25/02/2024 TO REMOVE
    public Release(String releaseVersion) {
        this(UUID.randomUUID().toString().replace("-", ""), "v. " + releaseVersion,
                List.of(new ReleaseNote("Release note 1"), new ReleaseNote("" +
                        "Nota un po piu lunga per testare bene il layout e sperarare che sia un bel layout perché non è il mio punto forte e spero di migliorarlo")),
                System.currentTimeMillis(), System.currentTimeMillis());
    }

    public Release(String id, String releaseVersion, List<ReleaseNote> releaseNotes, long creationDate,
                   long approbationDate) {
        super(id);
        this.releaseVersion = releaseVersion;
        this.releaseNotes = releaseNotes;
        this.creationDate = creationDate;
        this.approbationDate = approbationDate;
    }

    public String getReleaseVersion() {
        return releaseVersion;
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
