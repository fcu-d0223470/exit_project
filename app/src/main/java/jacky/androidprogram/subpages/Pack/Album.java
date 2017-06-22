package jacky.androidprogram.subpages.Pack;

/**
 * Created by Jacky on 7/27/2016.
 */
public class Album {
    private int id;
    private String name;
    private int CompletedStory;
    private int numOfStory;
    private int thumbnail;
    private String sthumbnail;

    public Album() {
    }

    public Album(int id,String name, int CompleteStory, int numOfStory, int thumbnail) {
        this.id=id;
        this.name = name;
        this.CompletedStory = CompleteStory;
        this.numOfStory = numOfStory;
        this.thumbnail = thumbnail;
    }

    public Album(int id,String name, int CompleteStory, int numOfStory, String thumbnail) {
        this.id=id;
        this.name = name;
        this.CompletedStory = CompleteStory;
        this.numOfStory = numOfStory;
        this.sthumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfStory() {
        return numOfStory;
    }

    public void setNumOfStory(int numOfStory) {this.numOfStory = numOfStory;}

    public int getCompletedStory() {
        return CompletedStory;
    }

    public void setCompletedStory(int completedStory) {this.CompletedStory = completedStory;}

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getsThumbnail() {
        return sthumbnail;
    }

    public void setsThumbnail(String thumbnail) {
        this.sthumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}