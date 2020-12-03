package com.scy.core.interfaces;

/**
 * @author: SCY
 * @date: 2020/11/24   18:01
 * @version: 1.0
 * @desc: 自己创建Retrofit时需要实现,方便获取不同的API方法
 */
public interface ApiCreator {

    /**
     *
     * @param clazz 相应api service的class
     * @param <T>  api service 的泛型
     * @return  相应的接口api service
     */
      <T> T createApi(Class<T> clazz);


}
