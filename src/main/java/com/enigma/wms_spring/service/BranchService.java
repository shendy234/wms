package com.enigma.wms_spring.service;

import com.enigma.wms_spring.dto.request.BranchRequest;
import com.enigma.wms_spring.dto.respon.BranchRespon;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BranchService {

    BranchRespon createBranch(BranchRequest branchRequest);
    BranchRespon updateBranch(BranchRequest branchRequest);
    BranchRespon getByIdBranch(String id);
    List<BranchRespon> getAllBranch();
    void deleteBranch(String id);

}
