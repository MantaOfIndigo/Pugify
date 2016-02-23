package com.example.administrator.pugify;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.pugify.item.Interactor;
import com.example.administrator.pugify.model.Feed;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 01/12/2015.
 */
public class PostActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PIC_CROP = 2;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("currentuser", Context.MODE_PRIVATE);


        getSupportActionBar().setTitle("Crea Post");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB252));

        setContentView(R.layout.activity_post);
        Log.v("stamp", "postin"+sharedPreferences.getBoolean("post", true));

        if (sharedPreferences.getString("username", null) == null ){
            Toast toast = Toast.makeText(getApplicationContext(), "Si è verificato un errore", Toast.LENGTH_LONG);
            toast.show();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", null);
            editor.putString("password", null);
            editor.putString("city", null);
            editor.putString("image", null);
            editor.putBoolean("post", false);
            editor.commit();
            startActivity(new Intent(getApplicationContext(), LoadActivity.class));
            return;
        }
        if (sharedPreferences.getString("username", null) != null && sharedPreferences.getBoolean("post", true) == true) {
            try {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("post", false);
                editor.commit();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
            } catch (ActivityNotFoundException anfe) {
                anfe.printStackTrace();
            }

        }

        Button btnImage = (Button) findViewById(R.id.btn_getImage);
        Button btnSend = (Button) findViewById(R.id.btn_sendToDb);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String currentUser = sharedPreferences.getString("username", null);

                EditText desc = (EditText) findViewById(R.id.post_desc);

                if (currentUser == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Si è verificato un errore", Toast.LENGTH_LONG);
                    toast.show();
                    editor.putString("username", null);
                    editor.putString("password", null);
                    editor.putString("city", null);
                    editor.putString("image", null);
                    editor.putBoolean("post", false);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), LoadActivity.class));
                    return;
                }

                Feed newFeed = new Feed(sharedPreferences.getString("username", null) + sharedPreferences.getString("password", null), currentUser, "", desc.getText().toString(), 0);
                if(imageToDB != null) {
                    Interactor.uploadNewFeed(newFeed, imageToDB);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    editor.putBoolean("post", true);
                    editor.commit();
                    startActivity(intent);
                }

                if(imageToDB == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Fallito", Toast.LENGTH_LONG);
                    toast.show();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException anfe){
                    anfe.printStackTrace();
                }
            }
        });
    }

    File cameraPhotoFile;
    String filePath;
    Uri picUri;
    Bitmap imageToDB;

    private void performCrop(){
        try{
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return_data", true);
            startActivityForResult(cropIntent, PIC_CROP);

        } catch (ActivityNotFoundException e){
            String errorMessage = "error";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("stamp", "" + requestCode);

        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == PICK_FROM_CAMERA) {
            picUri = data.getData();
            performCrop();
        }

        if (requestCode == PIC_CROP){
            Bundle extras = data.getExtras();
            Uri picPath = data.getData();
            try {
                Bitmap picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picPath);
                ImageView c = (ImageView) findViewById(R.id.imgFeed);
                c.setImageBitmap(picture);
                imageToDB = picture;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //filePath =  cameraPhotoFile.getAbsolutePath();
    }


}
