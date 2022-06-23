package top.xinsin.timer;

import com.aliyun.alidns20150109.Client;
import lombok.AllArgsConstructor;
import top.xinsin.util.InitClient;

import java.util.TimerTask;

@AllArgsConstructor
public class DDNSTimer extends TimerTask {
    private Client client;
    private String domainName;
    private String mark;
    private final InitClient initClient = new InitClient();

    @Override
    public void run() {
        this.initClient.run(client, domainName,mark);
    }
}
