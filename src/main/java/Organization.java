import java.util.ArrayList;


public class Organization {
    private String organizationName;
    private String createDate;
    private String address;
    private String phone;
    private String inn;
    private String ogrn;
    private ArrayList<Security> securities;

    public String getCreateDate() {
        return createDate;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getInn() {
        return inn;
    }

    public String getOgrn() {
        return ogrn;
    }

    public ArrayList<Security> getSecurities() {
        return securities;
    }
}
