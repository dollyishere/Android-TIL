package com.example.test_parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
// 다수(다양한 타입)의 데이터를 전달하기 위한 클래스
public class SimpleData implements Parcelable {

    int number;
    String message;

    // 값을 설정
    public SimpleData(int num, String msg) {
        number = num;
        message = msg;
    }

    // Parcel 객체를 이용한 값 설정
    public SimpleData(Parcel src) {
        number = src.readInt();
        message = src.readString();
    }

    // static final 객체 : CREATOR
    public static final Creator<SimpleData> CREATOR = new Creator<SimpleData>() {
        // Parcel 객체로부터 데이터를 추출
        @Override
        public SimpleData createFromParcel(Parcel in) {
            return new SimpleData(in);
        }

        @Override
        public SimpleData[] newArray(int size) {
            return new SimpleData[size];
        }
    };

    // 객체 유형 구별할 때 사용되는 메서드
    @Override
    public int describeContents() {
        return 0;
    }

    // 재정의 메서드: 객체가 가지고 있는 데이터를 Parcel 객체로 만들어주는 역할
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(message);
    }
}
