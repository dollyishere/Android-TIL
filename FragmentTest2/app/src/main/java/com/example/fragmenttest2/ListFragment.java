package com.example.fragmenttest2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ListFragment extends Fragment {


    public static interface ImageSelectionCallback {
        public void onImageSelected(int position);
    }

    public ImageSelectionCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 넘겨지는 context가 ImageSelectionCallback과 같은 타입인지 비교
        // 같으면 넘겨진 context를 ImageSelectionCallback과 같은 타입으로 callback에 저장
        if (context instanceof ImageSelectionCallback) {
            callback = (ImageSelectionCallback) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        Button button1 = rootView.findViewById(R.id.button);
        Button button2 = rootView.findViewById(R.id.button2);
        Button button3 = rootView.findViewById(R.id.button3);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    switch (v.getId()) {
                        case R.id.button:
                            callback.onImageSelected(0);
                            break;
                        case R.id.button2:
                            callback.onImageSelected(1);
                            break;
                        default:
                            callback.onImageSelected(2);
                            break;
                    }
                }
            }
        };

        button1.setOnClickListener(buttonClickListener);
        button2.setOnClickListener(buttonClickListener);
        button3.setOnClickListener(buttonClickListener);

        return rootView;
    }
}
