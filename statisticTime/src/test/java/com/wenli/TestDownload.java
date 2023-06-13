package com.wenli;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.*;
import java.util.Base64;

/**
 * @description:
 * @date: 2023-06-13 4:20 p.m.
 * @author: lzh
 */
public class TestDownload {
    @Test
    public void testImage() throws Exception {
        String imageUrl = "https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=https://google.com&size=32";
        String destinationFile = "image.png";

        String proxyHost = "127.0.0.1";
        int proxyPort = 7890;

        URL url = new URL(imageUrl);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        InputStream inputStream = connection.getInputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        System.out.println(base64Image);


        inputStream.close();
        connection.disconnect();
    }
}
