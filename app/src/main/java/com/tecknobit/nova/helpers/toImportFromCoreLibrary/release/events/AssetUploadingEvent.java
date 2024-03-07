package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events;

import static com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Verifying;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AssetUploadingEvent extends ReleaseStandardEvent {

    public static final String RELEASE_ASSET_KEY_URL = "assetUrl";

    public static final String COMMENTED_KEY = "commented";

    private final List<String> assetUrl;

    private final boolean commented;

    // TODO: 25/02/2024 TO REMOVE
    public AssetUploadingEvent() {
        this(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis(),
                List.of(
                        "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg"
                ), new Random().nextBoolean());
    }

    public AssetUploadingEvent(String id, long releaseEventDate, List<String> assetUrl,
                               boolean commented) {
        super(id, releaseEventDate, Verifying);
        this.assetUrl = assetUrl;
        this.commented = commented;
    }

    public List<String> getAssetUrl() {
        return assetUrl;
    }

    public boolean isCommented() {
        return commented;
    }

}
