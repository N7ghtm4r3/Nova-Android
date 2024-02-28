package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events;

import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseTag;

import java.io.Serializable;

public record RejectedTag(ReleaseTag tag, String comment) implements Serializable {

    public static final String TAG_KEY = "tag";

    public static final String COMMENT_KEY = "comment";

}
