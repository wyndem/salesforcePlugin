package cn.wenhaha.plugin.data.salesforce;

/**
 * 用户响应
 * --------
 *
 * @author ：wyndem
 * @Date ：Created in 2022-03-30 15:26
 */
public class SalesUser {
    private Integer id;
    private String name;
    private String password;
    private String url;
    private String cid;
    private String secret;
    private String token;
    private String loginJson;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginJson() {
        return loginJson;
    }

    public void setLoginJson(String loginJson) {
        this.loginJson = loginJson;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
