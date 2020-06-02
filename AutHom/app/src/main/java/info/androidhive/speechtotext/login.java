package info.androidhive.speechtotext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class login extends Activity {
    EditText username_et;
    EditText password_et;
    Map<String, String> dictionary;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide the action bar
        getActionBar().hide();

        username_et=findViewById(R.id.username_et);
        password_et=findViewById(R.id.password_et);

        dictionary = new HashMap<String, String>();

        dictionary.put("shubham15","1234");
        dictionary.put("shivendra15","1234");
        dictionary.put("srilakshmi17","1234");
        dictionary.put("anil15","1234");
        dictionary.put("rajat15","1234");


        pd= new ProgressDialog(this);
        pd.setMessage("Verifying...");
        pd.setCancelable(false);





    }

    void login(View v) throws InterruptedException {





        final String username= username_et.getText().toString().trim();
        final String password= password_et.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            username_et.setError("Can't be empty");
        }
        else if(TextUtils.isEmpty(password)){
            password_et.setError("Can't be empty");
        }
        else{
            pd.show();


            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    pd.cancel();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 2000);

           String pass= dictionary.get(username);

           if(password.equals(pass)){
               Intent i = new Intent(login.this,MainActivity.class);
               startActivity(i);
               login.this.finish();
           }
           else{
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setTitle("Invalid Authentication");
               builder.setMessage("Please enter right credentials");
               builder.show();
               builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       username_et.setText("");
                       password_et.setText("");
                   }
               });
           }
        }
    }
}
