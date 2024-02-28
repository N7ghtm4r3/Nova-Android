package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events;

import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.apimanager.formatters.TimeFormatter;
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.NovaItem;

import java.util.UUID;

@Structure
public abstract class ReleaseEvent extends NovaItem {

    public static final String RELEASE_EVENT_KEY = "releaseEvent";

    public static final String RELEASE_IDENTIFIER_KEY = "releaseEventId";

    public static final String RELEASE_EVENT_DATE_KEY = "releaseEventDate";

    protected final long releaseEventDate;

    // TODO: 25/02/2024 TO REMOVE
    public ReleaseEvent() {
        this(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis());
    }

    public ReleaseEvent(String id, long releaseEventDate) {
        super(id);
        this.releaseEventDate = releaseEventDate;
    }

    public long getReleaseEventTimestamp() {
        return releaseEventDate;
    }

    public String getReleaseEventDate() {
        return TimeFormatter.getStringDate(releaseEventDate);
    }

}
