package com.wa.msm.adventure.proxy;

import com.wa.msm.adventure.bean.CategoryBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "wa-category")
@RibbonClient(name = "wa-category")
public interface MSCategoryProxy {
    @GetMapping(value = "/category/{id}")
    Optional<CategoryBean> getCategory(@PathVariable("id") Long id);
}
