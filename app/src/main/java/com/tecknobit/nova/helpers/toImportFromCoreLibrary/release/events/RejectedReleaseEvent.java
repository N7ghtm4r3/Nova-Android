package com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events;

import static com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseEvent.ReleaseTag.Bug;
import static com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseEvent.ReleaseTag.Issue;
import static com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseEvent.ReleaseTag.LayoutChange;
import static com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseEvent.ReleaseTag.Tip;

import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RejectedReleaseEvent extends ReleaseStandardEvent {

    public static final String REASONS_KEY = "reasons";

    public static final String TAGS_KEY = "tags";

    private final String reasons;

    private final List<RejectedTag> tags;

    // TODO: 25/02/2024 TO REMOVE
    public RejectedReleaseEvent() {
        this(UUID.randomUUID().toString().replace("-", ""), System.currentTimeMillis(),
                "Prova commento quando versione viene rifiutata e viene messo per iscritto il motivo di tale rifiuto!", new ArrayList<>(
                       List.of(
                               new RejectedTag(Bug, "Bug Prova commento quando versione viene rifiutata e viene messo per iscritto il motivo di tale rifiuto!"),
                               new RejectedTag(Tip, ""),
                               //new RejectedTag(Tip, "Tip Prova commento quando versione viene rifiutata e viene messo per iscritto il motivo di tale rifiuto! "),
                               new RejectedTag(LayoutChange, "LayoutChange Prova commento quando versione viene rifiutata e viene messo per iscritto il motivo di tale rifiuto!"),
                               new RejectedTag(Issue, "Issue Prova commento quando versione viene rifiutata e viene messo per iscritto il motivo di tale rifiuto!")
                       )
                ));
    }

    public RejectedReleaseEvent(String id, long releaseEventDate, String reasons,
                                List<RejectedTag> tags) {
        super(id, releaseEventDate, ReleaseStatus.Rejected);
        this.reasons = reasons;
        this.tags = tags;
    }

    public String getReasons() {
        return reasons;
    }

    public List<RejectedTag> getTags() {
        return tags;
    }

}
