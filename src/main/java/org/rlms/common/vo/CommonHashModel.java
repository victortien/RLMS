package org.rlms.common.vo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class CommonHashModel<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<K, V> dataMap = new HashMap<>();

    public void putDataToMap(K key, V value) {
        dataMap.put(key, value);
    }

    public String getString(K key) {
        String retStr = dataMap.get(key) instanceof String ? dataMap.get(key).toString() : StringUtils.EMPTY;
        return StringUtils.trim(retStr);
    }

    public V getDataFromMap(K key) {
        return dataMap.get(key);
    }

    public V removeFromMap(K key) {
        return dataMap.remove(key);
    }

    public Set<K> getkeySet() {
        return dataMap.keySet();
    }

    public Set<V> getValues() {
        return dataMap.keySet().stream().map(dataMap::get).collect(Collectors.toSet());
    }

    public Set<Entry<K, V>> getEntrySet() {
        return dataMap.entrySet();
    }

    public void addAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            dataMap.put(key, value);
        }
    }

    public Map<K, V> getMapData() {
        return dataMap;
    }

    public String toString() {
        return dataMap.toString();
    }

    public void clear() {
        dataMap.clear();
    }

    public void sort(Comparator<Map.Entry<K,V>> comparator) {
        LinkedHashMap<K, V> sortedMap = dataMap.entrySet().stream().sorted(comparator).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
        this.dataMap = sortedMap;
    }

}
