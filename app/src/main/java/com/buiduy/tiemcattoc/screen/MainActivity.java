package com.buiduy.tiemcattoc.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buiduy.tiemcattoc.R;
import com.buiduy.tiemcattoc.adapter.DanhSachAdapter;
import com.buiduy.tiemcattoc.dialog.LuaChonKhachHangDialog;
import com.buiduy.tiemcattoc.dialog.ThemThoCatDialog;
import com.buiduy.tiemcattoc.object.KhachHang;
import com.buiduy.tiemcattoc.server.Server;
import com.buiduy.tiemcattoc.utility.KiemTraInternet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<KhachHang> arrKH;
    //ArrayList<ThoCatToc>arrTH;
    DanhSachAdapter adapter;
    ListView lst_danhsachkhachhang;
    ImageView img_luachonthem;
    TextView txtNam, txtNu;
    EditText edtTenKhachHang, edtSdtKhachHang;
    String TenKhachHang = "";
    ImageButton img_tailai;
    Button btn_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addViews();
        addEvents();
        init();
        ControlButton();

    }

    //resert lại danh sách
    private void init() {

        if (KiemTraInternet.checkInternetConnection(getApplicationContext())) {
            layallkhachhang();
        }
    }

    private void addViews() {
        //ÁNH XẠ
        //img_luachonthem = findViewById(R.id.img_luachonthem);
        lst_danhsachkhachhang = findViewById(R.id.lst_danhsachkhachhang);
        edtTenKhachHang = findViewById(R.id.edtTenKhachHang);
        edtSdtKhachHang = findViewById(R.id.edtSdtKhachHang);
        img_tailai = findViewById(R.id.img_tailai);
    }

    private void addEvents() {
        layallkhachhang();
        //-------------------------------------------
//        img_luachonthem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Chuyển từ màn hình MainActivity sang màn hình ThemKhachHangActivity
//                Intent intent_themKH = new Intent(MainActivity.this, ThemKhachHangActivity.class);
//                intent_themKH.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent_themKH);
//            }
//        });
        //----------------------------------
        edtSdtKhachHang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String textTimKiem = edtSdtKhachHang.getText().toString();
//                if (textTimKiem.length() > 0) {
//                    timKiemThongTin(textTimKiem);
//                } else {
//                    layallkhachhang();
//                }
            }
        });
        // Mở mục chọn Sua, Xoa, Cat, Lich su
        lst_danhsachkhachhang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new LuaChonKhachHangDialog(MainActivity.this, arrKH.get(position)).show();
                return false;
            }
        });
    }

    private void timKiemThongTin(String textTimKiem) {
        ArrayList<KhachHang> arrKH = new ArrayList<>();
        if (arrKH.size() > 0) {
            for (int i = 0; i < arrKH.size(); i++) {
                if (arrKH.get(i).sdt.indexOf(textTimKiem, 0) < 0 || arrKH.get(i).ten.indexOf(textTimKiem, 0) < 0) {
                    //arrKH_Chon.add(arrKH.get(i));
                    arrKH.remove(i);
                }
            }
            adapter = new DanhSachAdapter(MainActivity.this, 0, arrKH);
            lst_danhsachkhachhang.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Dữ liệu rỗng!", Toast.LENGTH_SHORT).show();
        }
    }
    //-------------------------------------------------------------------

    //-------------------------------------------------------------------
    // tải lại

    private void ControlButton() {
        img_tailai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                //Toast.makeText(getApplicationContext(),"Item update được chọn", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //------------------------------------------------------------------------------
    private void layallkhachhang() {
        arrKH = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_layallkhachhang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                if (response.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Không lấy được danh sách Khách hàng!", Toast.LENGTH_SHORT).show();
                } else { // Lấy được danh sách Khashc hàng
                    try {
                        if (arrKH.size() > 0) {
                            arrKH.clear();
                        }
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            KhachHang kh = new KhachHang();
                            kh.idkhach = jsonObject.getInt("idkhach");
                            kh.ten = jsonObject.getString("ten");
                            kh.gioitinh = jsonObject.getInt("gioitinh");
                            kh.ngaysinh = jsonObject.getString("ngaysinh");
                            kh.sdt = jsonObject.getString("sdt");
                            kh.diachi = jsonObject.getString("diachi");
                            kh.mota = jsonObject.getString("mota");
                            kh.solancat = jsonObject.getInt("solancat");
                            arrKH.add(kh);
                        }
                        adapter = new DanhSachAdapter(MainActivity.this, 0, arrKH);
                        lst_danhsachkhachhang.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // đếm tổng khách hàng cắt tóc
                    lst_danhsachkhachhang.getAdapter().getCount();
                    Toast.makeText(getApplicationContext(), "Số khách đã cắt tại tiệm:" + lst_danhsachkhachhang.getAdapter().getCount(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Lỗi lấy danh sách khách hàng từ HOST: "+error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    //--------------- MENU CHON-----------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menuitem_themkhach:
                //Toast.makeText(getApplicationContext(),"Item Thêm Khách được chọn", Toast.LENGTH_SHORT).show();
                // Chuyển từ màn hình MainActivity sang màn hình ThemKhachHangActivity
                Intent intent_themKH = new Intent(MainActivity.this, ThemKhachHangActivity.class);
                intent_themKH.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_themKH);
                break;
            case R.id.menuitem_gioithieu:
                //Toast.makeText(getApplicationContext(),"Item giới thiệu được chọn", Toast.LENGTH_SHORT).show();
                Intent intent_gioithieu = new Intent(MainActivity.this, GioiThieu.class);
                intent_gioithieu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_gioithieu);
                break;
            case R.id.menuitem_themtho:
                new ThemThoCatDialog(MainActivity.this).show();
                break;
            case R.id.menuitem_Thoat:
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
                builder.setTitle("Bạn có chắc muốn thoát khỏi app");
                builder.setMessage("Hãy lựa chọn bên dưới để xác nhận");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        onBackPressed();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                break;
            case R.id.menuitem_thongke: {
                Intent intent_thongke = new Intent(MainActivity.this, ThongKeActivity.class);
                intent_thongke.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_thongke);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}