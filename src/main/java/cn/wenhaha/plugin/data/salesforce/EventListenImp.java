package cn.wenhaha.plugin.data.salesforce;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.system.SystemUtil;
import cn.wenhaha.datasource.EventListen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-07-12 20:12
 */
public class EventListenImp implements EventListen {

    private Logger logger = LoggerFactory.getLogger(EventListenImp.class);

    private final String path = SystemUtil.getUserInfo().getCurrentDir() + File.separator
            + SalesForceContext.code + File.separator + "user.db";

    private final String jdbcUrl = "jdbc:sqlite:" + path;


    private final String sql="CREATE TABLE \"user\" (\n" +
            "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "  \"name\" TEXT,\n" +
            "  \"password\" TEXT,\n" +
            "  \"url\" TEXT,\n" +
            "  \"cid\" TEXT,\n" +
            "  \"secret\" TEXT,\n" +
            "  \"token\" TEXT,\n" +
            "  \"loginJson\" TEXT,\n" +
            "  \"last_update\" TEXT,\n" +
            "  \"create_time\" TEXT\n" +
            ");";



    @Override
    public void onLoad(String id) {
        logger.info("{} 正在加载插件", id);
        if (FileUtil.exist(path)) {
            logger.info("{} 文件已经创建了，不在进行创建",path);
            return;
        }
        FileUtil.touch(path);
        DataSource ds = new SimpleDataSource(jdbcUrl,"","");

        try {
            Db.use(ds).execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void onStart(String id) {
        DataSource ds = new SimpleDataSource(jdbcUrl, "", "");
        SalesForceContext.db=Db.use(ds);
        logger.info("我被启动了");

    }

    @Override
    public void onStop() {
        SalesForceContext.db=null;
        logger.info("我被停止");
    }

    @Override
    public void unLoad() {
        FileUtil.del(path);
        logger.info("我被卸载了");
    }
}
