package pointsets;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive nearest-neighbor implementation using a linear scan.
 */
public class NaivePointSet<T extends Point> implements PointSet<T> {
    private Node root;

    /**
     * Instantiates a new NaivePointSet with the given points.
     *
     * @param points a non-null, non-empty list of points to include
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public NaivePointSet(List<T> points) {
        root = new Node(points.get(0), false);
        for (int i = 1; i < points.size(); i++) {
            insert(root, points.get(i));
        }
    }

    private class Node {
        public Point value;
        public boolean isVertical;
        public Node left = null;
        public Node right = null;

        Node(Point value, boolean isVertical) {
            this.value = value;
            this.isVertical = isVertical;
        }
    }

    private void insert(Node currNode, Point point) {
        if (currNode.isVertical) {
            if (currNode.value.y() <= point.y()) {
                if (currNode.right == null) {
                    currNode.right = new Node(point, false);
                } else {
                    insert(currNode.right, point);
                }
            } else {
                if (currNode.left == null) {
                    currNode.left = new Node(point, false);
                } else {
                    insert(currNode.left, point);
                }
            }
        } else { // isHorizontal
            if (currNode.value.x() <= point.x()) {
                if (currNode.right == null) {
                    currNode.right = new Node(point, true);
                } else {
                    insert(currNode.right, point);
                }
            } else {
                if (currNode.left == null) {
                    currNode.left = new Node(point, true);
                } else {
                    insert(currNode.left, point);
                }
            }
        }
    }

    /**
     * Returns the point in this set closest to the given point in O(N) time, where N is the number
     * of points in this set.
     */
    @Override
    public T nearest(Point target) {
        return (T) nearestHelper(root, target, root).value;
    }

    private Node nearestHelper(Node currNode, Point goal, Node best) {
        if (currNode == null) {
            return best;
        }
        if (currNode.value.distanceSquaredTo(goal) < best.value.distanceSquaredTo(goal)) {
            best = currNode;
        }
            best = nearestHelper(currNode.left, goal, best);
            best = nearestHelper(currNode.right, goal, best);
        return best;
    }

    @Override
    public List<T> allPoints() {
        List<T> result = new ArrayList<T>();
        allPointsHelper(root, result);
        return result;
    }

    private void allPointsHelper(Node currNode, List<T> result) {
        if (currNode != null) {
            result.add((T) currNode.value);
            allPointsHelper(currNode.left, result);
            allPointsHelper(currNode.right, result);
        }
    }
}
