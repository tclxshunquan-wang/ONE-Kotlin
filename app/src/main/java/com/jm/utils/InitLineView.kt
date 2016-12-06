package com.jm.utils

import android.graphics.Color
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * Created by shunq-wang on 2016/11/28.
 */
inline fun ViewManager.recyclerView() = recyclerView {}

inline fun ViewManager.recyclerView(init: RecyclerView.() -> Unit) = ankoView({ RecyclerView(it) }, init)

inline fun ViewManager.swipeRefreshLayout() = swipeRefreshLayout {}
inline fun ViewManager.swipeRefreshLayout(init: SwipeRefreshLayout.() -> Unit) = ankoView({
    SwipeRefreshLayout(it).apply {
        setColorSchemeColors(Color.parseColor("#2196f3"), Color.parseColor("#4caf50"), Color.parseColor("#ff9800"), Color.parseColor("#f44336"))
    }
}, init)


inline fun ViewManager.cardView() = cardView {}

inline fun ViewManager.cardView(init: CardView.() -> Unit) = ankoView({ CardView(it) }, init)