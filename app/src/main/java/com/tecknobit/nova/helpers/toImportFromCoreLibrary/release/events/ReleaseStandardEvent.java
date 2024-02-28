package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events;

import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release;

import java.util.UUID;

public class ReleaseStandardEvent extends ReleaseEvent {

    public static final String RELEASE_EVENT_STATUS_KEY = "status";

    protected final Release.ReleaseStatus status;

    // TODO: 25/02/2024 TO REMOVE
    public ReleaseStandardEvent(Release.ReleaseStatus status) {
        this(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis(),
                status);
    }

    public ReleaseStandardEvent(String id, long releaseEventDate, Release.ReleaseStatus status) {
        super(id, releaseEventDate);
        this.status = status;
    }

    public Release.ReleaseStatus getStatus() {
        return status;
    }

}
