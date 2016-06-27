package codme.myaxisvoiceadmin;

/**
 * Created by Alen John Abraham on 26-06-2016.
 */
public class UserClass {

    String address,display_name,email,img_url,phone_number,scroll_message;


    public UserClass() {
    }

    public UserClass(String email, String address, String display_name, String img_url, String phone_number, String scroll_message) {
        this.email = email;
        this.address = address;
        this.display_name = display_name;
        this.img_url = img_url;
        this.phone_number = phone_number;
        this.scroll_message = scroll_message;
    }

    public String getAddress() {
        return address;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getEmail() {
        return email;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getScroll_message() {
        return scroll_message;
    }
}
