package com.sheaye.throttle;

/**
 * 生成类会继承该接口
 * Created by yexinyan on 2017/5/24.
 */

public interface IBinder<T> {
    void bind(T target, Object source, Finder finder);
}
