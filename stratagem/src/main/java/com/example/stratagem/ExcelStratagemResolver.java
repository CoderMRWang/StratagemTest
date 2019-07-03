package com.example.stratagem;

import com.example.pojo.Role;
import com.example.stratagem.interfaces.Resolver;

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

    @Override
    public Object resolve(Object... object) throws InvocationTargetException, IllegalAccessException {
        ExcelStratagem excelStratagem=new ExcelStratagem();
        Role role=(Role) object[0];
        String key=setKey("ExcelStratagemResolver",role.getRolename(),role.getRoleid());
        Object result= this.getKey(key);
        if (result instanceof Method){
          return   Method.class.cast(result).invoke(excelStratagem,object[0]);
        }else{
            return result;
        }
    }

    public static void main(String[] args) {
        Role role=new Role();
        role.setRolename("rule1");
        role.setRoleid("4");
        Role role1=new Role();
        role1.setRolename("rule1");
        role1.setRoleid("1");
        try {
            ExcelStratagemResolver excelStratagemResolver=new ExcelStratagemResolver();
            System.out.println(excelStratagemResolver.resolve(role));
            System.out.println(excelStratagemResolver.resolve(role1));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
