package com.closedevice.fastapp.view.dialog;

public interface IDialog {

    void hideWaitDialog();

    WaitDialog showWaitDialog();

    WaitDialog showWaitDialog(int resid);

    WaitDialog showWaitDialog(String text);
}
