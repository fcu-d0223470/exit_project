package jacky.androidprogram.Database;

/**
 * Created by Jacky on 7/11/2016.
 */
public class Task {
    private int task_number;
    private String task_name;
    private String task_block;
    private int task_type;
    private String task_picture;
    private String task_picture_sd;
    private String task_introduction;
    private int task_part;
    private int task_hot;
    private String task_developer;
    private int task_show;
    private String task_date;

    public Task(){}
    public Task(int num,String name,String block,int type,String picture,String intro,int part,int hot,String dev,int show,String tdate)
    {
        this.task_number=num;
        this.task_name=name;
        this.task_block=block;
        this.task_type=type;
        this.task_picture=picture;
        this.task_introduction=intro;
        this.task_part=part;
        this.task_hot=hot;
        this.task_developer=dev;
        this.task_show=show;
        this.task_date=tdate;
    }
    public Task(int num,String name,String block,int type,String picture,String pictureSD,String intro,int part,int hot,String dev,int show,String tdate)
    {
        this.task_number=num;
        this.task_name=name;
        this.task_block=block;
        this.task_type=type;
        this.task_picture=picture;
        this.task_picture_sd=pictureSD;
        this.task_introduction=intro;
        this.task_part=part;
        this.task_hot=hot;
        this.task_developer=dev;
        this.task_show=show;
        this.task_date=tdate;
    }
    public void setTask_number(int task_number){this.task_number=task_number;}
    public void setTask_name(String task_name){this.task_name=task_name;}
    public void setTask_block(String task_block){this.task_block=task_block;}
    public void setTask_introduction(String task_introduction){this.task_introduction=task_introduction;}
    public void setTask_part(int task_part){this.task_part=task_part;}
    public void setTask_type(int task_type){this.task_type=task_type;}
    public void setTask_developer(String task_developer){this.task_developer=task_developer;}
    public void setTask_date(String task_date){this.task_date=task_date;}
    public void setTask_picture(String task_picture){this.task_picture=task_picture;}
    public void setTask_picture_sd(String task_picture_sd){this.task_picture_sd=task_picture_sd;}
    public void setTask_show(int task_show){this.task_show=task_show;}
    public void setTask_hot(int task_hot){this.task_hot=task_hot;}

    public int getTask_number(){return this.task_number;}
    public String getTask_name(){return this.task_name;}
    public String getTask_block(){return this.task_block;}
    public String getTask_introduction(){return this.task_introduction;}
    public int getTask_part(){return this.task_part;}
    public int getTask_type(){return this.task_type;}
    public String getTask_developer(){return this.task_developer;}
    public String getTask_date(){return this.task_date;}
    public String getTask_picture(){return this.task_picture;}
    public String getTask_picture_sd(){return this.task_picture_sd;}
    public int getTask_show(){return this.task_show;}
    public int getTask_hot(){return this.task_hot;}
}
