package ru.android.polenova;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FilePin {

    private static final String FILE_PASSWORD = "password_notes";

    static boolean exportPIN(Context context, String stringPin) {
        String stringPassword = stringPin.toString();
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILE_PASSWORD, Context.MODE_PRIVATE)));
            bufferedWriter.write(stringPassword);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "пароль сохранен", Toast.LENGTH_SHORT).show();
        return false;
    }

    static String importPIN(Context context) {
        String stringPassword;
        BufferedReader bufferedReader = null;
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(FILE_PASSWORD);
            streamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(streamReader);
            stringPassword = bufferedReader.readLine().toString();
            return stringPassword;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (streamReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
