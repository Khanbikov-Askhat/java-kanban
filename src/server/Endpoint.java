package server;

public enum Endpoint { // Вынесла запросы в перечисление
    GET_ALL_TASKS,
    GET_HISTORY,
    GET_TASKS,
    GET_EPICS,
    GET_SUBTASKS,
    GET_TASK_ID,
    GET_EPIC_ID,
    GET_SUBTASK_ID,
    GET_SUBTASKS_EPIC,
    POST_TASK_ID,
    POST_EPIC_ID,
    POST_SUBTASK_ID,
    DELETE_TASKS,
    DELETE_EPICS,
    DELETE_SUBTASKS,
    DELETE_TASK_ID,
    DELETE_EPIC_ID,
    DELETE_SUBTASK_ID,
    UNKNOWN
}
