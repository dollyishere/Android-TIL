package com.example.samplerecylerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    // 어댑터 잘못 만들면 어플리케이션도 강제 종료
    ArrayList<Movie> items = new ArrayList<Movie>();

    /* 반드시 재정의 */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext()); // 레이아웃 펼쳐주어야 하기에 LayoutInflater 필요
        // inflater 객체 얻어냄
        // inflate가 실제로 펼쳐주는 메서드임
        // 하나의 항목에 대한 값을 가지고 있는 R.layout.movie_item 전달
        View itemView = inflater.inflate(R.layout.movie_item, viewGroup, false);

        // 뷰 홀더 객체 생성할 때 내부 요소가 값 설정(itemView => 레이아웃에 관련된 정보)
        return new ViewHolder(itemView);
    }

    /* 반드시 재정의 */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // viewholder에서 만들어준 해당 위젯 넣어주는 것
        Movie item = items.get(position);
        viewHolder.setItem(item); // ViewHolder의 setItem()
    }

    /* 반드시 재정의 */
    // 어댑터에 해당 값에 변화가 일어났을 시, 리마인드 시켜주는 메서드
    @Override
    public int getItemCount() {
        return items.size();
    }

    /* item 추가 메서드 선언 */
    public void addItem(Movie item) {
        items.add(item);
    }

    /* items 설정 메서드 선언 */
    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }

    /* items 추출 메서드 선언 */
    public Movie getItem(int position) {
        return items.get(position);
    }

    /* movie_item.xml 위젯에 데이터 설정 ViewHolder 클래스 선언 */
    // 하나의 항목을 구성하는 레아아웃의 각각의 위젯에다가 해당 데이터를 설정하는 역할을 담당함
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
        }

        public void setItem(Movie item) {
            textView.setText(item.movieNm); // 영화제목
            textView2.setText(item.audiCnt + " 명"); // 관람객 수
        }

    }

}
