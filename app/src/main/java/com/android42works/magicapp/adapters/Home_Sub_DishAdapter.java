package com.android42works.magicapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.Home_ChildInterface;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class Home_Sub_DishAdapter extends RecyclerView.Adapter<Home_Sub_DishAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Home_ChildInterface myInterface;
    private ArrayList<HomeDishesResponse.Dishes> dishesList;
    private int parentPosition;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public Home_Sub_DishAdapter(Context context, Home_ChildInterface myInterface, ArrayList<HomeDishesResponse.Dishes> dishesList, int parentPosition) {

        this.context = context;
        this.myInterface = myInterface;
        this.dishesList = dishesList;
        this.parentPosition = parentPosition;
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

        for(int i=0; i<dishesList.size(); i++){
            String id = dishesList.get(i).getId();
            if(id.equalsIgnoreCase(dishId)){
                dishesList.get(i).setFavorited(isFavourite);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public Home_Sub_DishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_home_child_dish, parent, false);
        return new Home_Sub_DishAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Home_Sub_DishAdapter.ViewHolder holder, final int position) {

        if(position==5){

            holder.rl_see_all.setVisibility(View.VISIBLE);
            holder.rl_parent.setVisibility(View.GONE);

        }else {

            holder.rl_parent.setVisibility(View.VISIBLE);
            holder.rl_see_all.setVisibility(View.GONE);

            holder.txt_name.setText(dishesList.get(position).getName());

            holder.txt_calorie.setText(dishesList.get(position).getCalories());

            String strPrice = dishesList.get(position).getPrice();

            if (strPrice.trim().length() == 0) {
                holder.txt_cost.setText("N/A");
            } else {
                double value = Double.parseDouble(strPrice);
                holder.txt_cost.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));
            }

            if (dishesList.get(position).getVeg().equalsIgnoreCase("veg")) {
                holder.img_veg_nonveg.setImageResource(R.drawable.ic_veg);
            } else if (dishesList.get(position).getVeg().equalsIgnoreCase("non-veg")) {
                holder.img_veg_nonveg.setImageResource(R.drawable.ic_nonveg);
            } else if (dishesList.get(position).getVeg().equalsIgnoreCase("contain egg")) {
                holder.img_veg_nonveg.setImageResource(R.drawable.ic_egg);
            }

            if (dishesList.get(position).getFavorited().equalsIgnoreCase("true")) {
                holder.img_favourite.setImageResource(R.drawable.ic_heart_filled);
            } else {
                holder.img_favourite.setImageResource(R.drawable.ic_heart_empty);
            }

            if (dishesList.get(position).getBest_seller().equalsIgnoreCase("1")) {
                holder.txt_best_seller.setVisibility(View.VISIBLE);
                holder.rl_bg_bestseller.setVisibility(View.VISIBLE);
            } else {
                holder.txt_best_seller.setVisibility(View.GONE);
                holder.rl_bg_bestseller.setVisibility(View.GONE);
            }

            holder.txt_option_label.setText("Allergens: ");
            holder.txt_option.setText("N/A");

            if (dishesList.get(position).getAttributes() != null) {

                HomeDishesResponse.Attributes[] attributesArray = dishesList.get(position).getAttributes();

                if (attributesArray.length != 0) {

                    for (int i = 0; i < attributesArray.length; i++) {

                        String optionName = attributesArray[i].getName();
                        if (optionName.equalsIgnoreCase("Allergens")) {

                            if (attributesArray[i].getValues() != null) {

                                String strAttributes = "";

                                String[] strArrayattributes = attributesArray[i].getValues();

                                for (int j = 0; j < strArrayattributes.length; j++) {

                                    if (j != 0) {
                                        strAttributes = strAttributes + ", " + strArrayattributes[j];
                                    } else {
                                        strAttributes = strArrayattributes[j];
                                    }

                                }

                                if (strAttributes.trim().length() != 0) {
                                    holder.txt_option.setText(strAttributes);
                                }

                            }

                        }

                    }


                }

            }

                String imageURL = dishesList.get(position).getImage();
                if (imageURL.trim().length() != 0) {

                    holder.img_dish.setVisibility(View.VISIBLE);
                    holder.ll_desc.setVisibility(View.GONE);

                    if (dishesList.get(position).getIsImageLoaded().equalsIgnoreCase("false")) {

                        holder.progress_load.setVisibility(View.VISIBLE);

                        imageLoader.displayImage(imageURL, holder.img_dish, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                dishesList.get(position).setIsImageLoaded("true");
                                holder.img_dish.setVisibility(View.VISIBLE);
                                holder.progress_load.setVisibility(View.GONE);
                                holder.ll_desc.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                holder.img_dish.setVisibility(View.GONE);
                                holder.progress_load.setVisibility(View.GONE);
                                holder.ll_desc.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                holder.img_dish.setVisibility(View.GONE);
                                holder.progress_load.setVisibility(View.GONE);
                                holder.ll_desc.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } else {

                    holder.img_dish.setVisibility(View.GONE);
                    holder.progress_load.setVisibility(View.GONE);
                    holder.ll_desc.setVisibility(View.VISIBLE);

                }

            String strHtml = "<b>Description : </b>" + dishesList.get(position).getDescription();
            holder.txt_desc.setText(Html.fromHtml(strHtml));

        }

        // Listeners

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onDishClick(dishesList.get(position).getId());
            }
        });

        holder.rl_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onSeeAllClick(parentPosition);
            }
        });

        holder.img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status = dishesList.get(position).getFavorited();
                if (status.equalsIgnoreCase("true")) {
                    myInterface.onDishFavouriteClick(dishesList.get(position).getId(), "false");
                } else {
                    myInterface.onDishFavouriteClick(dishesList.get(position).getId(), "true");
                }

            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name, txt_cost, txt_desc, txt_best_seller, txt_calorie, txt_option_label, txt_option;
        private RelativeLayout rl_parent, rl_see_all, rl_bg_bestseller;
        private LinearLayout ll_desc;
        private ImageView img_favourite, img_dish, img_veg_nonveg;
        private AVLoadingIndicatorView progress_load;

        ViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_cost = itemView.findViewById(R.id.txt_cost);
            txt_desc = itemView.findViewById(R.id.txt_desc);
            txt_option_label = itemView.findViewById(R.id.txt_option_label);
            txt_option = itemView.findViewById(R.id.txt_option);
            txt_calorie = itemView.findViewById(R.id.txt_calorie);
            txt_best_seller = itemView.findViewById(R.id.txt_best_seller);
            rl_parent = itemView.findViewById(R.id.rl_parent);
            img_favourite = itemView.findViewById(R.id.img_favourite);
            img_dish = itemView.findViewById(R.id.img_dish);
            img_veg_nonveg = itemView.findViewById(R.id.img_veg_nonveg);
            ll_desc = itemView.findViewById(R.id.ll_desc);
            rl_bg_bestseller = itemView.findViewById(R.id.rl_bg_bestseller);
            progress_load = itemView.findViewById(R.id.progress_load);
            rl_see_all = itemView.findViewById(R.id.rl_see_all);

        }

    }

    @Override
    public int getItemCount() {

        int count =  dishesList.size();
        if(count<5){
            return count;
        }else {
            return 6;
        }

    }



}
