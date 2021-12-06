package com.reddevx.thenewquotes.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import com.reddevx.thenewquotes.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        if (isNetworkAvailable(this)){
            Handler(Looper.myLooper()!!).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)
//        }else{
//            showDialog()
//        }

    }


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw =   connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            TODO("VERSION.SDK_INT < M")
        }

    }

    private fun showDialog(){
        val mDialog: AlertDialog

        val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
            when(which){
                    DialogInterface.BUTTON_NEGATIVE -> finishAffinity()
            }
        }
        val builder = AlertDialog.Builder(this)
            .setTitle("No internet!")
            .setMessage("Make sure you are connected to the network")
            .apply {
                setNegativeButton("CLOSE",dialogInterface)
            }
        mDialog = builder.create()
        mDialog.show()

    }
}




