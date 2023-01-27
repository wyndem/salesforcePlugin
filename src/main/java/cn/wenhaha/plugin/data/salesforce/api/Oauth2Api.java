package cn.wenhaha.plugin.data.salesforce.api;


import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import cn.wenhaha.plugin.data.salesforce.http.OkHttpUtil;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;

/**
 * oauth授权
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-04-04 12:33
 */
public class Oauth2Api {

    private static final HTTP http = OkHttpUtil.buildHttp();


    /**
     * <p>
     *    获取token
     * </p>
     * @Author: Wyndem
     * @DateTime: 2022-04-04 12:37
     */
    public static String token(String oauthUrl,String userName,String password,String clientId,String secret ){
        HttpResult httpResult = http.sync(URLUtil.decode(oauthUrl)+"/services/oauth2/token")
                .addBodyPara("username", userName)
                .addBodyPara("password", password)
                .addBodyPara("client_id", clientId)
                .addBodyPara("client_secret", secret)
                .addBodyPara("grant_type", "password").post();
        return httpResult.getBody().toString();
    }





    /**
     * <p>
     *    如果失效 则 true 反之 false
     * </p>
     * @Author: Wyndem
     * @DateTime: 2022-04-04 12:37
     */
    public static boolean checkTokenTimeout(String json,Integer minute){
        try {
            long now = System.currentTimeMillis();
            String createTime = JSONUtil.parseObj(json).getStr("issued_at");
            return now - Long.parseLong(createTime) >= (minute * 60000L);
        } catch (Exception e) {
            return true;
        }
    }



}
