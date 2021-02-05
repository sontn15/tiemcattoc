package com.buiduy.tiemcattoc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.buiduy.tiemcattoc.screen.ThemKhachHangActivity;
import com.buiduy.tiemcattoc.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThemThoCatDialog extends Dialog {

    TextView txvThemThoCatToc, txvHuyBoThemThoCatToc;
    EditText edtTenThoCatToc;
    public ThemThoCatDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dg_them_tho_cat);

        // Anh xa bac cau
        txvThemThoCatToc = findViewById(R.id.txvThemThoCatToc);
        txvHuyBoThemThoCatToc = findViewById(R.id.txvHuyBoThemThoCatToc);
        edtTenThoCatToc = findViewById(R.id.edtTenThoCatToc);
        // bat su kien
        txvThemThoCatToc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 xyLyThemThoCat();
            }
        });

        txvHuyBoThemThoCatToc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();// đóng màn hình dialog lại
            }
        });
    }
    private void xyLyThemThoCat()
    {
        final String tentho = edtTenThoCatToc.getText().toString();

        if(tentho.length()==0)
        {
            Toast.makeText(getContext(),"Chưa nhập tên thợ!", Toast.LENGTH_LONG).show();
        } else
            {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_themthocat, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    if (response.equals("1")) {
                        Toast.makeText(getContext(), "Thêm thợ cắt tóc thành công!", Toast.LENGTH_LONG).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Thêm thợ cắt tóc thất bại!"+response, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Lỗi thêm thợ cắt tóc: " + error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ten", tentho);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("thocat_json", jsonArray.toString());
                    return hashMap;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }
}
