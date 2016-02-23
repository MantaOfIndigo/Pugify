package com.example.administrator.pugify.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.administrator.pugify.item.Interactor;
import com.example.administrator.pugify.tools.idMaker;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by Administrator on 24/11/2015.
 */
public class Owner implements Serializable {

    private String id;
    private String name;
    private String surname;
    private String email;

    public String getId(){
        return this.id;
    }
    public String getName(){
        if(this.name.isEmpty())
            return "noname";
        return this.name;
    }
    public String getSurname(){
        return this.surname;
    }
    public String getEmail(){
        return this.email;
    }


    public Owner(String name, String surname, String email){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.id = name + surname + new idMaker().createNewIdentifier();
    }

    public Owner getOwnerFromId(String id){
        return Interactor.retrieveOwnerFromId(id);
    }

    @Override
    public String toString(){
        return "Owner [id=" + this.id + ", name=" + this.getName() +
                ", surname=" + this.getSurname() + ", email=" + getSurname() +
                "]";
    }

}
