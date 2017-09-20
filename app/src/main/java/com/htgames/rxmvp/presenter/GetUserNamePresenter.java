package com.htgames.rxmvp.presenter;

import com.htgames.rxmvp.view.ISetUserName;

/**
 * Created by yudenghao on 2017/9/14.
 */

public class GetUserNamePresenter implements IGetUserName {

    ISetUserName iSetUserName;

    public GetUserNamePresenter(ISetUserName iSetUserName) {
        this.iSetUserName = iSetUserName;
    }

    @Override
    public void getUserName(String uid) {
        if (uid.equals("1")) {
            iSetUserName.setUserName("123");
        }
    }
}
