package demo.tdsss.wifistate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * @author TDSSS
 * @datetime 2023/11/22 17:03
 */
public class IpUtils {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    static void getIp(Context context){
        final NetworkRequest request =
                new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .build();
        final ConnectivityManager connectivityManager =
                context.getSystemService(ConnectivityManager.class);
        final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
            }

            @Override
            public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
            }
        };
        connectivityManager.requestNetwork(request, networkCallback); // For request
        connectivityManager.registerNetworkCallback(request, networkCallback); // For listen

    }
}
