/**
 * 唯有读书,不慵不扰
 */
package com.xiaoyu.proxy.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.xiaoyu.core.common.bean.ProxyWrapper;
import com.xiaoyu.core.common.extension.SpiManager;
import com.xiaoyu.core.common.generic.GenericReference;
import com.xiaoyu.core.common.utils.StringUtil;
import com.xiaoyu.core.rpc.service.GenericService;
import com.xiaoyu.proxy.api.IProxy;

/**
 * 泛型调用服务发布
 * 
 * @author hongyu
 * @date 2018-07
 * @description
 */
public class GenericRequestLauncher {

    /**
     * generateKey->proxy
     */
    private static final ConcurrentMap<String, Object> Ref_Map = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T launch(GenericReference ref) throws Exception {
        if (ref == null) {
            return null;
        }
        if (StringUtil.isBlank(ref.getInterfaceName())) {
            throw new Exception("interfacxeName must be provided");
        }
        String key = generateKey(ref);
        if (!Ref_Map.containsKey(key)) {
            IProxy proxy = SpiManager.defaultSpiExtender(IProxy.class);
            ProxyWrapper wrapper = new ProxyWrapper(GenericService.class)
                    .setGeneric(true)
                    .setRealRef(ref.getInterfaceName());
            Map<String, Object> attach = new HashMap<>(4);
            attach.put("tolerant", ref.getTolerant());
            attach.put("timeout", ref.getTimeout());
            attach.put("group", ref.getGroup() == null ? "" : ref.getGroup());
            wrapper.setAttach(attach);
            // safe concurrent
            Ref_Map.put(key, proxy.getProxy(wrapper));
        }
        return (T) Ref_Map.get(key);
    }

    private static String generateKey(GenericReference ref) {
        String key = (ref.getGroup() == null ? "" : ref.getGroup())
                .concat(":")
                .concat(ref.getInterfaceName());
        return key;
    }
}
