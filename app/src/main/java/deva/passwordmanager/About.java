package deva.passwordmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;

public class About extends AppCompatActivity {

    private final String LOG_TAG = "AboutActivityDebug";
    private EditText e1,e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.d(LOG_TAG,"At onCreate()");

        e1 = (EditText) findViewById(R.id.editText18);
        e2 = (EditText) findViewById(R.id.editText19);
        Button bExport = (Button) findViewById(R.id.button4);
        Button bChange = (Button) findViewById(R.id.button18);
        Button bExit = (Button) findViewById(R.id.button19);

        bExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export_function();
            }
        });
        bChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_pass();
            }
        });
        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    private void exit()
    {

        AlertDialog.Builder a = new AlertDialog.Builder(About.this);
        a.setMessage(R.string.confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(About.this, R.string.thank_you,Toast.LENGTH_SHORT).show();
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

    private void change_pass()
    {
        String real_password,old_password,new_password;
        old_password = e1.getText().toString();

        try
        {
            FileInputStream f1 = openFileInput("pass1.txt");
            InputStreamReader ias = new InputStreamReader(f1);
            BufferedReader br = new BufferedReader(ias);
            real_password=br.readLine();
            f1.close();

            if(real_password.equals(old_password))
            {
                new_password = e2.getText().toString();
                FileOutputStream f2 = openFileOutput("pass1.txt", MODE_PRIVATE);
                f2.write((new_password).getBytes());
                f2.close();

                Toast.makeText(this, "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Old Password is Incorrect!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "IO Error occurred!");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG,"At onPause()");
    }

    private void export_function()
    {
        DateFormat df = DateFormat.getDateTimeInstance();        //Wed, 4 Jul 2008 12:08:56 -0530
        String date = df.format(Calendar.getInstance().getTime());
        String data;
        int i;
        try {

            File directory = new File(Environment.getExternalStorageDirectory().getPath()+"/Documents/");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/Documents/"+"my_todo_list.txt");             //Export file creation
            myFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(myFile, false);

            FileInputStream fileInputStream = openFileInput("data1.txt");                   //To get Reminder Tasks
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            fos.write(("\n\nDate : " + date + "\n").getBytes());
            fos.write(("\nPassword List :- \n").getBytes());
            i=1;
            while (i<=8) {
                data=bufferedReader.readLine();
                fos.write(("\n"+i+". "+data).getBytes());
                i++;
            }
            fileInputStream.close();
            fos.close();

            AlertDialog.Builder a = new AlertDialog.Builder(About.this);
            a.setMessage("Passwords exported successfully to "+Environment.getExternalStorageDirectory().getPath()+"/Documents/")
                    .setCancelable(true)
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alert = a.create();
            alert.setTitle("Done!");
            alert.show();
            Log.d(LOG_TAG,"File successfully exported");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(About.this,"IO Error occurred!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"At onDestroy()");
    }
}
