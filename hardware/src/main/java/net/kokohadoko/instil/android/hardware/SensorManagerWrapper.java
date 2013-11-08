package net.kokohadoko.instil.android.hardware;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

/**
 * SensorManagerラッパークラス
 * 
 * @author inuko
 * @since 0.0.1
 */
public class SensorManagerWrapper {

	/** ログ出力用文字列 */
	private static final String LOG_TAG = SensorManagerWrapper.class.getSimpleName();

	/** 行列サイズ */
	public static final int MATRIX_SIZE = 16;
	/**  */
	public static final int AXIS_NUM = 3;
	/** 方位角を表す */
	public static String AZIMUTH_ANGLE = "azimuth_angle";
	/** 傾斜角を表す */
	public static String INCLINED_ANGLE = "inclined_angle";
	/** 回転角を表す */
	public static String ROTATION_ANGLE = "rotation_angle";

	/**
	 * デフォルトコンストラクタ
	 * 
	 * @since 0.0.1
	 */
	private SensorManagerWrapper() {
	}

	/**
	 * 端末が加速度センサーをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合に true, そうでない場合に false を返す
	 */
	public static boolean hasSystemFeatureAccelerometer(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
	}

	/**
	 * 端末が圧力センサーをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合に true, そうでない場合に false を返す
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static boolean hasSystemFeatureBarometer(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER);
	}

	/**
	 * 端末がコンパスをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合に true, そうでない場合に false を返す
	 */
	public static boolean hasSystemFeatureCompass(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
	}

	/**
	 * 端末がジャイロスコープをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合に true, そうでない場合に false を返す
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static boolean hasSystemFeatureGyroscope(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
	}

	/**
	 * 端末が照度センサーをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合に true, そうでない場合に false を返す
	 */
	public static boolean hasSystemFeatureLight(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
	}

