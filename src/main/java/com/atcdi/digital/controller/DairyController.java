package com.atcdi.digital.controller;


import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.entity.daliy.Dairy;
import com.atcdi.digital.service.DairyService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dairy")
@Api(tags  = "日志相关接口")
public class DairyController {
    @Resource
    DairyService dairyService;

    @GetMapping("/self")
    public StandardResponse getUserSelfDairy(){
        return StandardResponse.success(dairyService.getUserSelfDairyList());
    }


    @PostMapping("/update")
    public StandardResponse updateDairy(@NonNull Dairy record){
        if (dairyService.updateDairy(record)){
            return StandardResponse.success();
        }else{
            return StandardResponse.error();
        }
    }

    @DeleteMapping("/delete")
    public StandardResponse deleteDairy(@NonNull int dairyId){
        if (dairyService.deleteDairy(dairyId)){
            return StandardResponse.success();
        }else{
            return StandardResponse.error();
        }
    }

    @PostMapping("/create")
    public StandardResponse createDairy(@NonNull Dairy record){
        return StandardResponse.success(dairyService.createDairy(record));
    }

    @GetMapping("/user_tree")
    public StandardResponse getUserTree(){
        return StandardResponse.success(dairyService.getUserTree());
    }

    @GetMapping("/by_user")
    public StandardResponse getUserDairyByUsername(@NonNull int id){
        return StandardResponse.success(dairyService.getUserDairyListByUsername(id));
    }

}
