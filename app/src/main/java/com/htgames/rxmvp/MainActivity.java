package com.htgames.rxmvp;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.htgames.rxmvp.base.BaseActivity;
import com.htgames.rxmvp.daggerdemo.component.DaggerUserComponent;
import com.htgames.rxmvp.daggerdemo.component.DaggerUserNameComponent;
import com.htgames.rxmvp.daggerdemo.component.UserNameComponent;
import com.htgames.rxmvp.daggerdemo.model.UserModule;
import com.htgames.rxmvp.daggerdemo.model.UserNameModule;
import com.htgames.rxmvp.module.Other;
import com.htgames.rxmvp.module.User;
import com.htgames.rxmvp.rxbus.RxBus;
import com.htgames.rxmvp.rxbus.model.RxBusModel;
import com.htgames.rxmvp.test.TestAdapter;
import com.htgames.rxmvp.utils.NotifiManager;
import com.htgames.rxmvp.utils.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {
    @Inject
    User user;
    @Inject
    Other other;
    @BindView(R.id.rxbus)
    Button rxbus;
    @BindView(R.id.sho_name)
    Button shoName;
    @BindView((R.id.info))
    ListView info;
    private UserNameComponent userNameComponent;
    @Override
    protected void clickRefreshView() {
        ToastUtils.showToast("点击重试了");
//        showEmptyVIew();
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading();
                            ArrayList<String> strings = new ArrayList<>();
                            for (int i = 0; i < 1000; i++) {
                                strings.add(i + "");
                            }
                            info.setAdapter(new TestAdapter(strings));
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void initListener() {
        rxbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifiManager.getInstance(MainActivity.this).createCustomNotification();
                postRxBus(new RxBusModel("XXXX"));
            }
        });
        shoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextActivity.start(MainActivity.this);
            }
        });

    }

    @Override
    protected void initData() {
        openRxBus(this, RxBus.getInstance().doSubscribe(RxBusModel.class,
                new Action1<RxBusModel>() {
                    @Override
                    public void call(RxBusModel mainActivity) {
                        shoName.setText(mainActivity.getName());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showNetError();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void initInjector() {
        userNameComponent = DaggerUserNameComponent.builder().userNameModule(new UserNameModule()).build();
        DaggerUserComponent.builder().userNameComponent(userNameComponent).userModule(new UserModule()).build().inject(this);
    }

    @Override
    protected void initView() {
        shoName.setText(user.getName() + "===" + other.getName() + "==" + userNameComponent.setUserName());
    }

    @Override
    protected int attachView() {
        return R.layout.activity_main;
    }

}
