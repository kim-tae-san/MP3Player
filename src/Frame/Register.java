package Frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Register extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JPasswordField passwordField;
	private boolean checkid = false;
	private boolean checkcert = false;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
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
	public Register() {
		setTitle("Register");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 537, 501);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("회원가입\r\n");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 25));
		lblNewLabel.setBounds(14, 12, 161, 50);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("아이디");
		lblNewLabel_1.setBounds(35, 86, 62, 18);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("비밀번호");
		lblNewLabel_2.setBounds(35, 138, 62, 18);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("주소");
		lblNewLabel_3.setBounds(35, 198, 62, 18);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("좋아하는 노래");
		lblNewLabel_4.setBounds(35, 252, 89, 18);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("전화번호");
		lblNewLabel_5.setBounds(35, 313, 62, 18);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Email");
		lblNewLabel_6.setBounds(35, 373, 62, 18);
		contentPane.add(lblNewLabel_6);
		
		textField = new JTextField();
		textField.setBounds(127, 83, 116, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(127, 195, 213, 24);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("중복확인");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean check = true;
				if(textField.isEditable()) {
					try (FileReader rw = new FileReader(System.getProperty("user.dir") + "\\data\\userdata.txt");
							BufferedReader br = new BufferedReader( rw );)
					{
						String readline = null;
						br.readLine();
						while((readline = br.readLine())!=null) {
							String[] data = readline.split("/");
							if(textField.getText().equals(data[0].trim())) {
								check = false;
								break;
							}
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if(check) {
						JOptionPane.showMessageDialog(contentPane,"사용할 수 있는 아이디입니다.", "중복 확인", JOptionPane.INFORMATION_MESSAGE);
						checkid = true;
						textField.setEditable(false);
					}
					else {
						JOptionPane.showMessageDialog(contentPane,"이미 있는 아이디 입니다.", "중복 확인", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else {
					textField.setEditable(true);
					checkid = false;
				}
			}
		});
		btnNewButton.setBackground(Color.GRAY);
		btnNewButton.setBounds(256, 82, 105, 27);
		contentPane.add(btnNewButton);
		
		textField_3 = new JTextField();
		textField_3.setBounds(127, 249, 116, 24);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(127, 310, 116, 24);
		contentPane.add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(127, 370, 116, 24);
		contentPane.add(textField_5);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(127, 135, 116, 21);
		contentPane.add(passwordField);
		
		JButton btnNewButton_1 = new JButton("회원가입");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkid&&checkcert) {
					try( FileWriter fw = new FileWriter( System.getProperty("user.dir") + "\\data\\userdata.txt" ,true);
				             BufferedWriter bw = new BufferedWriter(fw);) 
						{
							 bw.write(textField.getText()+"/" + new String(passwordField.getPassword())+ "/" + textField_2.getText() + "/" + textField_3.getText()
							 + "/" + textField_4.getText()+"/"+textField_5.getText() + "/" + textField_1.getText());
							 bw.newLine();
							 bw.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					if(textField.getText().length()>0 && passwordField.getPassword().length>0 && textField_2.getText().length()>0&&
							textField_3.getText().length()>0&&textField_4.getText().length()>0&&textField_5.getText().length()>0) {
						JOptionPane.showMessageDialog(contentPane,"축하합니다 회원가입이 되셨습니다.", "회원가입 성공", JOptionPane.INFORMATION_MESSAGE);				
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(contentPane,"모든 칸을 채워주세요", "회원가입 실패", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else if(checkcert){
					JOptionPane.showMessageDialog(contentPane,"아이디 중복 여부를 확인해주세요.", "회원가입 실패", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(contentPane,"휴대폰 인증을 완료해주세요.", "회원가입 실패", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton_1.setBackground(Color.GRAY);
		btnNewButton_1.setBounds(199, 415, 105, 27);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("인증");
		btnNewButton_2.setBackground(Color.GRAY);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_4.getText().length()>0) {
					int n = new Random().nextInt(10000);
					String num = JOptionPane.showInputDialog(null,"인증번호가 발송되었습니다! 발송된 인증번호를 입력해주세요.\n" + Integer.toString(n) ,
							"Certification",JOptionPane.INFORMATION_MESSAGE);
					if(Integer.parseInt(num) == n) {
						JOptionPane.showMessageDialog(contentPane,"인증이 되었습니다.", "Successful", JOptionPane.INFORMATION_MESSAGE);
						checkcert = true;
						textField_4.setEditable(false);
					}
					else {
						JOptionPane.showMessageDialog(contentPane,"인증실패", "Failed", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(contentPane,"번호를 입력해주세요", "오류", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton_2.setBounds(256, 309, 105, 27);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_7 = new JLabel("@");
		lblNewLabel_7.setBounds(256, 373, 21, 18);
		contentPane.add(lblNewLabel_7);
		
		textField_1 = new JTextField();
		textField_1.setBounds(280, 370, 116, 24);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
	}
}
