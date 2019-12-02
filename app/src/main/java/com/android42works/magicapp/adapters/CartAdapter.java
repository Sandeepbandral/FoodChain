package com.android42works.magicapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android42works.magicapp.R;
import com.android42works.magicapp.cart.Items;
import com.android42works.magicapp.interfaces.CartInterface;
import com.android42works.magicapp.utils.SessionManager;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private CartInterface myInterface;
    private ArrayList<Items> cartList;
    private float totalCartItemsCost;
    private SessionManager sessionManager;


    public CartAdapter(Context context, CartInterface myInterface, ArrayList<Items> cartList) {
        this.context = context;
        this.myInterface = myInterface;
        this.cartList = cartList;
        this.mInflater = LayoutInflater.from(context);
        totalCartItemsCost = 0f;
        sessionManager = new SessionManager(context);
    }

    public void updateList(ArrayList<Items> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
        totalCartItemsCost = 0f;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_cart, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {

        holder.txt_name.setText(cartList.get(position).getItem_name());
        holder.txt_quantity.setText(cartList.get(position).getQty());

        /*This is to calculate the total price against the total quantity*/
        float quantity = Float.parseFloat(cartList.get(position).getQty());
        float cost = Float.parseFloat(cartList.get(position).getPrice());

        double addOnPrice = 0f;

        Float totalCostOfSingleItem = quantity * cost;

        for (int i = 0; i < cartList.get(position).getAddons().length; i++) {
            addOnPrice = addOnPrice + Double.parseDouble(cartList.get(position).getAddons()[i].getPrice());
        }

        if (sessionManager.isUserSkippedLoggedIn()) {
            totalCostOfSingleItem = totalCostOfSingleItem + ((float) (addOnPrice * quantity));
        }

        if(cartList.get(position).getStatus().equalsIgnoreCase("1")){
            holder.txt_available.setVisibility(View.GONE);
        }else {
            holder.txt_available.setVisibility(View.VISIBLE);
        }

        /*if (cartList.get(position).getAddons() != null) {
            Addons[] addon = cartList.get(position).getAddons();
            for (Addons addOn : addon) {
                totalCostOfSingleItem += Float.parseFloat(addOn.getPrice());
            }
        }*/

        totalCartItemsCost += totalCostOfSingleItem;

        if (position == cartList.size() - 1)
            myInterface.setTotalCostOfCartItems(totalCartItemsCost);

        double value = totalCostOfSingleItem;

        holder.txt_cost.setText(context.getString(R.string.currencySymbol) + " " + String.format("%.2f", value));


        if (position == cartList.size() - 1) {
            holder.rl_divider.setVisibility(View.GONE);
        } else {
            holder.rl_divider.setVisibility(View.VISIBLE);
        }

        if (cartList.get(position).getVeg().equalsIgnoreCase("veg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_veg);
        } else if (cartList.get(position).getVeg().equalsIgnoreCase("non-veg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_nonveg);
        } else if (cartList.get(position).getVeg().equalsIgnoreCase("contain egg")) {
            holder.img_veg_nonveg.setImageResource(R.drawable.ic_egg);
        }

        // Listeners
        holder.txt_remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick_Dish("removeDish", position, cartList.get(position).getId());
            }
        });

        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick_Dish("decreaseQuantity", position, cartList.get(position).getId());
            }
        });

        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick_Dish("increaseQuantity", position, cartList.get(position).getId());
            }
        });

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onClick_Dish("openDish", position, cartList.get(position).getDish_id());
            }
        });

        holder.ll_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do Nothing just for touch spacing
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name, txt_cost, txt_quantity, txt_remove_item, txt_available;
        private ImageView img_veg_nonveg, img_minus, img_plus;
        private RelativeLayout rl_divider;
        private LinearLayout ll_qty;
        private RelativeLayout rl_parent;

        ViewHolder(View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.txt_name);
            txt_available = itemView.findViewById(R.id.txt_available);
            txt_cost = itemView.findViewById(R.id.txt_cost);
            txt_remove_item = itemView.findViewById(R.id.txt_remove_item);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            img_veg_nonveg = itemView.findViewById(R.id.img_veg_nonveg);
            img_minus = itemView.findViewById(R.id.img_minus);
            img_plus = itemView.findViewById(R.id.img_plus);
            rl_divider = itemView.findViewById(R.id.rl_divider);
            rl_parent = itemView.findViewById(R.id.rl_parent);
            ll_qty = itemView.findViewById(R.id.ll_qty);

        }

    }

}
