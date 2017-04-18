package com.jonmid.tallerasynctask;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jonmid.tallerasynctask.Models.JsonParser;
import com.jonmid.tallerasynctask.Parser.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressBar cargador;
    Button boton;
    List<Post> mysPost;
    LinearLayout myLinear;
    //ListView LIST;
    //ArrayList<String> arrayList;
    //ArrayAdapter<String> adapter;



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargador = (ProgressBar) findViewById(R.id.cargador);
        boton = (Button) findViewById(R.id.boton);
        myLinear= (LinearLayout) findViewById(R.id.myLinear);
       // LIST = (ListView) findViewById(R.id.list);
        //arrayList = new ArrayList<String>();


    }

    public Boolean isOnLine(){
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connec.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }

    public void onClickButton(View view){
        if (isOnLine()){
            MyTask task = new MyTask();
            task.execute("https://jsonplaceholder.typicode.com/posts");
        }else {
            Toast.makeText(this, "Sin conexión", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarDatos(){

        ArrayList<String> datos = new ArrayList<>();
        if(mysPost != null){
            for (Post post:mysPost){

            //datos.add(post.getTitle());
                TextView myText = new TextView(this);
                myText.setText(post.getTitle());
                myText.setTextColor(Color.BLUE);
                myLinear.addView(myText);
            }
            //ArrayAdapter<String> adaptador= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,datos);
            //LIST.setAdapter(adaptador)

        }


    }

    private class MyTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cargador.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String content = null;
            try {
                content = HttpManager.getData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                mysPost = JsonParser.parse(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cargarDatos();
            cargador.setVisibility(View.GONE);
        }
    }
}
