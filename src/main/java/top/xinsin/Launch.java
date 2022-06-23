package top.xinsin;

import com.aliyun.alidns20150109.Client;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.timer.DDNSTimer;
import top.xinsin.util.InitClient;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public final class Launch {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        File file = new File("Setting.properties");
        if (!file.exists()) {
            log.error("no file such");
            System.exit(0);
        }
        properties.load(new FileReader(file));
        String accessKeyId = properties.getProperty("accessKeyId");
        String accessKeySecret = properties.getProperty("accessKeySecret");
        String endpoint = properties.getProperty("endpoint");
        String domainName = properties.getProperty("DomainName");
        String mark = properties.getProperty("mark");
        InitClient initClient = new InitClient();
        Client client = initClient.createClient(accessKeyId, accessKeySecret, endpoint);
        TimerTask ddnsTimer = new DDNSTimer(client, domainName, mark);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(ddnsTimer,1000L,1000 * 60);

    }
}