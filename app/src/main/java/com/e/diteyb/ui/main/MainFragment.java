package com.e.diteyb.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.e.diteyb.MainActivity;
import com.e.diteyb.R;
import com.e.diteyb.VolleySingleton;


public class MainFragment extends Fragment {
    EditText edtTitle, edtContent;
    private MainViewModel mainViewModel;
    boolean firstime = true;

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.getInstance().showToolbarButtons();
                if (firstime) {
            MainActivity.getInstance().loadTexts();
            firstime = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(MainActivity.MAIN_ACTIVITY_INSTANCE).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        edtContent = root.findViewById(R.id.content_box);
        edtTitle = root.findViewById(R.id.title_box);
        mainViewModel.getContentText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                edtContent.setText(s);
            }
        });
        mainViewModel.getEdtTitleText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                edtTitle.setText(s);
            }
        });

        //content edit text
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String box = edtContent.getText().toString();
                MainActivity.BOXTEXT = box;
            }

            @Override
            public void afterTextChanged(Editable s) {
                VolleySingleton.getInstance().requestModifyText(MainActivity.TEXT_SELECTED_ID,MainActivity.TITLETEXT,MainActivity.BOXTEXT);
            }
        });
        //title edit text

        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = edtTitle.getText().toString();
                MainActivity.TITLETEXT = title;
            }

            @Override
            public void afterTextChanged(Editable s) {
                VolleySingleton.getInstance().requestModifyText(MainActivity.TEXT_SELECTED_ID,MainActivity.TITLETEXT,MainActivity.BOXTEXT);
            }
        });
        return root;
    }
}
