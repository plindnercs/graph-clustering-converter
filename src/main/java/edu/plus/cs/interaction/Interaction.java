package edu.plus.cs.interaction;

public class Interaction {
    private long sourceId;
    private long targetId;
    private InteractionType interactionType;
    private String timestamp;

    public Interaction(long sourceId, long targetId, InteractionType interactionType, String timestamp) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.interactionType = interactionType;
        this.timestamp = timestamp;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
