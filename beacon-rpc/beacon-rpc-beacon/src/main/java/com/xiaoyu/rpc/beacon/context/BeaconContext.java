/**
 * 唯有读书,不慵不扰
 * 
 */
package com.xiaoyu.rpc.beacon.context;

import com.xiaoyu.core.rpc.context.AbstractBeaconContext;
import com.xiaoyu.transport.api.Client;
import com.xiaoyu.transport.api.Server;
import com.xiaoyu.transport.netty.NettyClient;
import com.xiaoyu.transport.netty.NettyServer;

/**
 * @author hongyu
 * @date 2018-04
 * @description
 */
public class BeaconContext extends AbstractBeaconContext {

    @Override
    public Client doInitClient(String host, int port) throws Exception {
        Client client = new NettyClient(host, port);
        client.start();
        return client;
    }

    @Override
    public Server doInitServer(Integer port) throws Exception {
        Server server = new NettyServer(port);
        return server;
    }

}
