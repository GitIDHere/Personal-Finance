package com.samirvora.myfinance.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by James on 30/10/2016.
 */
public class ThisApplication extends Application{

    private static ThisApplication instance;

    public ThisApplication() {
        instance = this;
    }

    /*
     * The downside is that there is no guarantee that the non-static onCreate()
     * will have been called before some static initialization code tries to
     * fetch your Context object. That means your calling code will need to be
     * ready to deal with null values which sort of defeats the whole point of this question.
     */
    public static Context getContext(){
        return instance;
    }

}
