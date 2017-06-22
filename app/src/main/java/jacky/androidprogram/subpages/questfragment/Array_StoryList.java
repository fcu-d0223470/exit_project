package jacky.androidprogram.subpages.questfragment;

/**
 * Created by Jacky on 10/30/2016.
 */

public class Array_StoryList {
    private String story_name;
    private int story_part;
    private String story_picture;
    private String story_address;

    public Array_StoryList(){}
    public Array_StoryList(String s_name, int s_part, String s_pic,  String s_addr)
    {
        this.story_name=s_name;
        this.story_part=s_part;
        this.story_picture=s_pic;
        this.story_address=s_addr;
    }
    public void setStory_name (String story_name){this.story_name=story_name;}
    public void setStory_part (int story_part){this.story_part=story_part;}
    public void setStory_picture (String story_picture){this.story_picture=story_picture;}
    public void setStory_address (String story_address){this.story_address=story_address;}

    public String getStory_name(){return this.story_name;}
    public int getStory_part(){return this.story_part;}
    public String getStory_picture(){return this.story_picture;}
    public String getStory_address(){return this.story_address;}
}
