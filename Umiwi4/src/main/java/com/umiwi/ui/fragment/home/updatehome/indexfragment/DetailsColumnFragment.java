package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.ExperDetailsAlbumbean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * 详情专栏
 * Created by Administrator on 2017/2/28.
 */

public class DetailsColumnFragment extends BaseConstantFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_column_layout, null);
        String albumurl = ExperDetailsFragment.albumurl;
        if (albumurl!=null){
            getInfos(albumurl);
        }
        return view;
    }

    private void getInfos(String albumurl) {
        OkHttpUtils.get().url(albumurl).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("data","请求数据失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("data","请求数据成功 :"+data);
                if (data!=null){
                    ExperDetailsAlbumbean experDetailsAlbumbean = JsonUtil.json2Bean(data, ExperDetailsAlbumbean.class);

                }
            }
        });
    }
}
