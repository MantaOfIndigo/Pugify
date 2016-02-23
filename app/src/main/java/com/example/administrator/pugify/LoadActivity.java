package com.example.administrator.pugify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.pugify.item.Interactor;

import static android.app.PendingIntent.getActivity;

/**
 * Created by Administrator on 24/11/2015.
 */
public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        /*InputStream stream = null;

        File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        final int readLimit = 16 * 1024;

        if(picturesDir != null){
            imageFile = new File(picturesDir, "ld_gif.gif");
        } else{
            Log.i("stamp", "non available");
        }

        if(imageFile != null){
            try {
                stream = new BufferedInputStream(new FileInputStream(new File(getCacheDir(), "ld_gif.gif" )), readLimit);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            stream.mark(readLimit);
        }else{
            Log.i("stamp", "gif image not available");
        }

        try {
            stream = getAssets().open("ld_gif.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        View C = findViewById(R.id.ld_gif);
        ViewGroup parent = (ViewGroup) C.getParent();
        int index = parent.indexOfChild(C);
        parent.removeView(C);
        C = new GifWebView(this, "file:///android_asset/ld_gif.gif");
        parent.addView(C, index);
        //GifWebView view = new GifWebView(this, "file:///android_asset    /ld_gif.gif");

*/
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_load);



        SharedPreferences sharedPreferences = getSharedPreferences("currentuser", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("username", null);

        if(currentUser != null){
            startActivity(new Intent(this, ProfileActivity.class));
        }


        Button btnLOG = (Button) findViewById(R.id.ld_login);
        Button btnSIGN = (Button) findViewById(R.id.ld_signin);

    }

    public void logIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
    public void signIn(View view){
        startActivity(new Intent(this, SignInActivity.class));
    }
    public void profile(View view) {
        /*String url = Interactor.logIn("Giorgio", "Torino", "12345");
        if(url != null){
            SharedPreferences sharedPref =  getSharedPreferences("currentuser",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", "Giorgio");
            editor.putString("password", "12345");
            editor.putString("city", "Torino");
            editor.putString("image", url);
            editor.commit();
        }*/
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
