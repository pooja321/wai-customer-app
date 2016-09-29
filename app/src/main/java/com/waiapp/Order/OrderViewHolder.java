package com.waiapp.Order;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.waiapp.Model.Order;
import com.waiapp.R;
import com.waiapp.Utility.Constants;

import java.util.Objects;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    String Orderstatus;

    private TextView mTextViewOrderId, mTextViewOrderType, mTextViewOrderStatus, mTextViewOrderAmount, mTextViewOrderResource, mTextViewCurrentOrder;

    public OrderViewHolder(View itemView) {
        super(itemView);
        mTextViewOrderId = (TextView) itemView.findViewById(R.id.order_item_orderid);
        mTextViewOrderType = (TextView) itemView.findViewById(R.id.order_item_ordertype);
        mTextViewOrderStatus = (TextView) itemView.findViewById(R.id.order_item_orderstatus);
        mTextViewOrderAmount = (TextView) itemView.findViewById(R.id.order_item_orderamount);
        mTextViewOrderResource = (TextView) itemView.findViewById(R.id.order_item_orderresource);
        mTextViewCurrentOrder = (TextView) itemView.findViewById(R.id.order_item_current_order_ind);
    }

    public void bindView(Order order){
        Orderstatus = order.getOrderStatus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(Orderstatus, Constants.ORDER_STATUS_INPROGRESS) || Objects.equals(Orderstatus, Constants.ORDER_STATUS_ORDERED)){
                mTextViewCurrentOrder.setVisibility(View.VISIBLE);
            }else{
                mTextViewCurrentOrder.setVisibility(View.GONE);
            }
        }
        mTextViewOrderId.setText(String.valueOf(order.getOrderId()));
        mTextViewOrderType.setText(String.valueOf(order.getOrderType()));
        mTextViewOrderStatus.setText(String.valueOf(order.getOrderStatus()));
        mTextViewOrderAmount.setText(String.valueOf(order.getOrderAmount()));
        mTextViewOrderResource.setText(String.valueOf(order.getResourceId()));
    }
}
