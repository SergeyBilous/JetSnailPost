package ru.home.post.writer.commons;

public enum Status {
    ACCEPTED(1L),
    WAITING_FOR_TRANSPORT(2L),
    EN_ROUTE(3L),
    READY_FOR_DELIVERY(4L);
    public final Long label;

    Status(Long label) {
        this.label = label;
    }

    public static Status get(Long label) {
        for (Status s : values()) {
            if (s.label.equals(label))
                return s;
        }
        return null;
    }
}
