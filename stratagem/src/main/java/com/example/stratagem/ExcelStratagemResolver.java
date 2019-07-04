package com.example.stratagem;

import com.example.core.annotation.AnnotationResolver;
import com.example.pojo.Role;
import com.example.stratagem.interfaces.Resolver;
import com.example.timing.TimeingResolver;
import com.example.timing.annotation.Timing;
import com.example.timing.enums.TimeEnum;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
public class ExcelStratagemResolver extends SimpleStratagemResolver implements Resolver {

    public ExcelStratagemResolver() throws IOException, ClassNotFoundException {
    }

    public static void main(String[] args) {
        Role role=new Role();
        role.setRolename("rule1");
        role.setRoleid("4");
        Role role1=new Role();
        role1.setRolename("rule1");
        role1.setRoleid("1");
        try {
            AnnotationResolver.getInstance();
//            ExcelStratagemResolver excelStratagemResolver=new ExcelStratagemResolver();
//            System.out.println(excelStratagemResolver.resolve(role));
//            System.out.println(excelStratagemResolver.resolve(role1));
            System.out.println(TimeingResolver.methodMap);
            System.out.println(TimeingResolver.methodMap.get(ExcelStratagemResolver.class));
            System.out.println((SimpleStratagemResolver.class.cast(TimeingResolver.methodMap.get(ExcelStratagemResolver.class)).Test()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Timing(type = TimeEnum.S)
    @Override
    public Object resolve(Object... object) throws InvocationTargetException, IllegalAccessException {
        ExcelStratagem excelStratagem = new ExcelStratagem();
        Role role = (Role) object[0];
        String key = setKey("ExcelStratagemResolver", role.getRolename(), role.getRoleid());
        Object result = this.getKey(key);
        if (result instanceof Method) {
            return Method.class.cast(result).invoke(excelStratagem, object[0]);
        } else {
            return result;
        }
    }
}
