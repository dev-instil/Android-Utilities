package net.kokohadoko.instil.android.wifi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.kokohadoko.instil.android.wifi.exception.WiFiWrapperException;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

/**
 * Wifi関連の制御を管理するライブラリ
 * 
 * @author inuko
 * @since 0.0.1
 */
public class WifiWrapper {

	/** ログ出力用文字列 */
	private static final String LOG_TAG = WifiWrapper.class.getSimpleName();

	/**
	 * デフォルトコンストラクタ
	 * 
	 * @since 0.0.1
	 */
	private WifiWrapper() {
	}

	/**
	 * 端末がWiFiをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @return WiFiをサポートしている場合にtrue, そうでない場合にfalseを返す
	 */
	public static boolean hasSystemFeatureWiFi(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_WIFI);
	}

	/**
	 * 端末がWiFiDirectをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return WiFiDirectをサポートしている場合にtrue, そうでない場合にfalseを返す
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static boolean hasSystemFeatureWiFiDirect(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT);
	}

	/**
	 * WifiManagerの取得を行う
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return manager WifiManager
	 * @throws WiFiWrapperException WiFiManagerがnull または WiFiがサポートされていない場合に例外を生成する
	 */
	public static WifiManager getWifiManager(Context context) throws WiFiWrapperException {

		if (hasSystemFeatureWiFi(context)) {
			WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if (manager.isWifiEnabled()) {
				return manager;
			} else {
				throw new WiFiWrapperException(
						new IllegalStateException("WiFiManagerがnullです．"));
			}
		} else {
			throw new WiFiWrapperException(
					new UnsupportedOperationException("WiFiはサポートされていません．"));
		}
	}

	/**
	 * Check if scanning is always available. If this return true, apps can issue startScan() and fetch scan results even when Wi-Fi is turned off. To change this setting, see ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE.
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return 
	 * @throws WiFiWrapperException WiFiManagerがnullの場合に例外を生成する
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean isScanAlwaysAvailable(Context context) throws WiFiWrapperException {
		WifiManager manager = getWifiManager(context);
		return isScanAlwaysAvailable(manager);
	}

	/**
	 * Check if scanning is always available. If this return true, apps can issue startScan() and fetch scan results even when Wi-Fi is turned off. To change this setting, see ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE.
	 * 
	 * @since 0.0.1
	 * @param manager WifiManager
	 * @return 
	 * @throws WiFiWrapperException WiFiManagerがnullの場合に例外を生成する
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static boolean isScanAlwaysAvailable(WifiManager manager) throws WiFiWrapperException {
		if (manager == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("WiFiManagerがnullです．"));
		}

		return manager.isScanAlwaysAvailable();
	}

	/**
	 * AccessPointをスキャンして結果を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return the list of access points found in the most recent scan.
	 * @throws WiFiWrapperException 
	 */
	public static List<ScanResult> getScanResults(Context context) throws WiFiWrapperException {

		WifiManager manager = WifiWrapper.getWifiManager(context);
		if (manager != null) {
			if (manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
				// 
				manager.startScan();
				// 
				return manager.getScanResults();
			} else {
				throw new WiFiWrapperException("WiFiのスキャンが実行できない状態です．");
			}
		} else {
			throw new WiFiWrapperException("WiFiManagerの取得に失敗しました．");
		}
	}

	/**
	 * 指定されたSSIDに一致するアクセスポイントがあるかどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return 指定されたSSIDに一致するアクセスポイントが存在する場合に true, そうでない場合に falseを返す
	 * @see {@link #isMatchesSSID(Context, String)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean isMatchesSSID(Context context, String ssid) throws WiFiWrapperException {
		List<ScanResult> results = getMatchesScanResultsOfSSID(context, ssid);
		if (results.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 指定されたSSIDのいずれかに一致するアクセスポイントがあるかどうか
	 * 
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return 指定されたSSIDのいずれかに一致するアクセスポイントが存在する場合に true, そうでない場合に falseを返す
	 * @see {@link #isMatchesSSID(Context, String)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean isMatchesSSID(Context context, List<String> ssid) throws WiFiWrapperException {
		List<ScanResult> results = getMatchesScanResultsOfSSID(context, ssid);
		if (results.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 指定されたBSSIDに一致するアクセスポイントがあるかどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param bssid BSSID
	 * @return 指定されたBSSIDに一致するアクセスポイントが存在する場合に true, そうでない場合に falseを返す
	 * @see {@link #isMatchesBSSID(Context, List)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean isMatchesBSSID(Context context, String bssid) throws WiFiWrapperException {
		ScanResult result = getMatchesScanResultOfBSSID(context, bssid);
		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 指定されたBSSIDのいずれかに一致するアクセスポイントがあるかどうか
	 * 
	 * @param context コンテキスト
	 * @param bssid BSSID
	 * @return 指定されたBSSIDのいずれかに一致するアクセスポイントが存在する場合に true, そうでない場合に falseを返す
	 * @see {@link #isMatchesBSSID(Context, String)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean isMatchesBSSID(Context context, List<String> bssid) throws WiFiWrapperException {
		ScanResult result = getMatchesScanResultOfBSSID(context, bssid);
		if (result != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 指定されたSSIDに一致するアクセスポイントを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return SSIDと一致するScanResult
	 * @throws WiFiWrapperException 
	 */
	public static List<ScanResult> getMatchesScanResultsOfSSID(Context context, String ssid) throws WiFiWrapperException {

		if (ssid != null) {
			List<String> list = new ArrayList<String>();
			list.add(ssid);
			return getMatchesScanResultsOfSSID(context, list);
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("ssidがnullです．"));
		}

	}

	/**
	 * 指定されたSSIDのどれかに一致するアクセスポイントをすべて取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return SSIDと一致するScanResult
	 * @throws WiFiWrapperException 
	 */
	public static List<ScanResult> getMatchesScanResultsOfSSID(Context context, List<String> ssid) throws WiFiWrapperException {
		if (ssid != null) {
			List<ScanResult> scanResults = new ArrayList<ScanResult>();

			List<ScanResult> results = getScanResults(context);
			int size = results.size();
			for (int i = 0; i < size; i++) {
				ScanResult result = results.get(i);

				for (int j = 0; j < ssid.size(); j++) {
					String _ssid = ssid.get(j);
					
					if (_ssid != null && result.equals(_ssid)) {
						scanResults.add(result);
					}
				}
			}

			return scanResults;

		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("ssidがnullです．"));
		}
	}

	/**
	 * 指定されたBSSIDに一致するアクセスポイントを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param bssid BSSID
	 * @return BSSIDと一致するScanResult
	 * @throws WiFiWrapperException 
	 */
	public static ScanResult getMatchesScanResultOfBSSID(Context context, String bssid) throws WiFiWrapperException {
		if (bssid != null) {
			List<String> list = new ArrayList<String>();
			list.add(bssid);

			return getMatchesScanResultOfBSSID(context, list);
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("bssidがnullです．"));
		}
	}

	/**
	 * 指定されたBSSIDのどれかに一致するアクセスポイントを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param bssid BSSID
	 * @return BSSIDと一致するScanResult
	 * @throws WiFiWrapperException bssidがnullの場合に例外が生成される
	 */
	public static ScanResult getMatchesScanResultOfBSSID(Context context, List<String> bssid) throws WiFiWrapperException {
		if (bssid != null) {
			List<ScanResult> results = getScanResults(context);
			int size = results.size();
			for (int i = 0; i < size; i++) {
				ScanResult result = results.get(i);

				for (int j = 0; j < bssid.size(); j++) {
					String _bssid = bssid.get(j);
					if (_bssid != null && result.equals(_bssid)) {
						return result;
					}
				}
			}

			return null;

		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("bssidがnullです．"));
		}
	}

	/**
	 * 指定されたlevelより大きい、または小さいアクセスポイントをすべて取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param level RSSI値
	 * @param operator 比較演算子（より小さい、以下、より大きい、以上）
	 * @return 指定したlevelとOperatorの条件を満たすScanResult
	 * @see {@link #LESS}, {@link #LESS_THAN}, {@link #MORE}, {@link #MORE_THAN}
	 * @throws WiFiWrapperException 
	 */
	public static List<ScanResult> getScanResultsOfLevel(Context context, int level, int operator) throws WiFiWrapperException {
		if (level <= 0) {
			List<ScanResult> scanResults = new ArrayList<ScanResult>();

			List<ScanResult> results = getScanResults(context);
			Iterator<ScanResult> itr = results.iterator();
			while (itr.hasNext()) {
				ScanResult result = itr.next();
				switch (operator) {
					case Operator.LESS:{
						if (result.level < level) {
							scanResults.add(result);
						}
					}
						break;
						
					case Operator.LESS_THAN:
					{
						if (result.level <= level) {
							scanResults.add(result);
						}
					}
						break;
				
					case Operator.MORE:
					{
						if (level < result.level) {
							scanResults.add(result);
						}
					}
						break;

					case Operator.MORE_THAN:
					{
						if (level <= result.level) {
							scanResults.add(result);
						}
					}
						break;

					default:
						break;
				}
			}

			return scanResults;
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("引数が不正な値です．"));
		}
	}

	/**
	 * 指定された範囲内のlevelのアクセスポイントをすべて取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param high 上限値
	 * @param low 下限値
	 * @return 指定した範囲内のlevelの条件を満たすScanResult
	 * @exception WiFiWrapperException 
	 */
	public static List<ScanResult> getScanResultsOfLevelInTheRange(Context context, int high, int low) throws WiFiWrapperException {

		if (0 < high) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("上限値が不正な値です．"));
		}

		if (0 < low) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("下限値が不正な値です．"));
		}

		if (high >= low) {
			List<ScanResult> scanResults = new ArrayList<ScanResult>();

			List<ScanResult> results = getScanResults(context);
			Iterator<ScanResult> itr = results.iterator();
			while (itr.hasNext()) {
				ScanResult result = itr.next();
				if (low <= result.level && result.level <= high) {
					scanResults.add(result);
				}
			}

			return scanResults;
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("引数が不正です．"));
		}
	}

	/**
	 * 接続中のネットワークを切断する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return 成功した場合にtrue, そうでない場合に false を返す
	 * @see {@link #disableNetwork(WifiManager)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean disableNetwork(Context context) throws WiFiWrapperException {
		WifiManager manager = getWifiManager(context);
		return disableNetwork(manager);
	}

	/**
	 * 接続中のネットワークを切断する
	 * 
	 * @since 0.0.1
	 * @param manager WifiManager
	 * @return 成功した場合にtrue, そうでない場合に false を返す
	 * @see {@link #disableNetwork(Context)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean disableNetwork(WifiManager manager) throws WiFiWrapperException {
		
		if (manager == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("managerがnullです．"));
		}

		WifiInfo wifiInfo = getWifiInfo(manager);
		if (wifiInfo != null) {
			return manager.disableNetwork(wifiInfo.getNetworkId());
		} else {
			throw new WiFiWrapperException("WiFi情報の取得に失敗しました．");
		}
	}

	/**
	 * 接続中のネットワーク情報を取得する
	 * 
	 * @since 0.0.1
	 * @param wifiManager WifiManager
	 * @return wifiInfo or null
	 * @throws WiFiWrapperException managerがnullの場合に例外が生成される
	 */
	public static WifiInfo getWifiInfo(WifiManager manager) throws WiFiWrapperException {
		if (manager != null) {
			WifiInfo wifiInfo = manager.getConnectionInfo();
			return wifiInfo;
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("managerがnullです．"));
		}
	}

	/**
	 * 設定済みのWiFi設定からSSID, BSSIDが一致するものがあるかどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @param bssid BSSID
	 * @return SSID, BSSIDが一致する設定済みのWiFi設定が存在する場合に true, そうでない場合に falseを返す
	 * @throws WiFiWrapperException 
	 */
	public static boolean isWifiConfiguration(Context context, String ssid, String bssid) throws WiFiWrapperException {
		WifiConfiguration configuration = getMatchesWifiConfiguration(context, ssid, bssid);
		if (configuration != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 設定済みのWiFi設定からSSIDが一致するものがあるかどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return SSIDが一致する設定済みのWiFi設定が存在する場合に true, そうでない場合に falseを返す
	 * @throws WiFiWrapperException 
	 */
	public static boolean isWifiConfigurationOfSSID(Context context, String ssid) throws WiFiWrapperException {
		List<WifiConfiguration> configurations = getMatchesWifiConfigurationsOfSSID(context, ssid);
		if (configurations.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 設定済みのWiFi設定からBSSIDが一致するものがあるかどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param bssid BSSID
	 * @return BSSIDが一致する設定済みのWiFi設定が存在する場合に true, そうでない場合に falseを返す
	 * @throws WiFiWrapperException 
	 */
	public static boolean isWifiConfigurationOfBSSID(Context context, String bssid) throws WiFiWrapperException {
		WifiConfiguration configuration = getMatchesWifiConfigurationOfBSSID(context, bssid);
		if (configuration != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 設定済みのWiFi設定からSSIDが一致するものを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return SSIDが一致する設定済みのWiFi設定
	 * @throws WiFiWrapperException 
	 */
	public static List<WifiConfiguration> getMatchesWifiConfigurationsOfSSID(Context context, String ssid) throws WiFiWrapperException {

		if (ssid == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("ssidがnullです．"));
		}

		WifiManager manager = getWifiManager(context);
		if (manager != null) {
			List<WifiConfiguration> list = new ArrayList<WifiConfiguration>();
			// 設定済みのWiFi設定を取得
			List<WifiConfiguration> configurations = manager.getConfiguredNetworks();
			Iterator<WifiConfiguration> itr = configurations.iterator();
			while (itr.hasNext()) {
				WifiConfiguration configuration = itr.next();
				if (ssid.equals(configuration.SSID)) {
					list.add(configuration);
				}
			}

			return list;
		} else {
			throw new WiFiWrapperException("managerがnullです．");
		}
	}

	/**
	 * 設定済みのWiFi設定からBSSIDが一致するものを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param bssid BSSID
	 * @return BSSIDが一致する設定済みのWiFi設定
	 * @throws WiFiWrapperException WiFiManagerがnullまたはbssidがnullの場合に例外が生成される
	 */
	public static WifiConfiguration getMatchesWifiConfigurationOfBSSID(Context context, String bssid) throws WiFiWrapperException {

		if (bssid == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("BSSIDがnullです．"));
		}

		WifiManager manager = getWifiManager(context);
		if (manager != null) {
			// 設定済みのWiFi設定を取得
			List<WifiConfiguration> configurations = manager.getConfiguredNetworks();
			Iterator<WifiConfiguration> itr = configurations.iterator();
			while (itr.hasNext()) {
				WifiConfiguration configuration = itr.next();
				if (bssid.equals(configuration.BSSID)) {
					return configuration;
				}
			}

			return null;
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("WifiManagerがnullです．"));
		}
	}

	/**
	 * 設定済みのWiFi設定からSSIDとBSSIDが一致するものを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @param bssid BSSID
	 * @return SSID, BSSIDが一致する設定済みのWiFi設定
	 * @throws WiFiWrapperException 
	 */
	public static WifiConfiguration getMatchesWifiConfiguration(Context context, String ssid, String bssid) throws WiFiWrapperException {

		if (ssid == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("WifiConfigurationがnullです．"));
		}
		if (bssid == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("WifiConfigurationがnullです．"));
		}

		WifiManager manager = getWifiManager(context);
		if (manager != null) {
			// 設定済みのWiFi設定を取得
			List<WifiConfiguration> configurations = manager.getConfiguredNetworks();
			Iterator<WifiConfiguration> itr = configurations.iterator();
			while (itr.hasNext()) {
				WifiConfiguration configuration = itr.next();
				if (ssid.equals(configuration.SSID) && bssid.equals(configuration.BSSID)) {
					return configuration;
				}
			}

			return null;
		} else {
			throw new WiFiWrapperException("managerがnullです．");
		}
	}

	/**
	 * 指定されたWiFiに対して接続を行う
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param wifiConfiguration 
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(Context, int, boolean)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean enableNetwork(Context context, int networkId) throws WiFiWrapperException {
		return enableNetwork(context, networkId, true);
	}

	/**
	 * 指定されたWiFiに対して接続を行う
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param wifiConfiguration 
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(Context, int)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean enableNetwork(Context context, int networkId, boolean disableOthers) throws WiFiWrapperException {
		WifiManager manager = getWifiManager(context);
		return enableNetwork(manager, networkId, disableOthers);
	}

	/**
	 * 指定されたWiFiに対して接続を行う
	 * 
	 * @since 0.0.1
	 * @param manager WifiManager
	 * @param networkId NetworkId
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(WifiManager, int, boolean)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean enableNetwork(WifiManager manager, int networkId) throws WiFiWrapperException {
		return enableNetwork(manager, networkId, true);
	}

	/**
	 * 指定されたWiFiに対して接続を行う
	 * 
	 * @since 0.0.1
	 * @param wifiManager WifiManager
	 * @param networId NetworkId
	 * @param disableOthers 
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(WifiManager, int)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean enableNetwork(WifiManager manager, int networkId, boolean disableOthers) throws WiFiWrapperException {
		if (manager == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("managerがnullです．"));
		}

		if (networkId != -1) {

			if (disableNetwork(manager)) {
				return manager.enableNetwork(networkId, disableOthers);
			} else {
				throw new WiFiWrapperException("ネットワークの初期化に失敗しました．");
			}
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("networkIdが不正な値です．"));
		}
	}

	/**
	 * 指定されたWiFiに対して接続を行う
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param configuration 
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(Context, WifiConfiguration, boolean)}
	 * @throws WiFiWrapperException 
	 */
	public static boolean enableNetwork(Context context, WifiConfiguration configuration) throws WiFiWrapperException {
		WifiManager manager = getWifiManager(context);
		return enableNetwork(manager, configuration);
	}

	/**
	 * 指定されたWiFiに対して接続を行う
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param configuration 
	 * @param disableOthers 
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(Context, WifiConfiguration)}
	 */
	public static boolean enableNetwork(Context context, WifiConfiguration configuration, boolean disableOthers) throws WiFiWrapperException {
		WifiManager manager = getWifiManager(context);
		return enableNetwork(manager, configuration, disableOthers);
	}

	/**
	 * 指定されたWiFiに対して接続を行う
	 * 
	 * @since 0.0.1
	 * @param manager WifiManager
	 * @param configuration 
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(WifiManager, WifiConfiguration, boolean)}
	 * @throws WiFiWrapperException manager もしくは configuration が null の場合に例外を生成する
	 */
	public static boolean enableNetwork(WifiManager manager, WifiConfiguration configuration) throws WiFiWrapperException {
		return enableNetwork(manager, configuration, true);
	}

	/**
	 * 
	 * 
	 * @since 0.0.1
	 * @param manager WifiManager
	 * @param configuration 
	 * @param disableOthers 
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @see {@link #enableNetwork(WifiManager, WifiConfiguration)}
	 * @throws WiFiWrapperException manager もしくは configuration が null の場合に例外を生成する
	 */
	public static boolean enableNetwork(WifiManager manager, WifiConfiguration configuration, boolean disableOthers) throws WiFiWrapperException {
		if (manager == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("managerがnullです．"));
		}

		if (configuration == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("configurationがnullです．"));
		}

		return manager.enableNetwork(configuration.networkId, disableOthers);
	}

	/**
	 * 指定されたSSIDに一致するアクセスポイントに接続を行う
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws WiFiWrapperException 指定されたSSIDに一致するアクセスポイントが見つからなかった場合に例外が生成される
	 */
	public static boolean connectForSSID(Context context, String ssid) throws WiFiWrapperException {
		List<WifiConfiguration> configurations = getMatchesWifiConfigurationsOfSSID(context, ssid);
		if (configurations.isEmpty()) {
			throw new WiFiWrapperException("SSIDに一致するWiFiConfigurationがありませんでした．");
		} else {
			Iterator<WifiConfiguration> itr = configurations.iterator();
			while (itr.hasNext()) {
				WifiConfiguration wifiConfiguration = itr.next();
				if (ssid != null && ssid.equals(wifiConfiguration.SSID)) {
					return enableNetwork(context, wifiConfiguration);
				}
			}
		}

		return false;
	}

	/**
	 * 指定されたBSSIDに一致するアクセスポイントに接続を行う
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param bssid BSSID
	 * @return 成功した場合に true, そうでない場合に falseを返す
	 * @throws WiFiWrapperException 指定されたBSSIDに一致するアクセスポイントが見つからない場合に例外が生成される
	 */
	public static boolean connectForBSSID(Context context, String bssid) throws WiFiWrapperException {
		WifiConfiguration configuration = getMatchesWifiConfigurationOfBSSID(context, bssid);
		if (configuration != null) {
			if (bssid != null && bssid.equals(configuration.BSSID)) {
				return enableNetwork(context, configuration);
			}
		} else {
			throw new WiFiWrapperException("BSSIDに一致するWiFiConfigurationがありませんでした．");
		}

		return false;
	}

	/**
	 * WiFiが有効かどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return Wifiが有効な場合に true、そうでない場合にfalseを返す
	 * @see {@link #isWifiEnabled(Context, WifiManager)}
	 * @throws WiFiWrapperException managerがnullの場合に例外が生成される
	 */
	public static boolean isWifiEnabled(Context context) throws WiFiWrapperException {
		WifiManager manager = getWifiManager(context);
		return isWifiEnabled(context, manager);
	}

	/**
	 * WiFiが有効かどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param manager WifiManager
	 * @return Wifiが有効な場合に true、そうでない場合にfalseを返す
	 * @see {@link #isWifiEnabled(Context)}
	 * @throws WiFiWrapperException managerがnullの場合に例外が生成される
	 */
	public static boolean isWifiEnabled(Context context, WifiManager manager) throws WiFiWrapperException {
		if (manager != null) {
			return manager.isWifiEnabled();
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("WiFiManagerがnullです．"));
		}
	}

	/**
	 * WiFiの状態を切り替える
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param enabled 有効にする場合は true, そうでない場合は falseを指定
	 * @return 成功した場合に true, そうでない場合は false を返す
	 * @see {@link #setWifiEnabled(Context, WifiManager, boolean)}
	 * @throws WiFiWrapperException managerがnullの場合に例外が生成される
	 */
	public static boolean setWifiEnabled(Context context, boolean enabled) throws WiFiWrapperException {
		WifiManager wifiManager = getWifiManager(context);
		return setWifiEnabled(context, wifiManager, enabled);
	}

	/**
	 * WiFiの状態を切り替える
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param manager WifiManager
	 * @param enabled 有効にする場合は true, そうでない場合は falseを指定
	 * @return 成功した場合に true, そうでない場合は false を返す
	 * @see {@link #setWifiEnabled(Context, WifiManager, boolean)}
	 * @throws WiFiWrapperException managerがnullの場合に例外が生成される
	 */
	public static boolean setWifiEnabled(Context context, WifiManager manager, boolean enabled) throws WiFiWrapperException {
		if (manager != null) {
			return manager.setWifiEnabled(enabled);
		} else {
			throw new WiFiWrapperException(
					new IllegalArgumentException("WiFiManagerがnullです．"));
		}
	}

	/**
	 * セキュリティ認証のないWifiConfigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForNoneSecurity(WifiManager, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForNoneSecurity(Context context, String ssid) throws WiFiWrapperException {
		WifiManager wifiManager = getWifiManager(context);
		return registerWifiConfigurationForNoneSecurity(wifiManager, ssid);
	}

	/**
	 * セキュリティ認証のないWifiConfigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param wifiManager WifiManager
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForNoneSecurity(Context, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForNoneSecurity(WifiManager wifiManager, String ssid) throws WiFiWrapperException {
		WifiConfiguration configuration = createWifiConfigurationForNoneSecurity(ssid);
		int networkId = wifiManager.addNetwork(configuration);
		if (networkId != -1) {
			if (wifiManager.saveConfiguration()) {
				return wifiManager.updateNetwork(configuration);
			} else {
				throw new WiFiWrapperException("WiFi設定の保存に失敗しました．");
			}
		} else {
			throw new WiFiWrapperException("WiFi設定の登録に失敗しました．");
		}
	}

	/**
	 * WEP認証のWifiConfigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForWEP(WifiManager, String, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForWEP(Context context, String ssid, String password) throws WiFiWrapperException {
		WifiManager wifiManager = getWifiManager(context);
		return registerWifiConfigurationForWEP(wifiManager, ssid, password);
	}

	/**
	 * WEP認証のWifiConfigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param wifiManager WifiManager
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForWEP(Context, String, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForWEP(WifiManager wifiManager, String ssid, String password) throws WiFiWrapperException {
		WifiConfiguration configuration = createWifiConfigurationForWEP(ssid, password);
		int networkId = wifiManager.addNetwork(configuration);
		if (networkId != -1) {
			if (wifiManager.saveConfiguration()) {
				return wifiManager.updateNetwork(configuration);
			} else {
				throw new WiFiWrapperException("WiFi設定の保存に失敗しました．");
			}
		} else {
			throw new WiFiWrapperException(
					"WiFi設定の登録に失敗しました．");
		}
	}

	/**
	 * WPA認証のWifiConfigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForWPA(WifiManager, String, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForWPA(Context context, String ssid, String password) throws WiFiWrapperException {
		WifiManager wifiManager = getWifiManager(context);
		return registerWifiConfigurationForWPA(wifiManager, ssid, password);
	}

	/**
	 * WPA認証のWifiConfigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param wifiManager WifiManager
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForWPA(Context, String, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForWPA(WifiManager wifiManager, String ssid, String password) throws WiFiWrapperException {
		WifiConfiguration configuration = createWifiConfigurationForWPA(ssid, password);
		int networkId = wifiManager.addNetwork(configuration);
		if (networkId != -1) {
			if (wifiManager.saveConfiguration()) {
				return wifiManager.updateNetwork(configuration);
			} else {
				throw new WiFiWrapperException("WiFi設定の保存に失敗しました．");
			}
		} else {
			throw new WiFiWrapperException("WiFi設定の登録に失敗しました．");
		}
	}
	
	/**
	 * WPA2PSK認証のWifiConfigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForWPA2PSK(WifiManager, String, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForWPA2PSK(Context context, String ssid, String password) throws WiFiWrapperException {
		WifiManager wifiManager = getWifiManager(context);
		return registerWifiConfigurationForWPA2PSK(wifiManager, ssid, password);
	}

	/**
	 * WPA2PSK認証のWifiCofigurationを登録する
	 * 
	 * @since 0.0.1
	 * @param wifiManager WifiManager
	 * @param ssid SSID
	 * @return ネットワーク設定の更新に成功した場合に networkIdを, そうでない場合に -1 を返す
	 * @see {@link #registerWifiConfigurationForWPA2PSK(Context, String, String)}
	 * @throws WiFiWrapperException WiFi設定の保存，または登録に失敗した場合に例外が生成される
	 */
	public static int registerWifiConfigurationForWPA2PSK(WifiManager manager, String ssid, String password) throws WiFiWrapperException {
		WifiConfiguration configuration = createWifiConfigurationForWPA2PSK(ssid, password);
		int networkId = manager.addNetwork(configuration);
		if (networkId != -1) {
			if (manager.saveConfiguration()) {
				return manager.updateNetwork(configuration);
			} else {
				throw new WiFiWrapperException("WiFi設定の保存に失敗しました．");
			}
		} else {
			throw new WiFiWrapperException("WiFi設定の登録に失敗しました．");
		}
	}

	/**
	 * 指定されたSSIDでセキュリティ認証のないWifiConfigurationを生成する
	 * 
	 * @since 0.0.1
	 * @param wifiManager 
	 * @param ssid SSID
	 * @return 指定されたSSIDとパスワードが設定されたセキュリティ認証のないWifiConfiguration
	 * @see {@link #createWifiConfigurationForNoneSecurity(Context, String)}
	 * @throws WiFiWrapperException ssid がnullの場合に例外を生成する
	 */
	public static WifiConfiguration createWifiConfigurationForNoneSecurity(String ssid) throws WiFiWrapperException {

		if (ssid == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("ssidがnullです．"));
		}

		WifiConfiguration wifiConfiguration = new WifiConfiguration();
		wifiConfiguration.SSID = "\"" + ssid + "\"";
		wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wifiConfiguration.allowedAuthAlgorithms.clear();
		wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.PairwiseCipher.CCMP);
		wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.PairwiseCipher.TKIP);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

		return wifiConfiguration;
	}

	/**
	 * 指定されたSSIDとパスワードでWEP認証のWifiConfigurationを生成する
	 * 
	 * @since 0.0.1
	 * @param wifiManager 
	 * @param ssid SSID
	 * @param password パスワード
	 * @return 指定されたSSIDとパスワードが設定されたWEP認証のWifiConfiguration
	 * @see {@link #createWifiConfigurationForWEP(Context, String, String)}
	 * @throws WiFiWrapperException ssid または password がnullの場合に例外を生成する
	 */
	public static WifiConfiguration createWifiConfigurationForWEP(String ssid, String password) throws WiFiWrapperException {
		if (ssid == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("ssidがnullです．"));
		}

		if (password == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("passwordがnullです．"));
		}

		WifiConfiguration wifiConfiguration = new WifiConfiguration();
		wifiConfiguration.SSID = "\"" + ssid + "\"";
		wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
		wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		wifiConfiguration.wepKeys[0] = "\"password\"";
		wifiConfiguration.wepTxKeyIndex = 0;
		
		return wifiConfiguration;
	}

	/**
	 * 指定されたSSIDとパスワードでWPA認証のWifiConfigurationを生成する
	 * 
	 * @since 0.0.1
	 * @param ssid SSID
	 * @param password パスワード
	 * @return 指定されたSSIDとパスワードが設定されたWPA認証のWifiConfiguration
	 * @see {@link #createWifiConfigurationForWPA(Context, String, String)}
	 * @throws WiFiWrapperException ssid または password が null の場合に例外が生成される
	 */
	public static WifiConfiguration createWifiConfigurationForWPA(String ssid, String password) throws WiFiWrapperException {
		return createWifiConfigurationForWPA2PSK(ssid, password);
	}

	/**
	 * 指定されたSSIDとパスワードでWPA2PSK認証のWifiConfigurationを生成する
	 * 
	 * @since 0.0.1
	 * @param ssid SSID
	 * @param password パスワード
	 * @return 指定されたSSIDとパスワードが設定されたWPA2PSK認証のWifiConfiguration
	 * @see {@link #createWifiConfigurationForWPA2PSK(Context, String, String)}
	 * @throws WiFiWrapperException ssid または password が null の場合に例外が生成される
	 */
	public static WifiConfiguration createWifiConfigurationForWPA2PSK(String ssid, String password) throws WiFiWrapperException {
		if (ssid == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("ssidがnullです．"));
		}

		if (password == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("passwordがnullです．"));
		}

		WifiConfiguration wifiConfiguration = new WifiConfiguration();
		wifiConfiguration.SSID = "\"" + ssid + "\"";
		wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		wifiConfiguration.preSharedKey = "\"password\"";

		return wifiConfiguration;
	}

	/**
	 * DHCP情報を取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return DHCP情報を返す
	 * @see {@link #getDhcpInfo(WifiManager)}
	 * @throws WiFiWrapperException managerがnullの場合に例外を生成する
	 */
	@SuppressWarnings("deprecation")
	public static DhcpInfo getDhcpInfo(Context context) throws WiFiWrapperException {
		WifiManager manager = getWifiManager(context);
		return getDhcpInfo(manager);
	}

	/**
	 * DHCP情報を取得する
	 * 
	 * @since 0.0.1
	 * @param manager WifiManager
	 * @return DHCP情報を返す
	 * @see {@link #getDhcpInfo(Context)}
	 * @throws WiFiWrapperException managerがnullの場合に例外を生成する
	 */
	@SuppressWarnings("deprecation")
	public static DhcpInfo getDhcpInfo(WifiManager manager) throws WiFiWrapperException {
		if (manager == null) {
			throw new WiFiWrapperException(
					new IllegalArgumentException("WiFiManagerがnullです．"));
		}

		return manager.getDhcpInfo();
	}
}