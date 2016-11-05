package jp.co.kizuna.plus.chat.main.window;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainForm {

	/**
	 * ウィンドウタイトル
	 */
	private final static String WINDOW_TITLE = "Chattt";

	/**
	 * デフォルトウィンドウ幅
	 */
	private final static int DEFAULT_WIDTH = 800;

	/**
	 * デフォルトウィンドウ高さ
	 */
	private final static int DEFAULT_HEIGHT = 600;

	/**
	 * メインフレーム
	 */
	private JFrame mainFrame;

	/**
	 * メインパネル
	 */
	private UserPanel mainPanel;

	/**
	 * メッセージテキストエリア
	 */
	private JTextField textField;

	/**
	 * コンストラクタ
	 */
	public MainForm() {
		init();
	}

	/**
	 * フォームの初期化
	 */
	private void init() {
		this.mainFrame = new JFrame(WINDOW_TITLE);

		// 閉じるボタン押下時のアプリケーションの振る舞いを決定
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ウィンドウの初期サイズ（幅、高さ）をピクセル単位で設定
		mainFrame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// ウィンドウの表示場所を規定
		mainFrame.setLocationRelativeTo(null);

		mainPanel = new UserPanel();
		mainPanel.init();

		// コンテンツ登録
		initContents();

		mainFrame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				mainPanel.setXZoom((double) mainFrame.getWidth()
						/ DEFAULT_WIDTH);
				mainPanel.setYZoom((double) mainFrame.getHeight()
						/ DEFAULT_HEIGHT);
			}
		});
	}

	/**
	 * コンテンツの初期化
	 */
	private void initContents() {
		// JFrameよりContentPaneを取得
		Container contentPane = this.mainFrame.getContentPane();
		textField = new JTextField();
		textField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				int keycode = arg0.getKeyCode();
				if (keycode == KeyEvent.VK_ENTER) {
					String message = textField.getText();
					if (!"".equals(message)) {
						mainPanel.notify(message);
					}
				}
			}
		});

		// ボタンのインスタンスを生成
		JButton button = new JButton("送信");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String message = textField.getText();
				if (!"".equals(message)) {
					mainPanel.notify(message);
				}
			}
		});

		JPanel panel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
		panel.setLayout(boxLayout);

		// ラベルをContentPaneに配置
		panel.add(textField);
		textField
				.setFont(new Font(button.getFont().getFamily(), Font.PLAIN, 30));

		// ボタンをContentPaneに配置
		panel.add(button);
		button.setFont(new Font(button.getFont().getFamily(), Font.PLAIN, 30));

		contentPane.add(mainPanel, BorderLayout.CENTER);
		contentPane.add(panel, BorderLayout.PAGE_END);
	}

	/**
	 * ウィンドウを表示
	 */
	public void show() {
		mainFrame.setVisible(true);

	}

}
