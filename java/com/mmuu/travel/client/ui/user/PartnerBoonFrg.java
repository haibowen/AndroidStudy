package com.mmuu.travel.client.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.PublicRequestInterface;
import com.mmuu.travel.client.R;
import com.mmuu.travel.client.base.MFBaseFragment;
import com.mmuu.travel.client.bean.RequestResultBean;
import com.mmuu.travel.client.bean.user.PartnerBoonBean;
import com.mmuu.travel.client.mfConstans.MFConstansValue;
import com.mmuu.travel.client.mfConstans.MFStaticConstans;
import com.mmuu.travel.client.mfConstans.MFUrl;
import com.mmuu.travel.client.tools.GsonTransformUtil;
import com.mmuu.travel.client.tools.MFRunner;
import com.mmuu.travel.client.tools.MFUtil;
import com.mmuu.travel.client.ui.user.adapter.PartnerBoonAdp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 采蜜人福利Act
 */

public class PartnerBoonFrg extends MFBaseFragment implements View.OnClickListener, PublicRequestInterface {
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    @BindView(R.id.title_title)
    TextView titleTitle;
    @BindView(R.id.pull_refresh_listview)
    PullToRefreshListView pullRefreshlistview;
    private List<PartnerBoonBean.ListBean> datas;
    private PartnerBoonAdp adapter;
    private int pageNo = 1;
    private final int INIT_DATA_CODE = 1695701;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragContentView == null) {
            fragContentView = inflater.inflate(R.layout.frg_partnerboon, null);
            ButterKnife.bind(this, fragContentView);
            initView();
            if (!MFUtil.checkNetwork(mContext)) {
                View emptyView = LayoutInflater.from(mContext).inflate(R.layout.view_nodata, null);
                ImageView iv_nodata = (ImageView) emptyView.findViewById(R.id.iv_nodata);
                iv_nodata.setImageResource(R.drawable.nodata_nonet);
                TextView textView = (TextView) emptyView.findViewById(R.id.tv_text_hint);
                TextView tv_text_hintbutton = (TextView) emptyView.findViewById(R.id.tv_text_hintbutton);
                tv_text_hintbutton.setOnClickListener(this);
                tv_text_hintbutton.setVisibility(View.VISIBLE);
                textView.setText("无网络链接，点击");
                pullRefreshlistview.setEmptyView(emptyView);
                return fragContentView;
            }
            initData(true);
        }
        return fragContentView;
    }

    private void initData(boolean isShowDlg) {
        if (!MFUtil.checkNetwork(mContext)) {
            pullRefreshlistview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullRefreshlistview.onRefreshComplete();
                }
            }, 100);
            showNoNetworkDialog(true);
            return;
        }
        if (isShowDlg)
            dialogShow();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("userId", MFStaticConstans.getUserBean().getUser().getId() + "");
        requestParams.addBodyParameter("pageNo", pageNo + "");
        requestParams.addBodyParameter("pageSize", 10 + "");
//        MFRunner.requestPost(INIT_DATA_CODE, MFUrl.COUPONLIST, requestParams, this);
    }

    private void initView() {
        titleLeftImage.setImageResource(R.drawable.title_leftimgblack_selector);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleLeftImage.setScaleType(ImageView.ScaleType.CENTER);
        titleLeftImage.setOnClickListener(this);
        titleTitle.setText("福利券记录");

        pullRefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullRefreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo = 1;
                initData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;
                initData(false);
            }
        });
        adapter = new PartnerBoonAdp(mContext, datas);
        pullRefreshlistview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_image:
                mContext.finish();
                break;
            case R.id.tv_text_hintbutton:
                dialogShow();
                if (!MFUtil.checkNetwork(mContext)) {
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismmisDialog();
                        }
                    }, 120);
                    return;
                }
                initData(true);
                break;
        }
    }

    @Override
    public void onStart(int i, RequestParams requestParams) {

    }

    @Override
    public void onLoading(int i, RequestParams requestParams, long l, long l1, boolean b) {

    }

    @Override
    public void onSuccess(int i, RequestParams requestParams, ResponseInfo responseInfo) {
        if (!isAdded()) {
            return;
        }
        pullRefreshlistview.onRefreshComplete();
        pullRefreshlistview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefreshlistview.onRefreshComplete();
            }
        }, 100);
        dismmisDialog();
        switch (i) {
            case INIT_DATA_CODE:
                String myWalletDetailStringInfo = responseInfo.result.toString();
                RequestResultBean<PartnerBoonBean> requestResultBean = GsonTransformUtil.getObjectFromJson(myWalletDetailStringInfo, new TypeToken<RequestResultBean<PartnerBoonBean>>() {
                }.getType());
                if (requestResultBean != null && MFConstansValue.BACK_SUCCESS == requestResultBean.getCode()) {
                    PartnerBoonBean partnerBoonBean = requestResultBean.getData();
                    if (partnerBoonBean != null && partnerBoonBean.getList() != null) {
                        if (pageNo >= partnerBoonBean.getTotalPage()) {
//                            MFUtil.showToast(mContext, "没有更多数据了");
                            pullRefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else {
                            pullRefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
                        }
                        if (pageNo == 1) {
                            datas = partnerBoonBean.getList();
                        } else {
                            datas.addAll(partnerBoonBean.getList());
                        }
                    }
                    adapter.replaceData(datas);
                    adapter.notifyDataSetChanged();
                    View emptyView = LayoutInflater.from(mContext).inflate(R.layout.view_nodata, null);
                    ImageView iv_nodata = (ImageView) emptyView.findViewById(R.id.iv_nodata);
                    iv_nodata.setImageResource(R.drawable.nodata_nodata);
                    TextView textView = (TextView) emptyView.findViewById(R.id.tv_text_hint);
                    TextView tv_text_hintbutton = (TextView) emptyView.findViewById(R.id.tv_text_hintbutton);
                    tv_text_hintbutton.setVisibility(View.GONE);
                    textView.setText("您还没有福利记录");
                    pullRefreshlistview.setEmptyView(emptyView);
                } else if (requestResultBean != null && MFConstansValue.BACK_LOGOUT == requestResultBean.getCode()) {
                    if (pageNo > 1) {
                        pageNo--;
                    }
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                    MFStaticConstans.setUserBean(null);
                    startActivity(LoginAct.class, null);
                    mContext.finish();

                } else if (requestResultBean != null && (MFConstansValue.BACK_SYSTEMERROR == requestResultBean.getCode() || MFConstansValue.BACK_BUSINESS == requestResultBean.getCode())) {
                    if (pageNo > 1) {
                        pageNo--;
                    }
                    MFUtil.showToast(mContext, requestResultBean.getMessage());
                }
                break;
        }

    }

    @Override
    public void onFailure(int i, RequestParams requestParams, HttpException e, String s) {
        if (!isAdded()) {
            return;
        }
        if (pageNo > 1) {
            pageNo--;
        }
        MFUtil.showToast(mContext, getResources().getString(R.string.databackfail));
        dismmisDialog();
        pullRefreshlistview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefreshlistview.onRefreshComplete();
            }
        }, 100);
    }

    @Override
    public void onCancel(int i, RequestParams requestParams) {

    }
}
