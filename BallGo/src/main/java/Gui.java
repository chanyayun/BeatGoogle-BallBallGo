import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;

public class Gui extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea areaContent;
	private String buttonString;
	private static final Color purple = new Color(72, 61, 139);
	private static final double weight = 20;
	private static final double weight2 = 2;
	public Keyword keyword;
	public JButton btnSearch = new JButton("Search");
	public JTextField textField = new JTextField();
	private JScrollPane scrollPane;
	private JPanel wholePane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Gui() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 700);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.textHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		final JButton btnNba = new JButton("NBA");
		btnNba.setForeground(purple);
		btnNba.setBounds(280, 310, 55, 30);

		final JButton btnMlb = new JButton("MLB");
		btnMlb.setForeground(purple);
		btnMlb.setBounds(335, 310, 55, 30);

		final JButton btnNfl = new JButton("NFL");
		btnNfl.setForeground(purple);
		btnNfl.setBounds(390, 310, 55, 30);

		final JButton btnNhl = new JButton("NHL");
		btnNhl.setForeground(purple);
		btnNhl.setBounds(445, 310, 55, 30);

		buttonString = "";
		btnNba.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonString = "NBA";
				btnNba.setForeground(Color.RED);
				btnMlb.setForeground(purple);
				btnNfl.setForeground(purple);
				btnNhl.setForeground(purple);
			}
		});
		contentPane.add(btnNba);

		btnMlb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonString = "MLB";
				btnMlb.setForeground(Color.RED);
				btnNba.setForeground(purple);
				btnNfl.setForeground(purple);
				btnNhl.setForeground(purple);
			}
		});
		contentPane.add(btnMlb);

		btnNfl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonString = "NFL";
				btnNfl.setForeground(Color.RED);
				btnNba.setForeground(purple);
				btnMlb.setForeground(purple);
				btnNhl.setForeground(purple);
			}
		});
		contentPane.add(btnNfl);

		btnNhl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonString = "NHL";
				btnNhl.setForeground(Color.RED);
				btnNba.setForeground(purple);
				btnMlb.setForeground(purple);
				btnNfl.setForeground(purple);
			}
		});
		contentPane.add(btnNhl);
		contentPane.setLayout(null);

		textField.setBounds(280, 280, 220, 30);
		contentPane.add(textField);
		textField.setColumns(10);

		btnSearch.setBounds(500, 280, 75, 30);
		contentPane.add(btnSearch);

		JLabel lblImage = new JLabel("");
		lblImage.setBounds(297, 136, 211, 154);
		lblImage.setIcon(new ImageIcon(this.getClass().getResource("Ball.png")));
		contentPane.add(lblImage);

		btnSearch.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		keyword = new Keyword();
		String input = textField.getText();
		int m = input.indexOf(" ");
		String input1 = input;
		String input2 = "";
		String text = "";
		String text2 = "";

		// 接收使用者輸入
		// 有超過一個關鍵字ex:aaa bbb 會把它分成兩個keyword 轉成aaa+bbb的text
		// text是用在google搜尋裡
		if (!input.isEmpty()) {
			System.out.print(text);
			text = text2;
			while (input1.contains(" ")) {
				m = input1.indexOf(" ");
				input2 = input1.substring(m + 1, input1.length());
				input1 = input1.substring(0, m);

				keyword.addKeyword(new Keyword(input1, weight, buttonString));
				text = text + "+" + input1;
				input1 = input2;
			}
			keyword.addKeyword(new Keyword(input1, weight, buttonString));
			text = text + "+" + input1;
		}

		// 按按鈕 將buttonString(NBA/NFl...)加入keyword
		// 若沒有按按鈕則加入sport
		// 若沒有使用者輸入則將text設為buttonString
		if (buttonString != "") {
			keyword.addKeyword(new Keyword(buttonString, weight2, buttonString));
			if (input.isEmpty()) {
				text = buttonString;
			}
		} else {
			keyword.addKeyword(new Keyword("sport", weight2, buttonString));
			if (input.isEmpty()) {
				text = "sport";
			}
		}

		// 到Google或各網頁搜尋 抓出網頁清單
		try {
			new HtmlMatcher(buttonString, text);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		// 算分數、排序 得到最終結果
		WebList news = null;
		try {
			news = new WebList(keyword.keywords);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		areaContent = new JTextArea();
		areaContent.setBackground(SystemColor.textHighlight);
		areaContent.setLineWrap(true);
		areaContent.setColumns(10);

		areaContent.setText(HtmlMatcher.relatedKeyword + news.sort());

		wholePane = new JPanel();
		wholePane.setLayout(new BorderLayout());
		wholePane.setBounds(100, 100, 800, 600);
		this.setContentPane(wholePane);

		contentPane = new JPanel();

		textField.setColumns(10);
		contentPane.add(textField, BorderLayout.WEST);
		contentPane.add(btnSearch, BorderLayout.CENTER);

		contentPane.setBackground(SystemColor.textHighlight);

		scrollPane = new JScrollPane(areaContent);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getVerticalScrollBar().setBackground(purple);
		scrollPane.getViewport().setBackground(SystemColor.textHighlight);

		wholePane.add(contentPane, BorderLayout.NORTH);
		wholePane.add(scrollPane, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wholePane.updateUI();
		this.setVisible(true);
	}
}