	/**
	 * 端末が近接センサーをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合に true, そうでない場合に false を返す
	 */
	public static boolean hasSystemFeatureProximity(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY);
	}

	/**
	 * SensorManagerを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return SensorManager
	 * @throws Exception 端末がSensorManagerをサポートしていない場合に例外が生成される
	 */
	public static SensorManager getSensorManager(Context context) throws Exception {

		if (hasSystemFeatureAccelerometer(context)
		 || hasSystemFeatureBarometer(context)
		 || hasSystemFeatureCompass(context)
		 || hasSystemFeatureGyroscope(context)
		 || hasSystemFeatureLight(context)
		 || hasSystemFeatureProximity(context)) {
			SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
			return manager;
		 } else {
			 throw new Exception("端末がセンサーをサポートしていません．");
		 }
	}

	/**
	 * 実装されているセンサー一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return Sensor
	 * @throws Exception 
	 */
	public static List<Sensor> getSensorList(Context context) throws Exception {
		SensorManager manager = getSensorManager(context);
		return getSensorList(manager);
	}

	/**
	 * 実装されているセンサー一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param manager SensorManager
	 * @return Sensor
	 * @throws Exception managerがnullの場合に例外がされる
	 */
	public static List<Sensor> getSensorList(SensorManager manager) throws Exception {
		return getSensorList(manager, Sensor.TYPE_ALL);
	}

	/**
	 * 実装されているセンサー一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param manager SensorManager
	 * @param type SensorType
	 * @return Sensor
	 * @throws Exception managerがnullの場合に例外が生成される
	 */
	public static List<Sensor> getSensorList(SensorManager manager, int type) throws Exception {
		if (manager == null) {
			throw new Exception("managerがnullです．");
		}

		return manager.getSensorList(type);
	}

	/**
	 * 実装されているセンサー名一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return 実装されているセンサー名一覧
	 * @throws Exception managerがnullの場合に例外が生成される
	 */
	public static List<String> getSensorNameList(Context context) throws Exception {
		SensorManager manager = getSensorManager(context);
		return getSensorNameList(manager);
	}

	/**
	 * 実装されているセンサー名一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param manager 
	 * @return 実装されているセンサー名一覧
	 * @throws Exception managerがnullの場合に例外が生成される
	 */
	public static List<String> getSensorNameList(SensorManager manager) throws Exception {
		return getSensorNameList(manager, Sensor.TYPE_ALL);
	}

	/**
	 * 実装されているセンサー名一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param manager SensorManager
	 * @param type SensorType
	 * @return 実装されているセンサー名一覧
	 * @throws Exception managerがnullの場合に例外が生成される
	 */
	public static List<String> getSensorNameList(SensorManager manager, int type) throws Exception {
		List<Sensor> sensors = getSensorList(manager, type);
		List<String> names = new ArrayList<String>();
		for (Sensor sensor : sensors) {
			names.add(sensor.getName());
		}
		return names;
	}

	/**
	 * 地磁気・加速度センサーの方位角・傾斜角・回転角を取得する
	 * 
	 * @since 0.0.1
	 * @param x X軸
	 * @param y Y軸
	 * @return 方位角・傾斜角・回転角
	 * @throws Exception 方位角・傾斜角・回転角が取得できない場合に例外が生成される
	 */
	public static HashMap<String, Float> getAxisOfAccerometer(int x, int y) throws Exception {
		String[] key = new String[] {
				AZIMUTH_ANGLE,
				INCLINED_ANGLE,
				ROTATION_ANGLE
		};
		float[] value = getAttributes(x, y);
		if (value.length == 3) {
			HashMap<String, Float> map = new HashMap<String, Float>();
			for (int i = 0; i < 3; i++) {
				map.put(key[i], value[i]);
			}
			return map;
		} else {
			throw new Exception("方位角・傾斜角・回転角が取得できませんでした．");
		}
	}

	/**
	 * 傾きセンサーの方位角・傾斜角・回転角を取得する
	 * 
	 * @since 0.0.1
	 * @param x X軸
	 * @param y Y軸
	 * @return 方位角・傾斜角・回転角
	 * @throws Exception 方位角・傾斜角・回転角が取得できない場合に例外が生成される
	 */
	public static HashMap<String, Float> getAxisOfGyroscope(int x, int y) throws Exception {
		String[] key = new String[] {
				AZIMUTH_ANGLE,
				INCLINED_ANGLE,
				ROTATION_ANGLE
		};
		float[] value = getAttributes(x, y);
		if (value.length == 3) {
			HashMap<String, Float> map = new HashMap<String, Float>();
			for (int i = 0; i < 3; i++) {
				map.put(key[i], value[i]);
			}
			return map;
		} else {
			throw new Exception("方位角・傾斜角・回転角が取得できませんでした．");
		}
	}

	/**
	 * 方位角・傾斜角・回転角を取得する
	 * 
	 * @since 0.0.1
	 * @param x X軸
	 * @param y Y軸
	 * @return 方位角・傾斜角・回転角
	 * @throws Exception 引数の値が不正な場合に例外が生成される
	 */
	public static float[] getAttributes(int x, int y) throws Exception {

		if (x != SensorManager.AXIS_X
		 && x != SensorManager.AXIS_Y
		 && x != SensorManager.AXIS_Z
		 && x != SensorManager.AXIS_MINUS_X
		 && x != SensorManager.AXIS_MINUS_Y
		 && x != SensorManager.AXIS_MINUS_Z) {
			throw new Exception("xが不正な値です．");
		}

		if (y != SensorManager.AXIS_X
		 && y != SensorManager.AXIS_Y
		 && y != SensorManager.AXIS_Z
		 && y != SensorManager.AXIS_MINUS_X
		 && y != SensorManager.AXIS_MINUS_Y
		 && y != SensorManager.AXIS_MINUS_Z) {
			throw new Exception("yが不正な値です．");
		}

		float[] inR = new float[MATRIX_SIZE];
		float[] outR = new float[MATRIX_SIZE];
		float[] I = new float[MATRIX_SIZE];
		float[] gravity = new float[AXIS_NUM];
		float[] geomagnetic = new float[AXIS_NUM];
		float[] attribute = new float[AXIS_NUM];

		// 回転行列の計算
		SensorManager.getRotationMatrix(inR, I, gravity, geomagnetic);
		SensorManager.remapCoordinateSystem(inR, x, y, outR);
		SensorManager.getOrientation(outR, attribute);

		return attribute;
	}
}