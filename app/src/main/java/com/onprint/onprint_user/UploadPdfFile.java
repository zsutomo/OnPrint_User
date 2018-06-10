package com.onprint.onprint_user;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPdfFile extends Fragment implements OnPageChangeListener, OnLoadCompleteListener {


    private View view;
    private Button btn_pilihFile;
    private PDFView filePdfView;
    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;
    private Uri selectedFile;
    private String pdfPath;
    private int pageNumber=0;
    private String pdfFileName;
    private PDFView pdfView;
    private TextView tv_blm_uplod;

    public UploadPdfFile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_pdf_file, container, false);

        initView(view);
        pilihFile();



        return view;
    }

    private void initView(View view) {
        btn_pilihFile = (Button) view.findViewById(R.id.btn_pilih_file);
        filePdfView = view.findViewById(R.id.file_pdfView);
        tv_blm_uplod = (TextView) view.findViewById(R.id.blm_uplod);
    }

    private void pilihFile() {

        btn_pilihFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                uploadFile();
            }
        });

    }

    private void uploadFile() {
        Intent pdfIntent = new Intent();

        pdfIntent.setType("application/pdf")
                .setAction(Intent.ACTION_GET_CONTENT);
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE);
            return;

        }

        startActivityForResult(Intent.createChooser(pdfIntent, "SELECT PDF"), REQUEST_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent pdfFile){
        super.onActivityResult(requestCode, resultCode, pdfFile);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            btn_pilihFile.setVisibility(View.INVISIBLE);
            filePdfView.setVisibility(View.VISIBLE);
            tv_blm_uplod.setVisibility(View.INVISIBLE);


            selectedFile = pdfFile.getData();
            pdfPath = pdfFile.getData().getPath();

            displayPdfFile(selectedFile);

        } else {
            btn_pilihFile.setVisibility(View.VISIBLE);
            filePdfView.setVisibility(View.INVISIBLE);
            tv_blm_uplod.setVisibility(View.VISIBLE);

        }

    }

    private void displayPdfFile(Uri uri) {
        filePdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this.getContext()))
                .spacing(45)
                .load();

    }


    @Override
    public void loadComplete(int nbPages) {

        PdfDocument.Meta meta = filePdfView.getDocumentMeta();

        Log.e(TAG, "title = " + meta.getTitle());

        Log.e(TAG, "author = " + meta.getAuthor());

        Log.e(TAG, "subject = " + meta.getSubject());

        Log.e(TAG, "keywords = " + meta.getKeywords());

        Log.e(TAG, "creator = " + meta.getCreator());

        Log.e(TAG, "producer = " + meta.getProducer());

        Log.e(TAG, "creationDate = " + meta.getCreationDate());

        Log.e(TAG, "modDate = " + meta.getModDate());



        printBookmarksTree(filePdfView.getTableOfContents(), "-");

    }

    private void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {

        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {

                printBookmarksTree(b.getChildren(), sep + "-");

            }

        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = pageCount;

        pageNumber = page;
//        getActivity().setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));

    }

    private String getPdfFileName(Uri uri) {
        String result = null;

        if (uri.getScheme().equals("content")){
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

            try{
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            } finally {
                if (cursor != null){
                    cursor.close();
                }

            }
        }

        if (result == null){
            result = uri.getLastPathSegment();
        }

        return result;
    }
}
