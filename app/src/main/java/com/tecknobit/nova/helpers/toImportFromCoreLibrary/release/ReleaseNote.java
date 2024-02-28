package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release;

import com.tecknobit.nova.helpers.toImportFromCoreLibrary.NovaItem;

import java.util.UUID;

public class ReleaseNote extends NovaItem {

    public static final String RELEASE_NOTE_CONTENT_KEY = "content";

    private final String content;

    // TODO: 25/02/2024 TO REMOVE
    public ReleaseNote(String content) {
        this(UUID.randomUUID().toString().replace("-", ""), content);
    }

    public ReleaseNote(String id, String content) {
        super(id);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
