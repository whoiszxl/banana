package com.whoiszxl.cqrs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author whoiszxl
 */
@Data
@AllArgsConstructor
public class InstanceGenerateDTO {

    private List<String> instanceIdList;

    private String requestId;
}
