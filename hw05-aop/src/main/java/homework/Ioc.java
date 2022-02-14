package homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ioc {

    private Ioc() {

    }

    static TestLogging createTestLoggingProxied() {

        InvocationHandler handler = new CustomInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{TestLogging.class}, handler);
    }

    static class CustomInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;

        public CustomInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object object = method.invoke(testLogging, args);
            Arrays.stream(testLogging.getClass().getDeclaredMethods())
                    .filter(m ->
                            m.getName().equals(method.getName())
                                    && Arrays.stream(m.getParameterTypes()).allMatch(p -> Arrays.asList(method.getParameterTypes()).contains(p))
                                    && m.getParameterCount() == method.getParameterCount()
                                    && m.isAnnotationPresent(Log.class)
                    ).forEach(m -> System.out.printf("executed method: %s, params: %s%n", method.getName(), printParameters(args)));
            return object;
        }

        private Map<String, String> printParameters(Object... args) {
            Map<String, String> params = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                params.put("param" + (i + 1), args[i].toString());
            }
            return params;
        }
    }
}
