package com.app.wordservant.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.app.wordservant.R;

public class StaticDisplayReviewFragment extends SherlockFragment implements ReviewFragment{
	private TextView mScriptureReferenceField;
	private TextView mScriptureTextField;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.layout_no_edit_scripture, null);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		mScriptureReferenceField = (TextView) getView().findViewById(R.id.referenceText);
		mScriptureTextField = (TextView) getView().findViewById(R.id.scriptureText);


		/*@SuppressWarnings("deprecation")
		Gallery gallery = (Gallery) getView().findViewById(R.id.gallery1);
		adapter = new ImageAdapter(getActivity());
		gallery.setAdapter(adapter);

		//Find all the WordServantFiles and add them to the gallery.
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "WordServant");
		if(mediaStorageDir.exists()){
			for(int i=0;i<mediaStorageDir.listFiles().length;i++){
				adapter.addImage(mediaStorageDir.listFiles()[i]);
			}
		}

		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_PICTURES), "WordServant");
				((ImageView) getView().findViewById(R.id.mainImage)).setImageBitmap(
						ImageAdapter.decodeSampledBitmapFromResource(mediaStorageDir.listFiles()[position], 140, 140));
			}
		});

		((ImageButton) getView().findViewById(R.id.takePictureButton)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// create Intent to take a picture and return control to the calling application
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

				// start the image capture Intent
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				//((ImageView) getView().findViewById(R.id.mainImage)).setImageURI(fileUri);
			}

		});*/
	}

	@Override
	public void setScriptureReference(CharSequence reference) {
		// TODO Auto-generated method stub
		mScriptureReferenceField.setText(reference);
	}

	@Override
	public CharSequence getScriptureReference() {
		// TODO Auto-generated method stub
		return mScriptureReferenceField.getText();
	}

	@Override
	public void setScriptureText(CharSequence text) {
		// TODO Auto-generated method stub
		mScriptureTextField.setText(text);
	}

	@Override
	public CharSequence getScriptureText() {
		// TODO Auto-generated method stub
		return mScriptureTextField.getText();
	}
}
