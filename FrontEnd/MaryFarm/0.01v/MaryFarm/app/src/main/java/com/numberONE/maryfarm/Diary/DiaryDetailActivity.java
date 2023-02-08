package com.numberONE.maryfarm.Diary;

import static com.numberONE.maryfarm.R.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
=======
import android.content.SharedPreferences;
>>>>>>> 1925f4e1598ec31e201bc4ab3b522fbc474107e6
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.numberONE.maryfarm.Retrofit.Comments;
import com.numberONE.maryfarm.Retrofit.DetailsAPI;
import com.numberONE.maryfarm.Pick.PickActivity;
import com.numberONE.maryfarm.R;
import com.numberONE.maryfarm.Retrofit.ServerAPI;
import com.numberONE.maryfarm.databinding.ActivityDiaryDetailBinding;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiaryDetailActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    // 좋아요 구현
    private boolean sign=false;
    private TextView likeCount;
<<<<<<< HEAD
    private int likeCnt = 0;
    // Intent로 보낼 페이지 정보
    public TextView title;
    public TextView diaryContent;
    public Bitmap diaryImage;
=======
    private int likeCnt;
    private String commentContent;
>>>>>>> 1925f4e1598ec31e201bc4ab3b522fbc474107e6

    // 팝업 메뉴창 구현 (일지 추가하기, 수정하기, 재배완료 선택)
    ImageButton popUpBtn;

    // sharedpreference practice
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String userId;
    TextView textView;

    TextView contentView, likesView, titleView, startView, endView, afterView;
    ImageView profileImgView, detailImageView, commentImgView;
    EditText nicknameView, commentContentView;
    Button commentAddView;
    ImageButton nextBtnView, formBtnView;
    static int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_diary_detail);

        ActivityDiaryDetailBinding binding = ActivityDiaryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

<<<<<<< HEAD
        // 수정 페이지로 넘길 데이터
        title = binding.title;
        diaryContent = binding.diaryContent;
        BitmapDrawable diaryimg = (BitmapDrawable) binding.diaryDetailImage.getDrawable();
        diaryImage = diaryimg.getBitmap();
