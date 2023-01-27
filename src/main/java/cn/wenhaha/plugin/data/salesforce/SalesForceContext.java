package cn.wenhaha.plugin.data.salesforce;

import cn.hutool.db.Db;
import cn.wenhaha.datasource.*;
import cn.wenhaha.plugin.data.salesforce.http.OkHttpUtil;
import cn.wenhaha.plugin.data.salesforce.object.DataObjectContext;
import com.ejlchina.okhttps.HTTP;
import org.pf4j.Extension;

/**
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-12 20:04
 */
@Extension
public class SalesForceContext implements IDataSourcePlugin {

    public static Db db;
    public  static  final HTTP http = OkHttpUtil.buildHttp();
    public  static  final String  code  ="SFRestApi";
    public  static  final String  name  ="SF数据支持库";
    public  static  final String  icon  ="https://cloud.wenhaha.cn/api/v3/file/source/1399/dd.png?sign=-d0miYO2B_Pm3hxhpOa5qf4mn54Trmi-a-8zjfdGjLI%3D%3A0";

    @Override
    public Class<?>[] controller() {
        return new Class[]{SalesController.class};
    }


    @Override
    public IUserContext getUserContext() {
        return new UserContext();
    }

    @Override
    public IDataObject getDataObject() {
        return new DataObjectContext();
    }

    @Override
    public EventListen getEventListen() {
        return new EventListenImp();
    }
}
