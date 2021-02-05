package com.buiduy.tiemcattoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.buiduy.tiemcattoc.R;
import com.buiduy.tiemcattoc.object.KhachHang;
import com.buiduy.tiemcattoc.object.ThoCatToc;

import java.util.ArrayList;
import java.util.List;

public class DanhSachThoAdapter extends ArrayAdapter<ThoCatToc> {
    Context myCt;
    //ArrayList<KhachHang> myArr;
    ArrayList<ThoCatToc> myArr2;


    public DanhSachThoAdapter(@NonNull Context context, int resource, List<ThoCatToc> objects){
        super(context, resource,objects);
        this.myCt = context;
        this.myArr2 = new ArrayList<>(objects);
    }
    //-------------------------------------------------------------------------------

    public void updateDSKH(ArrayList<ThoCatToc>arrKH) {
        myArr2= arrKH;
        notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------------
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) myCt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_text_tho_cat, null);
        }
        if(myArr2.size() > 0) {
            // Anh xa View
            TextView txvtenthocat = convertView.findViewById(R.id.txvtenthocat);

            //Gan du lieu
            ThoCatToc TH = myArr2.get(position);
            txvtenthocat.setText(TH.idtho + ". "+TH.ten);
        }
        return convertView;
    }
}
