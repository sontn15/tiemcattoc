package com.buiduy.tiemcattoc.screen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buiduy.tiemcattoc.R;
import com.buiduy.tiemcattoc.object.LichSu;
import com.buiduy.tiemcattoc.server.Server;
import com.buiduy.tiemcattoc.utility.BitmapConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LichSuCatTocActivity extends AppCompatActivity {

    int vitri=0;
    int idkhach;
    int solancat;

    TextView txvTenKhachHang, txvSoLanCat, txvTenThoCat, txvngaycat, txvThoatLichSu;
    ImageButton imgbtn_lui, imgbtn_tien;
    ImageView img_anh;

    ArrayList<LichSu> arrLichSu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_cat_toc);

        addViews();
        addEvents();
    }

    private void addViews() {
        txvTenKhachHang = findViewById(R.id.txvTenKhachHang);
        txvSoLanCat = findViewById(R.id.txvSoLanCat);
        txvTenThoCat = findViewById(R.id.txvTenThoCat);
        txvngaycat = findViewById(R.id.txvngaycat);
        txvThoatLichSu = findViewById(R.id.txvThoatLichSu);

        imgbtn_tien = findViewById(R.id.imgbtn_tien);
        imgbtn_lui = findViewById(R.id.imgbtn_lui);

        img_anh = findViewById(R.id.img_anh);
    }

    private void addEvents() {
        getdata();
        getLichSuCatToc();
        //xuLyTienLui();
        xuLyThoat();
    }

    private void xuLyTienLui() {
        if(arrLichSu.size() > 0) {
            // Xử lý vị trí tiến
            imgbtn_tien.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vitri < arrLichSu.size() - 1) {
                        vitri++;
                    } else {
                        vitri = 0;
                    }
                    txvSoLanCat.setText((vitri + 1) + "");
                    // Đặt dữ liệu tại vitri lên giao diện
                    initData(arrLichSu.get(vitri).tenkhach,arrLichSu.get(vitri).tentho,arrLichSu.get(vitri).ngaycat,arrLichSu.get(vitri).anh );

                }
            });
            // Xử lý vị trí lùi
            imgbtn_lui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vitri > 0) {
                        vitri--;
                    } else {
                        vitri = arrLichSu.size() - 1;
                    }
                    txvSoLanCat.setText((vitri + 1) + "");
                    // Đặt dữ liệu tại vitri lên giao diện
                    initData(arrLichSu.get(vitri).tenkhach,arrLichSu.get(vitri).tentho,arrLichSu.get(vitri).ngaycat,arrLichSu.get(vitri).anh );

                }
            });
        } else {
            Toast.makeText(LichSuCatTocActivity.this,"Dữ liệu lịch sử RỖNG!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getdata() {
        // Lấy thông tin id, số lần cắt tóc
        Intent intent_data = getIntent();
        idkhach = intent_data.getIntExtra("idkhach",0);
        solancat = intent_data.getIntExtra("solancat",0);
        txvSoLanCat.setText(solancat+"");
    }

    private void getLichSuCatToc() {
        arrLichSu = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_lichsu, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                if(response.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Lấy thông tin lịch sử thất bại!", Toast.LENGTH_LONG).show();
                } else {
                // Tách lấy dữ liệu lịch sử khách cắt tóc
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            LichSu lichSu = new LichSu();
                            lichSu.idkhach = jsonObject.getInt("idkhach");
                            lichSu.tenkhach = jsonObject.getString("tenkhach");
                            lichSu.idtho = jsonObject.getInt("idtho");
                            lichSu.tentho = jsonObject.getString("tentho");
                            lichSu.ngaycat = jsonObject.getString("ngaycat");
                            lichSu.anh = BitmapConverter.stringToBitmap(jsonObject.getString("anh"));
                            arrLichSu.add(lichSu);
                        }
                        // Đưa lichSu[0] lên giao diện
                        initData(arrLichSu.get(0).tenkhach,arrLichSu.get(0).tentho,arrLichSu.get(0).ngaycat,arrLichSu.get(0).anh );
                        xuLyTienLui();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LichSuCatTocActivity.this, "Lấy thông tin lịch sử từ HOST thất bại" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("idkhach",idkhach);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("lichsucat_json", jsonArray.toString());
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initData(String tenkhach, String tentho, String ngaycat, Bitmap anh) {
        txvTenKhachHang.setText(tenkhach);
        txvTenThoCat.setText(tentho);
        txvngaycat.setText(ngaycat);
        img_anh.setImageBitmap(anh);
    }

    private void xuLyThoat() {
        txvThoatLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}