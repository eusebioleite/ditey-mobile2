package com.e.diteyb;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SettingsActivity extends AppCompatActivity {
    SeekBar skbVoiceSpeed,skbPausedRead;
    CheckBox chkPausedRead;
    private int STORAGE_PERMISSION_CODE = 1;
    //Switch swtDarkMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final ConfigFileHelper configFileHelper = new ConfigFileHelper();
        configFileHelper.readConfigFile(MainActivity.PATH);
        skbVoiceSpeed = findViewById(R.id.skb_voice_speed);
        skbPausedRead = findViewById(R.id.skb_pause_speed);
        chkPausedRead = findViewById(R.id.chk_pause_speed);

        if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            int pr = Integer.parseInt(configFileHelper.getJsonText("voicepaused",MainActivity.PATH));
            skbPausedRead.setProgress(pr);
            int vs = Integer.parseInt(configFileHelper.getJsonText("voicespeed",MainActivity.PATH));
            skbVoiceSpeed.setProgress(vs);
            String ch = configFileHelper.getJsonText("checked",MainActivity.PATH);
            if(ch.equals("true")) chkPausedRead.setChecked(true);else chkPausedRead.setChecked(false);
        } else {
            requestStoragePermission();
        }

        skbVoiceSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    configFileHelper.readConfigFile(MainActivity.PATH);
                    String value = String.valueOf(progress);
                    configFileHelper.writeString("voicespeed", value, MainActivity.PATH);
                } else {
                    requestStoragePermission();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        skbPausedRead.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    configFileHelper.readConfigFile(MainActivity.PATH);
                    String value = String.valueOf(progress);
                    configFileHelper.writeString("voicepaused", value, MainActivity.PATH);
                } else {
                    requestStoragePermission();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        chkPausedRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    configFileHelper.readConfigFile(MainActivity.PATH);
                    if(chkPausedRead.isChecked()){
                        configFileHelper.writeString("checked","true", MainActivity.PATH);
                    }else{
                        configFileHelper.writeString("checked","false", MainActivity.PATH);
                    }
                } else {
                    requestStoragePermission();
                }
            }
        });
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permissão Necessária")
                    .setMessage("Essa permissão é necessária para salvar as suas configurações de voz")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SettingsActivity.this,
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
            }
        }
    }
}

