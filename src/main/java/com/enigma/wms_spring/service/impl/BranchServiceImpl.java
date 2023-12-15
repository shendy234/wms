package com.enigma.wms_spring.service.impl;

import com.enigma.wms_spring.dto.request.BranchRequest;
import com.enigma.wms_spring.dto.respon.BranchRespon;
import com.enigma.wms_spring.entity.Branch;
import com.enigma.wms_spring.repository.BranchRepository;
import com.enigma.wms_spring.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    @Override
    public BranchRespon createBranch(BranchRequest branchRequest) {
        Branch branch = Branch.builder()
                .branchCode(branchRequest.getBranchCode())
                .branchName(branchRequest.getBranchName())
                .address(branchRequest.getAddress())
                .phoneNumber(branchRequest.getPhoneNumber())
                .build();
        branchRepository.save(branch);
        return BranchRespon.builder()
                .Id(branch.getId())
                .branchName(branch.getBranchName())
                .branchCode(branch.getBranchCode())
                .address(branch.getAddress())
                .phoneNumber(branch.getPhoneNumber())
                .build();
    }

    @Override
    public BranchRespon updateBranch(BranchRequest branchRequest) {
        Branch branch = Branch.builder()
                .id(branchRequest.getBranchId())
                .branchCode(branchRequest.getBranchCode())
                .branchName(branchRequest.getBranchName())
                .address(branchRequest.getAddress())
                .phoneNumber(branchRequest.getPhoneNumber())
                .build();
        branchRepository.save(branch);
        return BranchRespon.builder()
                .Id(branch.getId())
                .branchName(branch.getBranchName())
                .branchCode(branch.getBranchCode())
                .address(branch.getAddress())
                .phoneNumber(branch.getPhoneNumber())
                .build();
    }

    @Override
    public BranchRespon getByIdBranch(String id) {
        Branch branch = branchRepository.findById(id).orElse(null);
        if (branch != null) {
            return BranchRespon.builder()
                    .Id(branch.getId())
                    .branchName(branch.getBranchName())
                    .branchCode(branch.getBranchCode())
                    .address(branch.getAddress())
                    .phoneNumber(branch.getPhoneNumber())
                    .build();
        }
        return null;
    }

    @Override
    public List<BranchRespon> getAllBranch() {
        List<Branch> branches = branchRepository.findAll();
        return branches.stream().map(branch -> BranchRespon.builder()
                .Id(branch.getId())
                .branchCode(branch.getBranchCode())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .phoneNumber(branch.getPhoneNumber())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void deleteBranch(String id) {
        branchRepository.deleteById(id);
    }

}
