package com.htgames.rxmvp.dagger.module;

import com.htgames.rxmvp.presenter.GetUserNamePresenter;
import com.htgames.rxmvp.view.ISetUserName;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yudenghao on 2017/9/15.
 */
@Module
public class ActivityModule {
    @Provides
    GetUserNamePresenter provideGet(ISetUserName iSetUserName) {
        return new GetUserNamePresenter(iSetUserName);
    }
}
