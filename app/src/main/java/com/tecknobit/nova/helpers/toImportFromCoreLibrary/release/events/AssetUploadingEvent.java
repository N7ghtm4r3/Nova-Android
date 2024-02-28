package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events;

import java.util.UUID;

public class AssetUploadingEvent extends ReleaseEvent {

    public static final String RELEASE_ASSET_KEY_URL = "assetUrl";

    private final String assetUrl;

    // TODO: 25/02/2024 TO REMOVE
    public AssetUploadingEvent() {
        this(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis(),
                "");
    }

    public AssetUploadingEvent(String id, long releaseEventDate, String assetUrl) {
        super(id, releaseEventDate);
        this.assetUrl = assetUrl;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

}
