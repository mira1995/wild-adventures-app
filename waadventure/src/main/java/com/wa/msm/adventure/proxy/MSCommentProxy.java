package com.wa.msm.adventure.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "wa-comment", url = "localhost:9001")
public interface MSCommentProxy {
    @DeleteMapping(value = "/comment/adventure/{adventureId}")
    String deleteCommentByAdventureId(@PathVariable("adventureId") Long adventureId);
}
