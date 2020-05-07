package com.example.spokenwapp.adapters;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import javax.inject.Inject;

public class DividerItemDecorator extends RecyclerView.ItemDecoration {

    @Inject
    int space;

        @Inject
        public DividerItemDecorator(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView
                parent, @NonNull RecyclerView.State state) {

            outRect.left = space;
            outRect.right = space;


            //for vertical scrolling
            outRect.bottom = space;
            outRect.top = space;
        }
}
