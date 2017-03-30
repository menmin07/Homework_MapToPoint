package kg.app.kuba.homework_maptopoint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class PointinfoActivity extends AppCompatActivity {
    private final String[] PERMISSIONS_READ_EXTERNAL_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 3;

    private static final int REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE = 2;

    public static final int DATA_REQUEST_CODE = 100;
    public static final int DATA_RESULT_CODE = 101;
    public static final String EXTRA_PLACENAME = "extra_placename";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_DATEOFMARK = "extra_dateofmark";
    public static final String EXTRA_TIMEOFMARK = "extra_timeofmark";

    File imageFile;

    ImageView imageImageView;
    Button buttonSave, openGalleryButton, capturePictureButton;
    EditText placenameEditText, descriptionEditText, positionEditText, dateofmarkEditText, timeofmarkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointinfo);

        placenameEditText = (EditText)findViewById(R.id.placenameEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        positionEditText = (EditText)findViewById(R.id.positionEditText);
        dateofmarkEditText = (EditText)findViewById(R.id.dateofmarkEditText);
        timeofmarkEditText = (EditText)findViewById(R.id.timeofmarkEditText);


        imageImageView = (ImageView)findViewById(R.id.imageViewPointActivity);

        buttonSave = (Button)findViewById(R.id.buttonSave);
        openGalleryButton = (Button)findViewById(R.id.buttonGallery);
        capturePictureButton = (Button)findViewById(R.id.buttonCapturePicture);

        openGalleryButton.setOnClickListener(onOpenGalleryOpenListener);
        capturePictureButton.setOnClickListener(onCapturePictureOpenListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= RESULT_OK)
            return;

        if (requestCode == REQUEST_GALLERY) {
            Uri uri = data.getData();
            String chosenImagePath = getPath(uri);
            imageImageView.setImageBitmap(BitmapFactory.decodeFile(chosenImagePath));
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            imageImageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getPath()));
            decodeFileCorrectly(imageFile.getPath(), 300, 300);
        }
    }
    private Bitmap decodeFileCorrectly(String path, int destWidth, int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path,options);

        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        int inSampleSize = 1;
        if(destWidth < originalWidth || destHeight < originalHeight){
            int widthProportion = originalWidth / destWidth;
            int heightProportion = originalHeight / destHeight;

            inSampleSize = Math.max(widthProportion, heightProportion);
        }

        BitmapFactory.Options finalOptions = new BitmapFactory.Options();
        finalOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, finalOptions);
    }

    public String getPath(Uri uri){
        String[] columns = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, columns, null, null, null, null);
        cursor.moveToFirst();
        int indexColumn = cursor.getColumnIndex(columns[0]);
        String path = cursor.getString(indexColumn);
        return path;
    }
    public void openGallery(){
        if (hasPermissionReadExternalStorage()) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent,REQUEST_GALLERY);
        } else {
            requestPermissionReadExternalStorage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE && hasPermissionReadExternalStorage()){
            openGallery();
        }
    }

    private final View.OnClickListener onOpenGalleryOpenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openGallery();
        }
    };

    private  boolean hasPermissionReadExternalStorage(){
        int result = ActivityCompat.checkSelfPermission(this,PERMISSIONS_READ_EXTERNAL_STORAGE[0]);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;  //return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionReadExternalStorage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS_READ_EXTERNAL_STORAGE, REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);
        }
    }

    private final View.OnClickListener onCapturePictureOpenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imageFile = new File(getFilesDir(), UUID.randomUUID().toString()+ ".jpg");

            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            boolean canTaskPhoto = imageCaptureIntent.resolveActivity(getPackageManager()) != null;

            if (!canTaskPhoto)
                return;

            Uri imageUri = FileProvider.getUriForFile(PointinfoActivity.this,
                    "kg.app.kuba.homework_maptopoint.fileprovider", imageFile);
            List<ResolveInfo> activities = getPackageManager().queryIntentActivities(imageCaptureIntent,
                    PackageManager.MATCH_DEFAULT_ONLY);

//            for(int i = 0; i < activities.size(); i++) {
//                ResolveInfo activity = activities.get(1);
//            }

            for(ResolveInfo activity: activities){
                grantUriPermission(activity.activityInfo.packageName, imageUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(imageCaptureIntent,REQUEST_IMAGE_CAPTURE);
        }
    };

    public void onClickSave(View view) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_PLACENAME, placenameEditText.getText().toString());
        intent.putExtra(EXTRA_DESCRIPTION, descriptionEditText.getText().toString());
        intent.putExtra(EXTRA_POSITION, positionEditText.getText().toString());
        intent.putExtra(EXTRA_DATEOFMARK, dateofmarkEditText.getText().toString());
        intent.putExtra(EXTRA_TIMEOFMARK, timeofmarkEditText.getText().toString());
        setResult(DATA_RESULT_CODE, intent);
        finish();
    }

//

}
