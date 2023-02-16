package com.numberONE.maryfarm.ui.myfarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.numberONE.maryfarm.Diary.DiaryAddActivity;
import com.numberONE.maryfarm.Diary.DiaryDetailActivity;
import com.numberONE.maryfarm.R;
import com.numberONE.maryfarm.Retrofit.FollowFollowing;
import com.numberONE.maryfarm.Retrofit.ServerAPI;
import com.numberONE.maryfarm.Retrofit.dto.DetailDiariesPerPlantView.DetailDiariesPerPlantDTO;
import com.numberONE.maryfarm.Retrofit.dto.DetailDiariesPerPlantView.DetailDiaryDTO;
import com.numberONE.maryfarm.ui.AlgorithmPage.RecommendActivity;
import com.numberONE.maryfarm.Retrofit.Thumbnail;
import com.numberONE.maryfarm.Retrofit.UserInfo;
import com.numberONE.maryfarm.Retrofit.UserPlant;
import com.numberONE.maryfarm.databinding.FragmentMyfarmProfileBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HEAD;


public class MyfarmFragment extends Fragment {

    // 제가 작성한 글이 없을 시, 하단 페이지에서 보여줄 각 버튼 집합
    private View view;

    // 버튼 클릭시 디테일 화면으로 이동
    private Button detailBtn;

    // 취향 추천 알고리즘 페이지로 리다이렉트 시키는 버튼인데, 임시로 첫글 작성 버튼으로 만듦
    private ImageButton recommendBtn;

    // 취향 추천 알고리즘 페이지로 리다이렉트 시키는 버튼
    private ImageButton recommendMonthBtn;

    private TextView nickname;

    private ImageView userProfile;
    private String URL = "https://s3.ap-northeast-2.amazonaws.com/maryfarm.bucket/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myfarm_profile,container,false);

        SharedPreferences pref;
        String userId, userNickname, userImage;

        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        userId = pref.getString("userId", "Null");
        userNickname = pref.getString("userNickname", "Null");
        userImage = pref.getString("userImg","Null");


        recommendBtn = (ImageButton) view.findViewById(R.id.recommendBtn);
        recommendMonthBtn = (ImageButton) view.findViewById(R.id.recommendMonthBtn);
        nickname = (TextView) view.findViewById(R.id.myFarmName);
        nickname.setText(userNickname);
        userProfile = (ImageView) view.findViewById(R.id.profile_image);
        Glide.with(MyfarmFragment.this).load(URL + userImage).into(userProfile);

        TextView followerCnt = (TextView) view.findViewById(R.id.followCnt);
        TextView followingCnt = (TextView) view.findViewById(R.id.followingCnt);

