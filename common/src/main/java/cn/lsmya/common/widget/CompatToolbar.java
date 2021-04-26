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
    private TextView backTextView;
    private ImageView backImageView;
    private TextView titleView;
    private ImageView navigationIconView;
    private OnClickListener listener;
    private OnClickListener listenerText;
    private OnClickListener listenerImage;
    private OnMenuClickListener menuClickListener;
    private int navigationIconColor = -2;
    private int menuColor = Color.BLACK;
    private boolean hideNavigationIcon = false;
    private boolean titleCentre = true;
    private boolean clickEffect = true;
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
        backTextView = view.findViewById(R.id.ui_toolbar_backText);
        backImageView = view.findViewById(R.id.ui_toolbar_backSecond);
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
                hideNavigationIcon = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.CompatToolbar_toolbar_clickEffect) {
                clickEffect = typedArray.getBoolean(attr, false);
                if (!clickEffect) {
                    navigationIconView.setVisibility(VISIBLE);
                    toolbar.setNavigationIcon(null);
                }
            } else if (attr == R.styleable.CompatToolbar_toolbar_menuColor) {
                menuColor = typedArray.getColor(attr, Color.parseColor("#000000"));
            } else if (attr == R.styleable.CompatToolbar_toolbar_navigationText) {
                String value = typedArray.getString(attr);
                backTextView.setText(value);
            } else if (attr == R.styleable.CompatToolbar_toolbar_navigationSecondIcon) {
                int resourceId = typedArray.getResourceId(attr, 0);
                if (resourceId != 0) {
                    backImageView.setImageResource(resourceId);
                    backImageView.setVisibility(VISIBLE);
                }
            }
        }
        typedArray.recycle();
        setTitle(title);
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon == null) {
            navigationIcon = navigationIconView.getDrawable();
        }
        if (navigationIconColor != -2 && navigationIcon != null) {
            navigationIcon.setColorFilter(navigationIconColor, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(navigationIcon);
            navigationIconView.setImageDrawable(navigationIcon);
        }
        if (!backTextView.getText().toString().isEmpty()) {
            clickEffect = false;
            backTextView.setVisibility(VISIBLE);
            if (navigationIconColor != -2) {
                backTextView.setTextColor(navigationIconColor);
            }
            if (!clickEffect && navigationIcon != null) {
                navigationIconView.setVisibility(VISIBLE);
                toolbar.setNavigationIcon(null);
            }
        }
        Drawable backImageViewDrawable = backImageView.getDrawable();
        if (navigationIconColor != -2 && backImageViewDrawable != null) {
            backImageViewDrawable.setColorFilter(navigationIconColor, PorterDuff.Mode.SRC_ATOP);
            backImageView.setImageDrawable(backImageViewDrawable);
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
        backTextView.setOnClickListener(v -> {
            if (listenerText != null) {
                listenerText.onClick(v);
            }
        });
        backImageView.setOnClickListener(v -> {
            if (listenerImage != null) {
                listenerImage.onClick(v);
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
        if (hideNavigationIcon) {
            hideNavigationIcon();
        }
    }

    /**
     * 设置toolbar左侧按钮点击监听事件
     *
     * @param listener 监听回调
     */
    public void setOnNavigationClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置toolbar左侧文字按钮点击监听事件
     *
     * @param listener 监听回调
     */
    public void setOnNavigationTextClickListener(OnClickListener listener) {
        this.listenerText = listener;
    }
    /**
     * 设置toolbar左侧第二图片按钮点击监听事件
     *
     * @param listener 监听毁回调
     */
    public void setOnNavigationImageClickListener(OnClickListener listener) {
        this.listenerImage = listener;
    }

    /**
     * 设置toolbar左侧按钮Icon
     *
     * @param resId Icon资源文件
     */
    public void setNavigationIcon(int resId) {
        if (this.toolbar != null) {
            this.toolbar.setNavigationIcon(resId);
        }
    }

    /**
     * 隐藏toolbar左侧按钮
     */
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

    /**
     * 设置标题
     *
     * @param resId 标题内容
     */
    public void setTitle(@StringRes int resId) {
        this.setTitle(getContext().getText(resId));
    }

    /**
     * 设置标题
     *
     * @param title 标题内容
     */
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

    /**
     * 修改菜单按钮文本文案
     *
     * @param itemId 菜单按钮的id
     * @param title  要修改的内容
     */
    public void setMenuText(int itemId, CharSequence title) {
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

    /**
     * 修改菜单按钮文本文案
     *
     * @param itemId 菜单按钮的id
     * @param title  要修改的内容
     */
    public void setMenuText(int itemId, int title) {
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

    /**
     * 隐藏菜单
     */
    public void hideMenu(MenuItem menuItem, boolean visible) {
        menuItem.setVisible(visible);
    }

    public void hideMenu(MenuItem menuItem) {
        hideMenu(menuItem, false);
    }

    public void hideMenu(int menuId, boolean visible) {
        toolbar.getMenu().findItem(menuId).setVisible(visible);
    }

    public void hideMenu(int menuId) {
        hideMenu(menuId, false);
    }
}