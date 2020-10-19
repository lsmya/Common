package cn.lsmya.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import cn.lsmya.common.R;

/**
 * 沉浸式、版本兼容的Toolbar，状态栏透明.
 */
public class CompatToolbar extends RelativeLayout {
    private Toolbar toolbar;
    private TextView titleView;
    private ImageView navigationIconView;
    private OnClickListener listener;
    private OnMenuClickListener menuClickListener;

    public CompatToolbar(Context context) {
        this(context, null);
    }

    public CompatToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompatToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.common_toolbar, this);
        toolbar = view.findViewById(R.id.ui_toolbar_toolbar);
        titleView = view.findViewById(R.id.ui_toolbar_title);
        navigationIconView = view.findViewById(R.id.ui_toolbar_back);
        toolbar.setNavigationIcon(R.drawable.common_arrow_back);
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CompatToolbar);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.CompatToolbar_toolbar_title) {
                titleView.setText(typedArray.getString(attr));
            } else if (attr == R.styleable.CompatToolbar_toolbar_navigationIcon) {
                int resourceId = typedArray.getResourceId(attr, 0);
                if (0 != resourceId) {
                    toolbar.setNavigationIcon(resourceId);
                }
            } else if (attr == R.styleable.CompatToolbar_toolbar_menu) {
                int resourceId = typedArray.getResourceId(attr, 0);
                if (resourceId != 0) {
                    toolbar.inflateMenu(resourceId);
                }
            } else if (attr == R.styleable.CompatToolbar_toolbar_titleColor) {
                titleView.setTextColor(typedArray.getColor(attr, Color.parseColor("#111111")));
            } else if (attr == R.styleable.CompatToolbar_toolbar_hideNavigationIcon) {
                boolean hide = typedArray.getBoolean(attr, false);
                if (hide) {
                    hideNavigationIcon();
                }
            } else if (attr == R.styleable.CompatToolbar_toolbar_clickEffect) {
                boolean clickEffect = typedArray.getBoolean(attr, false);
                if (!clickEffect) {
                    navigationIconView.setVisibility(VISIBLE);
                    toolbar.setNavigationIcon(null);
                }
            }
        }
        typedArray.recycle();
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        navigationIconView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuClickListener != null) {
                    menuClickListener.onMenuClick(menuItem);
                }
                return true;
            }
        });
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setNavigationIcon(int resId) {
        if (this.toolbar != null) {
            this.toolbar.setNavigationIcon(resId);
        }
    }

    public void hideNavigationIcon() {
        if (this.toolbar != null) {
            this.toolbar.setNavigationIcon(null);
            this.navigationIconView.setVisibility(GONE);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.menuClickListener = listener;
    }

    public interface OnMenuClickListener {
        void onMenuClick(MenuItem menuItem);
    }

    public void setTitle(@StringRes int resId) {
        this.setTitle(getContext().getText(resId));
    }

    public void setTitle(CharSequence title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }

}