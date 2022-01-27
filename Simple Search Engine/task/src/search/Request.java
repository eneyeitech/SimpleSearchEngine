package search;

public class Request {
    private String tenant; // eneyeitech
    private String type; // cleaning
    private String phoneNumber; //0805

    public Request() {
        this.tenant = "";
        this.type = "";
        this.phoneNumber = "";

    }

    public Request(String tenant) {
        this.tenant = tenant;
        this.type = "";
        this.phoneNumber = "";
    }

    public Request(String tenant, String type) {
        this.tenant = tenant;
        this.type = type;
        this.phoneNumber = "";
    }

    public Request(String tenant, String type, String phoneNumber) {
        this.tenant = tenant;
        this.type = type;
        this.phoneNumber = phoneNumber;
    }


    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    protected static Request createPerson(String parser) {
        String[] data = parser.split("\\s+");
        return new Request(
                data.length >= 1 ? data[0] : "",
                data.length >= 2 ? data[1] : "",
                data.length >= 3 ? data[2] : "");
    }

    @Override
    public String toString() {
        return tenant + (type.isEmpty() ? "" : " ") + type + (phoneNumber.isEmpty() ? "" : " ")  + phoneNumber;
    }
}

