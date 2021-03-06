package com.xiaoyu.core.cluster.loadbalance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.xiaoyu.core.cluster.LoadBalance;
import com.xiaoyu.core.common.bean.BeaconPath;

/**
 * 一致性hash
 * 
 * @author hongyu
 * @date 2018-06
 * @description 尽量保证同一个请求service每次都映射到同一个server,这样可以保证server端的缓存有效性
 */
public class ConsistentHashLoadBalance implements LoadBalance {

    /**
     * hash->virtual providers
     */
    private static final SortedMap<Integer, String> Circle_Sorted_Map = new TreeMap<>();

    /**
     * true providers
     */
    private static final HashSet<String> Machines = new HashSet<>();

    private static final String Separator = ",";

    @SuppressWarnings("unchecked")
    @Override
    public <T> T select(List<T> providers) {
        int size = providers.size();
        if (size == 1) {
            return providers.get(0);
        }
        List<BeaconPath> pros = (List<BeaconPath>) providers;
        // 以service为key,这样保证同一个service请求同一个server
        String service = pros.get(0).getService();
        Map<String, BeaconPath> proMap = this.spreadTrueProviders(pros);

        // 找比她大的所有节点
        SortedMap<Integer, String> nodes = Circle_Sorted_Map.tailMap(hash(service));
        int key;
        String host;
        // 说明已是环的终点
        if (nodes.isEmpty()) {
            // 直接取第一个节点
            key = Circle_Sorted_Map.firstKey();
            String value = Circle_Sorted_Map.get(key);
            // 取出虚拟节点中的host
            host = value.substring(value.indexOf(Separator) + 1);
        } else {
            // 取下一个节点
            key = hash(service);
            int firstKey = nodes.firstKey();
            String value = Circle_Sorted_Map.get(firstKey);
            // 取出虚拟节点中的host
            host = value.substring(value.indexOf(Separator) + 1);
        }
        return (T) proMap.get(host);
    }

    /**
     * 根据真实节点生成虚拟节点
     * 
     * @param providers
     * @return
     */
    private Map<String, BeaconPath> spreadTrueProviders(List<BeaconPath> providers) {
        // TODO 如果有机器下线,这里并没有给清除,所以这里先重置.
        Circle_Sorted_Map.clear();
        Machines.clear();
        Map<String, BeaconPath> proMap = new HashMap<>(providers.size() << 1);
        for (BeaconPath p : providers) {
            proMap.put(p.getHost(), p);
            if (Machines.add(p.getHost())) {
                spreadVirtualProvider(p.getHost());
            }
        }
        return proMap;
    }

    /**
     * 初始化虚拟节点
     * 
     * @param host
     */
    private void spreadVirtualProvider(String host) {
        String virtual = null;
        String appendStr = Separator + host;
        // 1:32
        int size = 32;
        for (int i = 0; i < size; i++) {
            virtual = String.valueOf(i).concat(appendStr);
            Circle_Sorted_Map.put(hash(virtual), virtual);
        }
    }

    /**
     * FNV1_32_HASH
     * 保证hash的散列,来达到均匀铺开
     * 
     * @param str
     * @return
     */
    private static int hash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        int len = str.length();
        char[] chs = str.toCharArray();
        for (int i = 0; i < len; i++) {
            hash = (hash ^ chs[i]) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // hash负数取正数(相反数)
        if (hash < 0) {
            hash = ~hash + 1;
        }
        return hash;
    }

}
