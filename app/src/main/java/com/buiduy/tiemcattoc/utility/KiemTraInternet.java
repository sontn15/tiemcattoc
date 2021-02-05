package com.buiduy.tiemcattoc.utility;
import android.content.ContentProvider;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class KiemTraInternet {
    public static boolean checkInternetConnection(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(context, "Không có mạng mặc định nào hiện đang hoạt động", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!networkInfo.isConnected()) {
            Toast.makeText(context, "Mạng không được kết nối", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!networkInfo.isAvailable()) {
            Toast.makeText(context, "Không có mạng", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(context, "Mạng ổn", Toast.LENGTH_LONG).show();
        return true;
    }

}
