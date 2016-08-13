package com.waiapp.Order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.waiapp.Model.Order;
import com.waiapp.R;

/**
 * Created by keviv on 12/08/2016.
 */
public class OrderViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextViewOrderId, mTextViewOrderType, mTextViewOrderStatus, mTextViewOrderAmount, mTextViewOrderResource;

    public OrderViewHolder(View itemView) {
        super(itemView);
        mTextViewOrderId = (TextView) itemView.findViewById(R.id.order_item_orderid);
        mTextViewOrderType = (TextView) itemView.findViewById(R.id.order_item_ordertype);
        mTextViewOrderStatus = (TextView) itemView.findViewById(R.id.order_item_orderstatus);
        mTextViewOrderAmount = (TextView) itemView.findViewById(R.id.order_item_orderamount);
        mTextViewOrderResource = (TextView) itemView.findViewById(R.id.order_item_orderresource);
    }

    public void bindView(Order order){
        mTextViewOrderId.setText(String.valueOf(order.getOrderId()));
        mTextViewOrderType.setText(String.valueOf(order.getOrderType()));
        mTextViewOrderStatus.setText(String.valueOf(order.getOrderStatus()));
        mTextViewOrderAmount.setText(String.valueOf(order.getOrderAmount()));
        mTextViewOrderResource.setText(String.valueOf(order.getResourceId()));
    }
}
