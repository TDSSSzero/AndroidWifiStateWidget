package demo.tdsss.wifistate

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val TAG = "wifiState"

    var handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    val linkProperties = msg.obj as LinkProperties
                    findViewById<TextView>(R.id.ip).text = "${linkProperties.linkAddresses[1]}"
                }
                1->{
                    findViewById<TextView>(R.id.ip).text = "无连接"
                }
            }
        }
    }

    private val networkCallback = object : NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.w(TAG, "onAvailable: " )
        }

        override fun onLinkPropertiesChanged(
            network: Network,
            linkProperties: LinkProperties
        ) {
            super.onLinkPropertiesChanged(network, linkProperties)
            Log.w(TAG, "onLinkPropertiesChanged: ${linkProperties.linkAddresses}" )
            handler.sendMessage(Message.obtain(handler,0,linkProperties))
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.w(TAG, "onUnavailable: " )
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            handler.sendMessage(Message.obtain(handler,1))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        registerWifiState()

    }

    override fun onStop() {
        super.onStop()
        unregisterWifiState()
    }


    private fun registerWifiState(){
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun unregisterWifiState(){
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}