package com.cm.core;

import android.view.View;
import android.widget.ImageView;

class IVH extends BVH {

    public static final int TYPE = 1;
    ImageView mImageView;

    IVH(View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.ImageView);
    }
}