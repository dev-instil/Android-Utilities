package net.kokohadoko.instil.android.bluetooth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;

/**
 * BluetoothAdapterラッパークラス
 * 
 * @author inuko
 * @since 0.0.1
 */
public class BluetoothAdapterWrapper {

	/** ログ出力用文字列 */
	private static final String LOG_TAG = BluetoothAdapterWrapper.class.getSimpleName();

	/** 他のBluetooth端末から検出可能にする秒数を表す */
	public static final long DISCOVERABLE_DURATION = 120;

	/**
	 * デフォルトコンストラクタ
	 * 
	 * @since 0.0.1
	 */
	private BluetoothAdapterWrapper() {
	}

	/**
	 * 端末がBluetoothをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合にtrue, そうでない場合にfalseを返す
	 */
	public static boolean hasSystemFeatureBluetooth(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
	}

	/**
	 * 端末がBluetoothLEをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合にtrue, そうでない場合にfalseを返す
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean hasSystemFeatureBluetoothLE(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
	}

	/**
	 * BluetoothAdapterを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return BluetoothAdapter
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static BluetoothAdapter getBluetoothAdapter(Context context) throws Exception {
		if (hasSystemFeatureBluetooth(context)) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if (adapter != null) {
				return adapter;
			} else {
				throw new Exception("");
			}
		} else {
			throw new Exception("");
		}
	}

	/**
	 * Bluetoothが有効かどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return 有効な場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean isEnabled(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return isEnabled(adapter);
	}

	/**
	 * Bluetoothが有効かどうか
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 有効な場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean isEnabled(BluetoothAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		return adapter.isEnabled();
	}

	/**
	 * Bluetoothを有効にする
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean enabled(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return enabled(adapter);
	}

	/**
	 * Bluetoothを有効にする
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean enabled(BluetoothAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		return adapter.enable();
	}

	/**
	 * Bluetoothを無効にする
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean disabled(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return disabled(adapter);
	}

	/**
	 * Bluetoothを無効にする
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean disabled(BluetoothAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		return adapter.disable();
	}

	/**
	 * 周辺デバイスの検索を開始する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean startDiscovery(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return startDiscovery(adapter);
	}

	/**
	 * 周辺デバイスの検索を開始する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean startDiscovery(BluetoothAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		return adapter.startDiscovery();
	}

	/**
	 * 周辺デバイスの検索中かどうかを調べる
	 * 
	 * @param context コンテキスト
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean isDiscoverying(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return adapter.isDiscovering();
	}

	/**
	 * 周辺デバイスの検索中かどうかを調べる
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean isDiscoverying(BluetoothAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		return adapter.isDiscovering();
	}

	/**
	 * 周辺デバイスの検索を中止する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean cancelDiscovery(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return adapter.cancelDiscovery();
	}

	/**
	 * 周辺デバイスの検索を中止する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static boolean cancelDiscovery(BluetoothAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		if (isDiscoverying(adapter)) {
			return adapter.cancelDiscovery();
		} else {
			return false;
		}
	}

	/**
	 * Bluetooth設定画面へ遷移する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 */
	public static void startActivity(Context context) {
		Intent intent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
		context.startActivity(intent);
	}

