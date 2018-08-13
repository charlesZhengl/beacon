/**
 * 
 */
package com.xiaoyu.proxy.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.xiaoyu.core.common.bean.ProxyWrapper;
import com.xiaoyu.core.common.constant.BeaconConstants;
import com.xiaoyu.core.common.message.RpcRequest;
import com.xiaoyu.core.common.utils.IdUtil;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author hongyu
 * @date 2018-08
 * @description
 */
public abstract class AbstractInvocationHandler {

    /**
     * 在当method是父接口所属的时候;
     * method.getDeclaringClass()获取的是父接口的名称;
     * 因此这里需要持有一个接口的引用
     */
    private Class<?> ref;

    /**
     * 实际的接口名称
     */
    private String actualService;

    protected ProxyWrapper wrapper;

    public AbstractInvocationHandler(Class<?> ref) {
        this.ref = ref;
        actualService = ref.getName();
    }

    public AbstractInvocationHandler(ProxyWrapper wrapper) {
        this.ref = (Class<?>) wrapper.getTarget();
        this.wrapper = wrapper;
        if (wrapper.isGeneric()) {
            actualService = wrapper.getRealRef();
        } else {
            actualService = ref.getName();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getHandler(Class<T> t) {
        if (t == InvocationHandler.class) {
            return (T) new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return preInvoke(method, args);
                }
            };
        } else if (t == MethodInterceptor.class) {
            return (T) new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return preInvoke(method, args);
                }
            };
        }
        return null;
    }

    /**
     * 封装方法信息,获取信息,返回结果
     */
    private Object preInvoke(Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        final RpcRequest req = new RpcRequest()
                .setInterfaceName(actualService)
                .setParams(args)
                .setMethodName(methodName)
                .setReturnType(method.getReturnType())
                .setParamTypes(method.getParameterTypes());
        req.setHeartbeat(false);
        req.setId(IdUtil.requestId());
        if (BeaconConstants.EQUALS.equals(methodName)) {
            if (args == null || args.length == 0) {
                return false;
            }
            return ref == args[0];
        } else if (BeaconConstants.TO_STRING.equals(methodName)) {
            return this.doInvoke(req);
        } else if (BeaconConstants.HASHCODE.equals(methodName)) {
            return this.doInvoke(req);
        }
        return this.doInvoke(req);
    }

    /**
     * 检测service是否存在;获取一个client;等待请求结果;
     * TODO 是否大部分工作都在这里完成需要再考虑
     * 
     * @param req
     * @return
     * @throws Throwable
     */
    public abstract Object doInvoke(RpcRequest request) throws Throwable;
}