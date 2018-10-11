package com.wa.msm.comment.proxy;

import com.wa.msm.comment.bean.AdventureBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "wa-adventure", url = "localhost:9003")
public interface MSAdventureProxy {
    @GetMapping(value = "/adventure/{id}")
    Optional<AdventureBean> getAdventure(@PathVariable("id") Long id);
}
