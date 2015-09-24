package zhou.gank.io.util;

import java.security.MessageDigest;

/**
 * Created by zzhoujay on 2015/7/31 0031.
 * 加密相关工具类
 */
public class HashKit {

    private static java.security.SecureRandom random = new java.security.SecureRandom();

    public static String md5(String srcStr){
        return hash("MD5", srcStr);
    }

    public static String sha1(String srcStr){
        return hash("SHA-1", srcStr);
    }

    public static String sha256(String srcStr){
        return hash("SHA-256", srcStr);
    }

    public static String sha384(String srcStr){
        return hash("SHA-384", srcStr);
    }

    public static String sha512(String srcStr){
        return hash("SHA-512", srcStr);
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            StringBuilder result = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            for (byte b : bytes) {
                String hex = Integer.toHexString(b&0xFF);
                if (hex.length() == 1)
                    result.append("0");
                result.append(hex);
            }
            return result.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b&0xFF);
            if (hex.length() == 1)
                result.append("0");
            result.append(hex);
        }
        return result.toString();
    }

    /**
     * md5 128bit 16bytes
     * sha1 160bit 20bytes
     * sha256 256bit 32bytes
     * sha384 384bit 48bites
     * sha512 512bit 64bites
     */
    public static String generateSalt(int numberOfBytes) {
        byte[] salt = new byte[numberOfBytes];
        random.nextBytes(salt);
        return toHex(salt);
    }
}
