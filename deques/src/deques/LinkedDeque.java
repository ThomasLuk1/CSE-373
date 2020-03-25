package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back; // may be the same as front, if you're using circular sentinel nodes

    public LinkedDeque() {
        size = 0;
        front = new Node<T>(null);
        back = new Node<T>(null);
        front.next = back;
        back.prev = front;
    }

    static class Node<T> {
        // IMPORTANT: Do not rename these fields or change their visibility.
        // We access these during grading to test your code.
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> newNode = new Node(item);
        front.next.prev = newNode;
        newNode.next = front.next;
        newNode.prev = front;
        front.next = newNode;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> newNode = new Node(item);
        back.prev.next = newNode;
        newNode.prev = back.prev;
        newNode.next = back;
        back.prev = newNode;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T removed = front.next.value;
        front.next.next.prev = front;
        front.next = front.next.next;
        size -= 1;
        return removed;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removed = back.prev.value;
        back.prev.prev.next = back;
        back.prev = back.prev.prev;
        size -= 1;
        return removed;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> curr = front.next;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.value;
    }

    public int size() {
        return size;
    }
}