        RecyclerView recyclerView = view.findViewById(R.id.plantThumbnail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("https://985e5bce-3b72-4068-8079-d7591e5374c9.mock.pstmn.io/api/")
                //.baseUrl("https://maryfarm.shop/maryfarm-user-service/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI serverAPI1 = retrofit1.create(ServerAPI.class);
        Call<FollowFollowing> call1 = serverAPI1.getFollowInfo(userId);
        call1.enqueue(new Callback<FollowFollowing>() {
            @Override
            public void onResponse(Call<FollowFollowing> call1, Response<FollowFollowing> response) {
                followerCnt.setText(response.body().getFollowerCount()+"");
                followingCnt.setText(response.body().getFollowingCount()+"");

                Bundle bundle=getArguments();// 번들 받기
                if(bundle!=null){
                    String param=bundle.getString("parameter");
                    Log.d("MyfarmFragment", "write로 부터 넘어온 값"+ param);
                }
            }

            @Override
            public void onFailure(Call<FollowFollowing> call1, Throwable t) {

            }
        });


        // 업로드한 작물 피드 유무 확인
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://985e5bce-3b72-4068-8079-d7591e5374c9.mock.pstmn.io/api/")
                .baseUrl("https://maryfarm.shop/maryfarm-plant-service/api/")
                //.baseUrl("http://192.168.31.244:8000/maryfarm-plant-service/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI serverAPI = retrofit.create(ServerAPI.class);
        Call<List<UserPlant>> call = serverAPI.getUserPlant(userId);
        call.enqueue(new Callback<List<UserPlant>>() {
            @Override
            public void onResponse(Call<List<UserPlant>> call, Response<List<UserPlant>> response) {
                Log.d("", "onResponse: !!!!"+response.body());
                List<UserPlant> plantsId = response.body();

                List<String> list = new ArrayList<>();
                for(UserPlant u : plantsId) {
                    list.add(u.getPlantId());
                }

                if (response.body() == null){
                    recommendBtn.setVisibility(View.VISIBLE);
                    recommendMonthBtn.setVisibility(View.VISIBLE);
                } else {
                    List<Thumbnail> planThumbnails =new ArrayList<>();

                    for (int i=0; i <= list.size()-1; i++) {
                        List<DetailDiariesPerPlantDTO> diaries = new ArrayList<>();


                        Gson gson = new GsonBuilder().setLenient().create();
                        Retrofit retrofit2 = new Retrofit.Builder()
                                //.baseUrl("https://985e5bce-3b72-4068-8079-d7591e5374c9.mock.pstmn.io/api/")
                                .baseUrl("https://maryfarm.shop/maryfarm-plant-service/api/")
                                //.baseUrl("http://192.168.31.244:8000/maryfarm-plant-service/api/")
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        ServerAPI serverAPI2 = retrofit2.create(ServerAPI.class);
                        Call<DetailDiariesPerPlantDTO> call2 = serverAPI2.getDiaries(list.get(i));
                        call2.enqueue(new Callback<DetailDiariesPerPlantDTO>() {
                            @Override
                            public void onResponse(Call<DetailDiariesPerPlantDTO> call2, Response<DetailDiariesPerPlantDTO> response) {
                                if (response.body() != null) {
                                    DetailDiariesPerPlantDTO detailDiariesPerPlantDTO = response.body();
                                    String title = response.body().getTitle();
                                    String plantId = response.body().getPlantId();
                                    String thumbImg1 = null;
                                    String thumbImg2 = null;
                                    String thumbImg3 = null;
                                    String plantCreatedDate = response.body().getPlantCreatedDate();
                                    String harvestDate = response.body().getHarvestDate();

                                    for (int j = response.body().getDiaries().size() - 1; j >= 0; j--) {
                                        List<DetailDiaryDTO> diaries = (List) response.body().getDiaries();
                                        DetailDiaryDTO diary = diaries.get(j);

                                        if (j == response.body().getDiaries().size() - 1) {
                                            thumbImg1 = diary.getImagePath();
                                        } else if (j == response.body().getDiaries().size() - 2) {
                                            thumbImg2 = diary.getImagePath();
                                        } else if (j == response.body().getDiaries().size() - 3) {
                                            thumbImg3 = diary.getImagePath();
                                        }
                                    }

                                    Thumbnail thumbnail = new Thumbnail(title, thumbImg1, thumbImg2, thumbImg3, plantId, plantCreatedDate, harvestDate, null,detailDiariesPerPlantDTO);
                                    planThumbnails.add(thumbnail);

                                    recyclerView.setAdapter(new MyfarmAdapter(getContext(), planThumbnails));
                                }
                            }


                            @Override
                            public void onFailure(Call<DetailDiariesPerPlantDTO> call, Throwable t) {
                                Log.d("Failure : ", t.toString());
                            }
                        });
                    }

//                    Retrofit retrofit2 = new Retrofit.Builder()
//                            .baseUrl("https://985e5bce-3b72-4068-8079-d7591e5374c9.mock.pstmn.io/api/")
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//
//                    ServerAPI serverAPI2 = retrofit2.create(ServerAPI.class);
//                    Call<UserInfo> call2 = serverAPI2.getDiaries();

                }
            }

            @Override
            public void onFailure(Call<List<UserPlant>> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

        recommendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DiaryAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        recommendMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecommendActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return view;
    }

}