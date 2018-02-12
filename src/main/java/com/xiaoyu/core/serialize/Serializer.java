/**
 * 唯有读书,不慵不扰 
 **/
package com.xiaoyu.core.serialize;

/**
 * 2017年4月20日下午2:16:28
 * 
 * @author xiaoyu
 * @description 序列化
 */
public interface Serializer {

    /**
     * @param obj
     * @return
     */
    public <T> byte[] serialize(T obj);

    /**
     * @param data
     * @param cls
     * @return
     */
    public <T> T deserialize(byte[] data, Class<T> cls);
}
