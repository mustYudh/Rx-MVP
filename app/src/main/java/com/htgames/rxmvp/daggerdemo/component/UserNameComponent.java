package com.htgames.rxmvp.daggerdemo.component;

import com.htgames.rxmvp.daggerdemo.model.UserNameModule;

import dagger.Component;

/**
 * Created by yudenghao on 2017/9/14.
 */
@Component(modules = UserNameModule.class)
public interface UserNameComponent {
    String setUserName();
}
