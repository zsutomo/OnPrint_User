package com.onprint.onprint_user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextView btn_daftar;
    EditText namaLengkap;
    EditText noIdentitas;
    EditText alamatEmail;
    EditText nomorHandphone;
    EditText kataSandi;
    EditText kataSandi_konfirmasi;
    CheckBox checkBox_lihatsandi;
    FirebaseAuth mAuth;
    String nama_lengkap;
    String no_identitas;
    String alamat_email;
    String nomor_Handphone;
    String kata_sandi;
    String kata_Sandi_konfirmasi;
    ProgressBar progressBar;
    MaterialDialog.Builder materialDialog;
    MaterialDialog dialog;
    View view;
    DatabaseReference userdataReference;
    public FirebaseAuth.AuthStateListener authStateListener;
    public TextView tv_SignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getCurrentUser();
        pendaftaran();
        btnSignIn();

    }

    private void btnSignIn() {

        tv_SignIn = findViewById(R.id.tv_signIn);
        tv_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });
    }

    private void getCurrentUser() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(SignUp.this, HomeActivity.class));
            finish();
        }

    }

    private void pendaftaran() {

        btn_daftar = (TextView) findViewById(R.id.tv_daftar);
        namaLengkap = (EditText) findViewById(R.id.nama_lengkap);
        noIdentitas  = (EditText) findViewById(R.id.no_identitas);
        alamatEmail = (EditText) findViewById(R.id.alamat_email);
        nomorHandphone = (EditText) findViewById(R.id.no_handphone);
        kataSandi =(EditText) findViewById(R.id.kata_sandi);
        kataSandi_konfirmasi = (EditText) findViewById(R.id.katasandi_konfirmasi);
        checkBox_lihatsandi = (CheckBox) findViewById(R.id.lihat_sandi);
        view = (View) findViewById(android.R.id.content);

        checkBox_lihatsandi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    kataSandi.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    kataSandi_konfirmasi.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else {
                    kataSandi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    kataSandi_konfirmasi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesDaftar();
            }
        });
    }

    public void progressDialog() {

        materialDialog = new MaterialDialog.Builder(SignUp.this);
        materialDialog.title("Proses Authentikasi..")
                .content("Mohon Tunggu....")
                .progress(true, 100)
                .progressIndeterminateStyle(true);

        dialog = materialDialog.build();
        dialog.show();

    }

    private void prosesDaftar() {
        progressDialog();

        mAuth = FirebaseAuth.getInstance();
        nama_lengkap = namaLengkap.getText().toString().trim();
        no_identitas = noIdentitas.getText().toString().trim();
        alamat_email = alamatEmail.getText().toString().trim();
        nomor_Handphone = nomorHandphone.getText().toString().trim();
        kata_sandi = kataSandi.getText().toString().trim();
        kata_Sandi_konfirmasi = kataSandi_konfirmasi.getText().toString().trim();


        if (TextUtils.isEmpty(nama_lengkap) && TextUtils.isEmpty(no_identitas) && TextUtils.isEmpty(alamat_email) && TextUtils.isEmpty(nomor_Handphone) && TextUtils.isEmpty(kata_sandi) && TextUtils.isEmpty(kata_Sandi_konfirmasi) ){
            dialog.dismiss();
            Snackbar.make(view, "Semua Data Harus di Isi", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(nama_lengkap)){
            dialog.dismiss();
            Snackbar.make(view, "Nama Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(no_identitas)){
            dialog.dismiss();
            Snackbar.make(view, "No Indentitas Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(alamat_email)){
            dialog.dismiss();
            Snackbar.make(view, "Alamat Email Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(nomor_Handphone)){
            dialog.dismiss();
            Snackbar.make(view, "No HP Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();

            return;
        }

        if (TextUtils.isEmpty(kata_sandi)){
            dialog.dismiss();
            Snackbar.make(view, "Kata Sandi Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();

            return;
        }
        if (TextUtils.isEmpty(kata_Sandi_konfirmasi)){
            dialog.dismiss();
            Snackbar.make(view, "Konfirmasi Kata Sandi Tidak Boleh Kosong", Snackbar.LENGTH_LONG).show();

            return;
        }

        if (!kata_sandi.equals(kata_Sandi_konfirmasi)) {
            dialog.dismiss();
            Snackbar.make(view, "Kata Sandi Tidak Cocok", Snackbar.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(alamat_email, kata_sandi)
                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            dialog.dismiss();
                            Snackbar.make(view, "Proses Authentikasi Gagal, Silahkan Coba Lagi", Snackbar.LENGTH_LONG).show();

                        } else {
                            Log.d(alamat_email, "createUserWithEmail:success");
                            alamatEmail.setText("");
                            noIdentitas.setText("");
                            nomorHandphone.setText("");
                            kataSandi.setText("");
                            kataSandi_konfirmasi.setText("");
                            namaLengkap.setText("");
                            simpanDatakeDatabase();
                            dialog.dismiss();
                            startActivity(new Intent(SignUp.this, SignIn.class));
                            finish();
                        }

                    }
                });

    }

    private void simpanDatakeDatabase() {
        userdataReference = FirebaseDatabase.getInstance().getReference();
        String key = userdataReference.push().getKey();
        userdataReference.child("DataPengguna").child(key).child("id").setValue(key);
        userdataReference.child("DataPengguna").child(key).child("namaLengkap").setValue(nama_lengkap);
        userdataReference.child("DataPengguna").child(key).child("noIdentitas").setValue(no_identitas);
        userdataReference.child("DataPengguna").child(key).child("alamatEmail").setValue(alamat_email);
        userdataReference.child("DataPengguna").child(key).child("noHandphone").setValue(nomor_Handphone);

    }
}
