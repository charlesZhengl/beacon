package com.xiaoyu.transport;

import java.util.concurrent.Future;

/**
 * @author hongyu
 * @date 2018-02
 * @description
 */
public interface BaseChannel {
    
    /**
     * @param message
     * @return
     * @throws Exception
     */
    public Future<Object> send(Object message) throws Exception;

    /**
     * @param msg
     * @throws Exception
     */
    public void receive(Object msg) throws Exception;
}
