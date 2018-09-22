package com.cm.core;

import android.view.View;
import android.widget.Button;

class BUVH extends BVH {

    public static final int TYPE = 0;
    Button mBuyButton;

    BUVH(View itemView) {
        super(itemView);
        mBuyButton = itemView.findViewById(R.id.BuyButton);
    }
}