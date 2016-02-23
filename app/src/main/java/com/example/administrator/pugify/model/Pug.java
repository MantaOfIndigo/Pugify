package com.example.administrator.pugify.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;

import com.example.administrator.pugify.tools.idMaker;

import java.io.Serializable;

/**
 * Created by Administrator on 24/11/2015.
 */
public class Pug implements Serializable{

    private String id;
    private String name;
    private String city;
    private String password;
    private Bitmap profilePicture;
    private Owner owner;
    private int rate;
    private boolean valid;


    public String getId(){
        return this.id;
    }
    public String getTitle(){
        return this.name;
    }
    public String getDescription(){
        return this.city;
    }
    public Bitmap getPicture() { return this.profilePicture; }
    public String getPassword() { return this.password; }

    public Bitmap getProfilePicture(){
        return this.profilePicture;
    }
    public int getProfilePictureByte(){
        return 0;
        //return this.profilePicture;
    }
    public Owner getOwner(){
        return this.owner;
    }
    public int getRate(){
        return this.rate;
    }
    public void setRate(int value){
        this.rate = value;
    }
    public void addRate(){
        this.rate++;
    }
    public void setDescription(String value){
        this.city = value;
    }
    public void setProfilePicturePath(Bitmap value){
        this.profilePicture = value;
      /*  ByteArrayOutputStream stream = new ByteArrayOutputStream();
        value.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.profilePicture = stream.toByteArray();
    */
    }
    public Boolean isValid(){
        return this.valid;
    }

    public Pug(Parcel in){
        String[] data = new String[1];
        in.readStringArray(data);
        this.name = data[0];
    }

    public Pug(){
        super();
        if(name.isEmpty())
            return;
        this.name = name;
        this.city = city;
        this.profilePicture = profilePicture;
        this.owner = new Owner("","","");
        this.rate = 0;
        this.id = new idMaker().createNewIdentifier();
    }
    public Pug(String name, String description, Bitmap profilePicturePath, Owner owner, String password){
        super();
        this.city = ifNull(description);
        this.password = password;
        this.profilePicture = profilePicturePath;
        this.owner = owner;
        this.rate = 0;
        this.id = new idMaker().createNewIdentifier();
        this.valid = true;

        if(name.isEmpty())
            this.valid = false;

        this.name = ifNull(name);

    }

    public Pug(String id, String name, String description, Bitmap profilePicturePath, Owner owner){
        super();
        this.city = ifNull(description);

        this.profilePicture = profilePicturePath;
        this.owner = owner;
        this.rate = 0;
        this.id = id;
        this.valid = true;

        if(name.isEmpty())
            this.valid = false;

        this.name = ifNull(name);

    }

    public String ifNull(String value){
        if(value == null)
            return "";

        return value;
    }
    @Override
    public String toString(){
        return "[id=" + this.id + ", title=" + this.name + ", description=" +
                this.city + ", profilePicture=" + this.profilePicture +
                ", owner=" + this.owner + ", rate=" + this.rate + "]";
    }

    private Bitmap decodeFile(String path,int width){
        try{
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opt);

            final int REQUIRED_SIZE = (Integer) width;

            int scale = 1;
            while (opt.outWidth/ scale / 2 >= REQUIRED_SIZE && opt.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            BitmapFactory.Options opt2 = new BitmapFactory.Options();
            opt2.inSampleSize = scale;
            return  BitmapFactory.decodeFile(path, opt2);
        } catch (Throwable e){
            e.printStackTrace();
        }

        return null;
    }

}
