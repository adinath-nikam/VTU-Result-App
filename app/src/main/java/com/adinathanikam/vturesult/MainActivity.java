package com.adinathanikam.vturesult;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;




public class MainActivity extends AppCompatActivity {

    public WebView webView;
    public EditText ET_USN, ET_CAP;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView temp = findViewById(R.id.helloID);
        webView = findViewById(R.id.webview_id);
        ET_USN = findViewById(R.id.ET_USN_ID);
        ET_CAP = findViewById(R.id.ET_CAPTCHA_ID);





//------------------------------------------------------------------------------------------

        findViewById(R.id.BT_SUB_ID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUSN = ET_USN.getText().toString();
                String strCap = ET_CAP.getText().toString();

                System.out.println(">>>>> "+strUSN);
                System.out.println(">>>>> "+strCap);

                // Request
                HttpsTrustManager.allowAllSSL();
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    String URL = "https://results.vtu.ac.in/FMEcbcs22/resultpage.php";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("lns", strUSN);
                    jsonBody.put("captchacode", strCap);
                    final String requestBody = jsonBody.toString();

                    System.out.println(">> "+requestBody);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                              temp.setText(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                            temp.setText(error.toString());
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        // can get more details such as response.headers
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
                    };

                    requestQueue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Request

            }
        });



        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setDomStorageEnabled(true);




        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("Errrrrrr", "onReceivedSslError: "+error);
            }

            @Override
            public void onPageFinished(WebView webView, String url)
            {
                super.onPageFinished(webView, url);
                webView.evaluateJavascript(
                        "document.getElementsByTagName('Div')[0].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[4].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[11].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[13].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[14].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[16].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[19].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[20].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[21].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[22].style.display = 'none';"+
                        "document.getElementsByTagName('Div')[23].style.display = 'none';"

                        ,
                        new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.d("Log: ", s);
                    }
                });





            }


        });

        webView.loadUrl("https://results.vtu.ac.in/FMEcbcs22/index.php");




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


//        ------------------------------------------------------------------


    }
}

