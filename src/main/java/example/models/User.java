package example.models;


public class User {
    String username;
    String password;
    String remember;
    String lang;

    public User() {
        this.username = "";
        this.password = "";
        this.remember = "false";
        this.lang = "en";
    }

    public User(String userName, String password, String rememberMe, String lang) {
        this.username = userName;
        this.password = password;
        this.remember = rememberMe;
        this.lang = lang;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String rememberMe) {
        this.remember = rememberMe;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
