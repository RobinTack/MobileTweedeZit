package be.pxl.mobiledevelopmentproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl
{
    public String ReadTheURL(String placeURL) throws IOException{
        String Data = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(placeURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line = "";

            while( (line = bufferedReader.readLine()) != null ){
                stringBuilder.append(line);
            }

            Data = stringBuilder.toString();
            bufferedReader.close();


        } catch (MalformedURLException e) {
            System.out.println("----------------------------------------------MALFORMED URL EXECPTION-------------------------------------------------");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("----------------------------------------------IO EXECPTION-------------------------------------------------");
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }

        return Data;
    }
}
