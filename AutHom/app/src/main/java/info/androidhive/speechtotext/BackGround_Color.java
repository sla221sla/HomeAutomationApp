package info.androidhive.speechtotext;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

public class BackGround_Color extends AsyncTask<String,Void,String>  {

    Context ctx;
    public String ret;
    String prev;

    BackGround_Color(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        prev = params[0];
        char ch[] = prev.toCharArray();
        for (int i = 0;i<6;i++){
            if (ch[i]=='1'){
                MainActivity.textViews[i].setTextColor(Color.rgb(0,255,0));
            }
            else{
                MainActivity.textViews[i].setTextColor(Color.rgb(255,0,0));
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        try {
            //this.finalize();
            this.execute(prev);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}

