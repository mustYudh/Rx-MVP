package com.htgames.rxmvp.dagger.module;

import com.htgames.rxmvp.view.ISetUserName;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yudenghao on 2017/9/15.
 */
@Module
public class ISetUserNameModule {
    ISetUserName iSetUserName;

    public ISetUserNameModule(ISetUserName iSetUserName) {
        this.iSetUserName = iSetUserName;
    }

    @Provides
    public ISetUserName provideUserName() {
        return iSetUserName;
    }
}
