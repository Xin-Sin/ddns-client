package top.xinsin.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.*;
import com.aliyun.teaopenapi.models.Config;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.xinsin.entity.Record;
import top.xinsin.entity.RecordUpdate;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class InitClient {
    private int num;
    @SneakyThrows
    public void run(Client client,String DomainName){
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("DomainName",DomainName);
        DescribeDomainRecordsRequest build = DescribeDomainRecordsRequest.build(objectObjectHashMap);
        DescribeDomainRecordsResponse describeDomainRecordsResponse = client.describeDomainRecords(build);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(describeDomainRecordsResponse.body));
        List<Record> records = JSON.parseArray(jsonObject.getJSONObject("domainRecords").getJSONArray("record").toJSONString(), Record.class);
        for (Record r : records) {
            if ("thisMark".equals(r.getRemark())) {
                // TODO: 6/19/22 此处写替换逻辑
                Record recordInfo = getRecordInfo(client, r.getRecordId());
                String localIp = IP.getLocalIp();
                if (localIp == null) {
                    log.error("未查询到本机ip!!");
                } else {
                    if (!localIp.equals(recordInfo.getValue())) {
                        log.info("正在更换解析ip中!欲更新为{}", localIp);
                        if (updateRecord(client, new RecordUpdate(recordInfo.getRecordId(), recordInfo.getRR(), recordInfo.getType(), localIp, 600L))) {
                            num++;
                            log.info("更换成功,当前更换次数为{}", num);
                        } else {
                            log.error("更换失败,请联系xinxin进行抢修(还是别联系了!!!!!!!!)");
                        }
                    } else {
                        log.warn("检测ip没有更换,跳过!");
                    }
                }
            }
        }
    }

    private boolean updateRecord(Client client, RecordUpdate recordUpdate){
        UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
        updateDomainRecordRequest.setRecordId(recordUpdate.getRecordId())
                .setRR(recordUpdate.getRR())
                .setValue(recordUpdate.getValue())
                .setTTL(recordUpdate.getTTl())
                .setType(recordUpdate.getType());
        try {
            client.updateDomainRecord(updateDomainRecordRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SneakyThrows
    private Record getRecordInfo(Client client, String recordId){
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("RecordId",recordId);
        DescribeDomainRecordInfoRequest build = DescribeDomainRecordInfoRequest.build(stringStringHashMap);
        DescribeDomainRecordInfoResponse describeDomainRecordInfoResponse = client.describeDomainRecordInfo(build);
        return JSON.parseObject(JSON.toJSONString(describeDomainRecordInfoResponse.body), Record.class);
    }

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return CreateClient
     * @throws Exception
     */
    public Client createClient(String accessKeyId, String accessKeySecret,String endpoint) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = endpoint;
        return new Client(config);
    }
}
