package manager;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public Node<Task> head;
    public Node<Task> tail;
    public int size = 0;
    public Map<Integer, Node> historyTaskMap = new HashMap<>();

    public Node<Task> linkLast (Task task) {
        if (size >= 10) {
            final Node<Task> first = head;
            removeNode(first);
        }

        final Node<Task> oldTail = tail;
        final Node<Task> newNode;
        newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
        Task nodeTask = newNode.task;
        historyTaskMap.put(nodeTask.getId(), newNode);
        return newNode;
    }

    @Override
    public void add(Task task) {

            remove(task.getId());
            linkLast(task);


    }

    public List<Task> getTasks() {
        List<Task> listOfTasks = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            listOfTasks.add(node.task);
            node = node.next;
        }
        return listOfTasks;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyTaskMap.containsKey(id)) {
            removeNode(historyTaskMap.get(id));
            historyTaskMap.remove(id);
        }
    }

    public void removeNode(Node node) {
        Node<Task> previousNode = node.previous;
        Node<Task> nextNode = node.next;
        if (size == 1) {
            head = null;
            tail = null;
            node.task = null;
        } else if (size > 1) {
            if (previousNode == null) {
                head = nextNode;
                nextNode.previous = null;
                node.next = null;
                node.task = null;
            } else if (nextNode == null) {
                tail = previousNode;
                previousNode.next = null;
                node.previous = null;
                node.task = null;
            } else {
                previousNode.next = nextNode;
                nextNode.previous = previousNode;
                node.next = null;
                node.previous = null;
                node.task = null;
            }
        }
        if (size != 0) {
            size--;
        }
    }

}
