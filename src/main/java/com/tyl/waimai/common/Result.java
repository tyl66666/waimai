package com.tyl.waimai.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果
 * @param <T>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T>  implements Serializable {
    private Integer code;
    private String msg;
    private  T data;


    public static <T> Result<T> success(){
        return new Result<T>(1,"success",null);
    }

    public static <T> Result<T> success(T data){
        return new Result<T>(1,"success",data);
    }

    public static <T> Result<T> success(T data,String message){
        return new Result<T>(1,message,data);
    }

    public static <T> Result<T> success(String message){
        return new Result<T>(1,message,null);
    }




    public static <T> Result<T> fail(){
        return new Result<T>(0,"fa  il",null);
    }

    public static <T> Result<T> fail(Integer code){
        return new Result<T>(code,"fail",null);
    }

    public static <T> Result<T> fail(Integer code,String message){
        return new Result<T>(code,message,null);
    }

    public static <T> Result<T> fail(String message){
        return new Result<T>(0,message,null);
    }
}
