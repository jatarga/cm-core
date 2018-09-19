package com.cm.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.dingmouren.layoutmanagergroup.echelon.EchelonLayoutManager;
import com.squareup.picasso.Picasso;

public class Gallery extends Activity implements BillingProcessor.IBillingHandler {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private EchelonLayoutManager mLayoutManager;
    private SharedPreferences sharedPreferences;
    private BillingProcessor bp;
    private static String PREMIUM_USER = "PREF_PREMIUM_USER";

    public static String[] IMAGES = new String[]{
            "https://cdn.vox-cdn.com/thumbor/2Khos1l0173GIp2ViWEUw44r63Y=/0x45:1100x778/1400x1400/filters:focal(0x45:1100x778):format(png)/cdn.vox-cdn.com/uploads/chorus_image/image/25614799/clash_of_clans.0.png",
            "https://encrypted-tbn0.gstatic.com/IMAGES?q=tbn:ANd9GcTgPtVuUAO0IxgWOSxEM9K99Rga9630k8Ctl0MARzqjE08WEDgh3g",
            "https://www.windowscentral.com/sites/wpcentral.com/files/styles/xlarge/public/field/image/2016/01/clash-of-clans.jpg?itok=hM1UgPNB",
            "https://www.windowscentral.com/sites/wpcentral.com/files/styles/xlarge/public/field/image/2016/01/clash-of-clans.jpg?itok=hM1UgPNB",
            "https://www.windowscentral.com/sites/wpcentral.com/files/styles/xlarge/public/field/image/2016/01/clash-of-clans.jpg?itok=hM1UgPNB",
            "https://www.windowscentral.com/sites/wpcentral.com/files/styles/xlarge/public/field/image/2016/01/clash-of-clans.jpg?itok=hM1UgPNB",
    };
    public static int IMAGES_IAP_THRESHOLD = 2;
    public static String LICENSE_KEY = null;
    public static String PRODUCT_KEY = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bp = new BillingProcessor(this, LICENSE_KEY, this);
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);

        mRecyclerView = findViewById(R.id.RecyclerView);
        mRecyclerViewAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mLayoutManager = new EchelonLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private boolean isPremium() {
        return sharedPreferences.getBoolean(PREMIUM_USER, false);
    }

    private void setPremium() {
        sharedPreferences.edit().putBoolean(PREMIUM_USER, true).apply();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        bp.consumePurchase(productId);
        setPremium();
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {

            if (type == BuyViewHolder.TYPE) {
                return new BuyViewHolder(LayoutInflater.from(Gallery.this).inflate(R.layout.row_gallery_buy, parent, false));
            }

            return new ImageViewHolder(LayoutInflater.from(Gallery.this).inflate(R.layout.row_gallery_image, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {

            if (baseViewHolder.getItemViewType() == ImageViewHolder.TYPE) {
                ImageViewHolder vh = (ImageViewHolder) baseViewHolder;
                Picasso.get().load(IMAGES[i]).into(vh.mImageView);
            }

            if (baseViewHolder.getItemViewType() == BuyViewHolder.TYPE) {
                BuyViewHolder vh = (BuyViewHolder) baseViewHolder;
                vh.mBuyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bp.subscribe(Gallery.this, PRODUCT_KEY);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return IMAGES.length;
        }

        @Override
        public int getItemViewType(int position) {

            if (isPremium()) {
                return ImageViewHolder.TYPE;
            }

            if (position <= IMAGES_IAP_THRESHOLD) {
                return BuyViewHolder.TYPE;
            }

            return ImageViewHolder.TYPE;
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, Gallery.class));
    }

}
