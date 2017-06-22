package jacky.androidprogram.Database;

public class User {

    private int id;
    private String username;
    private String userpass;
    private String fname;
    private String lname;
    private String email;
    private String phone;
    private String sex;
    private String address;
    private String user_phone_id;
    private String createdate;

    public User()
    {
    }

    public User(int id, String username, String userpass)
    {
        this.id=id;
        this.username=username;
        this.userpass=userpass;
    }
    public User(String username, String userpass)
    {
    this.username=username;
    this.userpass=userpass;
    }
    public User(int id, String username, String userpass,String f,String l,String e,String p,String s,String a,String i,String c)
    {
        this.id=id;
        this.username=username;
        this.userpass=userpass;
        this.fname=f;
        this.lname=l;
        this.email=e;
        this.phone=p;
        this.sex=s;
        this.address=a;
        this.user_phone_id=i;
        this.createdate=c;
    }
    public User(int id, String username, String userpass,String f,String l,String e,String p,String s,String a,String i)
    {
        this.id=id;
        this.username=username;
        this.userpass=userpass;
        this.fname=f;
        this.lname=l;
        this.email=e;
        this.phone=p;
        this.sex=s;
        this.address=a;
        this.user_phone_id=i;
    }
    public User(String username, String userpass,String f,String l,String e,String p,String s,String a,String c)
    {
        this.username=username;
        this.userpass=userpass;
        this.fname=f;
        this.lname=l;
        this.email=e;
        this.phone=p;
        this.sex=s;
        this.address=a;
        this.createdate=c;
    }
    public User(String username, String userpass,String f,String l,String e,String p,String s,String a,String pid,String c)
    {
        this.username=username;
        this.userpass=userpass;
        this.fname=f;
        this.lname=l;
        this.email=e;
        this.phone=p;
        this.sex=s;
        this.address=a;
        this.user_phone_id=pid;
        this.createdate=c;
    }
    public void setUserAllInfoUpdate(String username, String userpass,String f,String l,String e,String p,String s,String a){
        this.username=username;
        this.userpass=userpass;
        this.fname=f;
        this.lname=l;
        this.email=e;
        this.phone=p;
        this.sex=s;
        this.address=a;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setUser_phone_id(String id){this.user_phone_id=id;}
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getId() {return id;}
    public String getUsername() {return username;}
    public String getUserpass() {return userpass;}
    public String getFname() {return fname;}
    public String getLname() {return lname;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getSex() {return sex;}
    public String getAddress() {return address;}
    public String getUser_phone_id(){return user_phone_id;}
    public String getCreatedate() {return createdate;}
}
