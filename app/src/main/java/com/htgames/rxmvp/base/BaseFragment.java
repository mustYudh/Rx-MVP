package com.htgames.rxmvp.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htgames.rxmvp.R;
import com.htgames.rxmvp.rxbus.RxBus;
import com.htgames.rxmvp.weight.EmptyLoadingView;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by yudenghao on 2017/9/16.
 */

public abstract class BaseFragment extends RxFragment implements IBaseView, EmptyLoadingView.OnClickRefresh {
    private Object TAG;
    private Subscription mSubscription;
    private Context context;

    @Nullable
    @BindView(R.id.load_empty_view)
    EmptyLoadingView emptyLoadingView;
    private View rootVIew;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //RxBus
        if (!openRxBus(TAG, mSubscription)) {
            RxBus.getInstance().addSubscription(TAG, mSubscription);
        }
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootVIew == null) {
            rootVIew = View.inflate(context, attachView(), null);
            ButterKnife.bind(this, rootVIew);
            initInjector();
            initViews();
        }
        ViewGroup viewGroup = (ViewGroup) rootVIew.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(rootVIew);
        }
        return rootVIew;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() || rootVIew != null) {
            initData();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getUserVisibleHint() && rootVIew != null) {
            initData();
        } else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }


    /**
     * dagger注入
     */
    protected abstract void initInjector();


    /**
     * 绑定布局
     * @return  布局id
     */
    abstract int attachView();


    /**
     * 初始化控件
     */
    protected abstract void initViews();


    /**
     *懒加载的形式去获取数据
     */
    protected abstract void initData();



    /**
     * 是否开启RxBus
     *
     * @return
     */
    protected boolean openRxBus(Object tag, Subscription subscription) {
        TAG = tag;
        mSubscription = subscription;
        if (TAG == null || mSubscription == null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
         return this.<T>bindToLifecycle();
    }

    @Override
    public void showLoading() {
        if (emptyLoadingView != null) {
            emptyLoadingView.setCurrentStatus(EmptyLoadingView.LOADING);
        }
    }

    @Override
    public void hideLoading() {
        if (emptyLoadingView != null) {
            emptyLoadingView.hide();
        }
    }

    @Override
    public void showNetError() {
        if (emptyLoadingView != null) {
            emptyLoadingView.setOnClickRefresh(this);
            emptyLoadingView.setCurrentStatus(EmptyLoadingView.ERROR);
        }
    }

    @Override
    public void showEmptyVIew() {
        if (emptyLoadingView != null) {
            emptyLoadingView.setCurrentStatus(EmptyLoadingView.EMPTY);
        }
    }

    @Override
    public void setOnClickListener() {
        clickRefreshView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (RxBus.getInstance().hasObservers()) {
            RxBus.getInstance().unSubscribe(this);
        }
    }

    /**
     * 点击重试
     */
    protected abstract void clickRefreshView();
}
