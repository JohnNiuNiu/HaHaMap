package com.john.hahamap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationActivity extends Activity {
	private LocationClient mLocationClient;
	private TextView LocationResult, ModeInfor;
	private Button startLocation, btnLocation, btnGeoLocation;
	private RadioGroup selectMode, selectCoordinates;
	private EditText frequence;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private CheckBox checkGeoLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		mLocationClient = ((MApplication) getApplication()).mLocationClient;

		LocationResult = (TextView) findViewById(R.id.textView1);
		ModeInfor = (TextView) findViewById(R.id.modeinfor);
		ModeInfor.setText(getString(R.string.hight_accuracy_desc));
		((MApplication) getApplication()).mLocationResult = LocationResult;
		frequence = (EditText) findViewById(R.id.frequence);
		checkGeoLocation = (CheckBox) findViewById(R.id.geolocation);
		startLocation = (Button) findViewById(R.id.addfence);
		startLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InitLocation();

				if (startLocation.getText().equals(
						getString(R.string.startlocation))) {
					mLocationClient.start();
					startLocation.setText(getString(R.string.stoplocation));
				} else {
					mLocationClient.stop();
					startLocation.setText(getString(R.string.startlocation));
				}

			}
		});
		selectMode = (RadioGroup) findViewById(R.id.selectMode);
		selectCoordinates = (RadioGroup) findViewById(R.id.selectCoordinates);
		selectMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				String ModeInformation = null;
				switch (checkedId) {
				case R.id.radio_hight:
					tempMode = LocationMode.Hight_Accuracy;
					ModeInformation = getString(R.string.hight_accuracy_desc);
					break;
				case R.id.radio_low:
					tempMode = LocationMode.Battery_Saving;
					ModeInformation = getString(R.string.saving_battery_desc);
					break;
				case R.id.radio_device:
					tempMode = LocationMode.Device_Sensors;
					ModeInformation = getString(R.string.device_sensor_desc);
					break;
				default:
					break;
				}
				ModeInfor.setText(ModeInformation);
			}
		});
		selectCoordinates
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radio_gcj02:
							tempcoor = "gcj02";
							break;
						case R.id.radio_bd09ll:
							tempcoor = "bd09ll";
							break;
						case R.id.radio_bd09:
							tempcoor = "bd09";
							break;
						default:
							break;
						}
					}
				});

		btnLocation = (Button) findViewById(R.id.btn_location);
		btnLocation.setOnClickListener(mClickListener);

		btnGeoLocation = (Button) findViewById(R.id.btn_geo_location);
		btnGeoLocation.setOnClickListener(mClickListener);

	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	@Override
	protected void onStart() {
		mLocationClient.start();
		super.onStart();
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// ���ö�λģʽ
		option.setCoorType(tempcoor);// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		int span = 1000;
		try {
			span = Integer.valueOf(frequence.getText().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		option.setScanSpan(span);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(checkGeoLocation.isChecked());
		mLocationClient.setLocOption(option);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Class<?> clas = null;
			switch (v.getId()) {
			case R.id.btn_location:
				clas = MainActivity.class;
				break;
			case R.id.btn_geo_location:
				clas = GeoCoderActivity.class;
				break;
			default:
				break;
			}
			Intent it = new Intent(LocationActivity.this, clas);
			MApplication application = (MApplication) getApplication();
			BDLocation location = application.mMyLocationListener.getLocation();
			if (location != null) {
				it.putExtra(MyUtils.LATITUDE, location.getLatitude());
				it.putExtra(MyUtils.LONGITUDE, location.getLongitude());
			}
			startActivity(it);
		}
	};

}
