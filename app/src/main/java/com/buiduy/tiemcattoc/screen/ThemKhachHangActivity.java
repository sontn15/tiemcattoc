package com.buiduy.tiemcattoc.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buiduy.tiemcattoc.R;
import com.buiduy.tiemcattoc.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;

public class ThemKhachHangActivity extends AppCompatActivity {

    int gioitinh=1;

    EditText edtTenKhachHang, edtNgaySinhKhachHang;
    EditText edtSdtKhachHang,edtDiaChiKhachHang, edtMoTaKhachHang;

    TextView txvNam, txvNu, txvthemOrSua, txvHuyBo;
    //DatePickerDialog.OnDateSetListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_khach_hang);
        edtNgaySinhKhachHang=(EditText)findViewById(R.id.edtNgaySinhKhachHang);
        edtNgaySinhKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            chonngay(); // bật dialogdatepicker
            }
        });

        addViews();
        addEvents();
    }
    private  void chonngay()// tạo funtion // ấn vào edittext ngày sinh hiện ra một dialog
    {
        final Calendar calendar = Calendar.getInstance();
        int ngay=calendar.get(Calendar.DATE);
        int thang=calendar.get(Calendar.MONTH);
        int nam=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog= new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i:năm - i1:tháng - i2:ngày
            calendar.set(i,i1,i2); //chọn theo người dùng
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // băt sự kiện ngày tháng năm
            edtNgaySinhKhachHang.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam,thang,ngay);
        datePickerDialog.show();


    }
    // Anh xạ
    private void addViews() {
        edtTenKhachHang = findViewById(R.id.edtTenKhachHang);
        edtDiaChiKhachHang = findViewById(R.id.edtDiaChiKhachHang);
        edtNgaySinhKhachHang = findViewById(R.id.edtNgaySinhKhachHang);
         edtSdtKhachHang= findViewById(R.id.edtSdtKhachHang);
        edtMoTaKhachHang = findViewById(R.id.edtMoTaKhachHang);

        txvNam = findViewById(R.id.txvNam);
        txvNu = findViewById(R.id.txvNu);
        txvthemOrSua = findViewById(R.id.txvthemOrSua);
        txvHuyBo = findViewById(R.id.txvHuyBo);
    }

    private void addEvents() {
        txvNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gioitinh=1;
                //Toast.makeText(getApplicationContext(),"bạn đã chọn NAM", Toast.LENGTH_SHORT).show();
                txvNam.setBackgroundColor(BLACK);
                txvNam.setTextColor(Color.YELLOW);
                txvNu.setBackgroundColor(Color.WHITE);
                txvNu.setTextColor(BLACK);
            }
        });

        txvNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gioitinh=0;
                //Toast.makeText(getApplicationContext(),"bạn đã chọn NỮ", Toast.LENGTH_SHORT).show();
                txvNam.setBackgroundColor(Color.WHITE);
                txvNam.setTextColor(BLACK);
                txvNu.setBackgroundColor(BLACK);
                txvNu.setTextColor(Color.YELLOW);
            }
        });

        txvthemOrSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xyLyThemKhachHang();
            }
        });

        txvHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void xyLyThemKhachHang() {
        final String tenkh, ngaysinh, sdt, diachi, mota;
        tenkh = edtTenKhachHang.getText().toString();
        ngaysinh = edtNgaySinhKhachHang.getText().toString();
        sdt = edtSdtKhachHang.getText().toString();
        diachi = edtDiaChiKhachHang.getText().toString();
        mota = edtMoTaKhachHang.getText().toString();

        if(tenkh.length()==0 || sdt.length()==0 || ngaysinh.length()==0 || diachi.length()==0) {
            Toast.makeText(getApplicationContext(),"Nhập thông tin thiếu!", Toast.LENGTH_LONG).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_themkhachhang, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Thêm khách hàng thành công!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Thêm khách hàng thất bại!"+response, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ThemKhachHangActivity.this, "Lỗi thêm khách hàng: " + error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ten", tenkh);
                        jsonObject.put("ngaysinh", ngaysinh);
                        jsonObject.put("sdt", sdt);
                        jsonObject.put("diachi", diachi);
                        jsonObject.put("gioitinh", gioitinh);
                        jsonObject.put("mota", mota);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("khachhang_json", jsonArray.toString());
                    return hashMap;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}