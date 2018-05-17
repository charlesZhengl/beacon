package com.xiaoyu.core.cluster;

import java.util.List;

import com.xiaoyu.core.common.message.RpcRequest;

/**
 * @author hongyu
 * @date 2018-05
 * @description 容错机制
 */
public interface FaultTolerant {

    /**
     * 根据规则进行失败容错
     * 
     * @param providers
     * @return
     * @throws Throwable 
     */
    public Object invoke(RpcRequest request, List<?> providers) throws Throwable;

}