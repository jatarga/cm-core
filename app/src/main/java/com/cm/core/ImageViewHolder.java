package com.cm.core;

import android.view.View;
import android.widget.ImageView;

class ImageViewHolder extends BaseViewHolder {

    public static final int TYPE = 1;
    ImageView mImageView;

    ImageViewHolder(View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.ImageView);
    }
}