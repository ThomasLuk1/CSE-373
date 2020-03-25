package deques;

import org.junit.jupiter.api.Test;

public class LinkedDequeTests extends AbstractDequeTests {
    public static <T> LinkedDequeAssert<T> assertThat(LinkedDeque<T> deque) {
        return new LinkedDequeAssert<>(deque);
    }

    @Override
    protected <T> Deque<T> createDeque() {
        return new LinkedDeque<>();
    }

    @Override
    protected <T> void checkInvariants(Deque<T> deque) {
        // cast so we can use the LinkedDeque-specific version of assertThat defined above
        assertThat((LinkedDeque<T>) deque).isValid();
    }

    @Test
    void testAddFirst() {
        LinkedDeque<Integer> testDeque = new LinkedDeque();
        for (int i = 0; i <= 19; i++)  {
            testDeque.addFirst(i);
        }
        System.out.println("size: " + testDeque.size());
        for (int i = 0; i <= 19; i++)  {
            System.out.println(testDeque.removeFirst());
        }
        System.out.println("size: " + testDeque.size());
    }
    // You can write additional tests here if you only want them to run for LinkedDequeTests and not ArrayDequeTests
}
