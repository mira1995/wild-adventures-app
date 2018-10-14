package com.wa.msm.image.proxy;

import com.wa.msm.image.bean.CategoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "wa-category", url = "localhost:9002")
public interface MSCategoryProxy {
    @GetMapping(value = "/category/{id}")
    Optional<CategoryBean> getCategory(@PathVariable("id") Long id);
}
