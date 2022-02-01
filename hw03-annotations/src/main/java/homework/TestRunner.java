package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {

    public <T> void start(Class<T> clazz) {

        Map<Status, Integer> result = new EnumMap<>(Status.class);

        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        Method[] methodsAll = clazz.getDeclaredMethods();
        for (Method method : methodsAll) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }

        validateAnnotations(beforeMethods);
        validateAnnotations(afterMethods);

        testMethods.forEach(method -> {
            Object object = ReflectionHelper.instantiate(clazz);
            try {
                invokeBeforeMethod(object, beforeMethods);
                invokeTestMethod(object, method);
                System.out.printf("УСПЕХ: %s%n", method.getName());
                System.out.println("========================================");
                result.put(Status.SUCCESS, result.get(Status.SUCCESS) == null ? 1 : result.get(Status.SUCCESS) + 1);
            } catch (TestException e) {
                System.out.printf("ОШИБКА: %s%n", e.getMessage());
                System.out.println("========================================");
                result.put(Status.FAILURE, result.get(Status.FAILURE) == null ? 1 : result.get(Status.FAILURE) + 1);
            } finally {
                invokeAfterMethod(object, afterMethods);
            }
        });
        Integer success = result.get(Status.SUCCESS) == null ? 0 : result.get(Status.SUCCESS);
        Integer failure = result.get(Status.FAILURE) == null ? 0 : result.get(Status.FAILURE);
        System.out.printf("Результаты теста. %n" +
                        "Общее количество тестов: %d%n" +
                        "Количество успешных тестов: %d%n" +
                        "Количество упавших тестов: %d",
                success + failure,
                success,
                failure);
    }

    private void invokeBeforeMethod(Object object, List<Method> beforeMethods) {
        if (!beforeMethods.isEmpty()) {
            try {
                System.out.println("========================================");
                System.out.printf("Вызов метода @Before: %s%n", beforeMethods.get(0).getName());
                beforeMethods.get(0).invoke(object);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void invokeTestMethod(Object object, Method method) {
        try {
            System.out.printf("%nВыполняется тест: %s%n%n", method.getName());
            method.invoke(object);

        } catch (InvocationTargetException e) {
            throw new TestException(e.getCause());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void invokeAfterMethod(Object object, List<Method> afterMethods) {
        if (!afterMethods.isEmpty()) {
            try {
                System.out.printf("Вызов метода @After: %s%n", afterMethods.get(0).getName());
                afterMethods.get(0).invoke(object);
                System.out.printf("========================================%n%n");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateAnnotations(List<Method> methods) {
        if (!methods.isEmpty() && methods.size() > 1) {
            throw new RuntimeException(String.format("Допустим только один метод c аннотацией: %s, найдено %d",
                    methods.get(0).getDeclaredAnnotations()[0], methods.size()));
        }
    }
}