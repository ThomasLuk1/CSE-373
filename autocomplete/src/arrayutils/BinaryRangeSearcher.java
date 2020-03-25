package arrayutils;

import java.util.Arrays;
import java.util.Comparator;
/**
 * Make sure to check out the interface for more method details:
 * @see ArraySearcher
 */
public class BinaryRangeSearcher<T, U> implements ArraySearcher<T, U> {
    private final T[] array;
    private final Matcher<T, U> matcher;
    public static <T, U> BinaryRangeSearcher<T, U> forUnsortedArray(T[] array,
                                                                    Comparator<T> sortUsing,
                                                                    Matcher<T, U> matchUsing) {
        Arrays.sort(array, sortUsing);
        return new BinaryRangeSearcher<>(array, matchUsing);
    }

    protected BinaryRangeSearcher(T[] array, Matcher<T, U> matcher) {
        if (array == null) {
            throw new IllegalArgumentException();
        }
        for (T item : array) {
            if (item == null) {
                throw new IllegalArgumentException();
            }
        }
        this.array = array;
        this.matcher = matcher;
    }

    public MatchResult<T> findAllMatches(U target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        int first;
        int last;
        if (array.length == 1) {
            if (this.matcher.match(array[0], target) == 0) {
                first = 0;
                last = 0;
            } else {
                first = -1;
                last = -1;
            }
        } else {
            first = findFirst(0, array.length - 1, target);
            last = findLast(0, array.length - 1, target);
        }
        MatchResult<T> matchesArray;
        if (first == -1 && last == -1) {
           matchesArray = new MatchResult<T>(array, 0, 0);
        }
        else if (first == -1) {
            matchesArray = new MatchResult<T>(array, first, first + 1);
        }
        else if (last == -1) {
            matchesArray = new MatchResult<T>(array, last, last + 1);
        }
        else {
            matchesArray = new MatchResult<T>(array, first, last + 1);
        }
        return matchesArray;
    }

    private int findFirst(int left, int right, U target) {
        if (right >= left) {
            int frontIndex = (left + right) / 2;
            if (frontIndex == 0) {
                if (this.matcher.match(array[frontIndex], target) == 0) {
                    return frontIndex;
                } else {
                    return findFirst(frontIndex + 1, right, target);
                }
            }
            else if (this.matcher.match(array[frontIndex], target) == 0 &&
                this.matcher.match(array[frontIndex - 1], target) < 0) {
                return frontIndex;
            }
            else if (this.matcher.match(array[frontIndex], target) > 0 ||
                this.matcher.match(array[frontIndex - 1], target) == 0) {
                return findFirst(left, frontIndex - 1, target);
            }
            return findFirst(frontIndex + 1, right, target);
        }
        return -1;
    }

    private int findLast(int left, int right, U target) {
        if (right >= left) {
            int lastIndex = (left + right) / 2;
            if (lastIndex == array.length - 1) {
                if (this.matcher.match(array[lastIndex], target) == 0) { // found it
                    return lastIndex;
                } else {
                    return findLast(left, lastIndex - 1, target);
                }
            }
            else if (this.matcher.match(array[lastIndex], target) == 0 &&
                this.matcher.match(array[lastIndex + 1], target) > 0) { // found it
                return lastIndex;
            }
            else if (this.matcher.match(array[lastIndex], target) < 0 ||
                this.matcher.match(array[lastIndex + 1], target) == 0) {
                return findLast(lastIndex + 1, right, target); // recurse right
            }
            return findLast(left, lastIndex - 1, target); //recurse left
        }
        return -1;
    }

    public static class MatchResult<T> extends AbstractMatchResult<T> {
        final T[] array;
        final int start;
        final int end;
        protected MatchResult(T[] array) {
            this(array, 0, 0);
        }
        protected MatchResult(T[] array, int startInclusive, int endExclusive) {
            this.array = array;
            this.start = startInclusive;
            this.end = endExclusive;
        }
        @Override
        public int count() {
            return this.end - this.start;
        }
        @Override
        public T[] unsorted() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }
}
