package co.edu.unal.webservices;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServiceConnection {
    private static final int CONNECTION_TIMEOUT=300;
    private static final int DATARETRIEVAL_TIMEOUT=500;
    private static final String baseurl="https://www.datos.gov.co/resource/9zqj-pbk5.json?ciudad=Bogotá";

    public  ServiceConnection(){}

    public static JSONArray requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();
        Log.i("Reto","Iniciando.. " + serviceUrl);

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.i("Reto","Error "+ statusCode);
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                Log.i("Reto","Error "+ statusCode);
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream( urlConnection.getInputStream());
            return new JSONArray(getResponseText(in));

        } catch (MalformedURLException e) {
            Log.i("Reto","Excepcion "+ e.getMessage());
            // URL is invalid
        } catch (SocketTimeoutException e) {
            Log.i("Reto","Excepcion "+ e.getMessage());
            // data retrieval or connection timed out
        } catch (IOException e) {
            Log.i("Reto","Excepcion "+ e.getMessage());
            // could not read response body
            // (could not create input stream)
        } catch (JSONException e) {
            Log.i("Reto","Excepcion "+ e.getMessage());
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    public List<String> findListNames(String param) throws JSONException {
        String url=baseurl+"&$select="+param+"&$group="+param;
        Log.i("Reto",url);
        JSONArray items = requestWebService(url);
        List<String> foundItems = new ArrayList<String>(5);
        Log.i("Reto",param+": "+items.getJSONObject(0).getString(param));
        try {
            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                foundItems.add(obj.getString(param));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // handle exception
        }
        return foundItems;
    }
    private String inString(List<String> params){
        String result="in('";
        for(String p: params)
            result+=(p+"','");
        result =result.substring(0,result.length()-2);
        result+=")";
        Log.i("Reto",result);
        return result;
    }

    public List<Item> filterItems(String a_o, String ordenar, List<String> ops, List<String> kpis) throws JSONException {
        String filtop=inString(ops);
        JSONArray serviceResult = requestWebService(baseurl+"&a_o="+a_o+"&$order="+ordenar+
                                "&$where=operador "+filtop);
        List<Item> foundItems = new ArrayList<Item>(20);
        Log.i("Reto","filtro");
        try {
            JSONArray items = serviceResult;//.getJSONArray("items")
            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);

                if(obj.getString("kpi").contains("destino")) continue;
                foundItems.add(
                        new Item(
                                obj.getString("operador"),
                                obj.getDouble("valor"),
                                obj.getString("kpi"),
                                obj.getString("unidad")));

            }
        } catch (JSONException e) {
            e.printStackTrace();
            // handle exception
        }
        return foundItems;
    }
}
