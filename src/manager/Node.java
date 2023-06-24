package manager;

import task.Task;

public class Node {
    Task task;
    Node next;
    Node previous;

    public Node(Node previous, Task task, Node next) {
        this.previous = previous;
        this.task = task;
        this.next = next;
    }
}
