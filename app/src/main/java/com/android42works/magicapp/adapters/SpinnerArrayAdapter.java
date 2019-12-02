package com.android42works.magicapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerArrayAdapter {

    private Context context;
    private Spinner mySpinner;
    private String[] spinnerList;
    private int spinnerlayout, hintcolor, normalcolor;
    private boolean disableFirstIndex = false;

    public SpinnerArrayAdapter(Context context, Spinner mySpinner, ArrayList<String> spinnerArrayList,
                               int spinnerlayout, boolean disableFirstIndex, int normalcolor, int hintcolor) {

        this.context = context;
        this.mySpinner = mySpinner;
        this.spinnerlayout = spinnerlayout;
        this.disableFirstIndex = disableFirstIndex;
        this.hintcolor = hintcolor;
        this.normalcolor = normalcolor;

        spinnerList = new String[spinnerArrayList.size()];
        for (int i=0; i<spinnerArrayList.size(); i++){
            spinnerList[i] = spinnerArrayList.get(i);
        }

    }

    public void setAdapter(){

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, spinnerlayout,spinnerList){
            @Override
            public boolean isEnabled(int position){

                if(disableFirstIndex) {

                    if(position == 0)
                    {
                        // Disable the first item from Spinner and First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }

                }

                return true;

            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView tv = (TextView) view;

                if(disableFirstIndex) {

                    if(position == 0){
                        tv.setTextColor(context.getResources().getColor(hintcolor));
                    }
                    else {
                        tv.setTextColor(context.getResources().getColor(normalcolor));
                    }

                }

                tv.setTextColor(context.getResources().getColor(normalcolor));

                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(spinnerlayout);
        mySpinner.setAdapter(spinnerArrayAdapter);

    }

}
