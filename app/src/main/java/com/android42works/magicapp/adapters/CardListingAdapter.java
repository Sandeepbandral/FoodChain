package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.interfaces.CardListingInterface;
import com.android42works.magicapp.models.CardsListModel;
import com.android42works.magicapp.responses.StripeSavedCardsListResponse;
import com.android42works.magicapp.utils.AppUtils;

import java.util.ArrayList;

public class CardListingAdapter extends RecyclerView.Adapter<CardListingAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private String defaultCardId = "";
    private CardListingInterface cardListingInterface;
    private ArrayList<CardsListModel> cardsList;
    private Context context;

    public CardListingAdapter(Context context, CardListingInterface cardListingInterface, ArrayList<CardsListModel> cardsList, String defaultCardId) {
        this.context = context;
        this.cardsList = cardsList;
        this.cardListingInterface = cardListingInterface;
        this.defaultCardId = defaultCardId;
        this.mInflater = LayoutInflater.from(context);
    }

    public void updateList(ArrayList<CardsListModel> cardsList, String defaultCardId){
        this.cardsList = cardsList;
        this.defaultCardId = defaultCardId;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(cardsList.get(position).getData().getId().equalsIgnoreCase(defaultCardId)){
            holder.btn_default.setVisibility(View.VISIBLE);
            holder.txt_setdefault.setText("");
        }else {
            holder.btn_default.setVisibility(View.GONE);
            holder.txt_setdefault.setText("Set as Default");
        }

        String cardBrand = cardsList.get(position).getData().getBrand();
        holder.img_card.setImageResource(AppUtils.getCardImage(cardBrand));

        holder.txt_cardno.setText("xxxx-xxxx-xxxx-" + cardsList.get(position).getData().getLast4());

        if(position==cardsList.size()-1){
            holder.rl_divider.setVisibility(View.GONE);
        }else {
            holder.rl_divider.setVisibility(View.VISIBLE);
        }

        if(cardsList.get(position).isSelected()){
            holder.img_tick.setImageResource(R.drawable.ic_tick_primary);
        }else {
            holder.img_tick.setImageResource(R.drawable.ic_tick_white);
        }

        /* Listeners */

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListingInterface.onClick_SelectCard(position);
            }
        });

        holder.txt_setdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListingInterface.onClick_SaveAsDefault(position);
            }
        });

        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListingInterface.onClick_DeleteCard(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_cardno, txt_setdefault;
        private ImageView img_card, img_close, img_tick;
        private RelativeLayout rl_divider, rl_parent;
        private Button btn_default;

        ViewHolder(View itemView) {
            super(itemView);

            img_card = itemView.findViewById(R.id.img_card);
            txt_cardno = itemView.findViewById(R.id.txt_cardno);
            rl_divider = itemView.findViewById(R.id.rl_divider);
            btn_default = itemView.findViewById(R.id.btn_default);
            txt_setdefault = itemView.findViewById(R.id.txt_setdefault);
            img_close = itemView.findViewById(R.id.img_close);
            img_tick = itemView.findViewById(R.id.img_tick);
            rl_parent = itemView.findViewById(R.id.rl_parent);

        }

    }

}
