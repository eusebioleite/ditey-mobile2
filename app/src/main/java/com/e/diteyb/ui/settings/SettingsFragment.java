package com.e.diteyb.ui.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.e.diteyb.ConfigFileHelper;
import com.e.diteyb.MainActivity;
import com.e.diteyb.R;

import java.lang.reflect.Method;

public class SettingsFragment extends Fragment {
    SeekBar skbVoiceSpeed,skbPausedRead;
    CheckBox chkPausedRead;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final ConfigFileHelper configFileHelper = new ConfigFileHelper();
        configFileHelper.readConfigFile(MainActivity.PATH);
        skbVoiceSpeed = root.findViewById(R.id.skb_voice_speed_frag);
        skbPausedRead = root.findViewById(R.id.skb_pause_speed_frag);
        chkPausedRead = root.findViewById(R.id.chk_pause_speed_frag);
        int pr = Integer.parseInt(configFileHelper.getJsonText("voicepaused",MainActivity.PATH));
        skbPausedRead.setProgress(pr);
        int vs = Integer.parseInt(configFileHelper.getJsonText("voicespeed",MainActivity.PATH));
        skbVoiceSpeed.setProgress(vs);
        String ch = configFileHelper.getJsonText("checked",MainActivity.PATH);
        if(ch.equals("true")) chkPausedRead.setChecked(true);else chkPausedRead.setChecked(false);

        skbVoiceSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    configFileHelper.readConfigFile(MainActivity.PATH);
                    String value = String.valueOf(progress);
                    configFileHelper.writeString("voicespeed", value, MainActivity.PATH);
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
                    configFileHelper.readConfigFile(MainActivity.PATH);
                    String value = String.valueOf(progress);
                    configFileHelper.writeString("voicepaused", value, MainActivity.PATH);
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
                    configFileHelper.readConfigFile(MainActivity.PATH);
                    if(chkPausedRead.isChecked()){
                        configFileHelper.writeString("checked","true", MainActivity.PATH);
                    }else{
                        configFileHelper.writeString("checked","false", MainActivity.PATH);
                    }
            }
        });
        return root;
    }
}
