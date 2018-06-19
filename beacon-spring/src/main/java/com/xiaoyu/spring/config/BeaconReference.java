/**
 * 唯有读书,不慵不扰
 * 
 */
package com.xiaoyu.spring.config;

import com.xiaoyu.core.common.bean.BeaconBean;

/**
 * @author hongyu
 * @date 2018-05
 * @description 对应于beacon-reference
 */
public class BeaconReference extends BeaconBean {

    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 请求超时
     */
    private String timeout;

    /**
     * 重试次数
     */
    private Integer retry = 0;

    /**
     * 启动时检查
     */
    private boolean check;

    public boolean getCheck() {
        return check;
    }

    public BeaconReference setCheck(boolean check) {
        this.check = check;
        return this;
    }

    public Integer getRetry() {
        return retry;
    }

    public BeaconReference setRetry(Integer retry) {
        this.retry = retry;
        return this;
    }

    public String getTimeout() {
        return timeout;
    }

    public BeaconReference setTimeout(String timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public BeaconReference setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
        return this;
    }

}
