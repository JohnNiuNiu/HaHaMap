package com.john.hahamap;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends Activity {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;

	private BitmapDescriptor mBdA;
	private Marker mMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationEnabled(true);

		mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);

		LatLng centLl = MyUtils.getLlByIntent(getIntent());
		MyUtils.initMapCenter(centLl, mBaiduMap);
		mBdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		mMarker = MyUtils.addOverLayToMap(mBdA, centLl, mBaiduMap);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		mBdA.recycle();
	}

	private OnMarkerClickListener mOnMarkerClickListener = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(final Marker marker) {
			final LatLng ll = marker.getPosition();
			Point p = mBaiduMap.getProjection().toScreenLocation(ll);
			p.y -= 47;
			LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);

			Button button = new Button(getApplicationContext());

			if (marker == mMarker) {
				button.setText(marker.getPosition().latitude + " // "
						+ marker.getPosition().longitude);

			}
			InfoWindow mInfoWindow = new InfoWindow(button, llInfo,
					mOnInfoWindowClickListener);
			mBaiduMap.showInfoWindow(mInfoWindow);
			return true;
		}
	};

	private OnInfoWindowClickListener mOnInfoWindowClickListener = new OnInfoWindowClickListener() {
		public void onInfoWindowClick() {
			mBaiduMap.hideInfoWindow();
		}
	};

}
