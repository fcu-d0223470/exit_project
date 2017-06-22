package jacky.androidprogram.subpages.Pack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.R;
import jacky.androidprogram.subpages.PackQuestDetail;

/**
 * Created by Jacky on 7/27/2016.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    int task_id;
    int Owner_id;
    DBHandler db= new DBHandler(mContext);

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            //ImageLoader.getInstance().displayImage("http://www.ebizlearning.com.my/web/picture/2/2/main.jpg", thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album album = albumList.get(position);
        //int rand=0 + (int)(Math.random() * ((album.getNumOfSongs() - 0) + 1));
        holder.title.setText(album.getName());
        holder.count.setText("("+album.getCompletedStory()+"/"+album.getNumOfStory()+")");
        if(album.getCompletedStory()==album.getNumOfStory())holder.count.setText(holder.count.getText()+" "+mContext.getResources().getString(R.string.complete) );

        // loading album cover using Glide library
        //Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        File imgFile = new  File(album.getsThumbnail());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.thumbnail.setImageBitmap(myBitmap);
        }

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,String.valueOf(album.getId()),Toast.LENGTH_SHORT).show();
                task_id=album.getId();
                Intent Q = new Intent(mContext, PackQuestDetail.class);
                Bundle bundle=new Bundle();
                bundle.putInt("task_id",task_id);
                bundle.putInt("user_id",Owner_id);
                Q.putExtras(bundle);
                //String username = getIntent().getExtras().getString("owner");
                //Q.putExtra("owner",username);
                mContext.startActivity(Q);
            }
        });
        holder.overflow.setVisibility(View.GONE);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_my_album:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_add_photo:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
    public void setOwner_id(int id){this.Owner_id=id;}
}