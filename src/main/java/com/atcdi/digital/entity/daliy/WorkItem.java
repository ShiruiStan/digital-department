package com.atcdi.digital.entity.daliy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class WorkItem {
    @JsonIgnore
    int itemId;
    String itemName;
    String itemClass;

}
