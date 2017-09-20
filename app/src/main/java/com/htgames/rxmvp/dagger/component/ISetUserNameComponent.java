package com.htgames.rxmvp.dagger.component;

import com.htgames.rxmvp.dagger.module.ISetUserNameModule;
import com.htgames.rxmvp.view.ISetUserName;

import dagger.Component;

/**
 * Created by yudenghao on 2017/9/15.
 */
@Component(modules = ISetUserNameModule.class)
public interface ISetUserNameComponent {
    ISetUserName getISetUserName();
}
