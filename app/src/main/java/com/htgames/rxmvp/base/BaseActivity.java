package com.htgames.rxmvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.htgames.rxmvp.R;
import com.htgames.rxmvp.rxbus.RxBus;
import com.htgames.rxmvp.utils.StatusBarUtil;
import com.htgames.rxmvp.weight.EmptyLoadingView;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by yudenghao on 2017/9/15.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements IBaseView, EmptyLoadingView.OnClickRefresh {
    private Fragment fragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Object TAG;
    private Subscription mSubscription;
    protected int statusBarColor = -1;

    @Nullable
    @BindView(R.id.load_empty_view)
    EmptyLoadingView emptyLoadingView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //RxBus
        if (!openRxBus(TAG, mSubscription)) {
            RxBus.getInstance().addSubscription(TAG, mSubscription);
        }
        setContentView(attachView());
        //沉浸式状态栏
        if (statusBarColor != -1) {
            StatusBarUtil.setColor(this, statusBarColor, 0);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        }
        //绑定布局
        if (attachView() == 0) {
            throw new RuntimeException("have u create your layout?");
        }
        ButterKnife.bind(this);
        initInjector();
        initView();
        initListener();
        initData();
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

    /**
     * 点击重试
     */
    protected abstract void clickRefreshView();

    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    /**
     * 初始化数据
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

    /**
     * 发送RxBus
     *
     * @param o
     */
    protected void postRxBus(Object o) {
        RxBus.getInstance().post(o);
    }


    /**
     * Dagger注入
     */
    protected abstract void initInjector();


    /**
     * 绑定布局
     *
     * @return 布局资源ID
     */
    protected abstract int attachView();


    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化ToolBar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    public void initToolBae(Toolbar toolbar, boolean homeAsUpEnabled, int title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }


    /**
     * toolBar返回按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (RxBus.getInstance().hasObservers()) {
            RxBus.getInstance().unSubscribe(this);
        }
    }


    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    /**
     * =============================================FRAGMENT_UTIL========================================
     */


    //加载Fragment
    public void loadFragment(Fragment fg, int contentId) {
        if (fragment != null) {
            if (fg.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment).show(fg).commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction().hide(fragment).add(contentId, fg).commitAllowingStateLoss();
            }

        } else {
            fragmentManager.beginTransaction().add(contentId, fg).commit();
        }
        fragment = fg;
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }


    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
