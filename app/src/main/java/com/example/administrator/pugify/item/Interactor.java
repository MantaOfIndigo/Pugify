package com.example.administrator.pugify.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.pugify.R;
import com.example.administrator.pugify.exception.MultipleParseObjectFoundException;
import com.example.administrator.pugify.model.Feed;
import com.example.administrator.pugify.model.Owner;
import com.example.administrator.pugify.model.Pug;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 03/12/2015.
 */

public class Interactor extends AppCompatActivity{

    public static Object visitorRequest(String username, Boolean get){
        if(get){
            if (userVisitExist(username)){
                try {
                    return getVisitor(username);
                }catch (MultipleParseObjectFoundException e){
                    return null;
                }
            }
            else {
                return null;
            }
        }else{
            if(userVisitExist(username)){
                return false;
            }else {
                uploadNewVisitor(username);
            }
        }

        return false;
    }
    private static void uploadNewVisitor(String user){
        ParseObject newVisit = new ParseObject("Visit");
        newVisit.put("username", user);
        newVisit.saveInBackground();

    }

    private static ParseObject getVisitor(String username) throws MultipleParseObjectFoundException{
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Visit");
        query.whereEqualTo("username", username);
        try {
            ArrayList<ParseObject> result = (ArrayList<ParseObject>) query.find();
            if(result.size() == 1){
                return result.get(0);
            }else throw new MultipleParseObjectFoundException();
        } catch (ParseException e) {
            return null;
        }
    }

    private static Boolean userVisitExist(String username){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Visit");
        query.whereEqualTo("username", username);
        try {
            List<ParseObject> result = query.find();
        } catch (ParseException e) {
            return false;
        }

       return true;
    }
    public static ArrayList<Feed> getNewsFeed() {
        final ArrayList<Feed> feedList = new ArrayList<>();
        final Bitmap[] bmp = new Bitmap[1];
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
        List<ParseObject> list = null;
        try {
            list = query.find();
            for (int i = 0; i < list.size(); i++){

                ParseObject c = list.get(i);

                ParseFile f = c.getParseFile("image");
                feedList.add(new Feed((String) c.getObjectId(), c.getString("pugId"), c.getString("pugName"), f.getUrl(), c.getString("description"), c.getInt("rate"), c.getBoolean("reported")));
                }
            } catch (ParseException e) {
            e.printStackTrace();
        }

        return feedList;
        //ParseFile image = (ParseFile) pugs.get("image");
        //Log.i("stamp", "" + image.getUrl());

    }
    public static void liked(String objectId){

        Log.v("stamp", "id-" + objectId);


        ParseQuery query =  ParseQuery.getQuery("Feed");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    int count = (int) object.get("rate");

                    object.put("rate", count + 1);
                    object.saveInBackground();
                }
            }
        });

    }
    public static Owner retrieveOwnerFromId(String id){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Owner");
        query.whereEqualTo("ownerId", id);
        try {
            List<ParseObject> result = query.find();

            if (result.size() > 1)
                return null;

            return new Owner((String) result.get(0).get("name"),(String) result.get(0).get("surname"),(String) result.get(0).get("email"));

        } catch (ParseException e) {
            return null;
        }
    }

    public static void uploadNewOwner(Owner owner){
        ParseObject newOwner = new ParseObject("Owner");
        newOwner.put("ownerId", owner.getId());
        newOwner.put("name", owner.getName());
        newOwner.put("surname", owner.getSurname());
        newOwner.put("email", owner.getEmail());
        newOwner.put("username", owner.getName()+owner.getSurname());
        newOwner.saveInBackground();
    }

    public static void uploadNewFeed(Feed feed, Bitmap feedImage){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bit = feedImage;
            bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            ParseFile file = new ParseFile("FEED" + feed.getPugId() + new Date().getTime() + ".png", image);
            file.saveInBackground();

            ParseObject newFeed = new ParseObject("Feed");
            newFeed.put("feedId", "feed" + feed.getPugId() + new Date().getTime());
            newFeed.put("pugId", feed.getPugId());
            newFeed.put("pugName", feed.getPugName());
            newFeed.put("description", feed.getDescription());
            newFeed.put("rate", 0);
            newFeed.put("image", file);
            newFeed.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static boolean uploadNewPug(Pug pug){
       try {
           ByteArrayOutputStream stream = new ByteArrayOutputStream();
           Bitmap bit = pug.getPicture();
           bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
           byte[] image = stream.toByteArray();
           ParseFile file = new ParseFile("PROFILE" + new Date().getTime() + ".png", image);
           file.saveInBackground();

           Interactor.uploadNewOwner(pug.getOwner());

           ParseObject newPug = new ParseObject("Pugs");
           newPug.put("pugId", pug.getId());
           newPug.put("name", pug.getTitle());
           newPug.put("city", pug.getDescription());
           newPug.put("image", file);
           newPug.put("ownerId", pug.getOwner().getId());
           newPug.put("rate", 0);
           newPug.put("password", pug.getPassword());
           newPug.save();
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }

        return true;
    }

    public static String logIn(String username, String city, String password){

        ParseQuery<ParseObject> logQuery = ParseQuery.getQuery("Pugs");

        logQuery.whereEqualTo("name", username);

        try{
            List<ParseObject> result = logQuery.find();

            for(int i = 0; i < result.size();i++){

                String pass = result.get(i).getString("password");
                String cit = result.get(i).getString("city");

                if(password.compareTo(pass) == 0 && city.compareTo(cit) == 0){
                    ParseFile url = result.get(i).getParseFile("image");
                    return url.getUrl();

                }
            }

        } catch (ParseException e){
            return null;
        }

        return null;
    }

    public static void sendReport(Feed feed){
        ParseObject report = new ParseObject("Report");
        report.put("feedId", feed.getObjectId());
        report.put("feedAuthor", feed.getPugId());
        report.put("feedAuthorName", feed.getPugName());
        report.put("feedimage", feed.getImageLink());
        report.saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Feed");
        query.getInBackground(feed.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null){
                    parseObject.put("reported", true);
                    parseObject.saveInBackground();
                }
            }
        });
    }

}

