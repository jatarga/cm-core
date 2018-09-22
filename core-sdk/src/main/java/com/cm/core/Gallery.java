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

    private RecyclerView rv;
    private RecyclerViewAdapter ra;
    private EchelonLayoutManager ml;
    private SharedPreferences sp;
    private BillingProcessor bp;
    private static String PU = "PU";

    public static String[] I = new String[]{};
    public static int T = 2;
    public static String L = null;
    public static String P = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bp = new BillingProcessor(this, L, this);
        sp = getSharedPreferences("prefs", MODE_PRIVATE);

        rv = findViewById(R.id.RecyclerView);
        ra = new RecyclerViewAdapter();
        rv.setAdapter(ra);
        ml = new EchelonLayoutManager(this);
        rv.setLayoutManager(ml);

    }

    private boolean isPremium() {
        return sp.getBoolean(PU, false);
    }

    private void setPremium() {
        sp.edit().putBoolean(PU, true).apply();
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        bp.consumePurchase(productId);
        setPremium();
        ra.notifyDataSetChanged();
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

    private class RecyclerViewAdapter extends RecyclerView.Adapter<BVH> {

        @NonNull
        @Override
        public BVH onCreateViewHolder(@NonNull ViewGroup parent, int type) {

            if (type == BUVH.TYPE) {
                return new BUVH(LayoutInflater.from(Gallery.this).inflate(R.layout.row_gallery_buy, parent, false));
            }

            return new IVH(LayoutInflater.from(Gallery.this).inflate(R.layout.row_gallery_image, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BVH BVH, int i) {

            if (BVH.getItemViewType() == IVH.TYPE) {
                IVH vh = (IVH) BVH;
                Picasso.get().load(I[i]).into(vh.mImageView);
            }

            if (BVH.getItemViewType() == BUVH.TYPE) {
                BUVH vh = (BUVH) BVH;
                vh.mBuyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bp.subscribe(Gallery.this, P);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return I.length;
        }

        @Override
        public int getItemViewType(int position) {

            if (isPremium()) {
                return IVH.TYPE;
            }

            if (position <= T) {
                return BUVH.TYPE;
            }

            return IVH.TYPE;
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, Gallery.class));
    }

}