	/**
	 * 他のBluetoothデバイスからの検出を可能にする
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static void discoverable(Context context) throws Exception {
		discoverable(context, DISCOVERABLE_DURATION);
	}

	/**
	 * 他のBluetoothデバイスからの検出を可能にする
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param duration 秒数
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static void discoverable(Context context, long duration) throws Exception {
		if (hasSystemFeatureBluetooth(context)) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
			context.startActivity(intent);
		} else {
			throw new Exception("");
		}
	}

	/**
	 * ペアリング済みのデバイス一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return ペアリング済みのデバイス一覧を取得する
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static Set<BluetoothDevice> getBoundedDevices(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return adapter.getBondedDevices();
	}

	/**
	 * ペアリング済みのデバイス一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return ペアリング済みのデバイス一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static Set<BluetoothDevice> getBoundedDevices(BluetoothAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		return adapter.getBondedDevices();
	}

	/**
	 * ペアリング済みのデバイス名一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return ペアリング済みのデバイス名一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<String> getBoundedDevicesName(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return getBoundedDevicesName(adapter);
	}

	/**
	 * ペアリング済みのデバイス名一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return ペアリング済みのデバイス名一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<String> getBoundedDevicesName(BluetoothAdapter adapter) throws Exception {
		Set<BluetoothDevice> devices = getBoundedDevices(adapter);
		List<String> names = new ArrayList<String>();
		Iterator<BluetoothDevice> itr = devices.iterator();
		while (itr.hasNext()) {
			BluetoothDevice device = itr.next();
			names.add(device.getName());
		}

		return names;
	}

	/**
	 * ペアリング済みのデバイスのアドレス一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return ペアリング済みのデバイスのアドレス一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<String> getBoundedDevicesAddress(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return getBoundedDevicesAddress(adapter);
	}

	/**
	 * ペアリング済みのデバイスのアドレス一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return ペアリング済みのデバイスのアドレス一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<String> getBoundedDevicesAddress(BluetoothAdapter adapter) throws Exception {
		Set<BluetoothDevice> devices = getBoundedDevices(adapter);
		List<String> addresses = new ArrayList<String>();
		Iterator<BluetoothDevice> itr = devices.iterator();
		while (itr.hasNext()) {
			BluetoothDevice device = itr.next();
			addresses.add(device.getAddress());
		}

		return addresses;
	}

	/**
	 * ペアリング済みのデバイス種別を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return ペアリング済みのデバイス種別一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<Integer> getBoundedDevicesType(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return getBoundedDevicesType(adapter);
	}

	/**
	 * ペアリング済みのデバイス種別一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return ペアリング済みのデバイス種別一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<Integer> getBoundedDevicesType(BluetoothAdapter adapter) throws Exception {
		Set<BluetoothDevice> devices = getBoundedDevices(adapter);
		List<Integer> types = new ArrayList<Integer>();
		Iterator<BluetoothDevice> itr = devices.iterator();
		while (itr.hasNext()) {
			BluetoothDevice device = itr.next();
			types.add(device.getType());
		}

		return types;
	}

	/**
	 * ペアリング済みのデバイスのクラス一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return ペアリング済みのデバイスのクラス一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<BluetoothClass> getBoundedDevicesClass(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return getBoundedDevicesClass(adapter);
	}

	/**
	 * ペアリング済みのデバイスのクラス一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return ペアリング済みのデバイスのクラス一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<BluetoothClass> getBoundedDevicesClass(BluetoothAdapter adapter) throws Exception {
		Set<BluetoothDevice> devices = getBoundedDevices(adapter);
		List<BluetoothClass> classes = new ArrayList<BluetoothClass>();
		Iterator<BluetoothDevice> itr = devices.iterator();
		while (itr.hasNext()) {
			BluetoothDevice device = itr.next();
			classes.add(device.getBluetoothClass());
		}

		return classes;
	}

	/**
	 * ペアリング済みのデバイスのUUID一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return ペアリング済みのデバイスのUUID一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<ParcelUuid[]> getBoundedDevicesUuid(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return getBoundedDevicesUuid(adapter);
	}

	/**
	 * ペアリング済みのデバイスのUUID一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return ペアリング済みのデバイスのUUID一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<ParcelUuid[]> getBoundedDevicesUuid(BluetoothAdapter adapter) throws Exception {
		Set<BluetoothDevice> devices = getBoundedDevices(adapter);
		List<ParcelUuid[]> uuids = new ArrayList<ParcelUuid[]>();
		Iterator<BluetoothDevice> itr = devices.iterator();
		while (itr.hasNext()) {
			BluetoothDevice device = itr.next();
			uuids.add(device.getUuids());
		}

		return uuids;
	}

	/**
	 * ペアリング済みのデバイスの状態一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return ペアリング済みのデバイスの状態一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<Integer> getBoundedDevicesBondState(Context context) throws Exception {
		BluetoothAdapter adapter = getBluetoothAdapter(context);
		return getBoundedDevicesBondState(adapter);
	}

	/**
	 * ペアリング済みのデバイスの状態一覧を取得する
	 * 
	 * @since 0.0.1
	 * @param adapter BluetoothAdapter
	 * @return ペアリング済みのデバイスの状態一覧
	 * @throws Exception 端末がBluetoothをサポートしていない場合に例外を生成する
	 */
	public static List<Integer> getBoundedDevicesBondState(BluetoothAdapter adapter) throws Exception {
		Set<BluetoothDevice> devices = getBoundedDevices(adapter);
		List<Integer> bondStates = new ArrayList<Integer>();
		Iterator<BluetoothDevice> itr = devices.iterator();
		while (itr.hasNext()) {
			BluetoothDevice device = itr.next();
			bondStates.add(device.getBondState());
		}

		return bondStates;
	}
}