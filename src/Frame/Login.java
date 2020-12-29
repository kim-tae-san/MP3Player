package Frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Color;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	public static ArrayList<String> userdata;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		userdata = new ArrayList<String>();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Login");
		setBounds(100, 100, 493, 401);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(136, 132, 116, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(136, 194, 116, 27);
		contentPane.add(passwordField);
		
		JButton btnNewButton = new JButton("Login\r\n");
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userdata = new ArrayList<String>();
				try (FileReader rw = new FileReader(System.getProperty("user.dir") + "\\data\\userdata.txt");
						BufferedReader br = new BufferedReader( rw );)
				{
					String readline = null;
					br.readLine();
					while((readline = br.readLine())!=null) {
						String[] data = readline.split("/");					
						if(data[0].trim().equals(textField.getText())) {
							for(String ud:data) {
								userdata.add(ud);
							}
						}
					}
				}
				catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(textField.getText().length()<1) {
					JOptionPane.showMessageDialog(contentPane,"아이디를 입력해주세요", "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					if(userdata.size() == 0 || !userdata.get(0).equals(textField.getText())) {
						JOptionPane.showMessageDialog(contentPane,"아이디를 확인해주세요.", "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
					}
					else if(userdata.get(1).trim().equals(new String(passwordField.getPassword()))) {
						MP3Frame.login = true;
						MP3Frame.panel_1.setVisible(false);
						MP3Frame.panel_5.setVisible(true);
						MP3Frame.loginLabel.setText(userdata.get(0)+"님 반갑습니다!");
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(contentPane,"비밀번호를 확인해주세요", "로그인 실패", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		btnNewButton.setBounds(306, 132, 105, 90);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("아이디/비밀번호 찾기");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String favoritesong = JOptionPane.showInputDialog(null,"가장 좋아하는 노래는?" , "아이디/비밀번호 찾기",JOptionPane.INFORMATION_MESSAGE);
				String id = "";
				String password = "";
				try (FileReader rw = new FileReader(System.getProperty("user.dir") + "\\data\\userdata.txt");
						BufferedReader br = new BufferedReader( rw );)
				{
					String readline = null;
					br.readLine();
					while((readline = br.readLine())!=null) {
						String[] data = readline.split("/");					
						if(data.length>3 && data[3].trim().equals(favoritesong)) {
							id = data[0];
							password = data[1];
						}
					}
				}
				catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(id.length()>0) {
					JOptionPane.showMessageDialog(contentPane,"아이디:" + id + "\n비밀번호:" + password, "아이디/비밀번호 찾기", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(contentPane,"아이디/비밀번호 찾기 실패", "아이디/비밀번호 찾기 실패", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton_1.setBackground(Color.LIGHT_GRAY);
		btnNewButton_1.setBounds(37, 297, 169, 27);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("회원가입");
		btnNewButton_2.setBackground(Color.LIGHT_GRAY);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame register = new Register();
				register.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(269, 297, 105, 27);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setBounds(106, 135, 27, 18);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(60, 194, 70, 25);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("안녕하세요 편리한 MP3Player사용을 위해 로그인 해주세요");
		lblNewLabel_2.setBounds(37, 35, 385, 50);
		contentPane.add(lblNewLabel_2);
		
	}
}
