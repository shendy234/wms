package com.enigma.wms_spring.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommonRespon<T> {
    private Integer statusCode;
    private String message;
    private T data;
    private PagingRespon paging;
}
