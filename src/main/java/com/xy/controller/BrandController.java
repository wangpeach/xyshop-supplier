package com.xy.controller;

import com.xy.models.Brand;
import com.xy.services.IBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

@RestController
@Scope("prototype")
@RequestMapping("brand/")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @PostMapping("list")
    public @ResponseBody
    List<Brand> list(@RequestParam String key) {
        Condition cond = new Condition(Brand.class);
        if(StringUtils.isNotEmpty(key)) {
            cond.createCriteria().andLike("name", "%"+ key +"%");
        }
        return brandService.selectListByCondition(cond);
    }

}
