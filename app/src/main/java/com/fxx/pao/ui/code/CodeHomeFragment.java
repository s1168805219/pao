package com.fxx.pao.ui.code;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.fxx.pao.R;
import com.fxx.pao.adapter.CodeRvAdapter;
import com.fxx.pao.base.BaseFragment;
import com.fxx.pao.model.CodeModel;
import com.fxx.pao.ui.code.codedetail.CodeDetailActivity;
import com.fxx.pao.ui.search.SearchActivity;
import com.fxx.pao.view.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 代码
 * Created by fxx on 2017/8/10 0010.
 */

public class CodeHomeFragment extends BaseFragment<CodeHomeContract.Presenter>
        implements CodeHomeContract.View,OnRefreshListener,OnLoadmoreListener, CodeRvAdapter.ItemClickListener {

    @BindView(R.id.srl)
    SmartRefreshLayout mSrl;
    @BindView(R.id.rv_code)
    RecyclerView mRvCodes;
    @BindView(R.id.iv_search_code)
    ImageView mIvSearchCode;

    private CodeRvAdapter mAdapter;
    private List<CodeModel.ItemsBean> mItems;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_codehome;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    public CodeHomeContract.Presenter getmPresenter() {
        return new CodeHomePresenter();
    }

    @Override
    public void presenterSetView() {
        if(mPresenter != null) mPresenter.setView(this);
    }

    @Override
    public void initView() {
        mRvCodes.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        mRvCodes.addItemDecoration(new SpaceItemDecoration());
        mRvCodes.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));
        mSrl.setOnRefreshListener(this);
        mSrl.setOnLoadmoreListener(this);
    }

    @Override
    public void initData() {
        mItems = new ArrayList<>();
        mAdapter =new CodeRvAdapter(mItems);
        mAdapter.setmItemClickListener(this);
        mRvCodes.setAdapter(mAdapter);
    }

    @Override
    public void loadData() {
        mSrl.autoRefresh();
    }


    @Override
    public void refreshCodeItems(List<CodeModel.ItemsBean> itemsBeen) {
        mItems.clear();
        mItems.addAll(itemsBeen);
        mAdapter.notifyDataSetChanged();

        if(mSrl.isRefreshing())
            mSrl.finishRefresh();
    }


    @Override
    public void appendCodeItems(List<CodeModel.ItemsBean> itemsBeen) {
        int oldSize=mItems.size();
        mItems.addAll(itemsBeen);
        mAdapter.notifyItemRangeChanged(oldSize,itemsBeen.size());
        if(mSrl.isLoading())
            mSrl.finishLoadmore();
    }

    /**
     * 上拉加载回调
     * @param refreshlayout
     */

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPresenter.loadMoreCodeItems();
    }

    /**
     * 下拉刷新回调
     * @param refreshlayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPresenter.loadInitCodeItems();
    }

    @Override
    public void onClick(int position) {
        CodeDetailActivity.start(this.getContext(),mItems.get(position).getId(),mItems.get(position).getTitle());
    }

    @OnClick({R.id.iv_search_code})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_search_code:
                SearchActivity.start(getContext(),SearchActivity.SEARCHTYPE_CODE);
                break;
        }
    }
}