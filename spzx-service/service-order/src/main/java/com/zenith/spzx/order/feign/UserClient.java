package com.zenith.spzx.order.feign;

import com.zenith.spzx.model.entity.user.UserAddress;
import com.zenith.spzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-user")
public interface UserClient {

    @GetMapping("/api/user/userAddress/getUserAddress/{id}")
    public Result<UserAddress> getUserAddress(@PathVariable Long id) ;
}
