package com.example.spokenwapp.adapters;

import android.view.View;

public interface OnItemTouchListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);

}
