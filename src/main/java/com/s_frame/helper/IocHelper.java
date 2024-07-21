package com.s_frame.helper;

import java.lang.reflect.Field;
import java.util.Map;

import com.s_frame.annotation.Inject;
import com.s_frame.utils.ArrayUtil;
import com.s_frame.utils.CollectionUtil;
import com.s_frame.utils.ReflectionUtil;

public class IocHelper {

    static {
        //获取所有Bean类与Bean实例之间的映射关系
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();    //获取Bean类
                Object beanInstance = beanEntry.getValue(); //获取Bean实例
                Field[] beanFields = beanClass.getFields(); //获取Bean类定义的所有成员变量
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    for (Field beanField : beanFields) {
                        if (beanField.isAnnotationPresent(Inject.class)) {//判断当前BeanField中是否带有Inject注解
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                //通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}