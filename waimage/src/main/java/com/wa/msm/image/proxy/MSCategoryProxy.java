package com.wa.msm.image.proxy;

import com.wa.msm.image.bean.CategoryBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "zuul-server")
@RibbonClient(name = "wa-category")
public interface MSCategoryProxy {
    @GetMapping(value = "/wa-category/category/{id}")
    Optional<CategoryBean> getCategory(@PathVariable("id") Long id);
}