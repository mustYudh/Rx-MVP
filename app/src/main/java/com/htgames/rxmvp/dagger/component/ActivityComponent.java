package com.htgames.rxmvp.dagger.component;

import com.htgames.rxmvp.NextActivity;
import com.htgames.rxmvp.dagger.module.ActivityModule;
import com.htgames.rxmvp.dagger.module.ISetUserNameModule;

import dagger.Component;

/**
 * Created by yudenghao on 2017/9/15.
 */
@Component(dependencies = ISetUserNameModule.class,modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(NextActivity nextActivity);
}
