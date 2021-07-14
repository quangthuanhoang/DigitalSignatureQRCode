package vn.edu.hust.soict.thuanhq.digitalsignatureqrcode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

final public class SHA256 {
    public byte[] hash(byte[] msgBytes) {
        String base = new String(msgBytes, StandardCharsets.UTF_8);
        byte[] tmp = null;
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            String hexString = "";

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString += "0";
                hexString += hex;
            }

            System.out.println(hexString.toString());
            tmp = hexString.getBytes();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return tmp;
    }
}