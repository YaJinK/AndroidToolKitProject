package com.magata.net.xxtea;
import java.io.UnsupportedEncodingException;
import android.util.Base64;
import android.util.Log;

public class XXTEA {
    public static final String EncryptKey = "LE3";

    private static final int delta = 0x9E3779B9;

    private static int MX(int sum, int y, int z, int p, int e, int[] k) {
        return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
    }

    public static byte[] Encrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return ToByteArray(Encrypt(ToUInt32Array(data, true), ToUInt32Array(FixKey(key), false)), false);
    }

    public static byte[] Encrypt(String data, byte[] key) {
        try {
            return Encrypt(data.getBytes("UTF-8"), key);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] Encrypt(byte[] data, String key) {
        try {
            return Encrypt(data, key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String EncryptToBase64String(byte[] data, byte[] key) {
        return Base64.encodeToString(Encrypt(data, key), Base64.DEFAULT);
    }

    public static String EncryptToBase64String(String data, byte[] key) {
        return Base64.encodeToString(Encrypt(data, key), Base64.DEFAULT);
    }

    public static String EncryptToBase64String(byte[] data, String key) {
        return Base64.encodeToString(Encrypt(data, key), Base64.DEFAULT);
    }

    public static String EncryptToBase64String(String data, String key) {
        return Base64.encodeToString(Encrypt(data, key), Base64.DEFAULT);
    }

    public static byte[] Decrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return ToByteArray(Decrypt(ToUInt32Array(data, false), ToUInt32Array(FixKey(key), false)), true);
    }

    public static byte[] Decrypt(byte[] data, String key) {
        try {
            return Decrypt(data, key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] DecryptBase64String(String data, byte[] key) {
        return Decrypt(Base64.decode(data, Base64.DEFAULT), key);
    }


    public static byte[] DecryptBase64String(String data, String key) {
        return Decrypt(Base64.decode(data, Base64.DEFAULT), key);
    }

    public static String DecryptToString(byte[] data, byte[] key) {
        try {
            return new String(Decrypt(data, key), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String DecryptToString(byte[] data, String key) {
        try {
            return new String(Decrypt(data, key), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String DecryptBase64StringToString(String data, byte[] key) {
        try {
            return new String(DecryptBase64String(data, key), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String DecryptBase64StringToString(String data, String key) {
        try {
            return new String(DecryptBase64String(data, key), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] Encrypt(String data, String key) {
        try {
            return Encrypt(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static int[] Encrypt(int[] v, int[] k) {
        int n = v.length - 1;
        if (n < 1) {
            return v;
        }
        int z = v[n], y, sum = 0, e;
        int p, q = 6 + 52 / (n + 1);
        while (0 < q--) {
            sum += delta;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = v[p + 1];
                z = v[p] += MX(sum, y, z, p, e, k);
            }
            y = v[0];
            z = v[n] += MX(sum, y, z, p, e, k);
        }
        return v;
    }

    private static int[] Decrypt(int[] v, int[] k) {
        int n = v.length - 1;
        if (n < 1) {
            return v;
        }
        int z, y = v[0], sum, e;
        int p, q = 6 + 52 / (n + 1);
        sum = (int)(q * delta);
        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = v[p - 1];
                y = v[p] -= MX(sum, y, z, p, e, k);
            }
            z = v[n];
            y = v[0] -= MX(sum, y, z, p, e, k);
            sum -= delta;
        }
        return v;
    }

    private static byte[] FixKey(byte[] key) {
        if (key.length == 16) return key;
        byte[] fixedkey = new byte[16];
        if (key.length < 16) {
            System.arraycopy(key, 0, fixedkey, 0, key.length);
        }
        else {
            System.arraycopy(key, 0, fixedkey, 0, 16);
        }

        return fixedkey;
    }

    private static int[] ToUInt32Array(byte[] data, boolean includeLength) {
        int length = data.length;
        int n = (((length & 3) == 0) ? (length >>> 2) : ((length >>> 2) + 1));
        int[] result;
        if (includeLength) {
            result = new int[n + 1];
            result[n] = length;
        }
        else {
            result = new int[n];
        }
        for (int i = 0; i < length; i++) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }

        return result;
    }

    private static byte[] ToByteArray(int[] data, Boolean includeLength) {
        int n = data.length << 2;
        String s = "";
        if (includeLength) {
            int m = data[data.length - 1];
            n -= 4;
            if ((m < n - 3) || (m > n)) {
                return null;
            }
            n = m;
            s = s + m;
        }

        byte[] result = new byte[n];
        for (int i = 0; i < n; i++) {
            result[i] = (byte)(data[i >>> 2] >>> ((i & 3) << 3));
        }
        return result;
    }
}
