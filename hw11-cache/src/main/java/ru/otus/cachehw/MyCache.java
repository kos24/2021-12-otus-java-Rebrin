package ru.otus.cachehw;


import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
    private final Map<K,V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K,V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        executeListeners(key, value, "cache updated");
    }

    @Override
    public void remove(K key) {
        executeListeners(key, cache.get(key),"item removed");
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        executeListeners(key, value, "get item");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(findListener(listener));
    }

    private WeakReference<HwListener<K,V>> findListener(HwListener<K, V> listener) {

        for (var weakListener: listeners) {
            if (Objects.equals(weakListener.get(), listener)) {
                return weakListener;
            }
        }
        return null;
    }

    private void executeListeners(K key, V value, String action) {
        listeners.stream()
                .map(Reference::get)
                .filter(Objects::nonNull)
                .forEach(l -> l.notify(key, value, action));
    }
}
