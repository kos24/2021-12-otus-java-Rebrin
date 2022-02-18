package homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {

    }

    static TestLogging createTestLoggingProxied() {

        InvocationHandler handler = new CustomInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{TestLogging.class}, handler);
    }

    static class CustomInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;
        private final Map<Method, Boolean> annotatedMethods;

        public CustomInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
            this.annotatedMethods = Arrays.stream(testLogging.getClass().getDeclaredMethods())
                    .collect(Collectors.toMap(method -> method, method -> method.isAnnotationPresent(Log.class)));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object object = method.invoke(testLogging, args);
            annotatedMethods.forEach((m, annotated) -> {
                if (methodMatches(m, method, annotated)) {
                    System.out.printf("executed method: %s, params: %s%n", method.getName(), printParameters(args));
                }
            });
            return object;
        }

        private Map<String, String> printParameters(Object... args) {
            Map<String, String> params = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                params.put("param" + (i + 1), args[i].toString());
            }
            return params;
        }

        private boolean methodMatches(Method method, Method methodToCompare, Boolean annotated) {
            return method.getName().equals(methodToCompare.getName())
                    && Arrays.stream(method.getParameterTypes()).allMatch(p -> Arrays.asList(methodToCompare.getParameterTypes()).contains(p))
                    && method.getParameterCount() == methodToCompare.getParameterCount()
                    && Boolean.TRUE.equals(annotated);
        }
    }
}
