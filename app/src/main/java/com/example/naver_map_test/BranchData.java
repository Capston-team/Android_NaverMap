package com.example.naver_map_test;

public class BranchData {
    String branch;
    String branchName;

    public BranchData(String branch_, String branchName_){
        branch=branch_;
        branchName=branchName_;
    }
    public String getBranch(){
       return branch;
    }
    public String getBranchName(){
        return branchName;
    }
}
