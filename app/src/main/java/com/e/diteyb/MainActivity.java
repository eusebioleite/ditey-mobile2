package com.e.diteyb;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.e.diteyb.ui.main.MainViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static MainActivity MAIN_ACTIVITY_INSTANCE;
    public static Context MAIN_ACTIVITY_CONTEXT;
    private int STORAGE_PERMISSION_CODE = 1;
    public static String txtboxTexts="[{\"id\":0,\"title\":\"error\",\"content\":\"error\"}]";
    public static String PATH;
    public static int TEXT_SELECTED_ID;
    public static String TITLETEXT, BOXTEXT;
    boolean ttsStop, buttonChecked,isCollapsed;
    TextToSpeech mTTS;
    private MainViewModel mainViewModel;
    boolean firstTime = true;
    boolean tb1selected,tb2selected,tb3selected,tb4selected,tb5selected;
    ImageButton btnPlay, btnStop, btnSettings,toggleButton;
    ImageView drawerLoginIcon;
    Button tb1,tb2,tb3,tb4,tb5,drawerLogin;
    TextView title1,title2,title3,title4,title5, containerTitle;
    TextView ppletter, drawerEmail, drawername;
    LinearLayout fulltb1,fulltb2,fulltb3,fulltb4,fulltb5,textboxContainer;
    ConstraintLayout ppicon;
    public DrawerLayout drawer;
    final ConfigFileHelper configFileHelper = new ConfigFileHelper();
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MAIN_ACTIVITY_INSTANCE = this;
        MAIN_ACTIVITY_CONTEXT = this.getApplicationContext();

        mainViewModel = new ViewModelProvider(MainActivity.MAIN_ACTIVITY_INSTANCE).get(MainViewModel.class);
        ppicon = findViewById(R.id.pp_round_container);
        ppletter = findViewById(R.id.pp_first_letter);
        drawerEmail = findViewById(R.id.drawer_email);
        drawername = findViewById(R.id.drawer_username);

        containerTitle = findViewById(R.id.textbox_container_title);
        textboxContainer = findViewById(R.id.textbox_container);

        fulltb1 = findViewById(R.id.textbox_full1);
        fulltb2 = findViewById(R.id.textbox_full2);
        fulltb3 = findViewById(R.id.textbox_full3);
        fulltb4 = findViewById(R.id.textbox_full4);
        fulltb5 = findViewById(R.id.textbox_full5);

        tb1 = findViewById(R.id.textbox1);
        tb2 = findViewById(R.id.textbox2);
        tb3 = findViewById(R.id.textbox3);
        tb4 = findViewById(R.id.textbox4);
        tb5 = findViewById(R.id.textbox5);

        title1 = findViewById(R.id.textbox_title1);
        title2 = findViewById(R.id.textbox_title2);
        title3 = findViewById(R.id.textbox_title3);
        title4 = findViewById(R.id.textbox_title4);
        title5 = findViewById(R.id.textbox_title5);

        drawerLogin = findViewById(R.id.drawer_login);
        drawerLoginIcon = findViewById(R.id.buttonIcon);

        toggleButton = findViewById(R.id.toggleButton);
        btnPlay = findViewById(R.id.toolbar_play_button);
        btnSettings = findViewById(R.id.toolbar_config_button);
        btnStop = findViewById(R.id.toolbar_stop_button);

        Toolbar toolbar = findViewById(R.id.toolbar_bro);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_main, R.id.nav_settings, R.id.nav_login, R.id.nav_register)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        PATH = getObbDir().toString();
        try {
            configFileHelper.createConfigFile(PATH);
            configFileHelper.readConfigFile(PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (firstTime) {
            try {
                TEXT_SELECTED_ID = getId(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            btnPlay.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);
            btnSettings.setVisibility(View.GONE);

            tb1selected = true;
            containerTitle.setBackgroundResource(R.drawable.shape_textbox_selected);
            fulltb1.setBackgroundResource(R.drawable.shape_textbox_selected);
            title1.setTextColor(Color.parseColor("#6A42F4"));
            tb1.setTextColor(Color.parseColor("#6A42F4"));
            tb1.setHintTextColor(Color.parseColor("#6A42F4"));
            firstTime = false;
        }

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Sua lingua não oferece suporte");
                    } else {
                        btnPlay.setEnabled(true);
                        btnStop.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Falha na Inicialização");
                }
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (!VolleySingleton.LOGADO) {
                    drawer.close();
                    Snackbar snackbar = Snackbar
                            .make(drawer, "você não fez login!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                if (VolleySingleton.LOGADO) {
                    try {
                        drawername.setText(VolleySingleton.USER_NAME);
                        drawerEmail.setText(VolleySingleton.USER_EMAIL);
                        ppletter.setText(VolleySingleton.PP_FIRST_LETTER);
                        /*
                        navname.setText(VolleySingleton.USER_NAME);
                        navemail.setText(VolleySingleton.USER_EMAIL);
                        ppletter.setText(VolleySingleton.PP_FIRST_LETTER);*/
                        final String t1 = getTitles(0), t2 = getTitles(1), t3 = getTitles(2),
                                t4 = getTitles(3), t5 = getTitles(4);
                        final String b1 = getTexts(0), b2 = getTexts(1), b3 = getTexts(2),
                                b4 = getTexts(3), b5 = getTexts(4);

                        title1.setText(t1);
                        tb1.setText(b1);
                        title2.setText(t2);
                        tb2.setText(b2);
                        title3.setText(t3);
                        tb3.setText(b3);
                        title4.setText(b4);
                        tb4.setText(t4);
                        title5.setText(t5);
                        tb5.setText(b5);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (tb1selected) {

                        TEXT_SELECTED_ID = getId(0);
                        title1.setText(TITLETEXT);
                        tb1.setText(BOXTEXT);
                    }
                    if (tb2selected) {
                        TEXT_SELECTED_ID = getId(1);
                        title2.setText(TITLETEXT);
                        tb2.setText(BOXTEXT);

                    }
                    if (tb3selected) {
                        TEXT_SELECTED_ID = getId(2);
                        title3.setText(TITLETEXT);
                        tb3.setText(BOXTEXT);

                    }
                    if (tb4selected) {
                        TEXT_SELECTED_ID = getId(3);
                        title4.setText(TITLETEXT);
                        tb4.setText(BOXTEXT);

                    }
                    if (tb5selected) {
                        TEXT_SELECTED_ID = getId(4);
                        title5.setText(TITLETEXT);
                        tb5.setText(BOXTEXT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        View.OnClickListener onclickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                switch (v.getId()) {

                    case R.id.drawer_login:
                        if(VolleySingleton.LOGADO){
                           VolleySingleton.BEARER_KEY = "";
                           VolleySingleton.LOGADO = false;
                           navController.navigate(R.id.nav_login);
                        }
                        btnPlay.setVisibility(View.GONE);
                        btnStop.setVisibility(View.GONE);
                        btnSettings.setVisibility(View.GONE);
                        if (navController.getCurrentDestination().getId() == R.id.nav_main) {
                            navController.navigate(R.id.action_nav_main_to_nav_login);
                        } else if (navController.getCurrentDestination().getId() == R.id.nav_settings) {
                            navController.navigate(R.id.action_nav_settings_to_nav_login);
                        } else if (navController.getCurrentDestination().getId() == R.id.nav_register) {
                            navController.navigate(R.id.action_nav_register_to_nav_login);
                        }
                        drawer.close();
                        break;
                    case R.id.toggleButton:
                        if (!isCollapsed) {
                            toggleButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                            if (VolleySingleton.LOGADO) {
                                textboxContainer.setVisibility(View.GONE);
                                containerTitle.setVisibility(View.GONE);
                                drawerLoginIcon.setImageResource(R.drawable.ic_baseline_exit_to_app_24);
                                drawerLogin.setText("Sair");
                                drawerLogin.setVisibility(View.VISIBLE);
                                drawerLoginIcon.setVisibility(View.VISIBLE);
                                isCollapsed = true;
                            } else {
                                textboxContainer.setVisibility(View.GONE);
                                containerTitle.setVisibility(View.GONE);
                                drawerLoginIcon.setImageResource(R.drawable.ic_baseline_person_24);
                                drawerLogin.setText("Entrar na conta");
                                drawerLogin.setVisibility(View.VISIBLE);
                                drawerLoginIcon.setVisibility(View.VISIBLE);
                                isCollapsed = true;
                            }
                        } else {
                            toggleButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                            drawerLogin.setVisibility(View.GONE);
                            drawerLoginIcon.setVisibility(View.GONE);
                            textboxContainer.setVisibility(View.VISIBLE);
                            containerTitle.setVisibility(View.VISIBLE);
                            isCollapsed = false;
                        }
                        break;
                    case R.id.toolbar_config_button:
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            btnPlay.setVisibility(View.GONE);
                            btnStop.setVisibility(View.GONE);
                            btnSettings.setVisibility(View.GONE);
                            navController.navigate(R.id.action_nav_main_to_nav_settings);
                        } else {
                            requestStoragePermission();
                        }
                        break;
                    case R.id.toolbar_stop_button:
                        mTTS.stop();
                        ttsStop = true;
                        break;
                    case R.id.toolbar_play_button:
                        ttsStop = false;
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            new Thread(t1).start();
                        } else {
                            requestStoragePermission();
                        }
                        break;
                    case R.id.textbox1:
                        if (!VolleySingleton.LOGADO) {
                            drawer.close();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Você não está logado")
                                    .setMessage("Para usar este conteúdo é preciso entrar.")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                        } else {

                            tb1selected = true;
                            buttonChecked = true;
                            if (tb2selected || tb3selected || tb4selected || tb5selected) {
                                tb2selected = false;
                                tb3selected = false;
                                tb4selected = false;
                                tb5selected = false;
                            }
                            if (tb1.getText() != null || title1.getText() != null) {
                                String title = title1.getText().toString(),
                                        content = tb1.getText().toString();
                                mainViewModel.setEdtContentText(content);
                                mainViewModel.setEdtTitle(title);
                            }
                            containerTitle.setBackgroundResource(R.drawable.shape_textbox_selected);
                            fulltb1.setBackgroundResource(R.drawable.shape_textbox_selected);
                            title1.setTextColor(Color.parseColor("#6A42F4"));
                            tb1.setTextColor(Color.parseColor("#6A42F4"));
                            tb1.setHintTextColor(Color.parseColor("#6A42F4"));
                            //reset other buttons
                            if (buttonChecked) {

                                fulltb2.setBackgroundResource(R.drawable.shape_textbox);
                                title2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb3.setBackgroundResource(R.drawable.shape_textbox);
                                title3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb4.setBackgroundResource(R.drawable.shape_textbox);
                                title4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb5.setBackgroundResource(R.drawable.shape_textbox);
                                title5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setHintTextColor(Color.parseColor("#e0e0e0"));
                            }
                            buttonChecked = false;
                        }
                        break;
                    case R.id.textbox2:
                        if (!VolleySingleton.LOGADO) {
                            drawer.close();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Você não está logado")
                                    .setMessage("Para usar este conteúdo é preciso entrar.")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                        } else {

                            tb2selected = true;
                            buttonChecked = true;
                            if (tb1selected || tb3selected || tb4selected || tb5selected) {
                                tb1selected = false;
                                tb3selected = false;
                                tb4selected = false;
                                tb5selected = false;
                            }
                            if (tb2.getText() != null || title2.getText() != null) {
                                String title = title2.getText().toString(),
                                        content = tb2.getText().toString();
                                mainViewModel.setEdtContentText(content);
                                mainViewModel.setEdtTitle(title);
                            }
                            fulltb1.setBackgroundResource(R.drawable.shape_textbox_selected);
                            fulltb2.setBackgroundResource(R.drawable.shape_textbox_selected);
                            title2.setTextColor(Color.parseColor("#6A42F4"));
                            tb2.setTextColor(Color.parseColor("#6A42F4"));
                            tb2.setHintTextColor(Color.parseColor("#6A42F4"));
                            //reset other buttons
                            if (buttonChecked) {
                                containerTitle.setBackgroundResource(R.drawable.shape_textbox);
                                title1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb3.setBackgroundResource(R.drawable.shape_textbox);
                                title3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb4.setBackgroundResource(R.drawable.shape_textbox);
                                title4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb5.setBackgroundResource(R.drawable.shape_textbox);
                                title5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setHintTextColor(Color.parseColor("#e0e0e0"));
                            }
                            buttonChecked = false;
                        }
                        break;

                    case R.id.textbox3:
                        if (!VolleySingleton.LOGADO) {
                            drawer.close();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Você não está logado")
                                    .setMessage("Para usar este conteúdo é preciso entrar.")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                        } else {

                            tb3selected = true;
                            buttonChecked = true;
                            if (tb2selected || tb1selected || tb4selected || tb5selected) {
                                tb2selected = false;
                                tb1selected = false;
                                tb4selected = false;
                                tb5selected = false;
                            }
                            if (tb3.getText() != null || title3.getText() != null) {
                                String title = title3.getText().toString(),
                                        content = tb3.getText().toString();
                                mainViewModel.setEdtContentText(content);
                                mainViewModel.setEdtTitle(title);
                            }
                            fulltb2.setBackgroundResource(R.drawable.shape_textbox_selected);
                            fulltb3.setBackgroundResource(R.drawable.shape_textbox_selected);
                            title3.setTextColor(Color.parseColor("#6A42F4"));
                            tb3.setTextColor(Color.parseColor("#6A42F4"));
                            tb3.setHintTextColor(Color.parseColor("#6A42F4"));
                            //reset other buttons
                            if (buttonChecked) {

                                containerTitle.setBackgroundResource(R.drawable.shape_textbox);
                                fulltb1.setBackgroundResource(R.drawable.shape_textbox);
                                title1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setHintTextColor(Color.parseColor("#e0e0e0"));


                                title2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb4.setBackgroundResource(R.drawable.shape_textbox);
                                title4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb5.setBackgroundResource(R.drawable.shape_textbox);
                                title5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setHintTextColor(Color.parseColor("#e0e0e0"));
                            }
                            buttonChecked = false;
                        }
                        break;
                    case R.id.textbox4:
                        if (!VolleySingleton.LOGADO) {
                            drawer.close();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Você não está logado")
                                    .setMessage("Para usar este conteúdo é preciso entrar.")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                        } else {

                            tb4selected = true;
                            buttonChecked = true;
                            if (tb2selected || tb3selected || tb1selected || tb5selected) {
                                tb2selected = false;
                                tb3selected = false;
                                tb1selected = false;
                                tb5selected = false;
                            }
                            if (tb4.getText() != null || title4.getText() != null) {
                                String title = title4.getText().toString(),
                                        content = tb4.getText().toString();
                                mainViewModel.setEdtContentText(content);
                                mainViewModel.setEdtTitle(title);
                            }
                            fulltb3.setBackgroundResource(R.drawable.shape_textbox_selected);
                            fulltb4.setBackgroundResource(R.drawable.shape_textbox_selected);
                            title4.setTextColor(Color.parseColor("#6A42F4"));
                            tb4.setTextColor(Color.parseColor("#6A42F4"));
                            tb4.setHintTextColor(Color.parseColor("#6A42F4"));
                            //reset other buttons
                            if (buttonChecked) {

                                containerTitle.setBackgroundResource(R.drawable.shape_textbox);
                                fulltb1.setBackgroundResource(R.drawable.shape_textbox);
                                title1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb2.setBackgroundResource(R.drawable.shape_textbox);
                                title2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setHintTextColor(Color.parseColor("#e0e0e0"));

                                title3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb5.setBackgroundResource(R.drawable.shape_textbox);
                                title5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setTextColor(Color.parseColor("#e0e0e0"));
                                tb5.setHintTextColor(Color.parseColor("#e0e0e0"));
                            }
                            buttonChecked = false;
                        }
                        break;
                    case R.id.textbox5:
                        if (!VolleySingleton.LOGADO) {
                            drawer.close();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Você não está logado")
                                    .setMessage("Para usar este conteúdo é preciso entrar.")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                        } else {
                            tb5selected = true;
                            buttonChecked = true;
                            if (tb1selected || tb2selected || tb3selected || tb4selected) {
                                tb1selected = false;
                                tb2selected = false;
                                tb3selected = false;
                                tb4selected = false;
                            }
                            if (tb5.getText() != null || title5.getText() != null) {
                                String title = title5.getText().toString(),
                                        content = tb5.getText().toString();
                                mainViewModel.setEdtContentText(content);
                                mainViewModel.setEdtTitle(title);
                            }
                            fulltb4.setBackgroundResource(R.drawable.shape_textbox_selected);
                            fulltb5.setBackgroundResource(R.drawable.shape_textbox_selected);
                            title5.setTextColor(Color.parseColor("#6A42F4"));
                            tb5.setTextColor(Color.parseColor("#6A42F4"));
                            tb5.setHintTextColor(Color.parseColor("#6A42F4"));
                            //reset other buttons
                            if (buttonChecked) {

                                containerTitle.setBackgroundResource(R.drawable.shape_textbox);
                                fulltb1.setBackgroundResource(R.drawable.shape_textbox);
                                title1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setTextColor(Color.parseColor("#e0e0e0"));
                                tb1.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb2.setBackgroundResource(R.drawable.shape_textbox);
                                title2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setTextColor(Color.parseColor("#e0e0e0"));
                                tb2.setHintTextColor(Color.parseColor("#e0e0e0"));

                                fulltb3.setBackgroundResource(R.drawable.shape_textbox);
                                title3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setTextColor(Color.parseColor("#e0e0e0"));
                                tb3.setHintTextColor(Color.parseColor("#e0e0e0"));

                                title4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setTextColor(Color.parseColor("#e0e0e0"));
                                tb4.setHintTextColor(Color.parseColor("#e0e0e0"));
                            }
                            buttonChecked = false;
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        drawerLogin.setOnClickListener(onclickHandler);
        toggleButton.setOnClickListener(onclickHandler);
        btnSettings.setOnClickListener(onclickHandler);
        btnStop.setOnClickListener(onclickHandler);
        btnPlay.setOnClickListener(onclickHandler);
        tb1.setOnClickListener(onclickHandler);
        tb2.setOnClickListener(onclickHandler);
        tb3.setOnClickListener(onclickHandler);
        tb4.setOnClickListener(onclickHandler);
        tb5.setOnClickListener(onclickHandler);
    }
    public String getTexts(int index) throws JSONException {
        JSONArray jsonArray = new JSONArray(MainActivity.txtboxTexts);
        String text = jsonArray.getJSONObject(index).getString("content");
        return text;
    }
    public String getTitles(int index) throws JSONException {
        JSONArray jsonArray = new JSONArray(MainActivity.txtboxTexts);
        String title = jsonArray.getJSONObject(index).getString("title");
        return title;
    }
    public int getId(int index) throws JSONException{
        int id;
        JSONArray jsonArray = new JSONArray(MainActivity.txtboxTexts);
        id = jsonArray.getJSONObject(index).getInt("id");
        return id;
    }

    public void showToolbarButtons(){
        btnSettings.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
    }

    public static synchronized MainActivity getInstance() {
        return MAIN_ACTIVITY_INSTANCE;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Text to Speech Thread
    private Runnable t1 = new Runnable() {
        @Override
        public void run(){
            try {
                configFileHelper.readConfigFile(PATH);
                String valueSkbPausedRead = configFileHelper.getJsonText("voicepaused", PATH);
                float pauseSpeed;
                switch (valueSkbPausedRead){
                    case "0":
                        pauseSpeed = 3;
                        break;

                    case "1":
                        pauseSpeed = 1;
                        break;

                    case "2":
                        pauseSpeed = 0.75f;
                        break;

                    case "3":
                        pauseSpeed = 0.5f;
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + valueSkbPausedRead);
                }
                String valueSkbVoiceSpeed = configFileHelper.getJsonText("voicespeed", PATH);
                switch (valueSkbVoiceSpeed){
                    case "0":
                        mTTS.setSpeechRate(0.3f);
                        break;

                    case "1":
                        mTTS.setSpeechRate(0.6f);
                        break;

                    case "2":
                        mTTS.setSpeechRate(0.9f);
                        break;

                    case "3":
                        mTTS.setSpeechRate(1.2f);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + valueSkbVoiceSpeed);
                }

                String text = BOXTEXT;
                String[] arr = text.split(" ");
                String valueChkPausedRead = configFileHelper.getJsonText("checked", PATH);
                if(valueChkPausedRead.equals("true")){
                    for (String ss : arr) {
                        if(!ttsStop){
                            Thread.sleep((long) (pauseSpeed * 1000));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mTTS.speak(ss,TextToSpeech.QUEUE_FLUSH,null,null);
                            } else {
                                mTTS.speak(ss, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }else break;
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
                    } else {
                        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    public Runnable runGetText = new Runnable() {
        @Override
        public void run() {
            if (VolleySingleton.LOGADO) {
                VolleySingleton.getInstance().requestGetTexts();
            }
        }
    };
    public void loadTexts(){
        Handler handler = new Handler();
        handler.postDelayed(runGetText, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permissão Necessária")
                    .setMessage("Ditey usa essa permissão para salvar suas configurações")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configFileHelper.createConfigFile(PATH);
                configFileHelper.readConfigFile(PATH);
            }
        }
    }


}