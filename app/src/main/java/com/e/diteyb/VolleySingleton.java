package com.e.diteyb;
import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class VolleySingleton extends Application {
    private RequestQueue mRequestQueue;
    public static boolean LOGADO = false;
    private String key;
    private final String
            urlModifyText="https://ditey-api-deploy.herokuapp.com/api/texts/",
            urlCreateText="https://ditey-api-deploy.herokuapp.com/api/texts",
            urlRegister = "https://ditey-api-deploy.herokuapp.com/api/auth/register",
            urlLogin="https://ditey-api-deploy.herokuapp.com/api/auth/login",
            urlListText="https://ditey-api-deploy.herokuapp.com/api/texts";
    private static VolleySingleton sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
    public static synchronized VolleySingleton getInstance() {
        return sInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void requestRegister(String username, String email, String password){
        JSONObject object = new JSONObject();
        try {
            object.put("name", username);
            object.put("email", email);
            object.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, urlRegister, object , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response ", response.toString());
                try {
                    key = response.getString("accessToken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        addToRequestQueue(registerRequest);
        JsonObjectRequest createText = new JsonObjectRequest(Request.Method.POST,urlCreateText,new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response ", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ key);
                return params;
            }
        };
        for (int i = 0; i < 6; i++) addToRequestQueue(createText);
    }

    public void requestGetTexts(){
        JsonArrayRequest getTextRequest = new JsonArrayRequest(Request.Method.GET, urlListText,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    Log.d("response ", response.toString());
                    MainActivity.txtboxTexts = response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ key);
                return params;
            }
        };
        addToRequestQueue(getTextRequest);
    }

    public void requestModifyText(int id, String title, String content){
    String url = urlModifyText + id;
    JSONObject object = new JSONObject();
        try {
            object.put("title", title);
            object.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    JsonObjectRequest modifyRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("response ", response.toString());
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }){
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Authorization", "Bearer "+ key);
            return params;
        }
    };
    addToRequestQueue(modifyRequest);
    }

    public void requestLogin(String email, String password){
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("password",password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, urlLogin, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response: ",response.toString());
                    LOGADO = true;
                    key = response.getString("accessToken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        addToRequestQueue(loginRequest);
    }

}
