package com.buiduy.tiemcattoc.screen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.buiduy.tiemcattoc.object.KhachHang;
import com.buiduy.tiemcattoc.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;

public class SuaKhachHangActivity extends AppCompatActivity {
    KhachHang kh;
    MainActivity ds;
    int id_suakhach;
    //------------------------------------------------
    int gioitinh = 1;

    EditText edtTenKhachHang, edtNgaySinhKhachHang;
    EditText edtSdtKhachHang, edtDiaChiKhachHang, edtMoTaKhachHang;

    TextView txvHuyBo, txvxoathongtinkhach, txvsuakhachhang;
    TextView txvNam, txvNu;


    //DatePickerDialog.OnDateSetListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_khach_hang);

        edtNgaySinhKhachHang = (EditText) findViewById(R.id.edtNgaySinhKhachHang);
        edtNgaySinhKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonngay(); // bật dialogdatepicker
            }
        });
        addViews();
        xulylaykhachhang();
        addEvents();

    }

    //-----------------------------------------------------------------------
    //bắt sự kiện
    private void addEvents() {
        txvNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gioitinh = 1;
                gioitinh1();
                //Toast.makeText(getApplicationContext(),"bạn đã chọn NAM", Toast.LENGTH_SHORT).show();
            }
        });

        txvNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gioitinh = 0;
                gioitinh0();
                //Toast.makeText(getApplicationContext(),"bạn đã chọn NỮ", Toast.LENGTH_SHORT).show();
            }
        });

        txvsuakhachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //xulylaykhachhang();
                xyLysuakhachhang();
                //dismiss();
                //Toast.makeText(getApplicationContext(), "bắt sự kiện click Sửa khách hàng".toString(), Toast.LENGTH_LONG).show();

            }
        });
        //----------------------------------------------------
        // xóa dữ liệu khách hàng
        txvxoathongtinkhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTenKhachHang.setText("");
                edtDiaChiKhachHang.setText("");
                edtNgaySinhKhachHang.setText("");
                edtSdtKhachHang.setText("");
                edtMoTaKhachHang.setText("");

            }
        });

        txvHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void gioitinh1() {
        txvNam.setBackgroundColor(BLACK);
        txvNam.setTextColor(Color.YELLOW);
        txvNu.setBackgroundColor(Color.WHITE);
        txvNu.setTextColor(BLACK);
    }

    private void gioitinh0() {
        txvNam.setBackgroundColor(Color.WHITE);
        txvNam.setTextColor(BLACK);
        txvNu.setBackgroundColor(BLACK);
        txvNu.setTextColor(Color.YELLOW);
    }

    //--------------------------------------------------------
    //ánh xạ
    private void addViews() {
        edtTenKhachHang = findViewById(R.id.edtTenKhachHang);
        edtNgaySinhKhachHang = findViewById(R.id.edtNgaySinhKhachHang);
        edtDiaChiKhachHang = findViewById(R.id.edtDiaChiKhachHang);
        edtSdtKhachHang = findViewById(R.id.edtSdtKhachHang);
        edtMoTaKhachHang = findViewById(R.id.edtMoTaKhachHang);

        txvNam = findViewById(R.id.txvNam);
        txvNu = findViewById(R.id.txvNu);
        txvHuyBo = findViewById(R.id.txvHuyBo);
        txvxoathongtinkhach = findViewById(R.id.txvxoathongtinkhach);
        txvsuakhachhang = findViewById(R.id.txvsuakhachhang);
    }

    private void chonngay()// tạo funtion // ấn vào edittext ngày sinh hiện ra một dialog
    {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i:năm - i1:tháng - i2:ngày
                calendar.set(i, i1, i2); //chọn theo người dùng
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // băt sự kiện ngày tháng năm
                edtNgaySinhKhachHang.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    //=----------------------------------------------------------------------------------------
    private void xulylaykhachhang() {
        Intent data = getIntent();//lay du lieu khách hàng chuyển snag màn hình
        id_suakhach = data.getIntExtra("idkhach", 0);
        String ten = data.getStringExtra("ten");
        edtTenKhachHang.setText(ten);
        edtNgaySinhKhachHang.setText(data.getStringExtra("ngaysinh"));
        edtDiaChiKhachHang.setText(data.getStringExtra("diachi"));
        edtSdtKhachHang.setText(data.getStringExtra("sdt"));
        edtMoTaKhachHang.setText(data.getStringExtra("mota"));

        int gtinh = data.getIntExtra("gioitinh", -1);
        if (gtinh == 1) {
            gioitinh = 1;
            gioitinh1();
        } else {
            gioitinh = 0;
            gioitinh0();
        }
        //Toast.makeText(getApplicationContext(),"ID Tên khách sửa nhận được : "+id_suakhach,Toast.LENGTH_SHORT).show();
    }

    private void xyLysuakhachhang() {
        final String ten, ngaysinh, sdt, diachi, mota;
        ten = edtTenKhachHang.getText().toString();
        ngaysinh = edtNgaySinhKhachHang.getText().toString();
        sdt = edtSdtKhachHang.getText().toString();
        diachi = edtDiaChiKhachHang.getText().toString();
        mota = edtMoTaKhachHang.getText().toString();

        if (ten.length() > 0 && sdt.length() > 0 && ngaysinh.length() > 0 && diachi.length() > 0) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_suakhachhang, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("1")) {
                        Toast.makeText(getApplicationContext(), "Sửa Khách hàng thành công!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Sửa khách hàng thất bại!" + response, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SuaKhachHangActivity.this, "Lỗi sửa thông tin khách hàng: " + error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("idkhach", id_suakhach);
                        jsonObject.put("ten", ten);
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
                    hashMap.put("suakhachhang_json", jsonArray.toString());
                    return hashMap;
                }
            };
            //Toast.makeText(getApplicationContext(),"id_suaKhach = "+id_suakhach, Toast.LENGTH_SHORT).show();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
