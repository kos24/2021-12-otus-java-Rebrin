package homework;

public class TestLoggingImpl implements TestLogging {

    @Override
    @Log
    public void calculation(int param) {
        System.out.printf("calculation: %s%n", param);
    }

    @Override
    public void calculation(int param1, int param2) {
        System.out.printf("calculation: %s, %s%n", param1, param2);
    }

    @Override
    @Log
    public void calculation(int param1, String param2) {
        System.out.printf("calculation: %s, %s%n", param1, param2);
    }
}
