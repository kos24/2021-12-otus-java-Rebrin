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
        listeners.stream()
                .map(Reference::get)
                .filter(Objects::nonNull).forEach(l ->
            l.notify(key, value, "cache updated")
        );
    }

    @Override
    public void remove(K key) {
        listeners.stream()
                .map(Reference::get)
                .filter(Objects::nonNull)
                .forEach(l -> l.notify(key, cache.get(key), "item removed"));
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        listeners.stream()
                .map(Reference::get)
                .filter(Objects::nonNull)
                .forEach(l -> l.notify(key, value, "get item"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(new WeakReference<>(listener));
    }
}
