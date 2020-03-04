package ru.home.post.writer.entities;

import java.util.HashMap;
import java.util.Map;

public enum ParcelStatus {
    ACCEPTED(1L),
    WAITING_FOR_TRANSPORT(2L),
    EN_ROUTE(3L),
    READY_FOR_DELIVERY(4L);
    public Long label;


    private ParcelStatus(Long label) {
        this.label = label;
    }
    private static final Map<Long, ParcelStatus> BY_LABEL = new HashMap<>();

    static {
        for (ParcelStatus e: values()) {
            BY_LABEL.put(e.label, e);
        }
    }
    public static ParcelStatus valueOfLabel(Long label) {
        return BY_LABEL.get(label);
    }
    @Override
    public String toString() {
        if (label == 1L)
            return "Accepted";
        if (label == 2L)
            return "Waiting";
        if (label == 3)
            return "En route";
        return "Ready";
    }
}
