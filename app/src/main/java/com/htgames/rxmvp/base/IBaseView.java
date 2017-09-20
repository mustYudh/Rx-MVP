package com.htgames.rxmvp.base;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Created by yudenghao on 2017/9/15.
 */

public interface IBaseView {
    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示网络错误，modify 对网络异常在 BaseActivity 和 BaseFragment 统一处理
     */
    void showNetError();

    /**
     * 显示空页面
     */
    void showEmptyVIew();
}
