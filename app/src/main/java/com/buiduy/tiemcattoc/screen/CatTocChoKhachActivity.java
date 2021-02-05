package com.buiduy.tiemcattoc.screen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.buiduy.tiemcattoc.adapter.DanhSachThoAdapter;
import com.buiduy.tiemcattoc.object.KhachHang;
import com.buiduy.tiemcattoc.object.ThoCatToc;
import com.buiduy.tiemcattoc.server.Server;
import com.buiduy.tiemcattoc.utility.BitmapConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CatTocChoKhachActivity extends AppCompatActivity {
    KhachHang kh;
    MainActivity ds;
    DanhSachThoAdapter adapter;
    ArrayList<ThoCatToc> arrTH;
    ImageView img_anh;

    TextView txvTenKhachHang,txvHuyBo;
    TextView txvluukhachcattoc;
    EditText edtngaykhachcattoc;
    Spinner spnThoCatToc;
    TextView txvmayanh,txvthuvien;
    Button btn_chupanh, btn_chonanh;
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 312;

    int idtho=0;
    int id_khachcattoc;
    String ngaycat;
    String strAnh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_toc_cho_khach);

        edtngaykhachcattoc=(EditText)findViewById(R.id.edtngaykhachcattoc);
        edtngaykhachcattoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonngay(); // bật dialogdatepicker
            }
        });

        addViews();
        addEvents();
        xulylaytenkhachhang();
        xulylayalltho();
    }
    private void addViews() {
        txvTenKhachHang = findViewById(R.id.txvTenKhachHang);
        edtngaykhachcattoc=findViewById(R.id.edtngaykhachcattoc);
        img_anh = findViewById(R.id.img_anh);
        img_anh.setScaleType(ImageView.ScaleType.CENTER_CROP);
             //-----------------------------------------------------
        btn_chupanh = findViewById(R.id.btn_chupanh);
        btn_chonanh = findViewById(R.id.btn_chonanh);
        //-----------------------------------------------------
        txvHuyBo=findViewById(R.id.txvHuyBo);
        txvluukhachcattoc=findViewById(R.id.txvluukhachcattoc);
        spnThoCatToc=findViewById(R.id.spnThoCatToc);
    }
    private  void chonngay()// tạo funtion // ấn vào edittext ngày sinh hiện ra một dialog
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
                edtngaykhachcattoc.setText(simpleDateFormat.format(calendar.getTime()));
                ngaycat = edtngaykhachcattoc.getText().toString();
                //Toast.makeText(getApplicationContext(),"Ngay cat : "+ngaycat, Toast.LENGTH_SHORT).show();
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void xulylaytenkhachhang() {
        Intent data = getIntent();//lay du lieu khách hàng chuyển snag màn hình
        id_khachcattoc = data.getIntExtra("idkhach", 0);
        String ten = data.getStringExtra("ten");
        txvTenKhachHang.setText(ten);
        //txvTenKhachHang.setText(kh.ten);
    }

    private void addEvents() {
        //băt sự kiện lưu khách cắt tóc
        txvluukhachcattoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "click lưu : ", Toast.LENGTH_LONG).show();
                xulyluukhachcattoc();
           }
        });

        txvHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_chupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChupAnh();
            }
        });

        btn_chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonAnh();
            }
        });

        spnThoCatToc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"id tho = "+arrTH.get(position).idtho, Toast.LENGTH_SHORT).show();
                idtho = arrTH.get(position).idtho;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idtho=1;
            }
        });
    }

    private void xulyluukhachcattoc() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_khachcattoc, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Cập nhật khách cắt tóc thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cập nhật khách cắt tóc thất bại!"+response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Lỗi cập nhật khách cắt tóc" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("idkhach", id_khachcattoc);
                    jsonObject.put("idtho", idtho);
                    jsonObject.put("ngaycat", ngaycat);
                    jsonObject.put("anh", strAnh);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("khachcattoc_json", jsonArray.toString());
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    //------xử lý chụp ảnh----------------------------------------
    private void xuLyChupAnh(){
        Intent intent_chupanh = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent_chupanh, REQUEST_TAKE_PHOTO);
    }
    //----------------Xử lý chọn ảnh-----------------------------------
    private void xuLyChonAnh(){
        Intent intent_chonanh = new Intent(Intent.ACTION_PICK);
        intent_chonanh.setType("image/*");
        startActivityForResult(intent_chonanh, REQUEST_CHOOSE_PHOTO);
    }
    //-----------------------------------------------------------
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap=null;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                Uri imageUrl = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(imageUrl);
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            // HIển thị ảnh chọn hoặc chụp lên giao diện
            img_anh.setImageBitmap(bitmap);

            // Lấy ảnh từ img_anh vào APP
            Bitmap bitmap2 = ((BitmapDrawable)img_anh.getDrawable()).getBitmap();
            // Mã hóa ảnh bitmap2 thành chuỗi để ghi vào HOST
            strAnh = BitmapConverter.bitmapToString(bitmap2);
            Log.d("strAnh", "onActivityResult: "+strAnh);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------
    private void xulylayalltho() {
            arrTH = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_layallthocat, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getApplicationContext(), "Danh sách thợ: "+response, Toast.LENGTH_SHORT).show();
                    if (response.equals("0")) {
                        Toast.makeText(getApplicationContext(), "Không lấy được danh sách Thợ cắt!", Toast.LENGTH_SHORT).show();
                    } else { // Lấy được danh sách thợ cắt
                        try {
                            if (arrTH.size() > 0) {
                                arrTH.clear();
                            }
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                ThoCatToc th = new ThoCatToc();
                                th.idtho = jsonObject.getInt("idtho");
                                th.ten = jsonObject.getString("ten");
                                arrTH.add(th);
                            }
                            adapter = new DanhSachThoAdapter(CatTocChoKhachActivity.this, 0, arrTH);
                            adapter.setDropDownViewResource(R.layout.item_text_tho_cat);
                            spnThoCatToc.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), "Lỗi lấy danh sách thợ từ HOST: "+error, Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
        //----------------------------------------------------------------------------------
    // sử lý lưu khách hàng căt tóc lần tiếp theo

}


