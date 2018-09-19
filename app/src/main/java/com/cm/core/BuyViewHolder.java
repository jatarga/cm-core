package com.cm.core;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

class BuyViewHolder extends BaseViewHolder {

    public static final int TYPE = 0;
    Button mBuyButton;

    BuyViewHolder(View itemView) {
        super(itemView);
        mBuyButton = itemView.findViewById(R.id.BuyButton);
    }
}