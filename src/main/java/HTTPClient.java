import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPClient {
    public static String doGetWithRetry(String httpUrl, Map<String, String> params) throws Exception {
        int retryTimes = 3;
        for(int i = 0; i < retryTimes; i++) {
            try {
                return doGet(httpUrl, params);
            } catch (Exception e) {
                System.out.println("retry for the " + (i+1) + " time");
            }
        }
        throw new Exception("retry failed");
    }
    public static String doGet(String httpUrl, Map<String, String> params) throws IOException {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuffer result = new StringBuffer();
        try {
            List<String> paramsList = new ArrayList<>();

            if(params != null && params.size() != 0) {
                params.forEach((key,val) -> paramsList.add(String.format("%s=%s", key, val)));
                httpUrl += "?" + String.join("&", paramsList);
            }
//            System.out.println(httpUrl);
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(1500000);
            connection.connect();
            if(connection.getResponseCode()==200) {
                is = connection.getInputStream();
                if(null != is){
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }
            }
        }catch (IOException e) {
//            e.printStackTrace();
            throw e;
        }finally {
            if(null != br) {
                try {
                    br.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null != is) {
                try {
                    is.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();
        }


        return result.toString();
    }
}
