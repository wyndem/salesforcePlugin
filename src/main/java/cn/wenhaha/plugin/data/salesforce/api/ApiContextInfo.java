package cn.wenhaha.plugin.data.salesforce.api;

import cn.wenhaha.plugin.data.salesforce.SalesForceContext;
import com.ejlchina.okhttps.HTTP;

/**
 * @ClassName: ApiContextInfo
 * @description: TODO api 基本信息
 * @author: wyndem
 * @date: 2020/7/25 20:34
 * @Version: 1.0
 **/
public class ApiContextInfo {

    //token 部分
    /** 失效状态码 **/
    public  static final Integer  FAIL=401;

    private static final String version="v55.0";

    // 批处理接口 部分
    public static final String COMPOSITEURL = "/services/data/"+version+"/composite/";

    public static final String COMPOSITEMETHOD = "PATCH";


    // 查询
    public static final String queryAll="/services/data/"+version+"/queryAll";
    //单独查询
    public static final String query="/services/data/"+version+"/query";

    //登录token
    public  static  final  String token="/services/oauth2/token";

    //对象
    public  static  final  String sobjects="/services/data/"+version+"/sobjects/";
    // 对象信息
    public  static  final  String describe="/services/data/"+version+"/sobjects/{}/describe";


    /**(已创建)请求成功并且服务器创建了新的资源*/
    public static final int HTTP_CODE_INSERT_SUCCESS_201 = 201;

    /**insert成功的状态码*/
    public static final int HTTP_CODE_INSERT_SUCCESS = 200;

    /**update成功的状态码*/
    public static final int HTTP_CODE_UPDATE_SUCCESS = 204;

    /**
     * 判断接口是否请求成功
     * @param httpCode
     * @return
     */
    public static boolean isSuccess(int httpCode){
        return HTTP_CODE_INSERT_SUCCESS == httpCode || HTTP_CODE_UPDATE_SUCCESS == httpCode ||
                HTTP_CODE_INSERT_SUCCESS_201 == httpCode;
    }


}
