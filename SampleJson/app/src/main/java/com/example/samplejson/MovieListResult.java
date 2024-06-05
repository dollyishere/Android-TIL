package com.example.samplejson;

import java.util.ArrayList;

/* json 문자열의 속성 중에서 값이 배열일 시, 즉 []일 경우 ArrayList를 이용하여 값을 저장 가능 */
public class MovieListResult {

    String boxofficeType;
    String showRange;

    ArrayList<Movie> dailyBoxOfficeList = new ArrayList<Movie>();

}
