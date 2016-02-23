package com.example.administrator.pugify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.pugify.item.Interactor;

/**
 * Created by andreamantani on 30/01/16.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void checkLogIn(View view){
        TextView name = (TextView) findViewById(R.id.login_name);
        TextView city = (TextView) findViewById(R.id.login_city);
        TextView password = (TextView) findViewById(R.id.login_password);

        if (!name.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !city.getText().toString().isEmpty()){
            String url = Interactor.logIn(name.getText().toString(), city.getText().toString(), password.getText().toString());
            if (url != null) {
                SharedPreferences sharedPref = getSharedPreferences("currentuser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", name.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.putString("city", city.getText().toString());
                editor.putString("image", url);
                editor.putBoolean("post", true);
                editor.commit();


                startActivity(new Intent(this, ProfileActivity.class));
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Credenziali errate!", Toast.LENGTH_LONG);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Credenziali er!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