=======
        int[] diaryId_list = {7, 8};

        // 상세 일지 정보 레드토핏
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://985e5bce-3b72-4068-8079-d7591e5374c9.mock.pstmn.io/api/diary/plant/"+diaryId_list[num]+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI serverAPI = retrofit.create(ServerAPI.class);
        Call<DetailsAPI> call = serverAPI.getDetails();

        call.enqueue(new Callback<DetailsAPI>() {
            @Override
            public void onResponse(Call<DetailsAPI> call, Response<DetailsAPI> response) {
                DetailsAPI detailsAPIS = response.body();

                Map<Object, Object> plant = response.body().getPlant();

                Map<Object, Object> user = (Map<Object, Object>) plant.get("user");

                profileImgView = findViewById(id.profileImg);
                Glide.with(DiaryDetailActivity.this).load(user.get("profilepath"))
                        .into(profileImgView);

                String title = (String) plant.get("title");
                titleView = findViewById(id.title);
                titleView.setText(title);

                String createdDate = (String) plant.get("createdDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                LocalDateTime startDT = LocalDateTime.parse(createdDate, formatter);
                System.out.println(startDT);

                startView = findViewById(R.id.startDate);
                startView.setText(startDT.toString().substring(0,10));

//                LocalDateTime endDT = LocalDateTime.of(2021, 8, 1, 14, 30, 55);

                Boolean active = (Boolean) plant.get("active");
                String lastModifiedDate = (String) plant.get("lastModifiedDate");
                endView = findViewById(id.endDate);

                if (active) {
                    endView.setText("ing");
                } else {
                    endView.setText(lastModifiedDate);
                }

                detailImageView = findViewById(id.diaryDetailImage);
                Glide.with(DiaryDetailActivity.this).load(detailsAPIS.getImagepath())
                        .into(detailImageView);

                contentView = findViewById(R.id.diaryContent);
                contentView.setText(detailsAPIS.getContent());

                likesView = findViewById(id.like_Count);
                likesView.setText(detailsAPIS.getLikes()+"");
                likeCnt = detailsAPIS.getLikes();

                String nickname = (String) user.get("nickname");
                nicknameView = findViewById(id.inputComment);
                nicknameView.setHint(nickname + "(으)로 댓글 달기...");

                commentImgView = findViewById(id.commentProfile);
                Glide.with(DiaryDetailActivity.this).load(user.get("profilepath"))
                        .into(commentImgView);

                ImageButton formerBtn = (ImageButton) findViewById(id.formerBtn);
                if (num == 0){
                    formerBtn.setVisibility(View.GONE);
                }

                ImageButton nextBtn = (ImageButton) findViewById(id.nextBtn);
                if (num == diaryId_list.length-1){
                    nextBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DetailsAPI> call, Throwable t) {
                Log.d("DiaryDetail", t.toString());
            }
        });

        // sharedpreference practice
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        userId = pref.getString("userId", "Null");
        textView = findViewById(R.id.userId);
        textView.setText(userId);
>>>>>>> 1925f4e1598ec31e201bc4ab3b522fbc474107e6

        // 클릭시 - 좋아요 & 숫자 증가
        binding.emptyHeartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sign) {
                    binding.emptyHeartIcon.setVisibility(View.GONE);
                    binding.fullHeartIcon.setVisibility(View.VISIBLE);
                    sign = true;
                    likeCnt++;
                    binding.likeCount.setText(""+likeCnt);
                }
            }
        });

        // 클릭시 - 좋아요 취소 & 숫자 감소
        binding.fullHeartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sign) {
                    binding.emptyHeartIcon.setVisibility(View.VISIBLE);
                    binding.fullHeartIcon.setVisibility(View.GONE);
                    sign = false;
                    likeCnt--;
                    binding.likeCount.setText(""+likeCnt);
                }
            }
        });

        // 추천 버튼 클릭시, 추천 페이지로 화면 이동
        ImageButton pickBtn = (ImageButton) findViewById(id.pickBtn);
        pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryDetailActivity.this, PickActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 다음 버튼 클릭시, 다음 일지 페이지로 화면 이동
        ImageButton nextBtn = (ImageButton) findViewById(id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = num + 1;
                Intent intent = new Intent(DiaryDetailActivity.this, DiaryDetailActivity.class);
                intent.putExtra("num", num);
                startActivity(intent);
                finish();
            }
        });

        // 이전 버튼 클릭시, 이전 일지 페이지로 화면 이동
        ImageButton formerBtn = (ImageButton) findViewById(id.formerBtn);
        formerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = num - 1;
                Intent intent = new Intent(DiaryDetailActivity.this, DiaryDetailActivity.class);
                intent.putExtra("num", num);
                startActivity(intent);
                finish();
            }
        });

        Retrofit retrofitComment = new Retrofit.Builder()
                .baseUrl("https://985e5bce-3b72-4068-8079-d7591e5374c9.mock.pstmn.io/api/diary/comment/"+diaryId_list[num]+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI commentAPI = retrofitComment.create(ServerAPI.class);

        RecyclerView recyclerView = findViewById(id.commentsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<List<Comments>> callComment = commentAPI.getComments();
        callComment.enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> callComment, Response<List<Comments>> response) {
                List<Comments> callComments = response.body();
                Log.d("dd", "onResponse: !!!!!!!!!!!"+num);
                recyclerView.setAdapter(new CommentAdapter(getApplicationContext(), callComments));
            }

            @Override
            public void onFailure(Call<List<Comments>> callComment, Throwable t) {
                Log.d("MainActivity", t.toString());
            }
        });

        // "게시" 버튼 클릭시, 댓글 저장
        commentAddView = findViewById(R.id.commentAddBtn);
        commentContentView = findViewById(R.id.inputComment);
        // 버튼 기본 = 비활성
        commentAddView.setEnabled(false);
        // 댓글란 텍스트 작성시, 활성으로 변경
        commentContentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                commentContent = commentContentView.getText().toString();
                if (commentContent.length() == 0){
                    commentAddView.setEnabled(false);
                } else {
                    commentAddView.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        commentAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "🌱🌻🌼 축 재배완료! 🥕🥦🌶", Toast.LENGTH_LONG).show();
                commentContentView.setText(null);
            }
        });
    }

    // ... 버튼 클릭시 팝업 메뉴 출력 (일지 수정, 일지 추가, 지배완료)
    public void showPopBtn(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(menu.menu_diary_detail);
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case id.editDiary:
                Intent intent = new Intent(DiaryDetailActivity.this, DiaryModifyActivity.class);
                intent.putExtra("diaryTitle", title.getText().toString());
                intent.putExtra("diaryContent", diaryContent.getText().toString());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                diaryImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] diarybyte = stream.toByteArray();
                intent.putExtra("diaryImage", diarybyte);
                startActivity(intent);
                finish();
                return true;
            case id.addDiary:
                Intent intent1 = new Intent(DiaryDetailActivity.this, DiaryAddActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case id.plantComplete:
                Toast.makeText(this, "🌱🌻🌼 축 재배완료! 🥕🥦🌶", Toast.LENGTH_LONG).show();
                String koreaNow = LocalDate.now(ZoneId.of("Asia/Seoul")).toString();
                Log.d("dd", "korea date "+koreaNow);

                TextView endDate = findViewById(id.endDate);
                endDate.setText(koreaNow);

                return true;
            default:
                return false;
        }
    }

    // 키보드 이외의 곳 터치할 경우, 키보드 사라지게하기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}