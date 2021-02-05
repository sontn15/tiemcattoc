package com.buiduy.tiemcattoc.screen;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buiduy.tiemcattoc.R;
import com.buiduy.tiemcattoc.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ThongKeActivity extends AppCompatActivity {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private Spinner spThang, spThangThoGioi;
    private TextView tvNgaySearch, tvNgay, tvThang, tvSoLuongNgay, tvSoLuongThang, tvThangThoGioi, tvTenThoGioi, tvSoLanThoGioi;
    private Button btnThongKeNgay, btnThongKeThang, btnThongKeThoGioi;
    private String ngaySearch, thangSearch, thangSearchThoGioi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        initView();
        initDate();
        initAdapter();
        addEvents();
    }

    private void initView() {
        tvNgay = this.findViewById(R.id.tvDateThongKeNgay);
        tvNgaySearch = this.findViewById(R.id.tvDateSearchThongKeNgay);
        tvThang = this.findViewById(R.id.tvDateThongKeThang);
        tvSoLuongNgay = this.findViewById(R.id.tvSoLuongThongKeNgay);
        tvSoLuongThang = this.findViewById(R.id.tvSoLuongThongKeThang);
        btnThongKeNgay = this.findViewById(R.id.btnThongKeNgay);
        btnThongKeThang = this.findViewById(R.id.btnThongKeThang);
        btnThongKeThoGioi = this.findViewById(R.id.btnThongKeThangThoGioi);
        tvThangThoGioi = this.findViewById(R.id.tvDateThongKeThangThoGioi);
        tvTenThoGioi = this.findViewById(R.id.tvTenThongKeThangThoGioi);
        tvSoLanThoGioi = this.findViewById(R.id.tvSoLanThoGioi);
        spThang = this.findViewById(R.id.spnChonThang);
        spThangThoGioi = this.findViewById(R.id.spnChonThangThoGioi);

        tvNgay.setText("");
        tvThang.setText("");
        tvSoLuongNgay.setText("");
        tvSoLuongThang.setText("");
        tvTenThoGioi.setText("");
        tvSoLanThoGioi.setText("");
        tvThangThoGioi.setText("");
    }
// lay ngay hien tai
    private void initDate() {
        Calendar c1 = Calendar.getInstance();// lay ngay hTAI SET LEN VỈEW
        ngaySearch = df.format(c1.getTime());
        tvNgaySearch.setText(ngaySearch);
    }
// khoi tạo spiner
    private void initAdapter() {
        List<String> months = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");// SET THANG NAM LEN SPINER
        String date1 = "01/2017";
        String date2 = format.format(new Date()); //

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        try {
            beginCalendar.setTime(format.parse(date1));
            finishCalendar.setTime(format.parse(date2));
            while (!beginCalendar.after(finishCalendar)) {
                String month = format.format(beginCalendar.getTime()).toUpperCase();
                months.add(month);
                beginCalendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThang.setAdapter(dataAdapter);
        spThangThoGioi.setAdapter(dataAdapter);
    }

    private void addEvents() {
        tvNgaySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ThongKeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view1, int year1, int monthOfYear, int dayOfMonth) {
                                ngaySearch = (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)// xư ly chọn ngay
                                        + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "/" + year1;
                                tvNgaySearch.setText(ngaySearch);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        spThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thangSearch = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spThangThoGioi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thangSearchThoGioi = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnThongKeNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.URL_THONG_KE_NGAY + "?ngaycat=" + ngaySearch,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String tongSoLan = "0";
                                    jsonObject.getString("tongsolan");
                                    if (!jsonObject.getString("tongsolan").equalsIgnoreCase("null")) {
                                        tongSoLan = jsonObject.getString("tongsolan");
                                    }
                                    tvNgay.setText(ngaySearch);
                                    tvSoLuongNgay.setText(tongSoLan);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThongKeActivity.this, "Có lỗi xảy ra, kiểm tra lại" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        return null;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ThongKeActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        btnThongKeThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.URL_THONG_KE_THANG + "?thangcat=" + thangSearch,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String tongSoLan = "0";
                                    jsonObject.getString("tongsolan");
                                    if (!jsonObject.getString("tongsolan").equalsIgnoreCase("null")) {
                                        tongSoLan = jsonObject.getString("tongsolan");
                                    }
                                    tvThang.setText(thangSearch);
                                    tvSoLuongThang.setText(tongSoLan);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThongKeActivity.this, "Có lỗi xảy ra, kiểm tra lại" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(ThongKeActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        btnThongKeThoGioi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.URL_THONG_THO_GIOI + "?thangcat=" + thangSearchThoGioi,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String tongSoLan = "0";
                                    String tenThoGioi = "Chưa xác định";
                                    jsonObject.getString("tongsolan");
                                    if (!jsonObject.getString("tongsolan").equalsIgnoreCase("null")) {
                                        tongSoLan = jsonObject.getString("tongsolan");
                                    }
                                    jsonObject.getString("ten");
                                    if (!jsonObject.getString("ten").equalsIgnoreCase("null")) {
                                        tenThoGioi = jsonObject.getString("ten");
                                    }
                                    tvThangThoGioi.setText(thangSearchThoGioi);
                                    tvSoLanThoGioi.setText(tongSoLan);
                                    tvTenThoGioi.setText(tenThoGioi);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ThongKeActivity.this, "Có lỗi xảy ra, kiểm tra lại" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(ThongKeActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}
