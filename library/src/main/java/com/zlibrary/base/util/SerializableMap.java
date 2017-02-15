package com.zlibrary.base.util;

import java.io.Serializable;
import java.util.Map;

/**
 * 序列化 Map
 */
public class SerializableMap implements Serializable {
    private static final long serialVersionUID = -8297889110100743553L;

    private Map<String, Object> map;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
