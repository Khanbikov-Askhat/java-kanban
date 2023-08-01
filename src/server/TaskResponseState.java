package server;

public enum TaskResponseState {
    CREATED,
    UPDATED,
    ALREADY_EXISTS,
    OVERLAP_BY_TIME,
    DELETED,
    HAS_NULL_FIELDS,
    NOT_DELETED,
    NOT_FOUND
}