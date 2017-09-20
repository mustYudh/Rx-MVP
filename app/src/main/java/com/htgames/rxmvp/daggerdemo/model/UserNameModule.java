package com.htgames.rxmvp.daggerdemo.model;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yudenghao on 2017/9/14.
 * User有参UserModule 需要参数Module  (参数赋值的仓库)
 */
@Module
public class UserNameModule {

    @Provides
    String setName() {
        return "有参数的Module";
    }
}
