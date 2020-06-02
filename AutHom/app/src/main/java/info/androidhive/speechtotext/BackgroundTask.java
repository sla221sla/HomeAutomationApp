package info.androidhive.speechtotext;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class BackgroundTask extends AsyncTask<String,Void,String>  {

    Context ctx;
    public String ret;
    BackgroundTask(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String ex_url  = params[0];
        try {
            URL url = new URL("https://api.thingspeak.com/channels/661647/fields/"+ex_url+"/last.txt?api_key=NM5G53W1VFUXHXLJ");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            ret = in.readLine();
            in.close();
            return ret;
        }catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
    }
}

