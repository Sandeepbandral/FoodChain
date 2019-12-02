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
import com.android42works.magicapp.interfaces.DishOptionsInterface;
import com.android42works.magicapp.responses.FavouriteResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private DishOptionsInterface myInterface;
    private ArrayList<FavouriteResponse.Dishes> dishList;

    private boolean refreshImages = true;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public FavouritesAdapter(Context context, DishOptionsInterface myInterface, ArrayList<FavouriteResponse.Dishes> dishList) {

        this.context = context;
        this.myInterface = myInterface;
        this.dishList = dishList;
        this.mInflater = LayoutInflater.from(context);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public void updateList(Boolean refreshImages, ArrayList<FavouriteResponse.Dishes> dishList) {
        this.dishList = dishList;
        this.refreshImages = refreshImages;
        notifyDataSetChanged();
    }

    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_favourite, parent, false);
        return new FavouritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavouritesAdapter.ViewHolder holder, final int position) {

        holder.txt_name.setText(dishList.get(position).getName());

        double value = Double.parseDouble(dishList.get(position).getPrice());
        holder.txt_cost.setText(context.getString(R.string.currencySymbol) + " "+String.format("%.2f", value));

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

            FavouriteResponse.Attributes[] attributesArray = dishList.get(position).getAttributes();

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

        if (refreshImages) {

            String imageURL = dishList.get(position).getImage();
            if (imageURL.trim().length() != 0) {

                imageLoader.displayImage(imageURL, holder.img_dish, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        holder.img_dish.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.img_dish.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        holder.img_dish.setVisibility(View.GONE);
                    }
                });

            } else {

                holder.img_dish.setVisibility(View.GONE);

            }

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
                myInterface.onClick_Dish(dishList.get(position).getId());
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

        private TextView txt_name, txt_cost, txt_calorie, txt_allergie;
        private RelativeLayout rl_parent, rl_divider;
        private ImageView img_dish, img_favourite, img_veg_nonveg;

        ViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_cost = itemView.findViewById(R.id.txt_cost);
            txt_calorie = itemView.findViewById(R.id.txt_calorie);
            txt_allergie = itemView.findViewById(R.id.txt_allergie);
            rl_parent = itemView.findViewById(R.id.rl_parent);
            rl_divider = itemView.findViewById(R.id.rl_divider);
            img_dish = itemView.findViewById(R.id.img_dish);
            img_favourite = itemView.findViewById(R.id.img_favourite);
            img_veg_nonveg = itemView.findViewById(R.id.img_veg_nonveg);

        }

    }

}
