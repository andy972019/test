package com.kuang.eduservice.client;

import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: StarSea99
 * @Date: 2020-11-03 13:12
 */
@Component
public class OrdersFileDegradeFeginClient implements OrdersClient{

    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
