package com.example.administrator.pugify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.pugify.item.Interactor;
import com.squareup.picasso.Picasso;

/**
 * Created by andreamantani on 29/01/16.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("currentuser", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("username", null);



        if(currentUser != null){
            TextView name = (TextView) findViewById(R.id.profile_name);
            TextView city = (TextView) findViewById(R.id.profile_city);
            ImageView img = (ImageView) findViewById(R.id.profile_image);
            name.setText(currentUser);
            city.setText( sharedPreferences.getString("city",null));
            WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;

            Picasso.with(getApplicationContext()).load(sharedPreferences.getString("image", null)).resize(width,width).into(img);
        }

    }

    public void goToHome(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("visit", false);
        startActivity(intent);
    }
    public void logout(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("currentuser", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", null);
        editor.putString("password", null);
        editor.putString("city", null);
        editor.putString("image", null);
        editor.putBoolean("post", false);
        editor.commit();

        startActivity(new Intent(this, LoadActivity.class));
    }
}
