package cn.wenhaha.plugin.data.salesforce.object;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.wenhaha.datasource.Column;
import cn.wenhaha.datasource.Obj;
import cn.wenhaha.datasource.ObjInfo;
import cn.wenhaha.datasource.exception.DataSourceException;
import cn.wenhaha.datasource.exception.NetTimeOutException;
import cn.wenhaha.datasource.exception.UserInvalidFailException;
import cn.wenhaha.plugin.data.salesforce.SalesForceContext;
import cn.wenhaha.plugin.data.salesforce.api.ApiContextInfo;
import cn.wenhaha.plugin.data.salesforce.api.Oauth2Api;
import com.ejlchina.okhttps.HttpResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static cn.wenhaha.plugin.data.salesforce.SalesForceContext.http;

/**
 *
 * 对象处理服务类
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-19 23:09
 */
@Slf4j
public class DataObjectService {


    public static  List<Obj>  getAllSimpleObjects(String tokenJson) {
        // 检查token是否过期
        boolean b = Oauth2Api.checkTokenTimeout(tokenJson,60);
        if (b){
            throw  new UserInvalidFailException("token失效");
        }

        JSONObject jsonObject = JSONUtil.parseObj(tokenJson);
        String url = jsonObject.getStr("instance_url") + ApiContextInfo.sobjects;

        HttpResult httpResult;
        try {
            httpResult = http.sync(url)
                    .addHeader("Authorization", StrUtil.format("Bearer {}",jsonObject.getStr("access_token")))
                    .addHeader("Content-Type", "application/json")
                    .get();
        } catch (Exception e) {
            throw new NetTimeOutException(StrUtil.format("link {} time out",url));
        }
        String body = httpResult.getBody().toString();
        if (httpResult.getStatus() == ApiContextInfo.FAIL){
            throw new UserInvalidFailException(body);
        }

        log.debug("响应体==>"+body);
        JSONObject jsonBody = JSONUtil.parseObj(body);
        if (!jsonBody.containsKey("sobjects")){
            throw new DataSourceException("sobjects为空");
        }
        List<Obj> customObjects = new ArrayList<>();
        JSONArray sobjects = jsonBody.getJSONArray("sobjects");
        for (int i = 0; i < sobjects.size(); i++) {
            JSONObject sobject = sobjects.getJSONObject(i);
//            log.info(sobject.toString());
            Obj obj = new Obj();
            obj.setName(sobject.getStr("label"));
            obj.setNameApi(sobject.getStr("name"));
            obj.setUpdateable(sobject.getBool("updateable"));
            obj.setCreateable(sobject.getBool("createable"));
            obj.setDeletable(sobject.getBool("deletable"));
            obj.setPluginName(SalesForceContext.name);
            obj.setPluginCode(SalesForceContext.code);
            customObjects.add(obj);
        }
        return customObjects;
    }



    public  static ObjInfo getInfoObject(String tokenJson, String objectApiName){
        // 检查token是否过期
        boolean b = Oauth2Api.checkTokenTimeout(tokenJson,60);
        if (b){
            throw  new UserInvalidFailException("token失效");
        }

        JSONObject jsonObject = JSONUtil.parseObj(tokenJson);
        String url = jsonObject.getStr("instance_url") + StrUtil.format(ApiContextInfo.describe,objectApiName);

        HttpResult  httpResult;
        try {
            httpResult = http.sync(url)
                    .addHeader("Authorization", StrUtil.format("Bearer {}",jsonObject.getStr("access_token")))
                    .addHeader("Content-Type", "application/json")
                    .get();
        } catch (Exception e) {
            throw new NetTimeOutException(StrUtil.format("link {} time out",url));
        }

        HttpResult.Body body = httpResult.getBody();

        if (httpResult.getStatus()==ApiContextInfo.FAIL){
            throw new UserInvalidFailException(body.toString());
        }


        JSONObject jsonBody = JSONUtil.parseObj(body.toString());


        ObjInfo objInfo = new ObjInfo();
        objInfo.setCreateable(jsonBody.getBool("createable"));
        objInfo.setDeletable(jsonBody.getBool("deletable"));
        objInfo.setUpdateable(jsonBody.getBool("updateable"));
        objInfo.setName(jsonBody.getStr("label"));
        objInfo.setNameApi(jsonBody.getStr("name"));
        objInfo.setPluginName(SalesForceContext.name);
        objInfo.setPluginCode(SalesForceContext.code);


        JSONArray standardFields = jsonBody.getJSONArray("fields");
        List<Column> columns = new ArrayList<>(standardFields.size());
        objInfo.setColumns(columns);
        for (int i = 0; i < standardFields.size(); i++) {
            JSONObject field = standardFields.getJSONObject(i);
            Column column = new Column();
            column.setName(field.getStr("label"));
            column.setNameApi(field.getStr("name"));
            column.setCreateable(field.getBool("updateable"));
            column.setUpdateable(field.getBool("updateable"));
            column.setCustom(field.getBool("custom"));
            column.setNullable(field.getBool("nillable"));
            column.setPrimaryKey(StrUtil.equals(column.getNameApi(),"Id"));
            column.setRequired(column.getNullable());
            column.setLength(field.getInt("length"));
            column.setDatatype(FieldTypeFactory.getType(field.getStr("type")));
            columns.add(column);
        }
        return objInfo;
    }
}
