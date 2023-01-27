package cn.wenhaha;

import cn.wenhaha.datasource.Obj;
import cn.wenhaha.datasource.ObjInfo;
import cn.wenhaha.plugin.data.salesforce.EventListenImp;
import cn.wenhaha.plugin.data.salesforce.UserService;
import cn.wenhaha.plugin.data.salesforce.object.DataObjectContext;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Before
    public void test(){
        new EventListenImp().onStart("1");
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        UserService.updateUser(1);
        DataObjectContext dataObjectContext = new DataObjectContext();
        List<Obj> list = dataObjectContext.list(1);
    }

    @Test
    public void testObjInfo()
    {

        DataObjectContext dataObjectContext = new DataObjectContext();
        ObjInfo account = dataObjectContext.info("Account", 1);
        System.out.println(account.getColumns().size());
    }


}
