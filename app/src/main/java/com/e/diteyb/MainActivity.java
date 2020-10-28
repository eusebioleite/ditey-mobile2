package com.e.diteyb;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    public static String txtboxTexts="[{\"id\":0,\"title\":\"error\",\"content\":\"error\"}]";
    public static String PATH;
    public static String TITLETEXT, BOXTEXT;
    public static boolean LAST=false;
    boolean ttsStop;
    ImageButton btnPlay, btnStop;
    TextToSpeech mTTS;
    EditText edtMainTitle,edtMainEditText;
    Menu mMenu;
    MenuItem mItem;
    final ConfigFileHelper configFileHelper = new ConfigFileHelper();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_login){
            if(VolleySingleton.LOGADO)item.setTitle("sair");
        }
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_login:
                if(VolleySingleton.LOGADO){
                    Toast.makeText(this, "Você já está logado!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
                return true;
            case R.id.action_saved_texts:
                Intent savedTextsIntent = new Intent(this, SavedTextsActivity.class);
                startActivity(savedTextsIntent);
                return true;
            case R.id.action_voice_commands:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Comandos de Voz")
                        .setMessage("iniciar - inicia o tts\n" +
                                "parar - para o tts por completo\n" +
                                "pausar - pausa o tts\n" +
                                "retomar - retoma a pausa feita no tts\n" +
                                "voltar - volta 5 palavras")
                        .setPositiveButton("ok",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SavedTextsActivity.TEXT_CHANGED) {
            edtMainTitle.setText(TITLETEXT);
            edtMainEditText.setText(BOXTEXT);
            SavedTextsActivity.TEXT_CHANGED = false;
        }
        Handler handler = new Handler();
        handler.postDelayed(runGetText,2000);
        configFileHelper.readConfigFile(PATH);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            btnPlay = findViewById(R.id.play_button);
            btnStop = findViewById(R.id.stop_button);
            edtMainEditText = findViewById(R.id.mainEditText);
            edtMainTitle = findViewById(R.id.edtMainTitle);
            PATH = getObbDir().toString();
        try {
            configFileHelper.createConfigFile(PATH);
            configFileHelper.readConfigFile(PATH);
            String t = configFileHelper.getJsonText("title", PATH),
                    b = configFileHelper.getJsonText("box", PATH);
            edtMainTitle.setText(t);
            edtMainEditText.setText(b);
        } catch (NullPointerException npe) {
            edtMainTitle.setText("sem permissão");
            edtMainEditText.setText("clique aqui para permitir o acesso ao armazenamento," +
                    "utilizamos essa permissão para salvar suas configurações");
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
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTTS.stop();
                    ttsStop = true;
                }
            });
            btnPlay.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    ttsStop = false;
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        new Thread(t1).start();
                    } else {
                        requestStoragePermission();
                    }
                }
            });
            edtMainTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    }else{
                        requestStoragePermission();
                    }
                }
            });
            edtMainTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        String title = edtMainTitle.getText().toString();
                        TITLETEXT = title;
                        configFileHelper.writeString("title", title, PATH);
                    } else {
                        requestStoragePermission();
                    }
                }
            });
            edtMainEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    }else{
                        requestStoragePermission();
                    }
                }
            });
            edtMainEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        String box = edtMainEditText.getText().toString();
                        BOXTEXT = box;
                        configFileHelper.writeString("box", box, PATH);
                    } else {
                        requestStoragePermission();
                    }
                }
            });

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

                String text = edtMainEditText.getText().toString();
                String[] arr = text.split(" ");
                String valueChkPausedRead = configFileHelper.getJsonText("checked", PATH);
                if(valueChkPausedRead.equals("true")){
                    for (String ss : arr) {
                        if(!ttsStop){
                            Thread.sleep((long) (pauseSpeed * 1000));
                            mTTS.speak(ss, TextToSpeech.QUEUE_FLUSH, null);
                        }else break;
                    }
                }else mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private Runnable runGetText = new Runnable() {
        @Override
        public void run() {
            if (VolleySingleton.LOGADO) {
                mItem = mMenu.findItem(R.id.action_login);
                mItem.setTitle("Sair");
                VolleySingleton.getInstance().requestGetTexts();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTTS != null){
            mTTS.stop();
            mTTS.shutdown();
            if(edtMainEditText.getText().length() != 0) {
                configFileHelper.writeString("box", BOXTEXT, PATH);
                configFileHelper.writeString("title", TITLETEXT, PATH);
                configFileHelper.writeString("last", "true", PATH);
            } else{
                configFileHelper.writeString("last", "false", PATH);
            }
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