package com.buiduy.tiemcattoc.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buiduy.tiemcattoc.R;
import com.buiduy.tiemcattoc.adapter.DanhSachAdapter;
import com.buiduy.tiemcattoc.object.KhachHang;
import com.buiduy.tiemcattoc.screen.CatTocChoKhachActivity;
import com.buiduy.tiemcattoc.screen.LichSuCatTocActivity;
import com.buiduy.tiemcattoc.screen.MainActivity;
import com.buiduy.tiemcattoc.screen.SuaKhachHangActivity;
import com.buiduy.tiemcattoc.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LuaChonKhachHangDialog extends Dialog {

    KhachHang kh;
    MainActivity ds;
    TextView txvXoaKhacHang, txvTenKhachHang;
    TextView txvSuaThongTinKhachHang,txvCatTocChoKhach,txvlichsukhachcat;
    DanhSachAdapter adapter;
    ListView lst_danhsachkhachhang;

    public LuaChonKhachHangDialog(@NonNull final Context context, KhachHang khachHang) {
        super(context);
        this.kh = khachHang;
        this.ds = (MainActivity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dg_lua_chon_khach_hang);
        // Anh xa
        txvlichsukhachcat=findViewById(R.id.txvlichsukhachcat);
         txvTenKhachHang = findViewById(R.id.txvTenKhachHang);
         txvXoaKhacHang = findViewById(R.id.txvXoaKhacHang);
         txvSuaThongTinKhachHang = findViewById(R.id.txvSuaThongTinKhachHang);
        txvCatTocChoKhach = findViewById(R.id.txvCatTocChoKhach);
        // Set thong tin
        txvTenKhachHang.setText(kh.ten);

        // Bắt sự kiện xem lịch sử cắt tóc ---------------------------
        txvlichsukhachcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_lichsu = new Intent(context.getApplicationContext(), LichSuCatTocActivity.class);
                intent_lichsu.putExtra("idkhach",kh.idkhach);
                intent_lichsu.putExtra("solancat",kh.solancat);
                intent_lichsu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent_lichsu);
            }
        });

        //Bắt sự kiện khách hàng cắt tóc-------------------------------
        txvCatTocChoKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_cattoc = new Intent(context.getApplicationContext(), CatTocChoKhachActivity.class);
                intent_cattoc.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_cattoc.putExtra("idkhach",kh.idkhach);
                intent_cattoc.putExtra("ten",kh.ten);
                context.startActivity(intent_cattoc);
                //Toast.makeText(getContext(), "Tên khách cắt tóc : "+kh.ten, Toast.LENGTH_LONG).show();

            }
        });
        // Bắt sự kiện sửa thông tin khách hàng và gửi thông tin khách hàng cần sửa sang màn hình SỬA
        txvSuaThongTinKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_suakhach = new Intent(context.getApplicationContext(), SuaKhachHangActivity.class);
                intent_suakhach.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Gán dữ liệu gửi sang màn hình sửa
                intent_suakhach.putExtra("idkhach",kh.idkhach);
                intent_suakhach.putExtra("ten",kh.ten);
                intent_suakhach.putExtra("ngaysinh",kh.ngaysinh);
                intent_suakhach.putExtra("sdt",kh.sdt);
                intent_suakhach.putExtra("diachi",kh.diachi);
                intent_suakhach.putExtra("gioitinh",kh.gioitinh);
                intent_suakhach.putExtra("mota",kh.mota);
                context.startActivity(intent_suakhach);
                //Toast.makeText(getContext(), "Tên khách sửa : "+kh.ten, Toast.LENGTH_LONG).show();
            }
        });
        //--------------------------------------------------------------------
        // Bắt sự kiện xóa khách hàng
        txvXoaKhacHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Bạn có muốn xóa khách hàng này không?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Có",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                xulyxoakhachhang();
                                dismiss();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "Không",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
//                xulyxoakhachhang();
//                dismiss();
//                //Toast.makeText(getContext(), "bắt sự kiện click vào xóa".toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //-------------------------------------------------------------------------
    private void xulyxoakhachhang()
    {
        final String  tenkhach = txvTenKhachHang.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_xoakhachhang, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_LONG).show();
                    //update();
                }
                else
                {
                    Toast.makeText(getContext(), "Xóa thất bại!!!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi vui lòng kiểm tra lại " + error.toString(), Toast.LENGTH_LONG).show();
           }
       })
      {

           @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ten",tenkhach);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              jsonArray.put(jsonObject);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("xoakhach_json", jsonArray.toString());
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    //--------------------------------------------------------------------------------
    // Xử lý sửa khách hàng

}

