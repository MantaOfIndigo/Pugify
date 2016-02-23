package com.example.administrator.pugify.tools;

import android.content.Intent;
import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 24/11/2015.
 */
public class idMaker {

    //mantiene List<String> presa dal database online
    //offre funzioni per:
    // - creazione di un nuovo id univoco

    private List<String> idList;

    public idMaker(){
        super();
        idList = new ArrayList<String>();
        idList.add("123456789e2");
        idList.add("fc3456789f2");
        idList.add("123cf678d92");
        idList.add("1234a789ab2");
    }
    public String createNewIdentifier(){
        String newIdentifier= "";
        Boolean cicle = false;

       while (!cicle) {
          Long number = new Date().getTime();
          Random rnd = new Random();
          newIdentifier = String.valueOf((number * (rnd.nextInt(19) + 1)));
          newIdentifier = Long.toHexString(number).toString();
           Log.v("id", newIdentifier);
          newIdentifier = permutation(newIdentifier);
          Log.v("id", newIdentifier);
          cicle = checkNewIdentifier(newIdentifier);
       }
        idList.add(newIdentifier);

        return newIdentifier;
    }


    private Boolean checkNewIdentifier(String value){
       if(value.length() < 11)
           return false;

       if(value.length() > 11){
            value = value.substring(0,11);
       }

       for (int i = 0; i < this.idList.size(); i++) {
           if(value == this.idList.get(i))
               return false;
       }
       return true;
    }

    private String permutation(String value){

        String returnString = "";
        Random rnd = new Random();
        int cutter;
        for (int i = 0; i < value.length();){
            cutter = rnd.nextInt(value.length());
            returnString += value.substring(cutter, cutter + 1);
            value = value.substring(0,cutter) + value.substring(cutter + 1,value.length());
        }

        return returnString;
    }

}
