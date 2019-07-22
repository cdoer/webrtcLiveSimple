package com.yxy.base.utils;

import com.yxy.base.exception.BizException;

import java.util.UUID;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public class CommonUtils {


    public static boolean isEmpty(String str){
        return str==null||str.trim().equals("");
    }
    public static String uuid(){
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-","");
    }

    public static void checkEmpty(String str,String name) throws Exception{
        if(isEmpty(str)) throw new BizException(name+"不能为空");
    }



}
