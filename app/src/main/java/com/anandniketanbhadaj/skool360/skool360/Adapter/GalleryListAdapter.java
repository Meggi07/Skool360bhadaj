package com.anandniketanbhadaj.skool360.skool360.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anandniketanbhadaj.skool360.R;
import com.anandniketanbhadaj.skool360.skool360.Fragments.HomeFragment;
import com.anandniketanbhadaj.skool360.skool360.Interfacess.onViewClick;
import com.anandniketanbhadaj.skool360.skool360.Utility.AppConfiguration;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.MyViewHolder> {

    List<String> arrayList;
    onViewClick onViewClick;
    List<String> name;
    private Context mContext;
    private ImageLoader imageLoader;
    private ArrayList<String> PhotoDetail;

    public GalleryListAdapter(Context mContext, ArrayList<String> name, ArrayList<String> arrayList, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.onViewClick = onViewClick;
    }

    @Override
    public GalleryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_grid_item, parent, false);

        return new GalleryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GalleryListAdapter.MyViewHolder holder, final int position) {
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .threadPriority(Thread.MAX_PRIORITY)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)// .enableLogging()
                .build();
        imageLoader.init(config.createDefault(mContext));

        String image, name;
        String[] splitvalue = arrayList.get(position).split("\\|");
        image = splitvalue[0];
        name = splitvalue[1];

        imageLoader.displayImage(AppConfiguration.GALLARY_LIVE+ image, holder.event_image_img);
        holder.pic_name.setText(name);

        holder.main_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoDetail=new ArrayList<>();
                PhotoDetail.add(String.valueOf(position));
                onViewClick.getViewClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pic_name;
        ImageView event_image_img;
        ConstraintLayout main_linear;

        public MyViewHolder(View view) {
            super(view);
            pic_name = (TextView) view.findViewById(R.id.pic_name);
            event_image_img = (ImageView) view.findViewById(R.id.event_image_img);
            main_linear = (ConstraintLayout) view.findViewById(R.id.main_linear);
        }


    }

    public ArrayList<String> getPhotoDetail() {
        return PhotoDetail;
    }
}


