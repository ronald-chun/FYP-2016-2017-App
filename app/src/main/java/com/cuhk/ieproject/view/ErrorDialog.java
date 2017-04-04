package com.cuhk.ieproject.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cuhk.ieproject.R;

import com.cuhk.ieproject.util.Error;

/**
 * Created by anson on 5/4/2017.
 */

public class ErrorDialog extends MaterialDialog {
    protected ErrorDialog(Builder builder) {
        super(builder);
    }

    public static class Builder extends MaterialDialog.Builder {
        protected Error error;

        public Builder(@NonNull Context context) {
            super(context);
        }

        public Builder error(Error error){
            this.error = error;
            this.title(R.string.error_title)
                    .content(error.getMessageForDisplay())
                    .positiveText(R.string.error_ok);
            return this;
        }
    }
}
