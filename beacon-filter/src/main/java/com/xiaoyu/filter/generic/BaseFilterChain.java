/**
 * 唯有读书,不慵不扰
 */
package com.xiaoyu.filter.generic;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.xiaoyu.core.common.extension.ExtenderHolder;
import com.xiaoyu.core.common.extension.SpiManager;
import com.xiaoyu.core.rpc.config.bean.Invocation;
import com.xiaoyu.filter.api.Filter;
import com.xiaoyu.filter.api.FilterChain;

/**
 * 调用链
 * 
 * @author hongyu
 * @date 2018-07
 * @description 初始化调用链,通过调用链对所有的filter进行调用,此类的spi是必须存在的
 */
public class BaseFilterChain implements FilterChain {

    private static final LinkedHashSet<Filter> filterList = new LinkedHashSet<>();

    @Override
    public void invoke(Invocation invocation) {
        if (filterList.isEmpty()) {
            ExtenderHolder<Filter> holder = SpiManager.holder(Filter.class);
            Collection<Filter> col = holder.values();
            if (!col.isEmpty()) {
                filterList.addAll(col);
            }
            // chain本身无需再调用了 remove chain
            if (!filterList.isEmpty()) {
                for (Filter f : filterList) {
                    if (f instanceof FilterChain) {
                        filterList.remove(f);
                        break;
                    }
                }
            }
        }
        Iterator<Filter> iter = filterList.iterator();
        Filter filter = null;
        while (iter.hasNext()) {
            filter = iter.next();
            filter.invoke(invocation);
        }
    }
}
