package com.htgames.rxmvp.daggerdemo.component;

import com.htgames.rxmvp.MainActivity;
import com.htgames.rxmvp.daggerdemo.model.UserModule;

import dagger.Component;

/**
 * Created by yudenghao on 2017/9/14.
 *
 */
@Component(dependencies = UserNameComponent.class,modules = UserModule.class)
public interface UserComponent {
    void inject(MainActivity mainActivity);
}
