package com.example.administrator.pugify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.administrator.pugify.item.Interactor;
import com.example.administrator.pugify.item.ItemView;
import com.example.administrator.pugify.model.Feed;
import com.example.administrator.pugify.model.Pug;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.example.administrator.pugify.R.layout.hm_item;

/**
 * Created by Administrator on 03/12/2015.
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB252));

        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("currentuser", Context.MODE_PRIVATE);


        ImageButton btnPost = (ImageButton) findViewById(R.id.hm_getMoreResults);

        boolean visit = false;

        if (sharedPreferences.getString("username", null) == null){
            btnPost.setEnabled(false);
            AlertDialog alert = new AlertDialog.Builder(HomeActivity.this).create();
            alert.setTitle(R.string.alertTitle);
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setMessage("Sei entrato in modalità visitatore, potrai solamente mettere mi piace ai contenuti. Non potrai caricare post o mandare report");
            alert.show();

            visit = true;


        }

        addItems(visit);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivity();
            }
        });

        //ArrayList<Pug> pugFeed = Interactor.getNewsFeed();


    }

    private void addItems(boolean visit){


        LinearLayout scroll = (LinearLayout) findViewById(R.id.hm_scrollContent);
        ArrayList<LinearLayout> list = new ArrayList<>();
        ArrayList<Feed> newsFeed = Interactor.getNewsFeed();
        scroll.removeAllViews();

        for(int i = newsFeed.size() - 1; i >= 0; i--){
            ItemView newItem = new ItemView(getBaseContext(), null,newsFeed.get(i), newsFeed.get(i).getImageLink());
            if (visit)
                newItem.btnReport.setEnabled(false);

            list.add(newItem);
        }

        for(int i = 0; i < list.size();i++){

            scroll.addView(list.get(i));
        }
    }
    public void refresh(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("currentuser", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("username", null) != null){
            addItems(false);
        }else{
            addItems(true);
        }

    }
    public void logoutAlert(View view){
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Logout")
                .setMessage("Sei sicuro di voler eseguire il logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        startActivity(new Intent(getApplicationContext(), LoadActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void postActivity(){
        startActivity(new Intent(this, PostActivity.class));
    }

    public void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("currentuser", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", null);
        editor.putString("password", null);
        editor.putString("city", null);
        editor.putString("image", null);
        editor.putBoolean("post", false);
        editor.commit();
    }
}
