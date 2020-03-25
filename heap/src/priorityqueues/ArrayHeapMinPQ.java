package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class ArrayHeapMinPQ<T extends Comparable<T>> implements ExtrinsicMinPQ<T> {
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    HashMap<T, Integer> itemIndex;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        itemIndex = new HashMap<T, Integer>();
        for (int i = 0; i < START_INDEX; i++) {
            items.add(null);
        }
    }

    private void swap(int a, int b) {
        swapIndex(items.get(a).getItem(), items.get(b).getItem());
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    private void swapIndex(T a, T b) {
        int temp = itemIndex.get(a);
        itemIndex.put(a, itemIndex.get(b));
        itemIndex.put(b, temp);
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || itemIndex.containsKey(item)) {
            throw new IllegalArgumentException("Item is null or is already present in the PQ");
        }
        PriorityNode<T> newNode = new PriorityNode<T>(item, priority);
        items.add(newNode);
        itemIndex.put(item, size());
        percolateUp(size());
    }

    @Override
    public boolean contains(T item) {
        return itemIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        T minItem = items.get(START_INDEX).getItem();
        PriorityNode<T> endNode = items.get(size());
        items.set(START_INDEX, endNode);
        items.remove(size());
        percolateDown(START_INDEX);
        itemIndex.remove(minItem);
        return minItem;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!itemIndex.keySet().contains(item)) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<T> newNode = new PriorityNode<T>(item, priority);
        items.set(itemIndex.get(item), newNode);
        percolateUp(itemIndex.get(item));
        percolateDown(itemIndex.get(item));

    }

    @Override
    public int size() {
        return items.size() - 1;
    }

    private void percolateUp(int i) {
        while (hasParent(i) && items.get(i).getPriority() < items.get(getParentIndex(i)).getPriority()) {
            swap(getParentIndex(i), i);
            itemIndex.put(items.get(i).getItem(), i);
            i = getParentIndex(i);
        }
    }

    private void percolateDown(int i) {
        while (hasLeftChild(i)) {
            int smallerChildIndex = getLeftChildIndex(i);
            if (hasRightChild(i) && rightChild(i).getPriority() < leftChild(i).getPriority()) {
                smallerChildIndex = getRightChildIndex(i);
            }
            if (items.get(i).getPriority() < items.get(smallerChildIndex).getPriority()) {
                itemIndex.put(items.get(i).getItem(), i);
                break;
            } else {
                swap(i, smallerChildIndex);
            }
            i = smallerChildIndex;
        }
    }

    // Helper Methods referenced from @bephrem1
    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex;
    }
    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex) / 2;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) <= size();
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) <= size();
    }

    private boolean hasParent(int index) {
        return index != 1 && getParentIndex(index) >= 1;
    }

    private PriorityNode<T> leftChild(int index) {
        return items.get(getLeftChildIndex(index));
    }

    private PriorityNode<T> rightChild(int index) {
        return items.get(getRightChildIndex(index));
    }
}
