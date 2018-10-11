package com.wa.msm.adventure.proxy;

import com.wa.msm.adventure.bean.CategoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "wa-category", url = "localhost:9002")
public interface MSCategoryProxy {
    @GetMapping(value = "/category/{id}")
    Optional<CategoryBean> getCategory(@PathVariable("id") Long id);

    @PatchMapping(value = "/category")
    CategoryBean updateCategory(@RequestBody CategoryBean category);
}
