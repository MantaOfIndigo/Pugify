package com.example.administrator.pugify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.example.administrator.pugify.item.Interactor;
import com.example.administrator.pugify.model.Owner;
import com.example.administrator.pugify.model.Pug;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 01/12/2015.
 */
public class SignInActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private float lastX;
    private ToggleButton pugButton;
    EditText edName;
    EditText edSurname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        edName = (EditText) findViewById(R.id.sg_txtName);
        edSurname = (EditText) findViewById(R.id.sg_txtSurname);
        Button btnCapture = (Button) findViewById(R.id.sg_btn_capture);

        btnCapture.setOnClickListener(new View.OnClickListener() {
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


        getSupportActionBar().setTitle("Registrazione");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB252));
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(!checkCredentials())
                    return false;

                float currentX = touchEvent.getX();

                if (lastX < currentX) {
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

                    viewFlipper.showNext();

                }

                if (lastX > currentX) {
                    if (viewFlipper.getDisplayedChild() == 1) {
                        Intent intent = new Intent(this, ProfileActivity.class);


                        pugButton = (ToggleButton) findViewById(R.id.sg_toggle);

                        if(!pugButton.isChecked()){
                           /* Toast toast2 = Toast.makeText(this, "non attivo!", Toast.LENGTH_SHORT);
                            toast2.show();

                            intent.putExtra("visit", true);*/
                        }else{

                            intent.putExtra("visit", false);
                        }

                        if(imageToDB != null) {
                            if(!uploadUser()){
                                String errorMessage = "upload error";
                                Toast toast3 = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                                toast3.show();
                                return false;
                            }
                            EditText name = (EditText) findViewById(R.id.sg_pugName);
                            EditText city = (EditText) findViewById(R.id.sg_pugCity);
                            EditText password = (EditText) findViewById(R.id.sg_pugPassword);

                            SharedPreferences sharedPref = getSharedPreferences("currentuser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("image", Interactor.logIn(name.getText().toString(), city.getText().toString(), password.getText().toString()));
                            editor.commit();

                          }
                        startActivity(intent);
                        break;
                    }

                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

                    viewFlipper.showPrevious();

                    if(viewFlipper.getDisplayedChild() == 2){
                        pugButton = (ToggleButton) findViewById(R.id.sg_toggle);
                        if(!pugButton.isChecked()){
                            startActivity(new Intent(this, HomeActivity.class));
                        }
                    }
                }
                break;
        }

        return false;
    }


    private boolean checkCredentials() {
        AlertDialog alert = new AlertDialog.Builder(SignInActivity.this).create();
        alert.setTitle(R.string.alertTitle);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        switch (viewFlipper.getDisplayedChild()) {
            case 0:

                if (edName.getText().toString().isEmpty() || edSurname.getText().toString().isEmpty()) {
                    alert.setMessage("Inserisci delle credenziali valide");
                    alert.show();
                    return false;
                }
                break;
            case 1:
                pugButton = (ToggleButton) findViewById(R.id.sg_toggle);
                //Interactor.visitorRequest(edName.getText().toString() + edSurname.getText().toString(), false);
                break;
        }
        return true;
    }

    private static final int PICK_FROM_CAMERA = 1 ;
    private static final int PIC_CROP = 2;

    File cameraPhotoFile;
    String filePath;
    Uri picUri;

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
    Bitmap imageToDB;

    private boolean uploadUser(){

        EditText pugParameters = (EditText) findViewById(R.id.sg_pugName);
        String pugName = pugParameters.getText().toString();
        pugParameters = (EditText) findViewById(R.id.sg_pugCity);
        String pugCity = pugParameters.getText().toString();


        EditText name = (EditText) findViewById(R.id.sg_pugName);
        EditText city = (EditText) findViewById(R.id.sg_pugCity);
        EditText password = (EditText) findViewById(R.id.sg_pugPassword);

        EditText ownerParameters = (EditText) findViewById(R.id.sg_txtName);
        String ownerName = ownerParameters.getText().toString();
        ownerParameters = (EditText) findViewById(R.id.sg_txtSurname);
        String ownerSurname = ownerParameters.getText().toString();

        Owner owner = new Owner(ownerName, ownerSurname, "emaildiprova");
        Pug pug = new Pug(name.getText().toString(), city.getText().toString(), imageToDB, owner, password.getText().toString());

        SharedPreferences sharedPref = getSharedPreferences("currentuser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", name.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putString("city", city.getText().toString());
        editor.putBoolean("post", true);
        editor.commit();


        return Interactor.uploadNewPug(pug);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("stamp", ""+requestCode);

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
                ImageView c = (ImageView) findViewById(R.id.sg_create_pug_image);
                c.setImageBitmap(picture);
                imageToDB = picture;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //filePath =  cameraPhotoFile.getAbsolutePath();
    }

}
