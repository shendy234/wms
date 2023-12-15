package com.enigma.wms_spring.controller;

import com.enigma.wms_spring.constant.AppPath;
import com.enigma.wms_spring.dto.request.BranchRequest;
import com.enigma.wms_spring.dto.respon.BranchRespon;
import com.enigma.wms_spring.dto.respon.CommonRespon;
import com.enigma.wms_spring.dto.respon.PagingRespon;
import com.enigma.wms_spring.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.BRANCH)
public class BranchController {
    private final BranchService branchService;

    @PostMapping
    public BranchRespon createBranch(@RequestBody BranchRequest branchRequest){
    return branchService.createBranch(branchRequest);
}

    @PutMapping
    public BranchRespon updateBranch(@RequestBody BranchRequest branchRequest){
        return branchService.updateBranch(branchRequest);
    }

    @GetMapping
    public List<BranchRespon> getAllBranch() {
        return branchService.getAllBranch();
    }

    @GetMapping("/{id}")
    public BranchRespon getByIdBranch(@PathVariable String id){
        return branchService.getByIdBranch(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBranch(@PathVariable String id){
        branchService.deleteBranch(id);
    }

    }
