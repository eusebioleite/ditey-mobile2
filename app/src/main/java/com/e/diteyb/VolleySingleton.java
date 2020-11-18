package com.e.diteyb;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e.diteyb.ui.login.LoginViewModel;
import com.e.diteyb.ui.main.MainViewModel;
import com.e.diteyb.ui.register.RegisterViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class VolleySingleton extends Application {
    private RequestQueue mRequestQueue;
    public static boolean LOGADO = false;
    public static String USER_NAME="Example", USER_EMAIL="example@example.com",PP_FIRST_LETTER="E";
    public static String BEARER_KEY;
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
                NavController navController =
                        Navigation.findNavController(MainActivity.MAIN_ACTIVITY_INSTANCE, R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_register_to_nav_login);
                try {
                    BEARER_KEY = response.getString("accessToken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RegisterViewModel registerViewModel =
                        new ViewModelProvider(MainActivity.MAIN_ACTIVITY_INSTANCE).get(RegisterViewModel.class);
                registerViewModel.isWrong(true);
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
                params.put("Authorization", "Bearer "+ BEARER_KEY);
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
                MainViewModel mainViewModel =
                        new ViewModelProvider(MainActivity.MAIN_ACTIVITY_INSTANCE).get(MainViewModel.class);
                try {
                    mainViewModel.setEdtTitle(response.getJSONObject(0).getString("title"));
                    mainViewModel.setEdtContentText(response.getJSONObject(0).getString("content"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ BEARER_KEY);
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
            params.put("Authorization", "Bearer "+ BEARER_KEY);
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
                    Log.d("response: ", response.toString());
                    LOGADO = true;
                    USER_NAME = response.getString("name");
                    USER_EMAIL = response.getString("email");
                    PP_FIRST_LETTER = String.valueOf(USER_NAME.charAt(0));
                    BEARER_KEY = response.getString("accessToken");
                    NavController navController =
                            Navigation.findNavController(MainActivity.MAIN_ACTIVITY_INSTANCE, R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LoginViewModel loginViewModel =
                        new ViewModelProvider(MainActivity.MAIN_ACTIVITY_INSTANCE).get(LoginViewModel.class);
                loginViewModel.isWrong(true);
                error.printStackTrace();
                Log.d("error ", error.getStackTrace().toString());
            }
        });
        addToRequestQueue(loginRequest);
    }

}
