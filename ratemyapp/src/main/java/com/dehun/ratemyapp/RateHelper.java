package com.dehun.ratemyapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 *
 * Copyright 2016 Michael Fabozzi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Allows to keep record of the number of times the app is launched and the number of time the
 * rate-dialog is shown, through shared preferences.
 *
 * When created an instance of the object, it's filled with default values, but through setter
 * methods it can be customized both in behaviour and appearance.
 *
 * The usage is very simple, is only needed to create an instance of rateHelper and call the
 * method: shouldShowRateDialog.
 *
 * If conditions are met, then it's shown a dialog that allows the user to rate the app on the
 * store or do it later.
 *
 * The very cool thing about how this works is that it maximize the % of user that will effectively
 * give a positive review of the app, by displaying the rate-dialog at the right time and
 * only if the user is enough satisfied by the app.
 * This is done through checks on number of times the app is launched and days after the app
 * has been installed or the last time the rate-dialog has been shown.
 */
public class RateHelper {

    /**
     * Default properties values
     */

    private static final int DEFAULT_LAUNCH_TO_ALLOW_SHOW = 8;
    private static final int DEFAULT_MAX_SHOW = 5;
    private static final int DEFAULT_DAYS_INTERVAL = 3;

    /**
     * Properties
     */

    // The context
    private Context mContext;

    // To store values, referencing to SPModel class
    private SharedPreferences sp;

    // To calculate when to re-display the rate-dialog
    private long lastShowTimestamp;

    // To don't be silly and don't display rate-dialog when reached the maximum times allowed
    private int showCounter;

    // To count the number of times the app has been launched
    private int launchCounter;

    // Number of launches after allow rate-dialog showings
    private int launchToAllowShow;

    // Maximum times a rate-dialog can be displayed
    private int maxShow;

    // The number of days after the dialog should appear
    private int daysInterval;

    // The title of the dialog
    private String dialogTitle;

    // The content of the dialog
    private String dialogContent;

    // Tint color of title and buttons
    private int tintColor;

    // String containing the link to follow to vote the app
    private String storeLink;

    /**
     * Constructor
     *
     * @param context
     */

    public RateHelper(Context context){

        // Set context
        mContext = context;

        // Initialize variables
        init();
    }

    /**
     * Initialize RateHelper properties in order to be ready
     * to check if showing or not the rate-dialog
     */

    private void init() {

        // Get shared preferences in order to get stored values or check if already initialized
        sp = mContext.getSharedPreferences(SPModel.getName(mContext), Context.MODE_PRIVATE);

        // If sp hasn't been initialized, do it with current values
        if(!SPModel.isInit(sp)){

            // Initialize shared preferences
            SPModel.init(sp);
        }

        // Fill variables that need sp
        lastShowTimestamp = sp.getLong(SPModel.LAST_SHOW_TIMESTAMP, -1);
        showCounter = sp.getInt(SPModel.SHOW_COUNTER, -1);
        launchCounter = sp.getInt(SPModel.LAUNCH_COUNTER, -1);

        // Now fill other variables with default values
        launchToAllowShow = DEFAULT_LAUNCH_TO_ALLOW_SHOW;
        maxShow = DEFAULT_MAX_SHOW;
        daysInterval = DEFAULT_DAYS_INTERVAL;
        dialogTitle = mContext.getResources().getString(R.string.dialog_title);
        dialogContent = mContext.getResources().getString(R.string.dialog_content);
        tintColor = mContext.getResources().getColor(R.color.default_tint);
        storeLink = mContext.getResources().getString(R.string.app_link_prefix).concat(mContext.getPackageName());
    }

    /**
     * Setters methods
     */

    public void setLaunchToAllowShow(int launchToAllowShow) {
        this.launchToAllowShow = launchToAllowShow;
    }

    public void setMaxShow(int maxShow) {
        this.maxShow = maxShow;
    }

