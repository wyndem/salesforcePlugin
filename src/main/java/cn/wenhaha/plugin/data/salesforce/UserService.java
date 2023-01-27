package cn.wenhaha.plugin.data.salesforce;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.wenhaha.datasource.DataUser;
import cn.wenhaha.datasource.exception.UserInvalidFailException;
import cn.wenhaha.plugin.data.salesforce.api.Oauth2Api;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-18 19:19
 */
public class UserService {


    public static SalesUser byId(Serializable id){
        List<Entity> entities = null;
        try {
            entities = SalesForceContext.db.find(Entity.create("user").set("id", id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(entities==null || entities.size()==0){
            return null;
        }
        return entities.get(0).toBean(SalesUser.class);
    }

    public static List<DataUser> list() {
        try {
            List<Entity> user = SalesForceContext.db.findAll("user");
            return user.stream().map(u->{
                DataUser dataUser = new DataUser();
                dataUser.setId(u.getStr("id"));
                dataUser.setName(u.getStr("name"));
                dataUser.setPassword(u.getStr("password"));
                dataUser.setCreateTime(u.getStr("create_time"));
                String updateStr = u.getStr("last_update");
                if(StrUtil.isNotEmpty(updateStr)){
                    Date date = new Date(Long.parseLong(updateStr));
                    dataUser.setLastUpdateTime(DateUtil.formatDateTime(date));
                }
                dataUser.setWebSite(u.getStr("url"));
                dataUser.setPluginCode(SalesForceContext.code);
                dataUser.setIcon(SalesForceContext.icon);
                dataUser.setPluginName(SalesForceContext.name);
                return dataUser;
            }).collect(Collectors.toList());


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(0);
    }


    public static int removeUser(Serializable id){
        try {
            return SalesForceContext.db.del(Entity.create("user").set("id", id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static SalesUser updateUser(Serializable id) {
        SalesUser salesUser = byId(id);
        if (salesUser == null) {
            return null;
        }

        String token = Oauth2Api.token(salesUser.getUrl(), salesUser.getName(),
                salesUser.getPassword(), salesUser.getCid(), salesUser.getSecret());
        JSONObject jsonObject = JSONUtil.parseObj(token);
        // 登录成功
        if (jsonObject.containsKey("access_token")) {
            salesUser.setToken(jsonObject.getStr("access_token"));
            salesUser.setLoginJson(token);
            Entity entity = Entity.create("user");
            entity.putAll(BeanUtil.beanToMap(salesUser));
            entity.set("last_update", Long.toString(System.currentTimeMillis()));
            try {
                SalesForceContext.db.update(entity, Entity.create("user").set("id", salesUser.getId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else {
            throw new UserInvalidFailException(jsonObject.getStr("message"));
        }
        return salesUser;
    }







}
