package jacky.androidprogram.subpages.questfragment;

/**
 * Created by Jacky on 10/30/2016.
 */

public class Array_TaskList {
    private int task_number;
    private String task_name;
    private String task_block;
    private int task_type;
    private String task_picture;
    private String task_introduction;
    private int task_part;
    private int task_hot;
    private int task_developer;
    private int task_show;

    private String task_date;

    public Array_TaskList(){}
    public Array_TaskList(int t_num,String t_name,String t_block,String t_pic,int t_type,int t_hot,String t_date)
    {
        this.task_number=t_num;
        this.task_name=t_name;
        this.task_block=t_block;
        this.task_picture=t_pic;
        this.task_type=t_type;
        this.task_hot=t_hot;
        this.task_date=t_date;
    }

    public Array_TaskList(int t_num,String t_name,String t_block,String t_pic,String t_intro,int t_type,int t_hot,String t_date)
    {
        this.task_number=t_num;
        this.task_name=t_name;
        this.task_block=t_block;
        this.task_picture=t_pic;
        this.task_introduction=t_intro;
        this.task_type=t_type;
        this.task_hot=t_hot;
        this.task_date=t_date;
    }
    public void setTask_number (int task_number){this.task_number=task_number;}
    public void setTask_name (String task_name){this.task_name=task_name;}
    public void setTask_block (String task_block){this.task_block=task_block;}
    public void setTask_picture (String task_picture){this.task_picture=task_picture;}
    public void setTask_introduction (String task_introduction){this.task_introduction=task_introduction;}
    public void setTask_type (int task_type){this.task_type=task_type;}
    public void setTask_part (int task_part){this.task_part=task_part;}
    public void setTask_developer (int task_developer){this.task_developer=task_developer;}
    public void setTask_hot (int task_hot){this.task_hot=task_hot;}
    public void setTask_date (String task_date){this.task_date=task_date;}

    public int getTask_number(){return this.task_number;}
    public String getTask_name(){return this.task_name;}
    public String getTask_block(){return this.task_block;}
    public String getTask_picture(){return this.task_picture;}
    public String getTask_introduction(){return this.task_introduction;}
    public int getTask_type(){return this.task_type;}
    public int getTask_part(){return this.task_part;}
    public int getTask_developer(){return this.task_developer;}
    public int getTask_hot(){return this.task_hot;}
    public String getTask_date(){return this.task_date;}
}
