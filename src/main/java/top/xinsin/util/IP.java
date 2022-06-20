package top.xinsin.util;

import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IP {
    private static Pattern compile = Pattern.compile("^[a-zA-Z({:\"]+([\\d.]+)[\"})]{3}$");
    @SneakyThrows
    public static String getLocalIp(){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://www.taobao.com/help/getip.php");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        Matcher matcher = compile.matcher(result.toString());
        return matcher.find() ? matcher.group(1) : null;
    }
}
