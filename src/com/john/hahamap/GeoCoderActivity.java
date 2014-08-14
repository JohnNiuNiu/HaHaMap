package com.john.hahamap;

import java.util.Locale;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 * 此demo用来展示如何进行地理编码搜索（用地址检索坐标）、反地理编码搜索（用坐标检索地址）
 */
public class GeoCoderActivity extends Activity implements
		OnGetGeoCoderResultListener {
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private BaiduMap mBaiduMap = null;
	private MapView mMapView = null;

	private Marker mMarkerA, mMarkerB, mMarkerC;

	private LatLng mCenterLatLng;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geocoder);
		CharSequence titleLable = "地理编码功能";
		setTitle(titleLable);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		mCenterLatLng = MyUtils.getLlByIntent(getIntent());
		MyUtils.initMapCenter(mCenterLatLng, mBaiduMap);
		mMarkerA = MyUtils.addOverLayToMap(
				BitmapDescriptorFactory.fromResource(R.drawable.icon_marka),
				mCenterLatLng, mBaiduMap);

	}

	/**
	 * 发起搜索
	 * 
	 * @param v
	 */
	public void SearchButtonProcess(View v) {
		if (v.getId() == R.id.reversegeocode) {
			EditText lat = (EditText) findViewById(R.id.lat);
			EditText lon = (EditText) findViewById(R.id.lon);
			LatLng ptCenter = new LatLng((Float.valueOf(lat.getText()
					.toString())), (Float.valueOf(lon.getText().toString())));
			// 反Geo搜索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(ptCenter));
		} else if (v.getId() == R.id.geocode) {
			EditText editCity = (EditText) findViewById(R.id.city);
			EditText editGeoCodeKey = (EditText) findViewById(R.id.geocodekey);
			// Geo搜索
			mSearch.geocode(new GeoCodeOption().city(
					editCity.getText().toString()).address(
					editGeoCodeKey.getText().toString()));
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(GeoCoderActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (mMarkerB != null) {
			mMarkerB.remove();
		}
		mMarkerB = MyUtils.addOverLayToMap(
				BitmapDescriptorFactory.fromResource(R.drawable.icon_markb),
				result.getLocation(), mBaiduMap);

		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));

		String strInfo = String.format("纬度：%.1f 经度：%.1f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(GeoCoderActivity.this, strInfo, Toast.LENGTH_LONG)
				.show();

		getDistance(mCenterLatLng, result.getLocation());

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(GeoCoderActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (mMarkerC != null) {
			mMarkerC.remove();
		}
		mMarkerC = MyUtils.addOverLayToMap(
				BitmapDescriptorFactory.fromResource(R.drawable.icon_markc),
				result.getLocation(), mBaiduMap);

		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));

		Toast.makeText(GeoCoderActivity.this, result.getAddress(),
				Toast.LENGTH_LONG).show();

		getDistance(mCenterLatLng, result.getLocation());

	}

	private void getDistance(LatLng start, LatLng end) {
		Toast.makeText(GeoCoderActivity.this, getDistanceMsg(start, end),
				Toast.LENGTH_LONG).show();
	}

	private String getDistanceMsg(LatLng start, LatLng end) {
		Double distance = DistanceUtil.getDistance(start, end);
		return String.format(Locale.CHINA, "距离: %.1f 米  //  %.1f 公里", distance,
				distance / 1000);
	}

	private OnMarkerClickListener mOnMarkerClickListener = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(final Marker marker) {
			final LatLng ll = marker.getPosition();
			Point p = mBaiduMap.getProjection().toScreenLocation(ll);
			p.y -= 47;
			LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);

			Button button = new Button(getApplicationContext());

			if (marker == mMarkerA) {
				button.setText(marker.getPosition().latitude + " // "
						+ marker.getPosition().longitude);

			} else if (marker == mMarkerB) {
				button.setText(getDistanceMsg(mCenterLatLng, ll));
			} else if (marker == mMarkerC) {
				button.setText(getDistanceMsg(mCenterLatLng, ll));
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
