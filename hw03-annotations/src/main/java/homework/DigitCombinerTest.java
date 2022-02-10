package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class DigitCombinerTest {

    DigitCombiner digitCombiner = new DigitCombiner();

    @Before
    void setUp() {
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

    @Test
    void shouldAddTwoNumbers() {
        System.out.println(digitCombiner.sum(2, 2) == 4);
    }

    @Test
    void shouldThrowException() {
        throw new RuntimeException();
    }

    @After
    void after() {
        System.out.println("Экземпляр тестового класса: " + Integer.toHexString(hashCode()));
    }

}
