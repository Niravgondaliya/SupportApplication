package com.support.me.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.stereotype.Service;

@Service
public class GetPostRequestService {

    private final String USER_AGENT = "Mozilla/5.0";

    public String getResponse(String urlList, String type, Map<String,String> headers) {
        List<String> st = Arrays.asList(urlList.split("\n"));
        System.out.println(st);
        StringBuffer response = new StringBuffer();
        // http.sendPost();
        st.stream().forEach(item -> {
            try {
                if ("get".equals(type)) {
                    response.append(sendGet(item, headers));
                }
                else if ("post".equals(type)) {
                	 String[] arr = item.split("\t");
                	 if(arr.length>1) {
                         response.append(sendPostWithBody(arr[0], arr[1], headers));
                	 }
                	 else{

                         response.append(sendPost(item, headers));
                	 }
                }
                response.append("\n\n");
            }
            catch (Exception e) {
                // response.append("\n-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_\n");
                response.append("\n******************************************************************************************************************************************************************\n");
                response.append(e.getMessage());
                response.append("\n\n******************************************************************************************************************************************************************\n\n");
                e.printStackTrace();
            }
        });
        return response.toString();
    }

    private StringBuffer sendGet(String url,  Map<String,String> headers) throws Exception {

        StringBuffer response = new StringBuffer();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        // add request header
        headers.forEach((key,value) ->{
            con.setRequestProperty(key,value);
        });
        con.setRequestProperty("User-Agent", USER_AGENT);


        int responseCode = con.getResponseCode();
        response.append("\nSending 'GET' request to URL : " + url);
        response.append("\nResponse Code : " + responseCode + "\n");
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer payload = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            payload.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(payload.toString());
        response.append(payload);

        return response;

    }

    // HTTP POST request
    private StringBuffer sendPost(String url, Map<String,String> headers) throws Exception {
        StringBuffer response = new StringBuffer();
        // String url =
        // "URL";
        // URL obj = new URL(url);
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // add reuqest header
        headers.forEach((key,value) ->{
            con.setRequestProperty(key,value);
        });
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        response.append("\nSending 'POST' request to URL : " + url);
        response.append("\nResponse Code : " + responseCode + "\n");

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer payload = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            payload.append(inputLine);
        }
        in.close();

        System.out.println(payload.toString());
        response.append(payload);
        return response;

    }
    
    private StringBuffer sendPostWithBody(String url, String jsonRequest, Map<String,String> headers) throws Exception {
//    	jsonRequest="jsonBody";

        disableSSLCertificateChecking();
    	StringBuffer response = new StringBuffer();
        // String url =
        // "URL";
        // URL obj = new URL(url);
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        // add reuqest header
        headers.forEach((key,value) ->{
            con.setRequestProperty(key,value);
        });
        
        con.setRequestMethod("POST");
//        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json");
        // Send post request
        con.setDoOutput(true);
        
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(jsonRequest.getBytes());
        wr.flush();
        wr.close();
        response.append("\n"+jsonRequest);
        int responseCode = con.getResponseCode();
        response.append("\nSending 'POST' request to URL : " + url);
        response.append("\nResponse Code : " + responseCode + "\n");

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer payload = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            payload.append(inputLine);
        }
        in.close();

        System.out.println(payload.toString());
        response.append(payload);
        return response;

    }
    
    
    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
