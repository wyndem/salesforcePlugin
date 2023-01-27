package cn.wenhaha.plugin.data.salesforce;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.wenhaha.plugin.data.salesforce.api.Oauth2Api;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-12 20:23
 */
@RestController
@RequestMapping("sales")
@CrossOrigin(origins = {"http://localhost:3000", "null"})
public class SalesController {


    @PostMapping("add")
    public String add(@RequestBody SalesUser salesUser) throws SQLException {

        int count = SalesForceContext.db.count(Entity.create("user").set("name", salesUser.getName()));
        if(count!=0){
            return "该账号已经被添加了";
        }

        String token = Oauth2Api.token(salesUser.getUrl(), salesUser.getName(),
                salesUser.getPassword(), salesUser.getCid(), salesUser.getSecret());
        JSONObject jsonObject = JSONUtil.parseObj(token);
        // 登录成功
        if(jsonObject.containsKey("access_token")){
            salesUser.setToken(jsonObject.getStr("access_token"));
            salesUser.setLoginJson(token);
            Entity entity = Entity.create("user");
            entity.putAll(BeanUtil.beanToMap(salesUser));
            entity.remove("id");
            entity.set("create_time", DateUtil.now());
            SalesForceContext.db.insert(entity);
            return "ok";
        }
        return token;
    }


    @PostMapping("update")
    public String update(@RequestBody SalesUser salesUser) throws SQLException {

        int count = SalesForceContext.db.count(Entity.create("user").set("id", salesUser.getId()));
        if(count==0){
            return "该账号末找到";
        }

        String token = Oauth2Api.token(salesUser.getUrl(), salesUser.getName(),
                salesUser.getPassword(), salesUser.getCid(), salesUser.getSecret());
        JSONObject jsonObject = JSONUtil.parseObj(token);
        // 登录成功
        if(jsonObject.containsKey("access_token")){
            salesUser.setToken(jsonObject.getStr("access_token"));
            salesUser.setLoginJson(token);
            Entity entity = Entity.create("user");
            entity.putAll(BeanUtil.beanToMap(salesUser));
            entity.set("last_update",Long.toString(System.currentTimeMillis()));
            SalesForceContext.db.update(entity,Entity.create("user").set("id", salesUser.getId()));
            return "ok";
        }
        return token;
    }



    @GetMapping("info/{id}")
    public Object update(@PathVariable("id")String id) throws SQLException {

        List<Entity> entities = SalesForceContext.db.find(Entity.create("user").set("id", id));
        if(entities==null || entities.size()==0){
            return "该账号末找到";
        }
        return entities.get(0);
    }





}
