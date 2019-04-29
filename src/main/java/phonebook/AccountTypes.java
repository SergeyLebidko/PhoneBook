package phonebook;

public enum AccountTypes {

    PHONE ("", "", "phone"),
    FACEBOOK ("https://", "www.facebook.com/", "facebook"),
    INSTAGRAM ("https://", "instagram.com/", "instagram"),
    MAIL("mailto:", "", "mail"),
    OK ("https://", "ok.ru/", "ok"),
    TELEGRAM ("https://", "t.me/", "telegram"),
    TWITTER ("https://", "twitter.com/", "twitter"),
    VK("https://", "vc.com/", "vk");

    private String protocol;
    private String address;
    private String type;

    AccountTypes(String protocol, String address, String type) {
        this.protocol = protocol;
        this.address = address;
        this.type = type;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

}
