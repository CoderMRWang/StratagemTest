package com.example.stratagem;


import com.example.pojo.Role;
import com.kayak.stratagem.annotation.Stratagem;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
public class ExcelStratagem {
    @Stratagem(name = "rule1",value = "1",result = "1",proxyClass ="ExcelStratagemResolver")
    public void type1(){
    }
    @Stratagem(name = "rule1",value = "2",result = "2",proxyClass ="ExcelStratagemResolver" )
    public void type2(){
    }
    @Stratagem(name = "rule1",value = "3",model = true,proxyClass = "ExcelStratagemResolver")
    public void type3(Role role){
        System.out.println(role.getRoleid()+"@@@@@@@@@@");
    }
    @Stratagem(name = "rule1",value = "4",model = true,proxyClass = "ExcelStratagemResolver")
    public void type4(Role role){
        System.out.println(role.getRoleid()+"@@@@@@@@@@"+role.getRolename());
    }

}
