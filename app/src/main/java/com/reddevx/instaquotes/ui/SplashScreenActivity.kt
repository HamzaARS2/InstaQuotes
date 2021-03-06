package com.reddevx.instaquotes.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.reddevx.instaquotes.R


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        checkNightMode()
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)


    }

    private fun checkNightMode() {
        val sp: SharedPreferences =
            getSharedPreferences(SettingsActivity.NIGHT_MODE_STATE, MODE_PRIVATE)
        SettingsActivity.NIGHT_MODE = sp.getBoolean(SettingsActivity.NIGHT_MODE_STATE, false)
        if (SettingsActivity.NIGHT_MODE){AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}

    }


//    private fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val nw =   connectivityManager.activeNetwork ?: return false
//            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
//            return when {
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                //for other device how are able to connect with Ethernet
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                //for check internet over Bluetooth
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
//                else -> false
//            }
//        } else {
//
//        }
//
//    }
//
//    private fun showDialog(){
//        val mDialog: AlertDialog
//
//        val dialogInterface = DialogInterface.OnClickListener { dialog, which ->
//            when(which){
//                    DialogInterface.BUTTON_NEGATIVE -> finishAffinity()
//            }
//        }
//        val builder = AlertDialog.Builder(this)
//            .setTitle("No internet!")
//            .setMessage("Make sure you are connected to the network")
//            .apply {
//                setNegativeButton("CLOSE",dialogInterface)
//            }
//        mDialog = builder.create()
//        mDialog.show()
//
//    }

}




