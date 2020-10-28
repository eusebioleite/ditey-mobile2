package com.e.diteyb;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;

public class SavedTextsActivity extends AppCompatActivity {
    public static boolean TEXT_CHANGED = false;
    Button btnText1,btnText2,btnText3,btnText4,btnText5;
    TextView txtTitle1, txtTitle2, txtTitle3, txtTitle4, txtTitle5;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_texts);
        txtTitle1 = findViewById(R.id.title1);
        txtTitle2 = findViewById(R.id.title2);
        txtTitle3 = findViewById(R.id.title3);
        txtTitle4 = findViewById(R.id.title4);
        txtTitle5 = findViewById(R.id.title5);
        btnText1 = findViewById(R.id.btn_txt1);
        btnText2 = findViewById(R.id.btn_txt2);
        btnText3 = findViewById(R.id.btn_txt3);
        btnText4 = findViewById(R.id.btn_txt4);
        btnText5 = findViewById(R.id.btn_txt5);
        if(!VolleySingleton.LOGADO){
        new AlertDialog.Builder(SavedTextsActivity.this)
        .setTitle("Você não está logado")
        .setMessage("Você precisa estar logado para acessar essa página, deseja fazer login?")
        .setPositiveButton("ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SavedTextsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        })
        .setNegativeButton("cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })
        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
        .create().show();
        }else {
            try {
            String t1=getTitles(0),t2=getTitles(1),t3=getTitles(2),
                    t4=getTitles(3),t5=getTitles(4);
            String b1=getTexts(0),b2=getTexts(1),b3=getTexts(2),
                    b4=getTexts(3),b5=getTexts(4);

            if (t1 == null || t1.equals("")|| t1.equals("error")||
                t2 == null || t2.equals("")||t2.equals("error") ||
                t3 == null || t3.equals("")|| t3.equals("error")||
                t4 == null || t4.equals("")|| t4.equals("error")||
                t5 == null || t5.equals("")|| t5.equals("error")||
                b1 == null || b1.equals("")|| b1.equals("error")||
                b2 == null || b2.equals("")|| b2.equals("error")||
                b3 == null || b3.equals("")|| b3.equals("error")||
                b4 == null || b4.equals("")|| b4.equals("error")||
                b5 == null || b5.equals("")|| b5.equals("error"))recreate();

            txtTitle1.setText(t1);
            btnText1.setText(b1);
            txtTitle2.setText(t2);
            btnText2.setText(b2);
            txtTitle3.setText(t3);
            btnText3.setText(b3);
            txtTitle4.setText(t4);
            btnText4.setText(b4);
            txtTitle5.setText(t5);
            btnText5.setText(b5);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        //click will set the main text the text of the buttons
        View.OnClickListener onClickHandler = new View.OnClickListener() {

            public void onClick(View v) {
                try{
                switch (v.getId()) {

                    case R.id.btn_txt1:
                            String s = btnText1.getText().toString();
                            String t = txtTitle1.getText().toString();
                            MainActivity.TITLETEXT = t;
                            MainActivity.BOXTEXT= s;
                            TEXT_CHANGED = true;
                            finish();
                            break;

                    case R.id.btn_txt2:
                        String s2 = btnText2.getText().toString();
                        String t2 = txtTitle2.getText().toString();
                        MainActivity.TITLETEXT = t2;
                        MainActivity.BOXTEXT= s2;
                        TEXT_CHANGED = true;
                        finish();
                        break;

                    case R.id.btn_txt3:
                        String s3 = btnText3.getText().toString();
                        String t3 = txtTitle3.getText().toString();
                        MainActivity.TITLETEXT = t3;
                        MainActivity.BOXTEXT= s3;
                        TEXT_CHANGED = true;
                        finish();
                        break;

                    case R.id.btn_txt4:
                        String s4 = btnText4.getText().toString();
                        String t4 = txtTitle4.getText().toString();
                        MainActivity.TITLETEXT = t4;
                        MainActivity.BOXTEXT= s4;
                        TEXT_CHANGED = true;
                        finish();
                        break;

                    case R.id.btn_txt5:
                        String s5 = btnText5.getText().toString();
                        String t5 = txtTitle5.getText().toString();
                        MainActivity.TITLETEXT = t5;
                        MainActivity.BOXTEXT= s5;
                        TEXT_CHANGED = true;
                        finish();
                        break;
                }
                }catch (Exception e){e.printStackTrace();}
            }
        };
        //long click will store the mainedittext text to the button
        View.OnLongClickListener onLongClickHandler = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try{
                    switch (v.getId()) {
                    case R.id.btn_txt1:
                            Toast.makeText(SavedTextsActivity.this, "Texto salvo com sucesso", Toast.LENGTH_SHORT).show();
                            int id1 = getId(0);
                            String title = MainActivity.TITLETEXT,
                                    content = MainActivity.BOXTEXT;
                            VolleySingleton.getInstance().requestModifyText(id1, title, content);
                            VolleySingleton.getInstance().requestGetTexts();
                            txtTitle1.setText(title);
                            btnText1.setText(content);
                        break;

                    case R.id.btn_txt2:
                            Toast.makeText(SavedTextsActivity.this, "Texto salvo com sucesso", Toast.LENGTH_SHORT).show();
                            int id2 = getId(1);
                            String title2 = MainActivity.TITLETEXT,
                                    content2 = MainActivity.BOXTEXT;
                            VolleySingleton.getInstance().requestModifyText(id2, title2, content2);
                            VolleySingleton.getInstance().requestGetTexts();
                            txtTitle2.setText(title2);
                            btnText2.setText(content2);
                        break;

                    case R.id.btn_txt3:
                            Toast.makeText(SavedTextsActivity.this, "Texto salvo com sucesso", Toast.LENGTH_SHORT).show();
                            int id3 = getId(2);
                            String title3 = MainActivity.TITLETEXT,
                                    content3 = MainActivity.BOXTEXT;
                            VolleySingleton.getInstance().requestModifyText(id3, title3, content3);
                            VolleySingleton.getInstance().requestGetTexts();
                            txtTitle3.setText(title3);
                            btnText3.setText(content3);
                        break;

                    case R.id.btn_txt4:
                            Toast.makeText(SavedTextsActivity.this, "Texto salvo com sucesso", Toast.LENGTH_SHORT).show();
                            int id4 = getId(3);
                            String title4 = MainActivity.TITLETEXT,
                                    content4 = MainActivity.BOXTEXT;
                            VolleySingleton.getInstance().requestModifyText(id4, title4, content4);
                            VolleySingleton.getInstance().requestGetTexts();
                            txtTitle4.setText(title4);
                            btnText4.setText(content4);

                        break;

                    case R.id.btn_txt5:
                            Toast.makeText(SavedTextsActivity.this, "Texto salvo com sucesso", Toast.LENGTH_SHORT).show();
                            int id5 = getId(4);
                            String title5 = MainActivity.TITLETEXT,
                                    content5 = MainActivity.BOXTEXT;
                            VolleySingleton.getInstance().requestModifyText(id5, title5, content5);
                            VolleySingleton.getInstance().requestGetTexts();
                            txtTitle5.setText(title5);
                            btnText5.setText(content5);

                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        };

        btnText1.setOnClickListener(onClickHandler);
        btnText2.setOnClickListener(onClickHandler);
        btnText3.setOnClickListener(onClickHandler);
        btnText4.setOnClickListener(onClickHandler);
        btnText5.setOnClickListener(onClickHandler);

        btnText1.setOnLongClickListener(onLongClickHandler);
        btnText2.setOnLongClickListener(onLongClickHandler);
        btnText3.setOnLongClickListener(onLongClickHandler);
        btnText4.setOnLongClickListener(onLongClickHandler);
        btnText5.setOnLongClickListener(onLongClickHandler);

    }
}
