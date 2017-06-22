package jacky.androidprogram.Database;

/**
 * Created by Jacky on 7/11/2016.
 */
public class Story {
    private int story_serial_number;
    private int story_number;
    private String story_name;
    private String story_address;
    private String story_longitute;
    private String story_latitude;
    private String story_picture;
    private String story_picture_sd;
    private String story_data;
    private int story_part;
    private int story_show;
    private String story_date;

    public Story(){}
    public Story(int story_serial_number,int story_number,String story_name,String story_address,String story_longtitute,String story_latitude,String story_picture,String story_data,int story_part,int story_show,String story_date)
    {
        this.story_serial_number=story_serial_number;
        this.story_number=story_number;
        this.story_name=story_name;
        this.story_address=story_address;
        this.story_longitute=story_longtitute;
        this.story_latitude=story_latitude;
        this.story_picture=story_picture;
        this.story_data=story_data;
        this.story_part=story_part;
        this.story_show=story_show;
        this.story_date=story_date;
    }
    public Story(int story_serial_number,int story_number,String story_name,String story_address,String story_longtitute,String story_latitude,String story_picture,String story_picture_sd,String story_data,int story_part,int story_show,String story_date)
    {
        this.story_serial_number=story_serial_number;
        this.story_number=story_number;
        this.story_name=story_name;
        this.story_address=story_address;
        this.story_longitute=story_longtitute;
        this.story_latitude=story_latitude;
        this.story_picture=story_picture;
        this.story_picture_sd=story_picture_sd;
        this.story_data=story_data;
        this.story_part=story_part;
        this.story_show=story_show;
        this.story_date=story_date;
    }
    public void setStory_serial_no(int story_serial_no){this.story_serial_number=story_serial_no;}
    public void setstory_number(int story_number){this.story_number=story_number;}
    public void setStory_name(String story_name){this.story_name=story_name;}
    public void setStory_address(String story_address){this.story_address=story_address;}
    public void setStory_longtitute(String story_longtitute){this.story_longitute=story_longtitute;}
    public void setStory_latitude(String story_latitude){this.story_latitude=story_latitude;}
    public void setStory_picture(String story_picture){this.story_picture=story_picture;}
    public void setStory_picture_sd(String story_picture_sd){this.story_picture_sd=story_picture_sd;}
    public void setStory_data(String story_data){this.story_data=story_data;}
    public void setStory_part(int story_part){this.story_part=story_part;}
    public void setStory_show(int story_show){this.story_show=story_show;}
    public void setStory_date(String story_date){this.story_date=story_date;}

    public long getStory_serial_no(){return this.story_serial_number;}
    public int getStory_number(){return this.story_number;}
    public String getStory_name(){return this.story_name;}
    public String getStory_address(){return this.story_address;}
    public String getStory_longtitute(){return this.story_longitute;}
    public String getStory_latitude(){return this.story_latitude;}
    public String getStory_picture(){return this.story_picture;}
    public String getStory_picture_sd(){return this.story_picture_sd;}
    public String getStory_data(){return this.story_data;}
    public int getStory_part(){return this.story_part;}
    public int getStory_show(){return this.story_show;}
    public String getStory_date(){return this.story_date;}
}
