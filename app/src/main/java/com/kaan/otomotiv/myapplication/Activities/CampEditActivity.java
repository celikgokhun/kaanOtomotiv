package com.kaan.otomotiv.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaan.otomotiv.myapplication.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CampEditActivity extends AppCompatActivity {

    Bitmap selectedImage;

    ImageView goodsImageView;

    EditText goodsTitleEditText;
    EditText goodsBodyEditText;
    EditText goodsPriceEditText;

    RadioButton AModelRadioButton;
    RadioButton BModelRadioButton;
    RadioButton CModelRadioButton;
    RadioButton DModelRadioButton;
    RadioButton EModelRadioButton;
    RadioButton FModelRadioButton;
    RadioButton GModelRadioButton;
    RadioButton HModelRadioButton;
    RadioButton IModelRadioButton;
    RadioButton JModelRadioButton;

    Button addCampButton;

    String models;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    Uri imageData;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_edit);

        goodsImageView = findViewById(R.id.goods_photo);

        goodsTitleEditText = findViewById(R.id.goods_title);
        goodsBodyEditText = findViewById(R.id.goods_body);
        goodsPriceEditText = findViewById(R.id.goods_price);

        AModelRadioButton = findViewById(R.id.A_model_rad);
        BModelRadioButton = findViewById(R.id.B_model_rad);
        CModelRadioButton = findViewById(R.id.C_model_rad);
        DModelRadioButton = findViewById(R.id.D_model_rad);
        EModelRadioButton = findViewById(R.id.E_model_rad);
        FModelRadioButton = findViewById(R.id.F_model_rad);
        GModelRadioButton = findViewById(R.id.G_model_rad);
        HModelRadioButton = findViewById(R.id.H_model_rad);
        IModelRadioButton = findViewById(R.id.I_model_rad);
        JModelRadioButton = findViewById(R.id.J_model_rad);

        addCampButton = findViewById(R.id.add_camp_btn);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void getModels() {
        if (AModelRadioButton.isChecked())
        {
            models =  models + AModelRadioButton.getText().toString();
        }

        if (BModelRadioButton.isChecked())
        {
            models =  models + BModelRadioButton.getText().toString();
        }

        if (CModelRadioButton.isChecked())
        {
            models =  models + CModelRadioButton.getText().toString();
        }

        if (DModelRadioButton.isChecked())
        {
            models =  models + DModelRadioButton.getText().toString();
        }

        if (EModelRadioButton.isChecked())
        {
            models =  models + EModelRadioButton.getText().toString();
        }

        if (FModelRadioButton.isChecked())
        {
            models =  models + FModelRadioButton.getText().toString();
        }

        if (GModelRadioButton.isChecked())
        {
            models =  models + GModelRadioButton.getText().toString();
        }

        if (HModelRadioButton.isChecked())
        {
            models =  models + HModelRadioButton.getText().toString();
        }

        if (IModelRadioButton.isChecked())
        {
            models =  models + IModelRadioButton.getText().toString();
        }

        if (JModelRadioButton.isChecked())
        {
            models =  models + JModelRadioButton.getText().toString();
        }

        //// burada bi çözüm var amma ve lakin

        models = models.replace("null", "");

    }

    public void postTheGoods(View view) {
        addCampButton.setVisibility(View.INVISIBLE);
        addCampButton.setClickable(false);
        getModels();

        if (imageData != null)
        {
            //universal unique id
            UUID uuid = UUID.randomUUID();
            final String imageName = "images/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download URL

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();

                            String goodsTitle = goodsTitleEditText.getText().toString();
                            String goodsBody = goodsBodyEditText.getText().toString();
                            String goodsPrice = goodsPriceEditText.getText().toString();
                            String goodsModels = models;

                            HashMap<String, Object> postData = new HashMap<>();

                            postData.put("goods_downloadUrl",downloadUrl);
                            postData.put("goods_title",goodsTitle);
                            postData.put("goods_body",goodsBody);
                            postData.put("goods_price",goodsPrice);
                            postData.put("goods_models",goodsModels);
                            postData.put("goods_date", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("GOODS").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(CampEditActivity.this,PanelAdminActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CampEditActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CampEditActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)    {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null ) {

            imageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28)
                {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    goodsImageView.setImageBitmap(selectedImage);
                }
                else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    goodsImageView.setImageBitmap(selectedImage);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
