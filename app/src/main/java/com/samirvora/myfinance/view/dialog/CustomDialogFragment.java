package com.samirvora.myfinance.view.dialog;

import android.support.v4.app.DialogFragment;

import java.util.List;

/**
 * Created by James on 05/11/2016.
 */
public abstract class CustomDialogFragment<T> extends DialogFragment{
    abstract void setDialogData(List<T> data);
}
