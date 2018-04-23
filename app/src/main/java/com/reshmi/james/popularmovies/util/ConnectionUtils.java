package com.reshmi.james.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.reshmi.james.popularmovies.R;

public final class ConnectionUtils {

    private static Toast mToast;

    private ConnectionUtils(){}

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isOnline(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm!=null && cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void onConnectionError(Context context){
        if(mToast != null){ mToast.cancel();}
        mToast =  Toast.makeText( context, R.string.connection_error, Toast.LENGTH_LONG);
        mToast.show();
    }

}
