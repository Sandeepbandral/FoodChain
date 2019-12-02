package com.android42works.magicapp.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class Home_SubPagerAdapter extends PagerAdapter{

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private Context context;
    private ArrayList<HomeDishesResponse.Banners> bannersList;

    public Home_SubPagerAdapter(Context context, ArrayList<HomeDishesResponse.Banners> bannersList) {
        this.context = context;
        this.bannersList = bannersList;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.listitem_image_slider, container, false);

        final ImageView img_offer = view.findViewById(R.id.img_offer);

        int itemPosition = position;

        if(bannersList.size()!=0){

            itemPosition = position % bannersList.size();

            if(bannersList.get(itemPosition).getBanner().trim().length()!=0){

                img_offer.setVisibility(View.VISIBLE);

                imageLoader.displayImage(bannersList.get(itemPosition).getBanner(), img_offer, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        img_offer.setVisibility(View.GONE);
                    }
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        img_offer.setVisibility(View.GONE);
                    }
                });

            }else {
                img_offer.setVisibility(View.GONE);
            }

        }

        container.addView(view);
        return view;
    }

}
