package com.scy.core.common;

import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.scy.core.http.BaseUrl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: SCY
 * @date: 2020/11/18   15:58
 * @version: 1.0
 * @desc: 反射获取父类信息,支持获取二级继承的父类泛型参数。
 */
public class ClassKit {

    /**
     *通过反射 获取当前类的父类的泛型 (VB) 对应 ViewBinding类
     * @param appCompatActivity activity
     * @param <VB> 当前类的vb
     * @return  当前类的vb
     */
    public static <VB extends ViewBinding>  VB getViewBinding(AppCompatActivity appCompatActivity){
//        try {
//            Class<?> aClass = (Class<?>) ((ParameterizedType)appCompatActivity.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
//            return (VB) method.invoke(aClass, appCompatActivity.getLayoutInflater());
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
        try {
            List<Type> typeList = new ArrayList<>();
            //获取父类，包括父类的泛型参数
            ParameterizedType parameterizedType = (ParameterizedType) appCompatActivity.getClass().getGenericSuperclass();
            Type[] actualTypeArguments1 = parameterizedType.getActualTypeArguments();
            //父类的父类泛型
            Class<?> superclass = appCompatActivity.getClass().getSuperclass();
            Class<?> superclassSuperclass = null;
            if (superclass != null) {
                superclassSuperclass = superclass.getSuperclass();
            }
            //父类的父类是Android SDK的类，就绕过获取泛型参数
            if (superclassSuperclass != null && !superclassSuperclass.getName().equals(AppCompatActivity.class.getName())) {
                Type type = superclass.getGenericSuperclass();
                ParameterizedType genericSuperclass = (ParameterizedType) type;
                Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
                typeList.addAll(Arrays.asList(actualTypeArguments));
            }
            typeList.addAll(Arrays.asList(actualTypeArguments1));
            Class<?> aClass = null;
            for (Type type : typeList) {
                //如果是继承viewBinding
                Class<?> clazz = (Class<?>) type;
                if (ViewBinding.class.isAssignableFrom(clazz)) {
                    aClass = clazz;
                    break;
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
//        try {
//            Class<?> aClass = (Class<?>) ((ParameterizedType) fragment.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//            Method method = aClass.getDeclaredMethod("inflate", LayoutInflater.class);
//            return (VB) method.invoke(aClass, fragment.getLayoutInflater());
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
        try {
            List<Type> typeList = new ArrayList<>();
            //获取父类，包括父类的泛型参数
            ParameterizedType parameterizedType = (ParameterizedType) fragment.getClass().getGenericSuperclass();
            Type[] actualTypeArguments1 = parameterizedType.getActualTypeArguments();
            //父类的父类泛型
            Class<?> superclass = fragment.getClass().getSuperclass();
            Class<?> superclassSuperclass = null;
            if (superclass != null) {
                superclassSuperclass = superclass.getSuperclass();
            }
            //父类的父类是Android SDK的类，就绕过获取泛型参数
            if (superclassSuperclass != null && !superclassSuperclass.getName().equals(Fragment.class.getName())) {
                ParameterizedType genericSuperclass = (ParameterizedType) superclass.getGenericSuperclass();
                Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
                typeList.addAll(Arrays.asList(actualTypeArguments));
            }
            typeList.addAll(Arrays.asList(actualTypeArguments1));
            Class<?> aClass = null;
            for (Type type : typeList) {
                //如果是继承viewBinding
                Class<?> clazz = (Class<?>) type;
                if (ViewBinding.class.isAssignableFrom(clazz)) {
                    aClass = clazz;
                    break;
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
    public static <VM extends ViewModel> VM getViewModel(AppCompatActivity appCompatActivity){
        List<Type> typeList = new ArrayList<>();
        ParameterizedType parameterizedType = (ParameterizedType) appCompatActivity.getClass().getGenericSuperclass();
        Type[] actualTypeArguments1 = parameterizedType.getActualTypeArguments();
        //父类的父类泛型
        Class<?> superclass = appCompatActivity.getClass().getSuperclass();
        Class<?> superclassSuperclass = null;
        if (superclass != null) {
            superclassSuperclass = superclass.getSuperclass();
        }
        //父类的父类是Android SDK的类，就绕过获取泛型参数
        if (superclassSuperclass != null && !superclassSuperclass.getName().equals(AppCompatActivity.class.getName())) {
            ParameterizedType genericSuperclass = (ParameterizedType) superclass.getGenericSuperclass();
            Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
            typeList.addAll(Arrays.asList(actualTypeArguments));
        }
        typeList.addAll(Arrays.asList(actualTypeArguments1));
        Class<VM> aClass = null;
        for (Type type : typeList) {
            //如果是继承viewBinding
            Class<VM> clazz = (Class<VM>) type;
            if (ViewModel.class.isAssignableFrom(clazz)) {
                aClass = clazz;
                break;
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
        List<Type> typeList = new ArrayList<>();
        ParameterizedType parameterizedType = (ParameterizedType) fragment.getClass().getGenericSuperclass();
        Type[] actualTypeArguments1 = parameterizedType.getActualTypeArguments();
        //父类的父类泛型
        Class<?> superclass = fragment.getClass().getSuperclass();
        Class<?> superclassSuperclass = null;
        if (superclass != null) {
            superclassSuperclass = superclass.getSuperclass();
        }
        //父类的父类是Android SDK的类，就绕过获取泛型参数
        if (superclassSuperclass != null && !superclassSuperclass.getName().equals(Fragment.class.getName())) {
            ParameterizedType genericSuperclass = (ParameterizedType) superclass.getGenericSuperclass();
            Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
            typeList.addAll(Arrays.asList(actualTypeArguments));
        }
        typeList.addAll(Arrays.asList(actualTypeArguments1));
        Class<VM> aClass = null;
        for (Type type : typeList) {
            //如果是继承viewBinding
            Class<VM> clazz = (Class<VM>) type;
            if (ViewModel.class.isAssignableFrom(clazz)) {
                aClass = clazz;
                break;
            }
        }
//        Class<VM> aClass = (Class<VM>) ((ParameterizedType) fragment.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
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
