package sprint.managers.historymanager;

import sprint.tasks.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> historyTasks = new LinkedList<>();
    private Node<Task> head;
    private Node<Task> tail;
    private final HashMap<Integer, Node<Task>> nodesMap;

    public InMemoryHistoryManager() {
        this.nodesMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        Node<Task> existingNode = nodesMap.get(task.getId());
        if (existingNode != null) {
            remove(task.getId());
        }
        Node<Task> newNode = new Node<>(tail, task, null);
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
        nodesMap.put(task.getId(), newNode);


        historyTasks.add(task);
    }


    @Override
    public void remove(int id) {
        Node<Task> nodeToRemove = nodesMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
            nodesMap.remove(id);
            historyTasks.remove(nodeToRemove.data);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

    private void removeNode(Node<Task> node) {
        if (node == null) return;

        final Node<Task> next = node.next;
        final Node<Task> prev = node.prev;

        if (prev == null) head = next;
        else prev.next = next;

        if (next == null) tail = prev;
        else next.prev = prev;
    }

    private static class Node<T> {
        private final T data;
        private Node<T> next;
        private Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
