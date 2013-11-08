package net.kokohadoko.instil.android.wifi.exception;

/**
 * WiFiWrapperにおける独自例外クラス
 * 
 * @author inuko
 * @since 0.0.1
 */
public class WiFiWrapperException extends Exception {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * デフォルトコンストラクタ
	 * 
	 * @since 0.0.1
	 */
	public WiFiWrapperException() {
	}

	/**
	 * コンストラクタ
	 * 
	 * @since 0.0.1
	 * @param detailMessage メッセージ
	 */
	public WiFiWrapperException(String detailMessage) {
		super(detailMessage);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * コンストラクタ
	 * 
	 * @since 0.0.1
	 * @param throwable 例外オブジェクト
	 */
	public WiFiWrapperException(Throwable throwable) {
		super(throwable);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * コンストラクタ
	 * 
	 * @since 0.0.1
	 * @param detailMessage メッセージ
	 * @param throwable 例外オブジェクト
	 */
	public WiFiWrapperException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
