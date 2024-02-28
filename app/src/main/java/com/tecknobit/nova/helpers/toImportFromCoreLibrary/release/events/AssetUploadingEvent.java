package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events;

import static com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Verifying;

import java.util.Random;
import java.util.UUID;

public class AssetUploadingEvent extends ReleaseStandardEvent {

    public static final String RELEASE_ASSET_KEY_URL = "assetUrl";

    public static final String COMMENTED_KEY = "commented";

    private final String assetUrl;

    private final boolean commented;

    // TODO: 25/02/2024 TO REMOVE
    public AssetUploadingEvent() {
        this(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis(),
                "", new Random().nextBoolean());
    }

    public AssetUploadingEvent(String id, long releaseEventDate, String assetUrl, boolean commented) {
        super(id, releaseEventDate, Verifying);
        this.assetUrl = assetUrl;
        this.commented = commented;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public boolean isCommented() {
        return commented;
    }

}
