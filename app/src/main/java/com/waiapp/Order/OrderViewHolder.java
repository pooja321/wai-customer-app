package com.waiapp.Order;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.waiapp.Model.Order;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    String Orderstatus;

    private TextView mTextViewOrderId, mTextViewOrderDate, mTextViewOrderStatus, mTextViewOrderAmount, mTextViewCurrentOrder;
    private ImageView mImageViewOrderType;

    public OrderViewHolder(View itemView) {
        super(itemView);
        mTextViewOrderId = (TextView) itemView.findViewById(R.id.order_item_tv_orderid);
        mTextViewOrderDate = (TextView) itemView.findViewById(R.id.order_item_tv_date);
        mTextViewOrderAmount = (TextView) itemView.findViewById(R.id.order_item_tv_orderamount);
        mTextViewOrderStatus = (TextView) itemView.findViewById(R.id.order_item_tv_orderstatus);
        mImageViewOrderType = (ImageView) itemView.findViewById(R.id.order_item_iv_ordertype);
    }

    public void bindView(Order order){
        HashMap<String, Object> orderBookingTime;
        Orderstatus = order.getOrderStatus();
        String orderType = order.getOrderType();
        switch (orderType){
            case Constants.ORDER_TYPE_COOKING:
                mImageViewOrderType.setImageResource(R.drawable.ic_local_dining_black_24dp);
                break;
            case Constants.ORDER_TYPE_CLEANING:
                mImageViewOrderType.setImageResource(R.drawable.ic_hanger_black_24dp);
                break;
            case Constants.ORDER_TYPE_WASHING:
                mImageViewOrderType.setImageResource(R.drawable.ic_person_black_24dp);
                break;
        }
        mTextViewOrderId.setText(String.valueOf(order.getOrderId()));
        mTextViewOrderStatus.setText(String.valueOf(order.getOrderStatus()));
        mTextViewOrderAmount.setText(String.valueOf(order.getOrderAmount()));
        orderBookingTime = order.getOrderbookingTime();
        Long timestamp = (Long) orderBookingTime.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
        Date date = new Date(timestamp);
        Log.v("wai", String.valueOf(timestamp));
        Log.v("wai", String.valueOf(date));
        SimpleDateFormat sfd = new SimpleDateFormat("EEE MMM D", Locale.US);
        mTextViewOrderDate.setText(sfd.format(date));
    }
}
