package top.xinsin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordUpdate {
    private String RecordId;
    private String RR;
    private String Type;
    private String Value;
    private Long TTl;
}
