package sprint4.managers.historymanager;

import sprint4.tasks.Task;

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
        } else {
            Node<Task> newNode = new Node<>(tail, task, null);
            if (tail != null) {
                tail.next = newNode;
            }
            tail = newNode;
            if (head == null) {
                head = newNode;
            }
            nodesMap.put(task.getId(), newNode);
        }

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

    @Override
    public void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        nodesMap.put(element.getId(), newNode);
        if (oldTail == null) head = newNode;
        else oldTail.next = newNode;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new LinkedList<>();
        Node<Task> currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
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

    private static class Node<Task> {
        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
