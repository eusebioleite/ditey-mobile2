package com.e.diteyb;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class ConfigFileHelper{
    public static String CONFIG="{\"id\":0,\"title\":\"error\",\"content\":\"error\"}";
    public JSONObject testJson;
    FileReader fileReader = null;
    FileWriter fileWriter = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;
    String response = null;
    public void createConfigFile(String path) {
        File file = new File(path,"config");
        if (!file.exists()) {
            try {
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("{}");
                bufferedWriter.close();
                readConfigFile(path);
                createComponents(path);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("File:","File already exists");
            }
        }
    }


    public void readConfigFile(String path){
        File file = new File(path, "config");
        if (file.exists()) {
            try {
                StringBuffer output = new StringBuffer();
                fileReader = new FileReader(file.getAbsolutePath());
                bufferedReader = new BufferedReader(fileReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    output.append(line + "\n");
                }
                response = output.toString();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getJsonText(String name,String path) {
        try {
            readConfigFile(path);
            testJson = new JSONObject(response);
            String content = testJson.get(name).toString();
            return content;
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public void writeString(String name, String content, String path) {
        try {
            JSONObject jsonFile = new JSONObject(response);
            jsonFile.put(name, content);
            File file = new File(path,"config");
            fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            String s = jsonFile.toString();
            bw.write(s);
            bw.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    public void createComponents(String path) {
        try {
            JSONObject jsonFile = new JSONObject(response);
            jsonFile.put("voicespeed", "1");
            jsonFile.put("voicepaused", "1");
            jsonFile.put("checked", "false");

            File file = new File(path,"config");
            fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            String s = jsonFile.toString();
            bw.write(s);
            bw.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
