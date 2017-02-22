package com.umiwi.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiMyCouponBeans;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

/**
 * 我的优惠券 适配器
 *
 * @author tangxiyong 2013-12-6下午2:29:08
 */
public class MyCouponAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;

    private ArrayList<UmiwiMyCouponBeans> mList;

    public MyCouponAdapter(Context context, ArrayList<UmiwiMyCouponBeans> mList) {
        super();
        mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = mLayoutInflater.inflate(R.layout.listitem_mycoupon, null);
        Holder holder = getHolder(view);

        final UmiwiMyCouponBeans listBeans = mList.get(position);
//		1;//未使用
//		2;//已使用
//		3;//已经消费
//		4;//停用
        switch (listBeans.getStatusnum()) {
            case 2:
                holder.tv_open.setVisibility(View.VISIBLE);
                holder.tv_open.setText(Html.fromHtml("<u>" + "解绑" + "</u>"));
                if (listBeans.isWill()) {
                    holder.expiretime.setText("24小时内即将过期");
                    holder.expiretime.setTextColor(UmiwiApplication.getContext().getResources().getColor(R.color.umiwi_red));
                } else {
                    holder.expiretime.setText("有效期至：" + listBeans.getExpiretime().substring(0, 10));
                }
                break;
            case 3:
                holder.expiretime.setText("已消费");
                holder.expiretime.setTextColor(UmiwiApplication.getContext().getResources().getColor(R.color.umiwi_gray_9));
                break;
            default:
                if (listBeans.isWill()) {
                    holder.expiretime.setText("24小时内即将过期");
                    holder.expiretime.setTextColor(UmiwiApplication.getContext().getResources().getColor(R.color.umiwi_red));
                } else {
                    holder.expiretime.setText("有效期至：" + listBeans.getExpiretime().substring(0, 10));
                }
                holder.tv_open.setVisibility(View.GONE);
                break;
        }

        holder.couponCode.setText("券号：" + listBeans.getCode());


        String s = new String(listBeans.getValue());
        String a[] = s.split("元");
        holder.couponValue.setText("￥" + a[0]);
        switch (listBeans.getTypeNumber()) {
            case 2:
                holder.couponType.setText("(限专辑使用)");
                break;
            case 3:
                holder.couponType.setText("(限购买会员)");
                break;
            default:
                holder.couponType.setText("(请更新版本)");
                break;
        }

        holder.tv_open.setOnClickListener(openCouponListener);
        holder.tv_open.setTag(listBeans.getCloseurl());

        holder.valueName.setText(listBeans.getValueName());
        holder.valueType.setText(listBeans.getValueType());


        return view;
    }

    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class Holder {
        /**
         * 优惠券号
         */
        public TextView couponCode;
        /**
         * 使用范围
         */
        public TextView couponType;
        /**
         * 有效期
         */
        public TextView expiretime;
        /**
         * 面值
         */
        public TextView couponValue;
        /**
         * 解绑
         */
        public TextView tv_open;

        public TextView valueType;
        public TextView valueName;

        public Holder(View view) {
            couponCode = (TextView) view.findViewById(R.id.coupon_code);

            couponType = (TextView) view.findViewById(R.id.coupon_type);
            couponValue = (TextView) view.findViewById(R.id.coupon_value);

            valueType = (TextView) view.findViewById(R.id.value_type);
            valueName = (TextView) view.findViewById(R.id.value_name);

            expiretime = (TextView) view.findViewById(R.id.expiretime);
            tv_open = (TextView) view.findViewById(R.id.mycoupon_open);
        }
    }

    View.OnClickListener openCouponListener;
    String colseUrl;

    public void setOpenCouponListener(View.OnClickListener l) {
        this.openCouponListener = l;
    }

}
