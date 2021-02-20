package com.scy.core.http;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * @author: SCY
 * @date: 2021/1/18   10:01
 * @version:
 * @desc:
 */
public class ParamHashMap extends HashMap<String,Object> {


    public static ParamHashMap builder(){
        return new ParamHashMap();
    }

    public ParamHashMap add(String key,Object value){
        if (value instanceof String) {
            if (!TextUtils.isEmpty((String) value)){
                put(key, value);
            }
        } else if (value instanceof Number){
            put(key, String.valueOf(value));
        }
        return this;
    }

}
