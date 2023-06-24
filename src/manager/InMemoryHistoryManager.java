package manager;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public Node head;
    public Node tail;
    public Map<Integer, Node> nodeMap = new HashMap<>();


    private void linkLast(Task task) {
        final Node node = new Node(tail, task, null);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        removeNode(id);
        linkLast(task);
        nodeMap.put(id, tail);
    }


    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node n = head;
        while (n != null) {
            tasks.add(n.task);
            n = n.next;
        }
        return tasks;
    }


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    @Override
    public void remove(int id) {
        removeNode(id);
    }


    public void removeNode(int id) {
        final Node node = nodeMap.remove(id);
        if (node == null) {
            return;
        }
        if (node.previous != null) {
            node.previous.next = node.next;
            if (node.next == null) {
                tail = node.previous;
            } else {
                node.next.previous = node.previous;
            }
        } else {
            head = node.next;
            if (head == null) {
                tail = null;
            } else {
                head.previous = null;
            }
        }
    }
}
