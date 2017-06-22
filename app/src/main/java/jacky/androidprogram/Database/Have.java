package jacky.androidprogram.Database;

/**
 * Created by Jacky on 7/11/2016.
 */
public class Have {
    private String have_id;
    private int have_number;
    private int have_story_part;
    private int have_flag;
    private int have_task_flag;
    private int have_story_flag;
    private String have_date;

    public Have(){}
    public Have(String have_id,int have_number,int have_story_part,int have_flag,int have_task_flag,int have_story_flag,String have_date)
    {
        this.have_id=have_id;
        this.have_number=have_number;
        this.have_story_part=have_story_part;
        this.have_flag=have_flag;
        this.have_task_flag=have_task_flag;
        this.have_story_flag=have_story_flag;
        this.have_date=have_date;
    }
    public Have(String have_id,int have_number,int have_story_part,int have_flag)
    {
        this.have_id=have_id;
        this.have_number=have_number;
        this.have_story_part=have_story_part;
        this.have_flag=have_flag;
    }
    public void setHave_id(String have_id){this.have_id=have_id;}
    public void setHave_number(int have_number){this.have_number=have_number;}
    public void setHave_story_part(int have_story_part){this.have_story_part=have_story_part;}
    public void setHave_flag(int have_flag){this.have_flag=have_flag;}
    public void setHave_task_flag(int have_task_flag){this.have_task_flag=have_task_flag;}
    public void setHave_story_flag(int have_story_flag){this.have_story_flag=have_story_flag;}
    public void setHave_date(String have_date){this.have_date=have_date;}

    public String getHave_id(){return this.have_id;}
    public int getHave_number(){return this.have_number;}
    public int getHave_story_part(){return  this.have_story_part;}
    public int getHave_flag(){return this.have_flag;}
    public int getHave_task_flag(){return this.have_task_flag;}
    public int getHave_story_flag(){return this.have_story_flag;}
    public String getHave_date(){return this.have_date;}

    public Have CopyOf (){
        Have copy = new Have(
                this.getHave_id(),
                this.getHave_number(),
                this.getHave_story_part(),
                this.getHave_flag(),
                this.getHave_task_flag(),
                this.getHave_story_flag(),
                this.getHave_date());
        return copy;
    }
}
