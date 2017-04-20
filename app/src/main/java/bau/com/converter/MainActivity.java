package bau.com.converter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    private final int USD = 0;
    private final int PLN = 1;
    private final int GBP = 2;
    private final int EUR = 3;


    public double usd;
    public double pln;
    public double gbp;
    public double eur;
    public double userValue;
    public String userMoney;
    public EditText etUserMoney;
    public TextView tvUsd;
    public TextView tvPln;
    public TextView tvGbp;
    public TextView tvEur;
    Spinner spCurrencyUser;
    public Context mContext;
    public int selectedCurrency;





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
        mContext = this;
        etUserMoney = (EditText) findViewById(R.id.et_user_money);
        tvUsd = (TextView) findViewById(R.id.tv_usd);
        tvPln = (TextView) findViewById(R.id.tv_pln);
        tvGbp = (TextView) findViewById(R.id.tv_gbp);
        tvEur = (TextView) findViewById(R.id.tv_eur);
        this.spCurrencyUser = (Spinner)    findViewById(R.id.spinner_currency);

        loadUserCurrency();


    }

    /***********************************************************************************************
     * Method array adapter
     **********************************************************************************************/
    private void loadUserCurrency(){

        final ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource
                        (this, R.array.user_currency, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spCurrencyUser.setAdapter(adapter);
        this.spCurrencyUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()){
                    case R.id.spinner_currency:
                        String[]  arrayUserCurrency = getResources().getStringArray(R.array.user_currency);
                        ArrayAdapter<CharSequence> adapter =
                                new ArrayAdapter<CharSequence>
                                        (mContext, android.R.layout.simple_spinner_item,
                                                android.R.id.text1, arrayUserCurrency);
                        adapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);
                        InputMethodManager imm =
                                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(spCurrencyUser.getWindowToken(), 0);

                        //store selected currency info
                        selectedCurrency = position;

                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *
     * @param view
     */
    public void getData(View view){

        if(etUserMoney.getText().toString().equals("")){
            etUserMoney.setError("topo mete un numero");
        }else {
            userValue = Double.parseDouble(etUserMoney.getText().toString());
            new DownloadCurrencyTask().execute();
            refreshCurrencyFields();
        }
    }

    /**
     * method
     */
    public void refreshCurrencyFields(){
        tvEur.setVisibility(View.VISIBLE);
        tvUsd.setVisibility(View.VISIBLE);
        tvPln.setVisibility(View.VISIBLE);
        tvGbp.setVisibility(View.VISIBLE);
        switch (selectedCurrency){
            case EUR:
                tvEur.setVisibility(View.GONE);
                break;

            case USD:
                tvUsd.setVisibility(View.GONE);
                break;

            case GBP:
                tvGbp.setVisibility(View.GONE);
                break;

            case PLN:
                tvPln.setVisibility(View.GONE);
                break;

            default:
                break;

        }

    }


    /**
     *
     */
    private class DownloadCurrencyTask extends AsyncTask<Void, Void, String> {
        private String getUrl(){
            String result = "https://api.fixer.io/latest?";
            switch (selectedCurrency){
                case EUR:
                    result += "symbols=USD,PLN,GBP&base=EUR";
                    break;
                case USD:
                    result += "symbols=EUR,PLN,GBP&base=USD";
                    break;
                case PLN:
                    result += "symbols=USD,EUR,GBP&base=PLN";
                    break;
                case GBP:
                    result += "symbols=USD,PLN,EUR&base=GBP";
            }
            return result;
        }


        protected String doInBackground(Void... params) {
            String result = "";
            InputStream inputStream = null;
            URL url = null;
            try {
                url = new URL(getUrl());
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

        /**
         *
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress) {
//            etProgressPercent(progress[0]);
        }

        /**
         *
         * @param result
         */
        protected void onPostExecute(String result) {

            Log.d(TAG, result);
            try{
                JSONObject data = new JSONObject(result);
                JSONObject rates = data.getJSONObject("rates");
                if(rates.has("USD")) usd = rates.getDouble("USD");
                if(rates.has("PLN"))   pln = rates.getDouble("PLN");
                if(rates.has("GBP")) gbp = rates.getDouble("GBP");
                if(rates.has("EUR"))  eur = rates.getDouble("EUR");

//                Log.d(TAG, String.valueOf(usd));


////                String base = data.getString("base");
//                JSONObject value = new JSONObject(result);
//                String country = data.getString("rates");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            multiplyCurrency();

        }
    }

    /**
     *
     */
    private void multiplyCurrency(){

        tvEur.setText("EUR " + String.valueOf(userValue * eur));

        tvUsd.setText("USD: " + String.valueOf(userValue * usd));

        tvPln.setText("PLN: " + String.valueOf(userValue * pln));

        tvGbp.setText("GBP: " + String.valueOf(userValue * gbp));




    }

}
