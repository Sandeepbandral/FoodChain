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
import com.android42works.magicapp.interfaces.HallMenu_ChildInterface;
import com.android42works.magicapp.interfaces.UnavailableDishInterface;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class HallMenu_ChildAdapter extends RecyclerView.Adapter<HallMenu_ChildAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private HallMenu_ChildInterface myInterface;
    private ArrayList<HallMenuResponse.Dishes> dishList;
    private Context context;
    private UnavailableDishInterface unavailableDishInterface;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public HallMenu_ChildAdapter(Context context, HallMenu_ChildInterface myInterface, ArrayList<HallMenuResponse.Dishes> dishList, UnavailableDishInterface unavailableDishInterface) {

        this.context = context;
        this.myInterface = myInterface;
        this.dishList = dishList;
        this.unavailableDishInterface = unavailableDishInterface;
        this.mInflater = LayoutInflater.from(context);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public void updateList(String dishId, String isFavourite) {

        for(int i=0; i<dishList.size(); i++){
            String id = dishList.get(i).getId();
            if(id.equalsIgnoreCase(dishId)){
                dishList.get(i).setFavorited(isFavourite);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public HallMenu_ChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_hallmenu_child_dish, parent, false);
        return new HallMenu_ChildAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HallMenu_ChildAdapter.ViewHolder holder, final int position) {

        if(dishList.get(position).getStatus().equalsIgnoreCase("1")){
            holder.rl_parent.setAlpha(1f);
        }else {
            holder.rl_parent.setAlpha(0.4f);
        }

        holder.txt_name.setText(dishList.get(position).getName());

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

            HallMenuResponse.Attributes[] attributesArray = dishList.get(position).getAttributes();

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

        if (dishList.get(position).getIsImageLoaded().equalsIgnoreCase("false")) {

            String imageURL = dishList.get(position).getImage();
            if (imageURL.trim().length() != 0) {

                imageLoader.displayImage(imageURL, holder.img_dish, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        dishList.get(position).setIsImageLoaded("true");
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


        // Listeners

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dishList.get(position).getStatus().equalsIgnoreCase("1")){
                    myInterface.onClick_dish(dishList.get(position).getId());
                }else {
                    new ConfirmOpenUnavailableDishDialog(context, unavailableDishInterface, dishList.get(position).getId()).show();
                }


            }
        });

        holder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dishList.get(position).getFavorited().equalsIgnoreCase("true")) {
                    myInterface.onClick_fav(dishList.get(position).getId(), "false");
                } else {
                    myInterface.onClick_fav(dishList.get(position).getId(), "true");
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
