package cn.lsmya.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
    private int navigationIconColor = -2;
    private int menuColor = Color.BLACK;
    private boolean titleCentre = true;
    private String title = "";

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
                title = typedArray.getString(attr);
            } else if (attr == R.styleable.CompatToolbar_toolbar_titleCentre) {
                titleCentre = typedArray.getBoolean(attr, true);
            } else if (attr == R.styleable.CompatToolbar_toolbar_navigationIcon) {
                int resourceId = typedArray.getResourceId(attr, 0);
                if (0 != resourceId) {
                    toolbar.setNavigationIcon(resourceId);
                }
            } else if (attr == R.styleable.CompatToolbar_toolbar_navigationIconColor) {
                navigationIconColor = typedArray.getColor(attr, Color.parseColor("#111111"));
            } else if (attr == R.styleable.CompatToolbar_toolbar_menu) {
                int resourceId = typedArray.getResourceId(attr, 0);
                if (resourceId != 0) {
                    toolbar.inflateMenu(resourceId);
                }
            } else if (attr == R.styleable.CompatToolbar_toolbar_titleColor) {
                int color = typedArray.getColor(attr, Color.parseColor("#111111"));
                titleView.setTextColor(color);
                toolbar.setTitleTextColor(color);
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
            } else if (attr == R.styleable.CompatToolbar_toolbar_menuColor) {
                menuColor = typedArray.getColor(attr, Color.parseColor("#000000"));
            }
        }
        typedArray.recycle();
        setTitle(title);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIconColor != -2 && navigationIcon != null) {
            navigationIcon.setColorFilter(navigationIconColor, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(navigationIcon);
        }
        toolbar.setNavigationOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
        });
        navigationIconView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
        });
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuClickListener != null) {
                menuClickListener.onMenuClick(menuItem);
            }
            return true;
        });
        for (int i1 = 0; i1 < toolbar.getMenu().size(); i1++) {
            MenuItem item = toolbar.getMenu().getItem(i1);
            SpannableStringBuilder style = new SpannableStringBuilder(item.getTitle());
            style.setSpan(new ForegroundColorSpan(menuColor), 0, item.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(style);
        }
    }

    public void setOnNavigationClickListener(OnClickListener listener) {
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
        this.title = title.toString();
        if (titleCentre) {
            if (titleView != null) {
                titleView.setText(title);
            }
        } else {
            toolbar.setTitle(title);
        }
    }

    public void setMenuText(int itemId,CharSequence title) {
        for (int i1 = 0; i1 < toolbar.getMenu().size(); i1++) {
            MenuItem item = toolbar.getMenu().getItem(i1);
            if (item.getItemId() == itemId) {
                item.setTitle(title);
                SpannableStringBuilder style = new SpannableStringBuilder(item.getTitle());
                style.setSpan(new ForegroundColorSpan(menuColor), 0, item.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                item.setTitle(style);
            }
        }
    }
    public void setMenuText(int itemId,int title) {
        for (int i1 = 0; i1 < toolbar.getMenu().size(); i1++) {
            MenuItem item = toolbar.getMenu().getItem(i1);
            if (item.getItemId() == itemId) {
                item.setTitle(title);
                SpannableStringBuilder style = new SpannableStringBuilder(item.getTitle());
                style.setSpan(new ForegroundColorSpan(menuColor), 0, item.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                item.setTitle(style);
            }
        }
    }
}