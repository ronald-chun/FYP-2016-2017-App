package com.cuhk.ieproject.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cuhk.ieproject.R;

/**
 * Created by anson on 5/4/2017.
 */

public class LoadingDialog extends MaterialDialog {
    protected LoadingDialog(Builder builder) {
        super(builder);
    }

    public static class Builder extends MaterialDialog.Builder {
        public Builder(@NonNull Context context) {
            super(context);

            this.title(R.string.loading)
                    .content(R.string.loading_content)
                    .progress(true, 0);
        }
    }
}
