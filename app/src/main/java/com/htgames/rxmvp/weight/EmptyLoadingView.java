package com.htgames.rxmvp.weight;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.htgames.rxmvp.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by yudenghao on 2017/9/16.
 */

public class EmptyLoadingView extends FrameLayout {
    @BindView(R.id.tv_net_error)
    FrameLayout tvNetError;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    @ViewStats
    private int CURRENT_STATUS = LOADING;
    public static final int LOADING = 1;
    public static final int ERROR = 2;
    public static final int EMPTY = 3;
    public static final int HIDE = 4;
    private OnClickRefresh onClickRefresh;


   public interface OnClickRefresh {
        void setOnClickListener();
    }

    public EmptyLoadingView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.layout_empty_loading, this);
        ButterKnife.bind(this);
        showStatusView();
        tvNetError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickRefresh != null) {
                    onClickRefresh.setOnClickListener();
                }
            }
        });
    }


    /**
     * 隐藏视图
     */
    public void hide() {
        CURRENT_STATUS = HIDE;
        showStatusView();
    }

    /**
     * 设置当前的状态
     *
     * @param stats 状态码
     */
    public void setCurrentStatus(@ViewStats int stats) {
        CURRENT_STATUS = stats;
        showStatusView();
    }


    public void setOnClickRefresh(OnClickRefresh onClickRefresh) {
        this.onClickRefresh = onClickRefresh;
    }

    /**
     * 根据状态显示当前的视图
     */
    private void showStatusView() {
        switch (CURRENT_STATUS) {
            case LOADING:
                setVisibility(VISIBLE);
                tvNetError.setVisibility(GONE);
                loadingProgress.setVisibility(VISIBLE);
                emptyView.setVisibility(GONE);
                break;
            case ERROR:
                setVisibility(VISIBLE);
                tvNetError.setVisibility(VISIBLE);
                loadingProgress.setVisibility(GONE);
                emptyView.setVisibility(GONE);
                break;
            case EMPTY:
                setVisibility(VISIBLE);
                tvNetError.setVisibility(GONE);
                loadingProgress.setVisibility(GONE);
                emptyView.setVisibility(VISIBLE);
                break;
            case HIDE:
                setVisibility(GONE);
                break;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOADING, ERROR, EMPTY, HIDE})
    @interface ViewStats {
    }

}
