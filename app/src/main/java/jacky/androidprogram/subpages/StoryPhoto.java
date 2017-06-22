package jacky.androidprogram.subpages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.Photo;
import jacky.androidprogram.R;

    public class StoryPhoto extends Activity{

        private LinearLayout lnrImages;
        private Button btnAddPhots;
        private Button btnSaveImages;
        private ArrayList<String> imagesPathList;
        private Photo[] photoList;
        private Bitmap yourbitmap;
        private Bitmap resized;
        private final int PICK_IMAGE_MULTIPLE =1;
        long story_selected;
        DBHandler db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_story_photo);
            lnrImages = (LinearLayout)findViewById(R.id.lnrImages);
            btnAddPhots = (Button)findViewById(R.id.btnAddPhots);
            btnSaveImages = (Button)findViewById(R.id.btnSaveImages);

            db=new DBHandler(this);
            Intent intentext =getIntent();
            Bundle bundle = intentext.getExtras();
            story_selected = bundle.getLong("photo_serial");

            List<Photo>photos=db.getAllPhotoBySerial(String.valueOf(story_selected));
            photoList=new Photo[photos.size()];
            int i=0;
            for(Photo photo:photos){
                photoList[i]=photo;
                ///////////////////////////////////////////////
                yourbitmap = BitmapFactory.decodeFile(photoList[i].getPhoto_dir());
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(yourbitmap);
                imageView.setAdjustViewBounds(true);
                lnrImages.addView(imageView);
                TextView imageURI = new TextView(this);
                imageURI.setText("::"+photoList[i].getPhoto_dir()+"::");
                imageURI.setTextColor(Color.WHITE);
                lnrImages.addView(imageURI);
                //////////////////////////////////////////////
                i++;
            }

            btnAddPhots.setVisibility(View.INVISIBLE);
            btnSaveImages.setVisibility(View.INVISIBLE);
            btnAddPhots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StoryPhoto.this,CustomPhotoGalleryActivity.class);
                    startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
                }
            });
            btnSaveImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imagesPathList !=null){
                        if(imagesPathList.size()>1) {
                            Toast.makeText(StoryPhoto.this, imagesPathList.size() + " no of images are selected", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(StoryPhoto.this, imagesPathList.size() + " no of image are selected", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(StoryPhoto.this," no images are selected", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
       /* @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnAddPhots:
                    Intent intent = new Intent(StoryPhoto.this,CustomPhotoGalleryActivity.class);
                    startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
                    break;
                case R.id.btnSaveImages:
                    if(imagesPathList !=null){
                        if(imagesPathList.size()>1) {
                            Toast.makeText(StoryPhoto.this, imagesPathList.size() + " no of images are selected", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(StoryPhoto.this, imagesPathList.size() + " no of image are selected", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(StoryPhoto.this," no images are selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if(requestCode == PICK_IMAGE_MULTIPLE){
                    imagesPathList = new ArrayList<String>();
                    String[] imagesPath = data.getStringExtra("data").split("\\|");
                    try{
                        lnrImages.removeAllViews();
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                    for (int i=0;i<imagesPath.length;i++){
                        imagesPathList.add(imagesPath[i]);
                        yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                        ImageView imageView = new ImageView(this);
                        imageView.setImageBitmap(yourbitmap);
                        imageView.setAdjustViewBounds(true);
                        lnrImages.addView(imageView);
                        TextView imageURI = new TextView(this);
                        imageURI.setText("::"+imagesPath[i]+"::");
                        lnrImages.addView(imageURI);
                    }
                }
            }

        }

}
