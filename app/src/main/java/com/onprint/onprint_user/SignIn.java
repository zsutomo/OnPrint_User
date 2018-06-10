package com.onprint.onprint_user;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    TextView btn_masuk;
    TextView btn_daftar;
    EditText alamatEmail;
    EditText kataSandi;
    CheckBox lihatSandi;
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener authStateListener;
    public String email;
    public String sandi;
    private MaterialDialog.Builder materialDialog;
    private MaterialDialog dialog;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        inisialisasi();
        prosesSignIn();
        tombolDaftar();
    }

    private void inisialisasi() {
        btn_masuk = (TextView) findViewById(R.id.tv_masuk);
        alamatEmail = (EditText) findViewById(R.id.alamat_email);
        kataSandi = (EditText) findViewById(R.id.kata_sandi);
        view = (View) findViewById(android.R.id.content);
        lihatSandi = (CheckBox) findViewById(R.id.show_password);

        mAuth = FirebaseAuth.getInstance();
    }

    private void tombolDaftar() {
        btn_daftar = findViewById(R.id.tv_daftar);
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            }
        });
    }

    private void getCurrentUser() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignIn.this, HomeActivity.class));
            finish();
        }
    }

    public void prosesSignIn() {

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog();
                email = alamatEmail.getText().toString().trim();
                sandi = kataSandi.getText().toString().trim();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(sandi)) {
                    dialog.dismiss();
                    Snackbar.make(view, "Email dan Sandi Tidak Boleh Kosong!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    dialog.dismiss();
                    Snackbar.make(view, "Email Tidak Boleh Kosong!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(sandi)) {
                    dialog.dismiss();
                    Snackbar.make(view, "Kata Sandi Tidak Boleh Kosong!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                dialog.show();

                mAuth.signInWithEmailAndPassword(email, sandi)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Snackbar.make(view, "Email atau Kata Sandi yang Anda masukan Salah!", Snackbar.LENGTH_LONG).show();

                                } else {
                                    dialog.dismiss();
                                    finish();
                                    startActivity(new Intent(SignIn.this, HomeActivity.class));

                                }
                                dialog.dismiss();
                            }
                        });
            }
        });

        lihatKataSandi();

    }

    private void lihatKataSandi() {
        lihatSandi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    kataSandi.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    kataSandi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private void welcomePopUp() {
        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(SignIn.this);
        aBuilder.setIcon(R.drawable.printer_blue);
        aBuilder.setTitle("Selamat");
        aBuilder.setMessage("Anda Telah Berhasil Login, Tekan OK Untuk Masuk Ke Menu Utama");
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(new Intent(SignIn.this, HomeActivity.class));
            }
        });
        aBuilder.show();

    }

    public void progressDialog() {

        materialDialog = new MaterialDialog.Builder(SignIn.this);
        materialDialog.title("Proses Authentikasi..")
                .content("Mohon Tunggu,..")
                .progress(true, 800)
                .cancelable(false)
                .progressIndeterminateStyle(true);

        dialog = materialDialog.build();
        dialog.show();


    }
}
