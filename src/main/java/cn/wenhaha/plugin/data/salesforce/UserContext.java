package cn.wenhaha.plugin.data.salesforce;

import cn.wenhaha.datasource.DataUser;
import cn.wenhaha.datasource.IUserContext;
import cn.wenhaha.plugin.data.salesforce.api.Oauth2Api;

import java.io.Serializable;
import java.util.List;

/**
 * 用户上下文
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-16 16:42
 */
public class UserContext  implements IUserContext<SalesUser>{


    @Override
    public SalesUser getUserInfo(Serializable id) {
        SalesUser salesUser = UserService.byId(id);
        if (salesUser!=null){
            if (Oauth2Api.checkTokenTimeout(salesUser.getLoginJson(),60)){
                return  updateUser(id);
            }
        }
        return salesUser;
    }

    @Override
    public SalesUser updateUser(Serializable id) {
        return UserService.updateUser(id);
    }

    @Override
    public List<DataUser> list() {
        return UserService.list();
    }

    @Override
    public boolean removeUser(Serializable id) {
        return UserService.removeUser(id)!=0;
    }
}
