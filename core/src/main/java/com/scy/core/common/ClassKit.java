package com.scy.core.common;

import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;
import com.scy.core.http.BaseUrl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author: SCY
 * @date: 2020/11/18   15:58
 * @version: 1.0
 * @desc: 反射获取父类信息
 */
public class ClassKit {

    /**
     *通过反射 获取当前类的父类的泛型 (VB) 对应 ViewBinding类,这里通过循环寻找是为了能让Activity有多个灵活泛型
     * @param appCompatActivity activity
     * @param <VB> 当前类的vb
     * @return  当前类的vb
     */
    public static <VB extends ViewBinding>  VB getViewBinding(FragmentActivity appCompatActivity){
        try {
            ParameterizedType parameterizedType= (ParameterizedType) appCompatActivity.getClass().getGenericSuperclass();
            Type[] types = parameterizedType.getActualTypeArguments();
            Class<?> aClass = null;
            for (Type type:types) {
                //如果是继承viewBinding
                Class<?> clazz= (Class<?>) type;
                if (clazz.isAssignableFrom(ViewBinding.class)){
                    aClass=clazz;
                }
            }
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            return (VB) method.invoke(aClass, appCompatActivity.getLayoutInflater());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     *通过反射 获取当前类的父类的泛型 (VB) 对应 ViewBinding类
     * @param fragment fragment
     * @param <VB> 当前类的vb
     * @return  当前类的vb
     */
    public static <VB extends ViewBinding>  VB getViewBinding(Fragment fragment){
        try {
            ParameterizedType parameterizedType= (ParameterizedType) fragment.getClass().getGenericSuperclass();
            Type[] types = parameterizedType.getActualTypeArguments();
            Class<?> aClass = null;
            for (Type type:types) {
                //如果是继承viewBinding
                Class<?> clazz= (Class<?>) type;
                if (clazz.isAssignableFrom(ViewBinding.class)){
                    aClass=clazz;
                }
            }
            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
            return (VB) method.invoke(aClass, fragment.getLayoutInflater());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *通过反射 获取当前类的父类的泛型 (VM) 对应 ViewModel类
     * @param appCompatActivity activity
     * @param <VM> 当前类的vm
     * @return  当前类的vm
     */
    public static <VM extends ViewModel> VM getViewModel(FragmentActivity appCompatActivity){
        ParameterizedType parameterizedType= (ParameterizedType) appCompatActivity.getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<VM> aClass = null;
        for (Type type:types) {
            //如果是继承viewBinding
            Class<?> clazz= (Class<?>) type;
            if (clazz.isAssignableFrom(ViewBinding.class)){
                aClass= (Class<VM>) clazz;
            }
        }
//        Class<VM> aClass = (Class<VM>) ((ParameterizedType) appCompatActivity.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return new ViewModelProvider(appCompatActivity).get(aClass);
    }

    /**
     *通过反射 获取当前类的父类的泛型 (VM) 对应 ViewModel类
     * @param fragment fragment
     * @param <VM> 当前类的vm
     * @return  当前类的vm
     */
    public static <VM extends ViewModel> VM getViewModel(Fragment fragment){
        ParameterizedType parameterizedType= (ParameterizedType) fragment.getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<VM> aClass = null;
        for (Type type:types) {
            //如果是继承viewBinding
            Class<?> clazz= (Class<?>) type;
            if (clazz.isAssignableFrom(ViewBinding.class)){
                aClass= (Class<VM>) clazz;
            }
        }
//        Class<VM> aClass = (Class<VM>) ((ParameterizedType) appCompatActivity.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return new ViewModelProvider(fragment).get(aClass);
    }


    /**
     * 获取相应的baseUrl
     * @return
     */
    public static String getHost(String clazzName){
        Class<?> aClass = null;
        try {
            aClass = Class.forName(clazzName);
//            boolean annotationPresent = aClass.isAnnotationPresent(BaseUrl.class);
//            if (!annotationPresent){
//                throw new NullPointerException(aClass.getName()+"类中不存在BaseUrl注解的变量。");
//            }
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field f : declaredFields) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(BaseUrl.class)) {
                    BaseUrl annotation = f.getAnnotation(BaseUrl.class);
                    return annotation != null ? (String) f.get(String.class) : "";
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return "";
    }
}
