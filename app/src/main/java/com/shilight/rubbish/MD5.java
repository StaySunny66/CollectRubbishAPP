package com.shilight.rubbish;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public String MD5(String PASS){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(PASS.getBytes());
            String md5Str  = new BigInteger(1, md.digest()).toString(16);
            if(md5Str.length()<32){
                md5Str = 0 + md5Str;
            }
            return md5Str;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error";

    }

}
