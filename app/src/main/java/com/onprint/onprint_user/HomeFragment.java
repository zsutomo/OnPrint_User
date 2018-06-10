package com.onprint.onprint_user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageButton btn_OrderPrint;
    private ImageButton btn_riwayat;
    private ImageButton btn_profil;
    private ImageButton btn_tentang;
    MaterialDialog.Builder dialogBuilder;
    MaterialDialog materialDialog;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        welcomePopUp();
        orderPrint(view);
        riwayatOrder(view);
        profilUser(view);
        tentangAplikasi(view);

        return view;
    }

    private void welcomePopUp() {

    }

    private void orderPrint(View view) {
        btn_OrderPrint = view.findViewById(R.id.order_print);
        btn_OrderPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),PdfFileView.class);
//                startActivity(intent);
                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fragment_container, new UploadPdfFile(), UploadPdfFile.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    private void riwayatOrder(View view) {

    }

    private void profilUser(View view) {

    }

    private void tentangAplikasi(View view) {
        btn_tentang = view.findViewById(R.id.tentang);
        btn_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getFragmentManager().beginTransaction()
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .replace(R.id.fragment_container, new Tentang(), Tentang.class.getSimpleName())
//                        .addToBackStack(null)
//                        .commit();
                new AlertDialog.Builder(getActivity())
                        .setTitle("U-Print")
                        .setIcon(R.drawable.printer_blue)
                        .setMessage("Aplikasi Ini Adalah Versi Alpha")
                        .show();
            }
        });

    }

}
