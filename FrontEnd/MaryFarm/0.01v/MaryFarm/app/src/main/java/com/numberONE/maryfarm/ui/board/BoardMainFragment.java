package com.numberONE.maryfarm.ui.board;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.numberONE.maryfarm.R;
import com.numberONE.maryfarm.Retrofit.Board.BoardArticle;
import com.numberONE.maryfarm.Retrofit.RetrofitApiSerivce;
import com.numberONE.maryfarm.Retrofit.RetrofitClient;
import com.numberONE.maryfarm.databinding.FragmentBoardMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardMainFragment extends Fragment {
    private static final String TAG = "BoardMainFragment";

    FragmentBoardMainBinding binding;

    RecyclerView recyclerView_board;

    RecyclerView.LayoutManager layoutManager_board;

    RecyclerView.Adapter adapter_board;

    List<BoardArticle> article;

    String region;
    private SharedPreferences preferences;

    public BoardMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentBoardMainBinding.inflate(inflater,container,false);

//        region = binding.boardSpinner.getSelectedItem().toString();

//      ------- 스피너 설정 --------
//        spinnerAdapter=ArrayAdapter.createFromResource(getActivity(),
//                R.array.board_spinner,R.layout.custom_spinner_layout);
//        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
//        binding.boardSpinner.setAdapter(spinnerAdapter);
        //      -------- 스피너 설정 끝 --------

//      ------------ 게시글 작성 후 해당 게시판으로 설정 -------------
        preferences=getActivity().getSharedPreferences("board",Context.MODE_PRIVATE);
        region = preferences.getString("type","type is null");
        Log.d(TAG, "shared spinner default region :  " + region);
        binding.boardSpinner.setSelection(spinnerDefaultType(region));
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.commit();
//      ------------- 게시글 작성 후 해당 게시판으로 설정 끝 -----------


        recyclerView_board=binding.boardRecycler;
        layoutManager_board=new LinearLayoutManager(getActivity());
        recyclerView_board.setLayoutManager(layoutManager_board);

//       ------- 지역 선택 스피너 서버 통신 ----------
        // 지역 선택하면 서버에 요청보내서 지역에 맞는 게시판 정보 불러오기
        binding.boardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                region = binding.boardSpinner.getSelectedItem().toString();
//             ------------  해당 게시판으로 이동 서버 연결 ----------
                RetrofitApiSerivce service = RetrofitClient.getInstance().create(RetrofitApiSerivce.class);
                Log.d(TAG, " 지역 게시판에 넣어준 지역 :  " +region);
                service.postRegion(region).enqueue(new Callback<List<BoardArticle>>() {
                    @Override
                    public void onResponse(Call<List<BoardArticle>> call, Response<List<BoardArticle>> response) {
                        Log.d(TAG, " 지역에 맞는 게시글들 불러오기 " );
                        Log.d(TAG, "postRegion res.code : " + response.code());
                        Log.d(TAG, "postRegion res body : " + response.body());
                        if(response.isSuccessful()){
                            article=response.body();
                            Log.d(TAG, "isSuccessful article"+ article.toString());
                            Log.d(TAG, "postRegion 서버 통신 완료");
                            Log.d(TAG, "postRegion 서버 코드 " +response.code());

                            //      -------    리사이클러 뷰 아이템  커스텀 클릭 리스너 처리 ----
                            BoardAdapter boardAdapter = new BoardAdapter(article);
                            recyclerView_board.setAdapter(boardAdapter);
                            boardAdapter.setOnItemClickListener(new BoardAdapter.OnItemClickListner() {
                                @Override
                                public void onItemClick(View v, int position, String articleId) {
                                    preferences=getActivity().getSharedPreferences("board", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=preferences.edit();
                                    editor.putString("board_articleId",articleId); // 아티클 아이디 저장 후 intent로 화면 전환
                                    editor.commit();
                                    BoardDetailFragment fragment=new BoardDetailFragment();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,fragment).commitAllowingStateLoss();
                                }
                            });
                            //      -------    리사이클러 뷰 아이템 클릭 리스너 처리 ----

                        }
                        if(response.code()==500){
                            if(article!=null){
                                Log.d(TAG, "500에러 : article : "+ article);
                            }else{
                                Log.d(TAG, "500에러 아티클 null ");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BoardArticle>> call, Throwable t) {
                        t.printStackTrace();
                        Log.d(TAG, "postRegion 서버 통신 오류 ");
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        ---- 게시판 글 작성 버튼 -----
        binding.boardEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoardWriteFragment fragment =new BoardWriteFragment();
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_activity,fragment).commitAllowingStateLoss();

            }
        });
//        ---- 게시판 글 작성 버튼 종료 -----




        ViewGroup view =binding.getRoot();
        return view;
    }

//         -------- 리사이클러 뷰  -----------
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        recyclerView_board=binding.boardRecycler;
//        layoutManager_board=new LinearLayoutManager(getActivity());
//        recyclerView_board.setLayoutManager(layoutManager_board);
//
//        adapter_board=new BoardAdapter(article);
//        recyclerView_board.setAdapter(adapter_board);
//    }
//         -------- 리사이클러 뷰 종료 -----------


    // 글 작성 후 해당 게시판 매칭 메서드
    private int spinnerDefaultType(String name){
        int num=0;
        switch (name){
            case "서울": num=0; break;
            case "인천": num=1; break;
            case "강원": num=2; break;
            case "충청": num=3; break;
            case "경상": num=4; break;
            case "전라": num=5; break;
            case "제주": num=6; break;
        }
        return num;
    }
    // 글 작성 후 해당 게시판 매칭 메서드
    private String spinnerDefaultType(int num){
        String str="";
        switch (num){
            case 0: str="서울"; break;
            case 1: str="인천"; break;
            case 2: str="강원"; break;
            case 3: str="충청"; break;
            case 4: str="경상"; break;
            case 5: str="전라"; break;
            case 6: str="제주"; break;
        }
        return str;
    }

}