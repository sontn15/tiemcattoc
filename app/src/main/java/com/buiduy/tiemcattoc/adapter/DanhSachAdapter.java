package com.buiduy.tiemcattoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.buiduy.tiemcattoc.R;
import com.buiduy.tiemcattoc.object.KhachHang;
import com.buiduy.tiemcattoc.object.ThoCatToc;
import com.buiduy.tiemcattoc.utility.StringFormatUtils;

import java.util.ArrayList;
import java.util.List;

public class DanhSachAdapter extends ArrayAdapter<KhachHang> {
    Context myCt;
    ArrayList<KhachHang> myArr;
    private List<KhachHang> lstFiltered;
    ArrayList<ThoCatToc> myArr2;

    public DanhSachAdapter(@NonNull Context context, int resource, @NonNull List<KhachHang> objects) {
        super(context, resource, objects);
        this.myCt = context;
        this.myArr = new ArrayList<>(objects);
        this.lstFiltered = new ArrayList<>(objects);
    }

    //---------------------------------------------------------------------------------------------------
    public void updateDSKH(ArrayList<KhachHang> arrKH) {
        myArr = arrKH;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myCt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_khach_hang, null);
        }
        // Anh xa View
        TextView txvTenKhachHang = convertView.findViewById(R.id.txvTenKhachHang);
        TextView txvGtKhachHang = convertView.findViewById(R.id.txvGtKhachHang);
        TextView txvNgaySinhKhachHang = convertView.findViewById(R.id.txvNgaySinhKhachHang);
        TextView txvSdtKhachHang = convertView.findViewById(R.id.txvSdtKhachHang);
        TextView txvSoLanCatKhachHang = convertView.findViewById(R.id.txvSoLanCatKhachHang);
        //Gan du lieu
        KhachHang kh = lstFiltered.get(position);
        txvTenKhachHang.setText(kh.ten);
        if (kh.gioitinh == 1) {
            txvGtKhachHang.setText("Nam");
        } else {
            txvGtKhachHang.setText("Nữ");
        }
        txvNgaySinhKhachHang.setText(kh.ngaysinh);
        txvSdtKhachHang.setText(kh.sdt);
        txvSoLanCatKhachHang.setText(kh.solancat + " lần");
        return convertView;
    }

    @Override
    public int getCount() {
        return lstFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Nullable
    @Override
    public KhachHang getItem(int position) {
        return lstFiltered.get(position);
    }

    /**
     * Filter for search in EditText
     */
    private Filter mFilter;

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SearchFilters();
        }
        return mFilter;
    }

    //filter class
    private class SearchFilters extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence str) {
            FilterResults results = new FilterResults();
            if (str == null || str.length() == 0) {
                lstFiltered = myArr;
            } else {
                str = StringFormatUtils.convertUTF8ToString(str.toString().trim().toLowerCase());
                List<KhachHang> lstRecordFilters = new ArrayList<>();
                for (KhachHang obj : myArr) {
                    String name = obj.ten != null ? StringFormatUtils.convertUTF8ToString(obj.ten.trim().toLowerCase()) : "";
                    String phoneNumber = obj.sdt != null ? StringFormatUtils.convertUTF8ToString(obj.sdt.trim().toLowerCase()) : "";
                    assert str != null;
                    if ((name != null && name.contains(str)) || (phoneNumber != null && phoneNumber.contains(str))) {
                        lstRecordFilters.add(obj);
                    }
                }
                lstFiltered = lstRecordFilters;
            }
            results.values = lstFiltered;
            results.count = lstFiltered.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lstFiltered = (List<KhachHang>) results.values;
            notifyDataSetChanged();
        }
    }

}
