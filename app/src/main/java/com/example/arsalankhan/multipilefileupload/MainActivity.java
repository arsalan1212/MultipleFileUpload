package com.example.arsalankhan.multipilefileupload;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private int REQUEST_MULTI_FILE=100;
    private ArrayList<String> arrayListFileName,arrayListUploadStatus;
    private MyAdapter adapter;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        storageReference = FirebaseStorage.getInstance().getReference().child("upload_images");

        arrayListFileName = new ArrayList<>();
        arrayListUploadStatus = new ArrayList<>();

        adapter = new MyAdapter(this,arrayListFileName,arrayListUploadStatus);
        mRecyclerView.setAdapter(adapter);

    }

    //upload button
    public void UploadFile(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        REQUEST_MULTI_FILE = 100;
        startActivityForResult(intent, REQUEST_MULTI_FILE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==REQUEST_MULTI_FILE && resultCode ==RESULT_OK){

            //if clip data is not null this means multiple file is selected,otherwise single file is selectd
            if(data.getClipData()!=null){

                int totalUploadFiles = data.getClipData().getItemCount();

                for(int i=0; i < totalUploadFiles; i++){

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imageName = getFileName(imageUri);
                    arrayListFileName.add(imageName);

                    arrayListUploadStatus.add("uploading");

                    final int position =i;
                    //uploading images to firebase storage
                    StorageReference imageStore = storageReference.child(imageName);
                    imageStore.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                //removing the current status of upload and then update it
                                arrayListUploadStatus.remove(position);
                                arrayListUploadStatus.add(position,"uploaded");
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //refresh the adapter
                adapter.notifyDataSetChanged();

            }
            else if(data.getData()!=null){

                //do code For multiple file upload
                Toast.makeText(this, "Single Image Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String getFileName(Uri uri){

        String result=null;

        if(uri.getScheme().equals("content")){

            Cursor cursor = getContentResolver().query(uri,null,null,null,null,null);

            if(cursor!=null){
                while (cursor.moveToNext()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }

        if(result ==null){
            result = uri.getPath();
            int cut = result.lastIndexOf("/");

            if(cut!=-1){
                result = result.substring(cut+1);
            }
        }
        return result;
    }
}
