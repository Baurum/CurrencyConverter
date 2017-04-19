package bau.com.converter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public double usd;
    public double pln;
    public double gbp;
    public double userValue;
    public String userMoney;
    public EditText etUserMoney;
    public TextView tvUsd;
    public TextView tvPln;
    public TextView tvGbp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApp();
    }
    /***********************************************************************************************
     * Method to start.
     **********************************************************************************************/
    private void initApp(){
        etUserMoney = (EditText) findViewById(R.id.et_user_money);
        tvUsd = (TextView) findViewById(R.id.tv_usd);
        tvPln = (TextView) findViewById(R.id.tv_pln);
        tvGbp = (TextView) findViewById(R.id.tv_gbp);

    }

    public void getData(View view){
        userValue = Double.parseDouble(etUserMoney.getText().toString());
        new DownloadCurrencyTask().execute();

    }
    private class DownloadCurrencyTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... params) {
            String result = "";
            InputStream inputStream = null;

            URL url = null;
            try {
                url = new URL("https://api.fixer.io/latest");
                // create HttpURLConnection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // make GET request to the given URL
                conn.connect();

                // receive response as inputStream
                inputStream = conn.getInputStream();

                // convert inputstream to string
                if(inputStream != null){
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
                    String line = "";
                    while((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    inputStream.close();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }

        protected void onProgressUpdate(Integer... progress) {
//            etProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {

            Log.d(TAG, result);
            try{
                JSONObject data = new JSONObject(result);
                JSONObject rates = data.getJSONObject("rates");
                usd = rates.getDouble("USD");
                pln = rates.getDouble("PLN");
                gbp = rates.getDouble("GBP");
                Log.d(TAG, String.valueOf(usd));


////                String base = data.getString("base");
//                JSONObject value = new JSONObject(result);
//                String country = data.getString("rates");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            multiplyCurrency();

        }
    }

    private void multiplyCurrency(){

        tvUsd.setText("USD: " + String.valueOf(userValue * usd));

        tvPln.setText("PLN: " + String.valueOf(userValue * pln));

        tvGbp.setText("GBP: " + String.valueOf(userValue * gbp));




    }

}
