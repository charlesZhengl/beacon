package com.xiaoyu.core.register;

import java.util.List;

import com.xiaoyu.core.common.bean.BeaconPath;

/**
 * @author hongyu
 * @date 2018-02
 * @description
 */
public interface Registry {

    /**
     * 注册地址
     * 
     * @param addr
     */
    public void address(String addr);

    /**
     * 关闭注册中心
     */
    public void close();

    /**
     * 是否连接
     * 
     * @param addr
     * @return
     */
    public boolean isInit();

    /**
     * 发现服务
     * 
     * @param service
     * @return
     */
    public boolean discoverService(String service);

    /**
     * 注册服务
     * 
     * @param beaconPath
     */
    public void registerService(BeaconPath beaconPath);

    /**
     * 取消注册服务
     * 
     * @param beaconPath
     */
    public void unregisterService(BeaconPath beaconPath);

    /**
     * 获取对应的provider是
     * 
     * @param service
     * @return
     */
    public List<BeaconPath> getProviders(String service);
}