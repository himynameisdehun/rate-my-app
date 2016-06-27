# rate-my-app
A helper to increment store's ratings by displaying a rate dialog at the right time.

I decided to create this little cute library because getting reviews and feedback from app's users is very important for both development and marketing, but it's also quiet difficult to obtain spontaneously.

This is a helper that will encourage app's users to rate the app by displaying a dialog when the probabilities to get a positive feedback are higher.

# How it works
Record the number of times the app is launched and the number of time the rate-dialog is shown, through shared preferences.
If conditions are met, then it's shown a dialog that allows the user to rate the app on the store or do it later.

It maximize the % of users that will effectively give a positive review of the app by displaying the rate-dialog at the right time and only if the user is enough satisfied by the app.
This is done through checks on number of times the app is launched and days after the app has been installed or the last time the rate-dialog has been shown.

# Usage
It's very simple, just initialize rateHelper in the onCreate of your app's launcher activity and call shouldShowRateDialog().

```Java
// Initialize rateHelper
RateHelper rateHelper = new RateHelper(context);

// Customize its behaviour & appearance, or if you want to use default values skip this line
rateHelper.setLaunchToAllowShow(launchToAllowShow);
...

// Call this to check if it's time to display the rate-dialog and do it if so
rateHelper.shouldShowRateDialog();
```

# API Reference
```Java
// Customize methods
public void setLaunchToAllowShow(int launchToAllowShow) 

public void setMaxShow(int maxShow)                     

public void setDaysInterval(int daysInterval)           

public void setDialogTitle(String dialogTitle)        

public void setDialogContent(String dialogContent)

public void setTintColor(int tintColor)

public void setStoreLink(String storeLink)

// Directly show rate-dialog
public void showRateDialog()

// Check if it's time and show rate-dialog
public void shouldShowRateDialog()
```

# Installation
To use it you just have to add this in your build.gradle dependencies:

```compile 'com.dehun.library:ratemyapp:1.0.1'```

Remember to use jCenter as repository.

# License

    Copyright 2016 Michael Fabozzi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
