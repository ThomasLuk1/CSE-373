package pointsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fast nearest-neighbor implementation using a k-d tree.
 */
public class KDTreePointSet<T extends Point> implements PointSet<T> {
    private Node root;

    /**
     * Instantiates a new KDTreePointSet with a shuffled version of the given points.
     * <p>
     * Randomizing the point order decreases likeliness of ending up with a spindly tree if the
     * points are sorted somehow.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public static <T extends Point> KDTreePointSet<T> createAfterShuffling(List<T> points) {
        Collections.shuffle(points);
        return new KDTreePointSet<T>(points);
    }

    /**
     * Instantiates a new KDTreePointSet with the given points.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    KDTreePointSet(List<T> points) {
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
            if (currNode.value.y() < point.y()) {
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
            if (currNode.value.x() < point.x()) {
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
     * Returns the point in this set closest to the given point in (usually) O(log N) time, where
     * N is the number of points in this set.
     */
    @Override
    public T nearest(Point target) {
        return (T) nearestHelper(root, target, root).value;
    }

    private Node nearestHelper(Node currNode, Point goal, Node best) {
        if (currNode == null) {
            return best;
        }
        Node goodSide;
        Node badSide;
        if (currNode.value.distanceSquaredTo(goal) <= best.value.distanceSquaredTo(goal)) {
            best = currNode;
        }
        if (currNode.isVertical) {
            if (goal.y() <= currNode.value.y()) {
                goodSide = currNode.left;
                badSide = currNode.right;
            } else {
                goodSide = currNode.right;
                badSide = currNode.left;
            }
        } else {
            if (goal.x() <= currNode.value.x()) {
                goodSide = currNode.left;
                badSide = currNode.right;
            } else {
                goodSide = currNode.right;
                badSide = currNode.left;
            }
        }
        best = nearestHelper(goodSide, goal, best);
        if (mightHaveSomethingUseful(badSide, goal, best, currNode)) {
            best = nearestHelper(badSide, goal, best);
        }
        return best;
    }

    private boolean mightHaveSomethingUseful(Node badSide, Point goal, Node best, Node currNode) {
        if (badSide == null) {
            return false;
        }
        Point closestPoint;
        if (!currNode.isVertical) {
            closestPoint = new Point(currNode.value.x(), goal.y());
        } else {
            closestPoint = new Point(goal.x(), currNode.value.y());
        }
        double closestDistance = closestPoint.distanceSquaredTo(goal);
        if (closestDistance < best.value.distanceSquaredTo(goal)) {
            return true;
        }
        return false;
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
