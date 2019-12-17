package com.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateData {

    public UpdateData(String data) throws IOException {
        String Url = "http://your_ip_here/update";

        //post data to server
        URL url = new URL(Url);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoOutput(true);
        urlConn.setDoInput(true);
        urlConn.setRequestMethod("POST");
        urlConn.connect();
        DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
        dos.writeBytes(data);
        dos.flush();
        dos.close();

        //print headers
        String cookieVal  = urlConn.getHeaderField(0);
        for(int i = 1;cookieVal != null; i++){
            System.out.println(i + ":" + cookieVal);
            cookieVal = urlConn.getHeaderField(i);
        }

        //print HttpResponse
        InputStream in = urlConn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine())!=null)sb.append(line);
        System.out.println(sb.toString());
    }
}