    public void setDaysInterval(int daysInterval) {
        this.daysInterval = daysInterval;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public void setDialogContent(String dialogContent) {
        this.dialogContent = dialogContent;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public void setStoreLink(String storeLink) {
        this.storeLink = storeLink;
    }

    /**
     * Increment launch counter and save changes into shared preferences
     */

    private void incrementLaunchCounter() {

        // Increment variables values
        launchCounter++;

            // Apply changes also to sharedPreferences to save it
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(SPModel.LAUNCH_COUNTER, launchCounter);
            editor.apply();
    }

    /**
     * Increment show counter, and last show timestamp and save changes into shared preferences
     */

    private void updateShowData() {

        // Increment show counter
        showCounter++;
        // Set as last show timestamp, current one
        lastShowTimestamp = System.currentTimeMillis();

        // Apply changes also to sharedPreferences to save it
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(SPModel.LAST_SHOW_TIMESTAMP, lastShowTimestamp);
        editor.putInt(SPModel.SHOW_COUNTER, showCounter);
        editor.apply();
    }

    /**
     * Set the value of show counter equal to max show allowed in order to don't display
     * ever again the rate-dialog.
     */

    private void noMoreShow(){

        // Set show counter equal to max show allowed plus 1
        showCounter = maxShow + 1;

        // Apply changes also to sharedPreferences to save it
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SPModel.SHOW_COUNTER, showCounter);
        editor.apply();
    }

    /**
     * Return a boolean indicating if launches are enough
     */

    private boolean isLaunchEnoughToShow(){
        return launchCounter >= launchToAllowShow;
    }

    /**
     * Return a boolean indicating if is passed enough time from last show
     */

    private boolean isTimeIntervalEnoughToShow() {
        return System.currentTimeMillis() >= lastShowTimestamp
                + TimeUnit.MILLISECONDS.convert(daysInterval, TimeUnit.DAYS);
    }

    /**
     * Return a boolean indicating if the number of showing has not exceed the max times allowed,
     * in order to don't be silly with user and avoid negative feedbacks
     */

    private boolean isShowBecameBoring() {
        return showCounter > maxShow;
    }

    /**
     * Create a custom dialog personalized the way we want and show it, adding onclick listener
     * to its buttons.
     */

    public void showRateDialog(){

        // Create the dialog
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set as content to dialog a custom layout
        dialog.setContentView(R.layout.dialog_rate);

        // Set the background color of window to be transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Prevent user dismissing dialog by back-pressing
        dialog.setCancelable(false);

        // Set that dialog can't be dismissed by touching outside it
        dialog.setCanceledOnTouchOutside(false);

        // Find title textview and content textview
        TextView titleTextView = (TextView) dialog.findViewById(R.id.textview_title);
        TextView contentTextView = (TextView) dialog.findViewById(R.id.textview_content);

        // Find buttons from dialog
        Button rateButton = (Button) dialog.findViewById(R.id.button_rate);
        Button laterButton = (Button) dialog.findViewById(R.id.button_later);

        // Set text to title and content textview
        titleTextView.setText(dialogTitle);
        contentTextView.setText(dialogContent);

        // Apply tint
        titleTextView.setTextColor(tintColor);
        rateButton.setTextColor(tintColor);
        laterButton.setTextColor(tintColor);

        // Set rate button onclick listener
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent to go to storeLink
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(storeLink));

                // Start it
                mContext.startActivity(i);

                // I have voted, so don't ever show rate-dialog
                noMoreShow();

                // Dismiss dialog
                dialog.dismiss();
            }
        });

        // Set later button onclick listener
        laterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Just dismiss dialog, because i've already incremented show value
                dialog.dismiss();
            }
        });

        // Rate-dialog has been fully customized, so show it
        dialog.show();
    }

    /**
     * To be called every time after creating an instance of RateHelper
     * It checks if rate-dialog has to be displayed or not, and show it if necessary incrementing
     * show counter and last show timestamp.
     */

    public void shouldShowRateDialog() {

        // Increment launch counter to count the number of tentatives to display rate-dialog
        incrementLaunchCounter();

        // If i've launched the app more times than required, the days interval from last
        // show is passed, and i don't have show the dialog too much times, then show rate-dialog
        if(isLaunchEnoughToShow() && isTimeIntervalEnoughToShow() && !isShowBecameBoring()) {

            // Show rate-dialog
            showRateDialog();

            // Increment show counter because dialog has been shown and update last show timestamp
            updateShowData();
        }
    }

}
