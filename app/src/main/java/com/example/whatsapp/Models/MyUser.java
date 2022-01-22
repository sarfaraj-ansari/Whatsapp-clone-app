package com.example.whatsapp.Models;

public class MyUser {

    String profilePic,username,useremail,userpass,userPhone,userid,lastMessage;

    public MyUser(String profilePic, String username, String useremail, String userpass, String userPhone, String userid, String lastMessage) {
        this.profilePic = profilePic;
        this.username = username;
        this.useremail = useremail;
        this.userpass = userpass;
        this.userPhone = userPhone;
        this.userid = userid;
        this.lastMessage = lastMessage;
    }

    public MyUser(){}

    /*public MyUser(String userPhone,String userpass)
    {
        this.userpass=userpass;
        this.userPhone=userPhone;
    }
*/
    public MyUser(String username, String useremail, String userpass) {
        this.username = username;
        this.useremail = useremail;
        this.userpass = userpass;

    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }
}
