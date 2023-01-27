package cn.wenhaha.plugin.data.salesforce.object;

import cn.wenhaha.datasource.IDataObject;
import cn.wenhaha.datasource.Obj;
import cn.wenhaha.datasource.ObjInfo;
import cn.wenhaha.datasource.exception.NotFondException;
import cn.wenhaha.plugin.data.salesforce.SalesUser;
import cn.wenhaha.plugin.data.salesforce.UserService;
import cn.wenhaha.plugin.data.salesforce.api.Oauth2Api;

import java.io.Serializable;
import java.util.List;

/**
 * 对象处理
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-18 19:51
 */
public class DataObjectContext  implements IDataObject {


    @Override
    public List<Obj> list(Serializable id) {
        SalesUser salesUser = UserService.byId(id);
        if (salesUser==null){
            throw new NotFondException("用户末找到");
        }
        if(Oauth2Api.checkTokenTimeout(salesUser.getLoginJson(),60)){
            salesUser = UserService.updateUser(id);
        }
        return DataObjectService.getAllSimpleObjects(salesUser.getLoginJson());

    }

    @Override
    public ObjInfo info(String nameApi, Serializable id) {
        SalesUser salesUser = UserService.byId(id);
        if (salesUser==null){
            throw new NotFondException("用户末找到");
        }
        if(Oauth2Api.checkTokenTimeout(salesUser.getLoginJson(),60)){
            salesUser = UserService.updateUser(id);
        }

        return DataObjectService.getInfoObject(salesUser.getLoginJson(),nameApi);
    }
}
