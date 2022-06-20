package top.xinsin;

import com.aliyun.alidns20150109.Client;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.util.InitClient;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

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
        InitClient initClient = new InitClient();
        Client client = initClient.createClient(accessKeyId, accessKeySecret, endpoint);
        while (true) {
            try {
                log.info("正在执行中更改ip接口!!");
                initClient.run(client, domainName);
                Thread.sleep(1000*60);
            }catch (InterruptedException e){
                //
                break;
            }catch (Exception e) {
                e.printStackTrace();
                log.error("执行更改ip接口出现未知错误!");
            }
        }
    }
}