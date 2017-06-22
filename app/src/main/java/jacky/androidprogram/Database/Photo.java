package jacky.androidprogram.Database;

/**
 * Created by Jacky on 12/1/2016.
 */

public class Photo {
    private long photo_serial;
    private String photo_dir;

    public Photo(){}
    public Photo (long serial,String photo){
        this.photo_serial=serial;
        this.photo_dir=photo;
    }

    public void setPhoto_serial(int serial){this.photo_serial=serial;}
    public void setPhoto_dir(String photo){this.photo_dir=photo;}

    public long getPhoto_serial(){return this.photo_serial;}
    public String getPhoto_dir(){return this.photo_dir;}
}
