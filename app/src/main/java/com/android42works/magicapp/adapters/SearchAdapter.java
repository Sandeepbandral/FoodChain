package com.android42works.magicapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.dialogs.ConfirmOpenUnavailableDishDialog;
import com.android42works.magicapp.interfaces.DishOptionsInterface;
import com.android42works.magicapp.interfaces.SearchInterface;
import com.android42works.magicapp.interfaces.UnavailableDishInterface;
import com.android42works.magicapp.responses.SearchResponse;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private DishOptionsInterface myInterface;
    private ArrayList<SearchResponse.Dishes> dishList;
    private Boolean showHallName = false;
    private UnavailableDishInterface unavailableDishInterface;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public SearchAdapter(Context context, DishOptionsInterface myInterface, ArrayList<SearchResponse.Dishes> dishList, Boolean showHallName, UnavailableDishInterface unavailableDishInterface) {

        this.context = context;
        this.myInterface = myInterface;
        this.showHallName = showHallName;
        this.dishList = dishList;
        this.unavailableDishInterface = unavailableDishInterface;
        this.mInflater = LayoutInflater.from(context);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.colorWhite)
                .showImageForEmptyUri(R.color.colorWhite)
                .showImageOnFail(R.color.colorWhite)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public void updateList(ArrayList<SearchResponse.Dishes> dishList, Boolean showHallName) {
        this.dishList = dishList;
        this.showHallName = showHallName;
        notifyDataSetChanged();
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_favourite, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.ViewHolder holder, final int position) {

        if(dishList.get(position).getStatus().equalsIgnoreCase("1")){
            holder.rl_parent.setAlpha(1f);
        }else {
            holder.rl_parent.setAlpha(0.4f);
        }

        holder.txt_name.setText(dishList.get(position).getName());

        if(showHallName){
            holder.txt_hallname.setVisibility(View.VISIBLE);
        }else {
            holder.txt_hallname.setVisibility(View.GONE);
        }

        holder.txt_hallname.setText(dishList.get(position).getHall_name());

        double value = Double.parseDouble(dishList.get(position).getPrice());

        holder.txt_cost.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));

        holder.txt_calorie.setText(dishList.get(position).getCalories());

        if (dishList.get(position).getVeg().equalsIgnoreCase("veg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_veg);
        } else if (dishList.get(position).getVeg().equalsIgnoreCase("non-veg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_nonveg);
        } else if (dishList.get(position).getVeg().equalsIgnoreCase("contain egg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_egg);
        }

        if (dishList.get(position).getFavorited().equalsIgnoreCase("true")) {
            holder.img_favourite.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.img_favourite.setImageResource(R.drawable.ic_heart_empty);
        }

        String strAttributes = "N/A";
        String strHtml = "<b>Allergens: </b>";

        if (dishList.get(position).getAttributes() != null) {

            SearchResponse.Attributes[] attributesArray = dishList.get(position).getAttributes();

            if (attributesArray.length != 0) {

                for(int i=0; i<attributesArray.length; i++){

                    String optionName = attributesArray[i].getName();
                    if(optionName.equalsIgnoreCase("Allergens")){

                        if (attributesArray[i].getValues() != null) {

                            strAttributes = "";

                            String[] strArrayattributes = attributesArray[i].getValues();

                            for (int j = 0; j < strArrayattributes.length; j++) {

                                if (j != 0) {
                                    strAttributes = strAttributes + ", " + strArrayattributes[j];
                                } else {
                                    strAttributes = strArrayattributes[j];
                                }

                            }

                        }

                    }

                }

            }

        }

        holder.txt_allergie.setText(Html.fromHtml(strHtml + strAttributes));

        if (position == dishList.size() - 1) {
            holder.rl_divider.setVisibility(View.GONE);
        } else {
            holder.rl_divider.setVisibility(View.VISIBLE);
        }


        String imageURL = dishList.get(position).getImage();
        if (imageURL.trim().length() != 0) {

            holder.rl_dish.setVisibility(View.VISIBLE);

                holder.progress_load.setVisibility(View.VISIBLE);

                imageLoader.displayImage(imageURL, holder.img_dish, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.progress_load.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.progress_load.setVisibility(View.GONE);
                        holder.rl_dish.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        holder.progress_load.setVisibility(View.GONE);
                        holder.rl_dish.setVisibility(View.GONE);
                    }
                });


        } else {

            holder.rl_dish.setVisibility(View.GONE);
            holder.progress_load.setVisibility(View.GONE);

        }



        if (position == dishList.size() - 1) {
            holder.rl_divider.setVisibility(View.GONE);
        } else {
            holder.rl_divider.setVisibility(View.VISIBLE);
        }

        // Listeners

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dishList.get(position).getStatus().equalsIgnoreCase("1")){
                    myInterface.onClick_Dish(dishList.get(position).getId());
                }else {
                    new ConfirmOpenUnavailableDishDialog(context, unavailableDishInterface, dishList.get(position).getId()).show();
                }

            }
        });

        holder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dishList.get(position).getFavorited().equalsIgnoreCase("true")) {
                    myInterface.onClick_Fav(position, dishList.get(position).getId(), "false");
                } else {
                    myInterface.onClick_Fav(position, dishList.get(position).getId(), "true");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name, txt_cost, txt_calorie, txt_allergie, txt_hallname;
        private RelativeLayout rl_parent, rl_divider, rl_dish;
        private ImageView img_dish, img_favourite, img_veg_nonveg;
        private AVLoadingIndicatorView progress_load;

        ViewHolder(View itemView) {
            super(itemView);

            txt_hallname = itemView.findViewById(R.id.txt_hallname);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_cost = itemView.findViewById(R.id.txt_cost);
            txt_calorie = itemView.findViewById(R.id.txt_calorie);
            txt_allergie = itemView.findViewById(R.id.txt_allergie);
            rl_parent = itemView.findViewById(R.id.rl_parent);
            rl_divider = itemView.findViewById(R.id.rl_divider);
            img_dish = itemView.findViewById(R.id.img_dish);
            img_favourite = itemView.findViewById(R.id.img_favourite);
            img_veg_nonveg = itemView.findViewById(R.id.img_veg_nonveg);
            rl_dish = itemView.findViewById(R.id.rl_dish);
            progress_load = itemView.findViewById(R.id.progress_load);

        }

    }

}
