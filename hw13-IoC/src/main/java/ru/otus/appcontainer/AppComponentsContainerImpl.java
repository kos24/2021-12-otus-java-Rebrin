package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exceptions.ContainerInstantiationException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private Map<String, String> beanNames = new HashMap<>();

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
        fillBeanNamesMap(appMethods);
        fillAppComponentsMap(object, appMethods, appComponentsByName);

    }

    private void fillAppComponentsMap(Object object, List<Method> appMethods, Map<String, Object> appComponentsByName) {
        for (Method method : appMethods) {
            List<Object> args = new ArrayList<>();
            try {
                Object component;
                if (method.getParameterTypes().length > 0) {
                    Arrays.stream(method.getParameterTypes()).forEachOrdered(
                            param -> args.add(appComponentsByName.get(param.getSimpleName()))
                    );
                    component = method.invoke(object, args.toArray());
                } else {
                    component = method.invoke(object);
                }
                appComponentsByName.put(method.getReturnType().getSimpleName(), component);
                appComponents.add(component);
            } catch (Exception e) {
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

    private void fillBeanNamesMap(List<Method> appMethods) {
        beanNames = appMethods.stream()
                .collect(Collectors.toMap(m -> m.getAnnotation(AppComponent.class).name(), m -> m.getReturnType().getSimpleName()));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {

        C object = (C) appComponentsByName.get(componentClass.getSimpleName());
        if (object == null) {
            if (componentClass.isInterface()) {
                for (Object obj : appComponents) {
                    if (Arrays.stream(obj.getClass().getInterfaces())
                            .anyMatch(componentClass::isAssignableFrom)) {
                        object = (C) appComponentsByName.get(obj.getClass().getSimpleName());
                    }
                }
            } else {
                String interfaceName = Arrays.stream(componentClass.getInterfaces())
                        .map(Class::getSimpleName)
                        .filter(appComponentsByName::containsKey)
                        .findFirst().orElse(null);
                object = (C) appComponentsByName.get(interfaceName);
            }
        }

        return object;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(beanNames.get(componentName));
    }

}
