package bau.com.converter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/***************************************************************************************************
 * SplashActivity
 **************************************************************************************************/
public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        splashTime();

    }

    /***********************************************************************************************
     * Method to init the second activity with a delay
     **********************************************************************************************/
    private void splashTime() {
        // Move on to the next activity in SPLASH_TIME_OUT seconds
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(mContext, MainActivity.class);
                startActivity(i);


            }
        }, 2000);
    }
}
