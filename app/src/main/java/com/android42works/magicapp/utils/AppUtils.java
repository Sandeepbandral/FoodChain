package com.android42works.magicapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.android42works.magicapp.R;
import com.android42works.magicapp.models.FutureTimeChildModel;
import com.android42works.magicapp.models.FutureTimeParentModel;
import com.android42works.magicapp.responses.HallMenuResponse;
import com.android42works.magicapp.responses.HomeDishesResponse;
import com.android42works.magicapp.responses.Range;
import com.android42works.magicapp.responses.Timing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AppUtils {

    static String strTimezoneSymbol = "";

    private static HomeDishesResponse.Halls[] halls;

    public static HomeDishesResponse.Halls[] checkAndSetTimingIssue(HomeDishesResponse.Halls[] hallsResponse){

        halls = hallsResponse;

        try {

            for (int i = 0; i < halls.length; i++) {

                Timing[] hallTiming = checkAndSetInternalTimings(halls[i].getTiming());
                halls[i].setTiming(hallTiming);

                if (null != hallsResponse[i].getMeal_timings()) {

                    HomeDishesResponse.Meal_timing[] timingList = hallsResponse[i].getMeal_timings();

                    for (int j = 0; j < timingList.length; j++) {

                        Timing[] mealTiming = timingList[j].getTiming();
                        if (null != mealTiming) {
                            mealTiming = checkAndSetInternalTimings(timingList[j].getTiming());
                            timingList[j].setTiming(mealTiming);
                        }

                    }

                    halls[i].setMeal_timings(timingList);

                }

            }

        }catch (Exception e){
            Log.e("time error", e.getMessage());
        }

       return halls;
    }

    public static Timing[] checkAndSetInternalTimings(Timing[] timing){

        try {

            for (int j = 0; j < timing.length; j++) {

                Range[] range = timing[j].getRange();

                for (int k = 0; k < range.length; k++) {

                    String openingTime = range[k].getStart_time();
                    String closingTime = range[k].getEnd_time();

                    String temp1 = openingTime.toLowerCase();
                    String temp2 = closingTime.toLowerCase();

                    Date dateHallOpeningTime = AppUtils.getTimeFrom12HoursString(openingTime);
                    Date dateHallClosingTime = AppUtils.getTimeFrom12HoursString(closingTime);

                    // CASE 1 ( PM -> AM )

                    if (temp1.contains("pm") && temp2.contains("am")) {

                        int positionSelected = 0;

                        if (strTimezoneSymbol.equalsIgnoreCase("plus")) {

                            if (j == 6) {
                                positionSelected = 0;
                            } else {
                                positionSelected = j + 1;
                            }

                        } else {

                            if (j == 0) {
                                positionSelected = 6;
                            } else {
                                positionSelected = j - 1;
                            }

                        }

                        Range[] rangeList = timing[positionSelected].getRange();
                        int newLength = rangeList.length + 1;
                        Range[] newRangeList = new Range[newLength];

                        for (int l = 0; l < rangeList.length; l++) {
                            newRangeList[l] = rangeList[l];
                        }

                        Range newRange = new Range();

                        if (strTimezoneSymbol.equalsIgnoreCase("plus")) {

                            newRange.setStart_time("12:00 AM");
                            newRange.setEnd_time(closingTime);
                            newRangeList[newLength - 1] = newRange;
                            timing[positionSelected].setRange(newRangeList);
                            range[k].setEnd_time("11:59 PM");
                            timing[j].setRange(range);

                        } else {

                            newRange.setStart_time(openingTime);
                            newRange.setEnd_time("11:59 PM");
                            newRangeList[newLength - 1] = newRange;
                            timing[positionSelected].setRange(newRangeList);
                            range[k].setStart_time("12:00 AM");
                            timing[j].setRange(range);

                        }

                    }

                    // CASE 2 ( AM -> AM  ||  PM -> PM )

                    if ((temp1.contains("am") && temp2.contains("am")) || (temp1.contains("pm") && temp2.contains("pm"))) {

                        if (dateHallOpeningTime.after(dateHallClosingTime)) {

                            int positionSelected = 0;

                            if (strTimezoneSymbol.equalsIgnoreCase("plus")) {

                                if (j == 6) {
                                    positionSelected = 0;
                                } else {
                                    positionSelected = j + 1;
                                }

                            } else {

                                if (j == 0) {
                                    positionSelected = 6;
                                } else {
                                    positionSelected = j - 1;
                                }

                            }

                            Range[] rangeList = timing[positionSelected].getRange();
                            int newLength = rangeList.length + 1;
                            Range[] newRangeList = new Range[newLength];

                            for (int l = 0; l < rangeList.length; l++) {
                                newRangeList[l] = rangeList[l];
                            }

                            Range newRange = new Range();

                            if (strTimezoneSymbol.equalsIgnoreCase("plus")) {

                                newRange.setStart_time("12:00 AM");
                                newRange.setEnd_time(closingTime);
                                newRangeList[newLength - 1] = newRange;
                                timing[positionSelected].setRange(newRangeList);
                                range[k].setEnd_time("11:59 PM");
                                timing[j].setRange(range);

                            } else {

                                newRange.setStart_time(openingTime);
                                newRange.setEnd_time("11:59 PM");
                                newRangeList[newLength - 1] = newRange;
                                timing[positionSelected].setRange(newRangeList);
                                range[k].setStart_time("12:00 AM");
                                timing[j].setRange(range);

                            }

                        }

                    }

                }

            }
        }catch (Exception e){
            Log.e("time error", e.getMessage());
        }

        return timing;

    }

    public static String getTimeZone(){

        String timezone = "";

        TimeZone tz = TimeZone.getDefault();

        String info = tz.getDisplayName(false, TimeZone.SHORT);

        if(info.contains("+")){
            strTimezoneSymbol = "plus";
        }else if(info.contains("-")){
            strTimezoneSymbol = "minus";
        }else {
            strTimezoneSymbol = "";
        }

        timezone = tz.getID();

       return  timezone;

    }

    public static String roundOffDecimalToInt(String value){

        String finalValue = "";

        try {
            float floatValue = Float.parseFloat(value);
            int intValue = Math.round(floatValue);
            finalValue = String.valueOf(intValue);
        }catch (Exception e){

        }

        return finalValue;
    }

    public static boolean isMealTimeCurrentlyClosed(String hallId, String mealId, HomeDishesResponse.Halls[] halls){

        boolean isMealTimingClosed = true;

        Calendar now = Calendar.getInstance();

        Date dateCurrentTime = AppUtils.getTimeFrom24HoursString(checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + checkForDoubleDigits(now.get(Calendar.MINUTE)));

        String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
            Date myDate = sdf.parse(selectedDate);
            sdf.applyPattern("EEE");
            String selectedDay = sdf.format(myDate);

            for (int i = 0; i < halls.length; i++) {

                String id = halls[i].getId();

                if (id.equalsIgnoreCase(hallId)) {

                    if(null!=halls[i].getTiming()) {

                        HomeDishesResponse.Meal_timing[] mealTimings = halls[i].getMeal_timings();

                        for (int j = 0; j < mealTimings.length; j++) {

                            String strMealId = mealTimings[j].getId();

                            if (strMealId.equalsIgnoreCase(mealId)) {

                                if(null!=mealTimings[j].getTiming()) {

                                    Timing[] timings = mealTimings[j].getTiming();

                                    for (int k = 0; k < timings.length; k++) {

                                        String strDay = timings[k].getDay();

                                        if (strDay.equalsIgnoreCase(selectedDay)) {

                                            if (null != timings[k].getRange()) {

                                                Range[] range = timings[k].getRange();

                                                for (int l = 0; l < range.length; l++) {

                                                    String openingTime = range[l].getStart_time();
                                                    String closingTime = range[l].getEnd_time();

                                                    Date dateMealOpeningTime = AppUtils.getTimeFrom12HoursString(openingTime);
                                                    Date dateMealClosingTime = AppUtils.getTimeFrom12HoursString(closingTime);

                                                    if (dateCurrentTime.equals(dateMealOpeningTime) || dateCurrentTime.equals(dateMealClosingTime)) {
                                                        isMealTimingClosed = false;
                                                        return isMealTimingClosed;
                                                    }

                                                    if (dateCurrentTime.after(dateMealOpeningTime) && dateCurrentTime.before(dateMealClosingTime)) {
                                                        isMealTimingClosed = false;
                                                        return isMealTimingClosed;
                                                    }

                                                }

                                            }

                                            return isMealTimingClosed;

                                        }

                                    }

                                }

                            }

                        }
                    }

                }

            }

        } catch (Exception e) {

        }

        return isMealTimingClosed;

    }

    public static boolean isHallCurrentlyClosed(String hallId, HomeDishesResponse.Halls[] halls){

        boolean isHallClosed = true;

        Calendar now = Calendar.getInstance();

        Date dateCurrentTime = AppUtils.getTimeFrom24HoursString(checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + checkForDoubleDigits(now.get(Calendar.MINUTE)));

        String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
            Date myDate = sdf.parse(selectedDate);
            sdf.applyPattern("EEE");
            String selectedDay = sdf.format(myDate);

            for (int i = 0; i < halls.length; i++) {

                String id = halls[i].getId();

                if (id.equalsIgnoreCase(hallId)) {

                    if(null!=halls[i].getTiming()) {

                        Timing[] timings = halls[i].getTiming();

                        for (int j = 0; j < timings.length; j++) {

                            String strDay = timings[j].getDay();

                            if (strDay.equalsIgnoreCase(selectedDay)) {

                                if (null != timings[j].getRange()) {

                                    Range[] range = timings[j].getRange();

                                    for (int k = 0; k < range.length; k++) {

                                        String hallOpeningTime = range[k].getStart_time();
                                        String hallClosingTime = range[k].getEnd_time();

                                        Date dateHallOpeningTime = AppUtils.getTimeFrom12HoursString(hallOpeningTime);
                                        Date dateHallClosingTime = AppUtils.getTimeFrom12HoursString(hallClosingTime);

                                        if (dateCurrentTime.equals(dateHallOpeningTime) || dateCurrentTime.equals(dateHallClosingTime)) {
                                            isHallClosed = false;
                                            return isHallClosed;
                                        }

                                        if (dateCurrentTime.after(dateHallOpeningTime) && dateCurrentTime.before(dateHallClosingTime)) {
                                            isHallClosed = false;
                                            return isHallClosed;
                                        }

                                    }

                                }

                                return isHallClosed;

                            }

                        }
                    }

                }
            }

        } catch (Exception e) {

        }

        return isHallClosed;

    }

    public static String getNextHallOpeningDateTime(String hallId, HomeDishesResponse.Halls[] halls){

        Calendar now = Calendar.getInstance();

        Date dateCurrentTime = AppUtils.getTimeFrom24HoursString(checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + checkForDoubleDigits(now.get(Calendar.MINUTE)));

        /* Getting hall timings */

        Timing[] timings = null;

        for (int i = 0; i < halls.length; i++) {

            String id = halls[i].getId();
            if (id.equalsIgnoreCase(hallId)) {
                timings = halls[i].getTiming();
            }

        }

        if(timings!=null) {

            /* Getting next opening date n time */

            for (int i = 0; i < 8; i++) {

                if (i != 0) {
                    now.add(Calendar.DATE, 1);
                }

                String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);

                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
                    Date myDate = sdf.parse(selectedDate);
                    sdf.applyPattern("EEE");
                    String selectedDay = sdf.format(myDate);

                    for (int j = 0; j < timings.length; j++) {

                        String strDay = timings[j].getDay();

                        if (strDay.equalsIgnoreCase(selectedDay)) {

                            if(null!=timings[j].getRange()) {

                                Range[] range = timings[j].getRange();

                                for (int k = 0; k < range.length; k++) {

                                    String hallOpeningTime = range[k].getStart_time();
                                    String hallClosingTime = range[k].getEnd_time();

                                    if (i == 0) {

                                        Date dateHallOpeningTime = AppUtils.getTimeFrom12HoursString(hallOpeningTime);
                                        Date dateHallClosingTime = AppUtils.getTimeFrom12HoursString(hallClosingTime);

                                        if (dateCurrentTime.equals(dateHallOpeningTime) || dateCurrentTime.equals(dateHallClosingTime)) {
                                            String nextOpeningDateTime = doubleToSingleTimeFirstDigit(hallOpeningTime) + " " + "Today.";
                                            return nextOpeningDateTime;
                                        }

                                        if (dateCurrentTime.after(dateHallOpeningTime) && dateCurrentTime.before(dateHallClosingTime)) {
                                            String nextOpeningDateTime = doubleToSingleTimeFirstDigit(hallOpeningTime) + " " + "Today.";
                                            return nextOpeningDateTime;
                                        }

                                        if(dateCurrentTime.before(dateHallOpeningTime)){
                                            String nextOpeningDateTime = doubleToSingleTimeFirstDigit(hallOpeningTime) + " " + "Today.";
                                            return nextOpeningDateTime;
                                        }

                                    } else {

                                        if(i==1){

                                            String nextOpeningDateTime = doubleToSingleTimeFirstDigit(hallOpeningTime) + " " + "Tomorrow.";
                                            return nextOpeningDateTime;

                                        }else {

                                            try{
                                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
                                                Date tempDate = sdf1.parse(selectedDate);
                                                sdf1.applyPattern("dd");
                                                String temp1 = sdf1.format(tempDate);
                                                String temp2 = temp1.substring(0,1);
                                                if(temp2.equals("0")){
                                                    temp1 = temp1.substring(1);
                                                }
                                                sdf1.applyPattern("MMM");
                                                String temp3 = sdf1.format(tempDate);
                                                String nextOpeningDateTime = doubleToSingleTimeFirstDigit(hallOpeningTime) + " on " + temp1 + " " + temp3 + ".";
                                                return nextOpeningDateTime;

                                            }catch (Exception e){}

                                        }


                                    }

                                }

                            }

                        }

                    }

                } catch (Exception e) {}

            }

        }

        return "";

    }

    public static String doubleToSingleTimeFirstDigit(String strTime){

        String strNewTime = "";

        String[] temp = strTime.split(":");
        strNewTime = temp[0];
        String temp2 = strNewTime.substring(0,1);
        if(temp2.equals("0")){
            strNewTime = strNewTime.substring(1);
        }

        strNewTime = strNewTime + ":" + temp[1];

        return strNewTime;
    }

    public static ArrayList<FutureTimeParentModel> getHallFutureTimings(String hallId, HomeDishesResponse.Halls[] halls){

        ArrayList<FutureTimeParentModel> futureTimings = new ArrayList<>();

        Calendar now = Calendar.getInstance();

        Date dateCurrentTime = AppUtils.getTimeFrom24HoursString(checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + checkForDoubleDigits(now.get(Calendar.MINUTE)));

        for (int x=0; x<7; x++){

            if(x!=0){ now.add(Calendar.DATE, 1); }

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
            String selectedDay = dayFormat.format(now.getTime());

            for (int i = 0; i < halls.length; i++) {

                String id = halls[i].getId();

                if (id.equalsIgnoreCase(hallId)) {

                    if(null!=halls[i].getTiming()) {

                        Timing[] timings = halls[i].getTiming();

                        List<FutureTimeChildModel> timingList = new ArrayList<>();

                        boolean isDayMatchFound = false;

                        for (int j = 0; j < timings.length; j++) {

                            String strDay = timings[j].getDay();

                            timingList = new ArrayList<>();

                            if (strDay.equalsIgnoreCase(selectedDay)){

                                if(null!=timings[j].getRange()) {

                                    Range[] range = timings[j].getRange();

                                    for (int k = 0; k < range.length; k++) {

                                        String hallOpeningTime = range[k].getStart_time();
                                        String hallClosingTime = range[k].getEnd_time();

                                        timingList.add(new FutureTimeChildModel(hallOpeningTime + " - " + hallClosingTime, hallOpeningTime, hallClosingTime));

                                    }

                                }

                            }

                            if(timingList.size()!=0){

                                isDayMatchFound = true;

                                String strSelectedDate = "N/A";

                                if(x==0){
                                    strSelectedDate = "Today";
                                }

                                if(x==1){
                                    strSelectedDate = "Tomorrow";
                                }

                                if(x>1){

                                    String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
                                    try{
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
                                        Date tempDate = sdf.parse(selectedDate);
                                        sdf.applyPattern("dd MMM, EEE");
                                        strSelectedDate = sdf.format(tempDate);
                                    }catch (Exception e){}

                                }

                                futureTimings.add(new FutureTimeParentModel(strSelectedDate, timingList, futureTimings.size()));

                            }


                        }

                        if(isDayMatchFound==false){

                            timingList = new ArrayList<>();
                            timingList.add(new FutureTimeChildModel("Hall is closed", "", ""));

                            String strSelectedDate = "N/A";

                            if(x==0){
                                strSelectedDate = "Today";
                            }

                            if(x==1){
                                strSelectedDate = "Tomorrow";
                            }

                            if(x>1){

                                String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
                                try{
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
                                    Date tempDate = sdf.parse(selectedDate);
                                    sdf.applyPattern("dd MMM, EEE");
                                    strSelectedDate = sdf.format(tempDate);
                                }catch (Exception e){}

                            }

                            futureTimings.add(new FutureTimeParentModel(strSelectedDate, timingList, futureTimings.size()));

                        }

                    }

                }
            }


        }

        return futureTimings;

    }


    public static ArrayList<FutureTimeParentModel> getMealFutureTimings(String hallId, String mealIds, String mealNames, HomeDishesResponse.Halls[] halls){

        ArrayList<FutureTimeParentModel> futureTimings = new ArrayList<>();



        String[] mealId = mealIds.split(",");
        String[] mealName = mealNames.split(",");

        for (int z=0; z<mealId.length; z++) {

            Calendar now = Calendar.getInstance();

            for (int x = 0; x < 7; x++) {

                if (x != 0) {
                    now.add(Calendar.DATE, 1);
                }

                SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
                String selectedDay = dayFormat.format(now.getTime());

                for (int i = 0; i < halls.length; i++) {

                    String id = halls[i].getId();

                    if (id.equalsIgnoreCase(hallId)) {

                        if (null != halls[i].getMeal_timings()) {

                            HomeDishesResponse.Meal_timing[] mealTimings = halls[i].getMeal_timings();

                            for (int y = 0; y < mealTimings.length; y++) {

                                String strMealId = mealTimings[y].getId();

                                if (strMealId.equalsIgnoreCase(mealId[z])) {

                                    if (null != halls[i].getTiming()) {

                                        Timing[] timings = mealTimings[y].getTiming();

                                        List<FutureTimeChildModel> timingList = new ArrayList<>();

                                        boolean isDayMatchFound = false;

                                        for (int j = 0; j < timings.length; j++) {

                                            String strDay = timings[j].getDay();

                                            timingList = new ArrayList<>();

                                            if (strDay.equalsIgnoreCase(selectedDay)) {

                                                if (null != timings[j].getRange()) {

                                                    Range[] range = timings[j].getRange();

                                                    for (int k = 0; k < range.length; k++) {

                                                        String hallOpeningTime = range[k].getStart_time();
                                                        String hallClosingTime = range[k].getEnd_time();

                                                        timingList.add(new FutureTimeChildModel(hallOpeningTime + " - " + hallClosingTime + "  (" + mealName[z] + ")", hallOpeningTime, hallClosingTime));

                                                    }

                                                }

                                            }

                                            if (timingList.size() != 0) {

                                                isDayMatchFound = true;

                                                String strSelectedDate = "N/A";

                                                if (x == 0) {
                                                    strSelectedDate = "Today";
                                                }

                                                if (x == 1) {
                                                    strSelectedDate = "Tomorrow";
                                                }

                                                if (x > 1) {

                                                    String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
                                                    try {
                                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
                                                        Date tempDate = sdf.parse(selectedDate);
                                                        sdf.applyPattern("dd MMM, EEE");
                                                        strSelectedDate = sdf.format(tempDate);
                                                    } catch (Exception e) {
                                                    }

                                                }

                                                if(z==0){
                                                    futureTimings.add(new FutureTimeParentModel(strSelectedDate, timingList, futureTimings.size()));
                                                }else {
                                                    List<FutureTimeChildModel> childList = futureTimings.get(x).getChildItems();
                                                    childList.addAll(timingList);
                                                    futureTimings.get(x).setChildList(childList);
                                                }
                                            }
                                        }

                                        if (isDayMatchFound == false) {

                                            timingList = new ArrayList<>();
                                            timingList.add(new FutureTimeChildModel("Meal is closed  (" + mealName[z] + ")", "", ""));

                                            String strSelectedDate = "N/A";

                                            if (x == 0) {
                                                strSelectedDate = "Today";
                                            }

                                            if (x == 1) {
                                                strSelectedDate = "Tomorrow";
                                            }

                                            if (x > 1) {

                                                String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
                                                try {
                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
                                                    Date tempDate = sdf.parse(selectedDate);
                                                    sdf.applyPattern("dd MMM, EEE");
                                                    strSelectedDate = sdf.format(tempDate);
                                                } catch (Exception e) {
                                                }

                                            }

                                            if(z==0){
                                                futureTimings.add(new FutureTimeParentModel(strSelectedDate, timingList, futureTimings.size()));
                                            }else {
                                                List<FutureTimeChildModel> childList = futureTimings.get(x).getChildItems();
                                                childList.addAll(timingList);
                                                futureTimings.get(x).setChildList(childList);
                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }
        }

        return futureTimings;

    }


//
//    public static ArrayList<FutureTimeParentModel> getFutureTimings(String hallId, HomeDishesResponse.Halls[] halls){
//
//        ArrayList<FutureTimeParentModel> futureTimings = new ArrayList<>();
//
//        Calendar now = Calendar.getInstance();
//
//        Date dateCurrentTime = AppUtils.getTimeFrom24HoursString(checkForDoubleDigits(now.get(Calendar.HOUR_OF_DAY)) + ":" + checkForDoubleDigits(now.get(Calendar.MINUTE)));
//
//        for (int x=0; x<7; x++){
//
//            if(x!=0){ now.add(Calendar.DATE, 1); }
//
//            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
//            String selectedDay = dayFormat.format(now.getTime());
//
//            for (int i = 0; i < halls.length; i++) {
//
//                String id = halls[i].getId();
//
//                if (id.equalsIgnoreCase(hallId)) {
//
//                    if(null!=halls[i].getTiming()) {
//
//                        HomeDishesResponse.Timing[] timings = halls[i].getTiming();
//
//                        List<FutureTimeChildModel> timingList = new ArrayList<>();
//
//                        boolean isDayMatchFound = false;
//
//                        for (int j = 0; j < timings.length; j++) {
//
//                            String strDay = timings[j].getDay();
//
//                            timingList = new ArrayList<>();
//
//                            if (strDay.equalsIgnoreCase(selectedDay)){
//
//                                if(null!=timings[j].getRange()) {
//
//                                    Range[] range = timings[j].getRange();
//
//                                    for (int k = 0; k < range.length; k++) {
//
//                                        String hallOpeningTime = range[k].getStart_time();
//                                        String hallClosingTime = range[k].getEnd_time();
//
//                                        if (x == 0) {
//
//                                            Date dateHallOpeningTime = AppUtils.getTimeFrom12HoursString(hallOpeningTime);
//                                            Date dateHallClosingTime = AppUtils.getTimeFrom12HoursString(hallClosingTime);
//
//                                            if (dateCurrentTime.equals(dateHallOpeningTime) || dateCurrentTime.equals(dateHallClosingTime)) {
//                                                timingList.add(new FutureTimeChildModel(hallOpeningTime + " - " + hallClosingTime, hallOpeningTime, hallClosingTime));
//                                            }
//
//                                            if (dateCurrentTime.after(dateHallOpeningTime) && dateCurrentTime.before(dateHallClosingTime)) {
//                                                timingList.add(new FutureTimeChildModel(hallOpeningTime + " - " + hallClosingTime, hallOpeningTime, hallClosingTime));
//                                            }
//
//                                            if(dateCurrentTime.before(dateHallOpeningTime)){
//                                                timingList.add(new FutureTimeChildModel(hallOpeningTime + " - " + hallClosingTime, hallOpeningTime, hallClosingTime));
//                                            }
//
//                                        } else {
//
//                                            timingList.add(new FutureTimeChildModel(hallOpeningTime + " - " + hallClosingTime, hallOpeningTime, hallClosingTime));
//
//                                        }
//
//                                    }
//
//                                }
//
//                            }
//
//                            if(timingList.size()!=0){
//
//                                isDayMatchFound = true;
//
//                                String strSelectedDate = "N/A";
//
//                                if(x==0){
//                                    strSelectedDate = "Today";
//                                }
//
//                                if(x==1){
//                                    strSelectedDate = "Tomorrow";
//                                }
//
//                                if(x>1){
//
//                                    String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
//                                    try{
//                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
//                                        Date tempDate = sdf.parse(selectedDate);
//                                        sdf.applyPattern("dd MMM, EEE");
//                                        strSelectedDate = sdf.format(tempDate);
//                                    }catch (Exception e){}
//
//                                }
//
//                                futureTimings.add(new FutureTimeParentModel(strSelectedDate, timingList, futureTimings.size()));
//
//                            }
//
//
//                        }
//
//                        if(isDayMatchFound==false){
//
//                            timingList = new ArrayList<>();
//                            timingList.add(new FutureTimeChildModel("Hall is closed", "", ""));
//
//                            String strSelectedDate = "N/A";
//
//                            if(x==0){
//                                strSelectedDate = "Today";
//                            }
//
//                            if(x==1){
//                                strSelectedDate = "Tomorrow";
//                            }
//
//                            if(x>1){
//
//                                String selectedDate = checkForDoubleDigits(now.get(Calendar.DAY_OF_MONTH)) + "-" + checkForDoubleDigits(now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
//                                try{
//                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.ENGLISH);
//                                    Date tempDate = sdf.parse(selectedDate);
//                                    sdf.applyPattern("dd MMM, EEE");
//                                    strSelectedDate = sdf.format(tempDate);
//                                }catch (Exception e){}
//
//                            }
//
//                            futureTimings.add(new FutureTimeParentModel(strSelectedDate, timingList, futureTimings.size()));
//
//                        }
//
//                    }
//
//                }
//            }
//
//
//        }
//
//        return futureTimings;
//
//    }

    public static String checkForDoubleDigits(int value) {

        String temp = String.valueOf(value);
        if (temp.length() == 1) {
            temp = "0" + temp;
        }

        return temp;
    }

    public static String roundOffToTwoDigits(String strDigit){

        double value = 0;
        try{ value = Double.parseDouble(strDigit); } catch (Exception e){}
        return String.format("%.2f", value);

    }

    public static String getTimeStamp() {

        Long longTimeStamp = System.currentTimeMillis() / 1000;
        String timeStamp = longTimeStamp.toString();
        return timeStamp;

    }

    public static boolean isStringNull(String str) {

        if (str == null) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isStringEmpty(String str) {

        if (str.trim().length() == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isEditTextEmpty(EditText editText) {

        if (editText.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValidEmail(EditText editText) {

        String email = editText.getText().toString();

        if (isStringEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

    }

    public static boolean isTextViewEmpty(TextView textView) {

        if (textView.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    public static String getCardType(String CreditCardNumber) {

        String strCardNo = CreditCardNumber.replace("-","");

        String ptVisa = "^4[0-9]{6,}$";
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        String ptAmeExp = "^3[47][0-9]{5,}$";
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        String ptUnion = "^(62[0-9]{14,17})$";

        if (strCardNo.matches(ptVisa))
            return "Visa";
        else if (strCardNo.matches(ptMasterCard))
            return "MasterCard";
        else if (strCardNo.matches(ptAmeExp))
            return "American Express";
        else if (strCardNo.matches(ptDinClb))
            return "Diners Club";
        else if (strCardNo.matches(ptDiscover))
            return "Discover";
        else if (strCardNo.matches(ptJcb))
            return "JCB";
        else if (strCardNo.matches(ptUnion))
            return "UnionPay";
        else
            return "invalid";
    }

    public static int getCardImage(String cardBrand) {

        if (cardBrand.equalsIgnoreCase("Visa"))
            return R.drawable.ic_visa;
        else if (cardBrand.equalsIgnoreCase("MasterCard"))
            return R.drawable.ic_master;
        else if (cardBrand.equalsIgnoreCase("American Express"))
            return R.drawable.ic_amex;
        else if (cardBrand.equalsIgnoreCase("Diners Club"))
            return R.drawable.ic_dinner;
        else if (cardBrand.equalsIgnoreCase("Discover"))
            return R.drawable.ic_discover;
        else if (cardBrand.equalsIgnoreCase("JCB"))
            return R.drawable.ic_jcb;
        else if (cardBrand.equalsIgnoreCase("UnionPay"))
            return R.drawable.ic_union;
        else
            return R.drawable.ic_card_default;
    }


    public static String getMonthString(int month) {

        String monthString = "";

        switch (month) {

            case 0:
                monthString = "January";
                break;
            case 1:
                monthString = "February";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "December";
                break;

        }

        return monthString;

    }

    public static Date getTimeFrom24HoursString(String strTime) {

        Date newDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            newDate = dateFormat.parse(strTime);
        } catch (Exception e) {
        }

        return newDate;
    }

    public static Date getTimeFrom12HoursString(String strTime) {

        Date newDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        try {
            newDate = dateFormat.parse(strTime);
        } catch (Exception e) {
            Log.e("Exception::", "::" + e.toString());
        }

        return newDate;
    }

    public static String getCurrentTime() {
        String delegate = "hh:mm aa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }

    public static String getTimeFromDate(Date selectedDate) {

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String time = localDateFormat.format(selectedDate);

        return time;
    }

    public static String getTime(Date selectedDate) {

        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm aa");
        String time = localDateFormat.format(selectedDate);

        return time;
    }

    public static String change24hrsTo12HrsFormat(String selectedTime) {
        String inputPattern = "HH:mm";
        String outputPattern = "hh:mm aa";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(selectedTime);
            str = outputFormat.format(date);
        } catch (Exception e) {

        }
        return str;
    }

    public static String changeOrderDateFormat(String selectedDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMM dd, yyyy, hh:mm aa";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(selectedDate);
            str = outputFormat.format(date);
        } catch (Exception e) {

        }
        return str;
    }

    public static String changeTimeFormat(String selectedDate) {

        Log.e("selectedDate::", "::" + selectedDate);

        String inputPattern = "yyyy-mm-dd HH:mm:ss";

        String outputPattern = "yyyy-MM-dd HH:mm";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(selectedDate);

            Log.e("date}}", "date}}" + date);

            str = outputFormat.format(date);
        } catch (Exception e) {
            Log.e("Exception???", "Exception???" + e.toString());
        }
        return str;
    }

    public static boolean isAnyMealIdActive(String hallId, String[] mealIdArray, HomeDishesResponse.Halls[] halls){

        boolean isAnyMealIdActive = false;

        for(int l=0; l<mealIdArray.length; l++){
            boolean isMealClosed = isMealTimeCurrentlyClosed(hallId, mealIdArray[l], halls);
            if(isMealClosed == false){
                isAnyMealIdActive = true;
            }
        }

        return isAnyMealIdActive;

    }

    public static String convertDate(String getDate) {

        String strDt = "N/A";

        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(getDate);

            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("MMM");
            SimpleDateFormat dateFormatter2 = new SimpleDateFormat(" dd, yyyy, hh:mm a");
            dateFormatter1.setTimeZone(TimeZone.getDefault());
            dateFormatter2.setTimeZone(TimeZone.getDefault());
            String temp2 = dateFormatter2.format(value).toUpperCase();
            strDt = dateFormatter1.format(value) + temp2;

        }
        catch (Exception e) { }

        return strDt;
    }

}
