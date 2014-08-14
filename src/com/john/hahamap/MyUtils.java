/**
 * 
 */
package com.john.hahamap;

import android.content.Intent;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * @author gao_gm
 * 
 */
public class MyUtils {
	public static final String LATITUDE = "LATITUDE", LONGITUDE = "LONGITUDE";

	public static LatLng getLlByIntent(Intent getIntent) {
		LatLng ll = null;
		if (getIntent != null) {
			if (getIntent.hasExtra(LATITUDE) && getIntent.hasExtra(LONGITUDE)) {
				// 120.079257,30.278758
				// 设定中心点坐标
				ll = new LatLng(getIntent.getDoubleExtra(LATITUDE, 30.278758),
						getIntent.getDoubleExtra(LONGITUDE, 120.079257));
			}
		}
		return ll;
	}

	public static void initMapCenter(LatLng ll, BaiduMap baiduMap) {
		if (ll != null) {
			// 定义地图状态
			MapStatus mMapStatus = new MapStatus.Builder().target(ll).zoom(18)
					.build();
			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			// 改变地图状态
			baiduMap.setMapStatus(mMapStatusUpdate);
		}
	}

	public static Marker addOverLayToMap(BitmapDescriptor bp, LatLng ll,
			BaiduMap baiduMap) {
		OverlayOptions ooA = new MarkerOptions().position(ll).icon(bp)
				.zIndex(10);
		return (Marker) (baiduMap.addOverlay(ooA));
	}

}
