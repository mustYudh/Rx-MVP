package com.htgames.rxmvp.daggerdemo.model;

import com.htgames.rxmvp.module.Other;
import com.htgames.rxmvp.module.User;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yudenghao on 2017/9/14.
 * 正在需要返回的User对象(有参的)  Dagger注入的对象
 */
@Module
public class UserModule {

    @Provides
    User provideSet(String s) {
        return new User(s);
    }

    @Provides
    Other provideOther() {
        return new Other("无参Dagger");
    }

}
