package cn.com.bluemoon.cardocr.lib.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.com.bluemoon.cardocr.lib.sign.Base64Util;
import cn.com.bluemoon.cardocr.lib.sign.YoutuSign;


/**
 * @author qingliang
 *         <p>
 *         人脸核身相关接口调用
 */
public class YTServerAPI {
    public final static String API_URL = "https://api.youtu.qq.com/youtu/ocrapi/";

    private static final String LOG_TAG = YTServerAPI.class.getName();
    private static int EXPIRED_SECONDS = 2592000;
    private String m_appid;
    private String m_secret_id;
    private String m_secret_key;
    private String m_user_id;
    private String m_end_point;

    private OnRequestListener mListener;

    public YTServerAPI(Context context) {
        try {
            Bundle metaData = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA).metaData;
            m_appid = String.valueOf(metaData.getInt("OCR_APP_KEY"));
            m_secret_id = metaData.getString("OCR_SECRET_ID");
            m_secret_key = metaData.getString("OCR_SECRET_KEY");
            m_user_id = String.valueOf(metaData.getInt("OCR_QQ_NUMBER"));
            m_end_point = API_URL;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public interface OnRequestListener {
        void onSuccess(int statusCode, String responseBody);

        void onFailure(int statusCode);
    }

    public void setRequestListener(OnRequestListener listener) {
        mListener = listener;
    }


    public void idCardOcr(String fileData, int cardType) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("image", fileData);
        data.put("app_id", m_appid);
        data.put("card_type", cardType); //0正面，1背面， 不传默认0
        sendHttpsRequest(data, "idcardocr");
    }

    public void bankCardOcr(String fileData) throws  JSONException {
        JSONObject data = new JSONObject();
        data.put("image", fileData);
        data.put("app_id", m_appid);
        sendHttpsRequest(data, "creditcardocr");
    }

    public void drivingLicenseOcr(String fileData,int type) throws  JSONException {
        JSONObject data = new JSONObject();
        data.put("image", fileData);
        data.put("app_id", m_appid);
        data.put("type", type);//识别类型，0表示行驶证识别，1表示驾驶证识别
        sendHttpsRequest(data, "driverlicenseocr");
    }

    /**
     * 保存bitmap到内存卡
     *
     * @param base64Data
     * @param filepath
     * @return
     */
    public static boolean savaBitmap(String base64Data, String filepath) {
        File f = new File(filepath);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        base64ToBitmap(base64Data).compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    private JSONObject sendHttpsRequest(JSONObject postData, String mothod) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());

            StringBuffer mySign = new StringBuffer("");
            YoutuSign.appSign(m_appid, m_secret_id, m_secret_key,
                    System.currentTimeMillis() / 1000 + EXPIRED_SECONDS,
                    m_user_id, mySign);

            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");

            URL url = new URL(m_end_point + mothod);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
            // set header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("user-agent", "youtu-android-sdk");
            connection.setRequestProperty("Authorization", mySign.toString());

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "text/json");
            connection.connect();

            // POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            postData.put("app_id", m_appid);
            out.write(postData.toString().getBytes("utf-8"));
            // 刷新、关闭
            out.flush();
            out.close();

            final int responseCode = connection.getResponseCode();
            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer resposeBuffer = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                resposeBuffer.append(lines);
            }
            // System.out.println(resposeBuffer+"\n");
            reader.close();
            // 断开连接
            connection.disconnect();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                if (mListener != null) {
                    mListener.onSuccess(responseCode, resposeBuffer.toString());
                }

            } else {
                if (mListener != null) {
                    mListener.onFailure(responseCode);
                }
            }
            JSONObject respose = new JSONObject(resposeBuffer.toString());
            return respose;
        } catch (FileNotFoundException e) {
            if (mListener != null) {
                mListener.onFailure(HttpsURLConnection.HTTP_NOT_FOUND); //没配置app_key
            }
        } catch (Exception e) {
            if (mListener != null) {
                mListener.onFailure(HttpsURLConnection.HTTP_BAD_GATEWAY); //断网
            }
        }
        return null;
    }


    private void GetBase64FromInputStream(InputStream is, StringBuffer base64) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line = "";
        StringBuffer data = new StringBuffer("");
        while ((line = in.readLine()) != null) {
            data.append(line);
        }
        base64.append(Base64Util.encode(data.toString().getBytes()));
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) throws IOException {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
        return result;
    }


    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


}