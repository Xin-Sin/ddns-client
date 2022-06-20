package top.xinsin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Record {
    private String status;
    private String RR;
    private String domainName;
    private int TTL;
    private String line;
    private boolean locked;
    private String type;
    private String requestId;
    private String value;
    private String recordId;
    private int weight;
    private String remark;
}
