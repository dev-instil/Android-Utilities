package net.kokohadoko.instil.android.nfc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;

/**
 * NfcAdapterラッパークラス
 * 
 * @author inuko
 * @since 0.0.1
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class NfcAdapterWrapper {

	/** ログ出力用文字列 */
	private static final String LOG_TAG = NfcAdapterWrapper.class.getSimpleName();
	/**  */
	public static final String UTF8 = "UTF-8";
	/**  */
	public static final String UTF16 = "UTF-16";

	/**
	 * デフォルトコンストラクタ
	 * 
	 * @since 0.0.1
	 */
	private NfcAdapterWrapper() {
	}

	/**
	 * 端末がNFCをサポートしているかを調べる
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return サポートしている場合にtrue, そうでない場合にfalseを返す
	 */
	public static boolean hasSystemFeatureNfc(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_NFC);
	}

	/**
	 * NfcAdapterを取得する
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return NfcAdapter
	 * @throws Exception 端末がNFCをサポートしていない場合に例外を生成する
	 */
	public static NfcAdapter getNfcAdapter(Context context) throws Exception {
		if (hasSystemFeatureNfc(context)) {
			NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
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
	 * NFCが有効かどうか
	 * 
	 * @since 0.0.1
	 * @param context コンテキスト
	 * @return 有効な場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がNFCをサポートしていない場合に例外を生成する
	 */
	public static boolean isEnabled(Context context) throws Exception {
		NfcAdapter adapter = getNfcAdapter(context);
		return isEnabled(adapter);
	}

	/**
	 * NFCが有効かどうか
	 * 
	 * @since 0.0.1
	 * @param adapter NfcAdapter
	 * @return 有効な場合に true, そうでない場合に falseを返す
	 * @throws Exception 端末がNFCをサポートしていない場合に例外を生成する
	 */
	public static boolean isEnabled(NfcAdapter adapter) throws Exception {
		if (adapter == null) {
			throw new Exception("");
		}

		return adapter.isEnabled();
	}

	/**
	 * RTD TextフォーマットのNdefレコードを生成する
	 * 
	 * @since 0.0.1
	 * @param isUtf8 UTF8の場合はtrue, UTF16の場合はfalse
	 * @param lang 
	 * @param text 
	 * @return 
	 * @throws Exception 
	 */
	public static NdefRecord createTextRecord(boolean isUtf8, String lang, String text) throws Exception {
		
		if (lang == null) {
			throw new Exception("langがnullです．");
		}
		
		if (text == null) {
			throw new Exception("textがnullです．");
		}

		byte[] langCode;
		byte[] textData;
		byte[] status;

		if (isUtf8) {
			langCode = lang.getBytes(UTF8);
			textData = text.getBytes(UTF8);
			status = new byte[]{(byte)((isUtf8 ? 0:(1<<7)) + langCode.length & 0x03)}; 
		} else {
			langCode = lang.getBytes(UTF16);
			textData = text.getBytes(UTF16);
			status = new byte[]{(byte)((isUtf8 ? 0:(1<<7)) + langCode.length & 0x03)}; 
		}

		byte[] payload = new byte[status.length + langCode.length + textData.length];
		System.arraycopy(status, 0, payload, 0, status.length);
		System.arraycopy(langCode, 0, payload, status.length, langCode.length);
		System.arraycopy(textData, 0, payload, status.length + langCode.length, textData.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[] {}, payload);
	}
}