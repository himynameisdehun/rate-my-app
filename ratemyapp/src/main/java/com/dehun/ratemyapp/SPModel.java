package com.dehun.ratemyapp;

import android.content.Context;
import android.content.SharedPreferences;

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
 * Model of Shared Preferences used to store needed values.
 */
public class SPModel {

    /**
     * Constants
     */

    protected static final String NAME_SUFFIX = "RateMyApp";

    /**
     * Variables Identifiers
     */


    protected static final String LAST_SHOW_TIMESTAMP = "LastShowTimestamp";
    protected static final String SHOW_COUNTER = "ShowCounter";
    protected static final String LAUNCH_COUNTER = "LaunchCounter";

    /**
     * Return SP name from concatenation between package name and the suffix in order
     * to have a unique identifier to be used to identify our SP
     *
     * @param context Used to get package name
     */

    protected static String getName(Context context) {
        return context.getPackageName().concat(NAME_SUFFIX);
    }

    /**
     * Return if SP has been initialized by checking if LAST_SHOW_TIMESTAMP is not null
     *
     * @param sp Shared Preferences used to check if variables has been initialized
     */

    protected static boolean isInit(SharedPreferences sp) {
        return sp.getLong(LAST_SHOW_TIMESTAMP, -1) != -1;
    }

    /**
     * Initialize sp by setting default current values to them
     *
     * @param sp Shared Preferences used to be initialized
     */

    protected static void init(SharedPreferences sp) {

        // Get editor
        SharedPreferences.Editor editor = sp.edit();

        // Put current timestamp as last show time to init it for the first time
        editor.putLong(LAST_SHOW_TIMESTAMP, System.currentTimeMillis());

        // Init show counter to 0 because rate-dialog hasn't been showed yet
        editor.putInt(SHOW_COUNTER, 0);

        // Init launch counter to 0
        editor.putInt(LAUNCH_COUNTER, 0);

        // Apply through editor values to sp
        editor.apply();
    }

    /**
     * Clear this shared preferences in order to set them back to be null
     *
     * @param context
     */

    public static void clear(Context context) {

        // Get shared preferences
        SharedPreferences sp = context.getSharedPreferences(SPModel.getName(context), Context.MODE_PRIVATE);

        // Get editor
        SharedPreferences.Editor editor = sp.edit();

        // Clear all of my values
        editor.clear();

        // Apply changes
        editor.apply();
    }

}
