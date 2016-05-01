package com.powerble;

import java.util.ArrayList;

import com.powerble.CommonUtils.BLELogger;

import android.R.array;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.widget.ListView;
public class Util {

    public static String VERSION_NAME="";
    
    
    public static void e(String tag,String content){
    	Log.e("pang",tag+"------"+content);
    }
}
