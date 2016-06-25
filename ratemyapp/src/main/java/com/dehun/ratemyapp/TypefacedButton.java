package com.dehun.ratemyapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

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
 * Custom button able to set to himself a custom font directly in xml
 * thanks to a custom attribute TypefacedView.
 */
public class TypefacedButton extends Button {

    public TypefacedButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedView);
        String fontName = styledAttrs.getString(R.styleable.TypefacedView_typeface);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
            setTypeface(typeface);
        }
    }

}
