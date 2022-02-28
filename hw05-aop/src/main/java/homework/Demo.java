package homework;

public class Demo {
    public static void main(String[] args) {
        TestLogging testLogging = Ioc.createTestLoggingProxied();
        testLogging.calculation(5);
        testLogging.calculation(3,5);
        testLogging.calculation(7,"test");
    }
}
