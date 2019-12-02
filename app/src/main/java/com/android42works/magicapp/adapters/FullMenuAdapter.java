package com.android42works.magicapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.utils.AppUtils;
import com.android42works.magicapp.utils.SessionManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FullMenuAdapter extends RecyclerView.Adapter<FullMenuAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<HomeDishesResponse.Halls> hallList;
    private HomeDishesResponse.Halls[] hallsResponse;
    private Context context;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();

    String getCurrentDay = "";

    Thread thread;

    public FullMenuAdapter(Context context, ArrayList<HomeDishesResponse.Halls> hallList, HomeDishesResponse.Halls[] hallsResponse) {

        this.context = context;
        this.hallList = hallList;
        this.hallsResponse = hallsResponse;
        this.mInflater = LayoutInflater.from(context);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
//                .displayer(new RoundedBitmapDisplayer(35))
                .showImageOnLoading(R.color.colorActivityBackground)
                .showImageForEmptyUri(R.color.colorPrimary)
                .showImageOnFail(R.color.colorPrimary)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        getCurrentDay = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
    }

    @Override
    public FullMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_hall, parent, false);
        return new FullMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FullMenuAdapter.ViewHolder holder, final int position) {

        holder.txt_name.setText(hallList.get(position).getName());

        imageLoader.displayImage(hallList.get(position).getLogo(), holder.img_hall, options, null);

        if (AppUtils.isHallCurrentlyClosed(hallList.get(position).getId(), hallsResponse)) {

            holder.rLayoutHallClosed.setVisibility(View.VISIBLE);

            holder.rLayoutAplha.setAlpha(.8f);

            holder.handler = new android.os.Handler();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                // TODO Auto-generated method stub

                    holder.handler.postDelayed(this, 2000);

                    if (holder.txtHallClosed.isShown()) {

                        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadeout);
                        // Now Set your animation
                        holder.txtHallClosed.startAnimation(fadeInAnimation);

                        holder.txtHallClosed.setVisibility(View.GONE);

                    } else {
                        holder.txtHallClosed.setVisibility(View.VISIBLE);

                        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadein);
                        // Now Set your animation
                        holder.txtHallClosed.startAnimation(fadeInAnimation);
                    }
                }

            };
            holder.handler.post(r);


        } else {

            holder.rLayoutHallClosed.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return hallList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name, txtHallClosed;
        private ImageView img_hall;
        private RelativeLayout rLayoutHallClosed, rLayoutAplha;
        private int getDay = 0;
        private Handler handler;

        ViewHolder(View itemView) {
            super(itemView);

            txtHallClosed = itemView.findViewById(R.id.txtHallClosed);
            txt_name = itemView.findViewById(R.id.txt_name);
            img_hall = itemView.findViewById(R.id.img_hall);

            rLayoutAplha = itemView.findViewById(R.id.rLayoutAplha);
            rLayoutHallClosed = itemView.findViewById(R.id.rLayoutHallClosed);
        }

    }

}
