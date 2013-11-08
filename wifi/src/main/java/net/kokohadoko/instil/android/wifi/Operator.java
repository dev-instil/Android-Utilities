package net.kokohadoko.instil.android.wifi;

/**
 * 比較演算子
 * 
 * @author inuko
 * @since 0.0.1
 */
public class Operator {

	/** ログ出力用文字列 */
	private static final String LOG_TAG = Operator.class.getSimpleName();

	/** より小さい */
	public static final int LESS = 1;
	/** 以下 */
	public static final int LESS_THAN = 2;
	/** 以上 */
	public static final int MORE = 3;
	/** より大きい */
	public static final int MORE_THAN = 4;

	/**
	 * デフォルトコンストラクタ
	 * 
	 * @since 0.0.1
	 */
	private Operator() {
	}
}
