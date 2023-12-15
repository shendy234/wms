package com.enigma.wms_spring.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PagingRespon {
    private Integer currentPage;
    private Integer totalPage;
    private Integer size;
}