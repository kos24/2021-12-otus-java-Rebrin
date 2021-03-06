package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exceptions.ContainerInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object object;
        try {
            object = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ContainerInstantiationException("No args constructor required");
        }
        List<Method> appMethods = getAnnotatedMethodsInOrder(configClass);
        if (appMethods.isEmpty()) {
            throw new ContainerInstantiationException("No AppComponent present in config file");
        }
        fillAppComponentsMap(object, appMethods);
    }

    private void fillAppComponentsMap(Object object, List<Method> appMethods) {
        for (Method method : appMethods) {
            Object[] args = new Object[method.getParameterCount()];

            try {
                Object component;

                for (int i = 0; i < args.length; i++) {

                    args[i] = getAppComponent(method.getParameterTypes()[i]);
                    if (args[i] == null) {
                        throw new ContainerInstantiationException(String.format("Component of type: %s not found",
                                method.getParameterTypes()[i]));
                    }
                }
                component = method.invoke(object, args);
                appComponents.add(component);
                if (appComponentsByName.containsKey(method.getAnnotation(AppComponent.class).name())) {
                    throw new ContainerInstantiationException(String.format("More than one component with the same name found : %s",
                            method.getAnnotation(AppComponent.class).name()));
                }
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), component);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new ContainerInstantiationException("Exception while parsing config file");
            }
        }
    }

    private List<Method> getAnnotatedMethodsInOrder(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(method ->
                        method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method ->
                        method.getAnnotation(AppComponent.class).order()
                )).toList();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        int count = 0;
        C obj = null;
        for (var component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                count++;
                obj = (C) component;
            }
        }
        if (count == 1) {
            return obj;
        } else if (count > 1) {
            throw new ContainerInstantiationException(
                    String.format("More than one component of the same type found : " +
                            "%s, please consider using qualifier", componentClass.getName()));
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
