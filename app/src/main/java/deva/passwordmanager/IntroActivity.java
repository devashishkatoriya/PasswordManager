package deva.passwordmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class IntroActivity extends AppCompatActivity {

    //Permission code that will be checked in the method onRequestPermissionsResult
    private final static int STORAGE_PERMISSION_CODE = 23;

    private final static String LOG_TAG = "IntroActivityDebug";
    private EditText e1;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Log.d(LOG_TAG,"At onCreate()");

        myPermission();

        count = 0;
        e1 = (EditText) findViewById(R.id.editText21);
        Button btn_enter = (Button) findViewById(R.id.button21);
        Button btn_exit = (Button) findViewById(R.id.button22);

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_activity();
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

    }

    private void next_activity()
    {
        String user_input = e1.getText().toString();
        String real_password;
        try {
            FileInputStream f1 = openFileInput("pass1.txt");
            InputStreamReader ias = new InputStreamReader(f1);
            BufferedReader br = new BufferedReader(ias);
            real_password=br.readLine();

            if(user_input.equals(real_password) && count<5)
            {
                Intent i = new Intent("deva.passwordmanager.MainActivity");
                startActivity(i);
            }
            else if(count<5)
            {
                count++;
                Toast.makeText(this, "Invalid Password! You have " + (5-count) + " attempts left.", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Intruder detected!!!", Toast.LENGTH_LONG).show();
            }

            f1.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            try {
                FileOutputStream f2 = openFileOutput("pass1.txt", MODE_PRIVATE);
                f2.write(("12345678").getBytes());
                f2.close();
                Toast.makeText(IntroActivity.this, "The default password is 12345678", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG,"pass1 was not created. I created one for you.");
            } catch (IOException e1) {
                e1.printStackTrace();
                Toast.makeText(this, "IO Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void exit()
    {

        AlertDialog.Builder a = new AlertDialog.Builder(IntroActivity.this);
        a.setMessage(R.string.confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(IntroActivity.this, R.string.thank_you,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = a.create();
        alert.setTitle("Alert!");
        alert.show();
    }

    private void myPermission()
    {
        if(isWriteStorageAllowed()){
            //If permission is already having then showing the toast
            //Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
            return;
        }

        //If the app has not the permission then asking for the permission
        Log.d(LOG_TAG,"Requesting permission.");
        requestStoragePermission();
    }

    //We are calling this method to check the permission status
    private boolean isWriteStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //Explain here why you need this permission
            Toast.makeText(IntroActivity.this, R.string.request_permission,Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode >= STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, R.string.thanks_permission,Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(this, R.string.sorry_permission,Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
