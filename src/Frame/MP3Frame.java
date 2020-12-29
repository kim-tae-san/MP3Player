package Frame;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import Music.MusicPlayer;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import java.awt.SystemColor;

public class MP3Frame extends JFrame {
	
	private final static int Linear = 0;
    private final static int Random = 1;
    private final static int Loop = 2;
    public static int playstatus = Linear;
	public static boolean flag = false;
	private static int rselected;
	static FileInputStream input;
	static MusicPlayer myplayer;
	private static float volume = 0.5F;
	public static int currentList = 0;
	public static int currentSong = 0;
	public static ArrayList<Vector<String>> MusicList = new ArrayList<Vector<String>>();
	public static ArrayList<String> ListName = new ArrayList<String>();
	public static ArrayList<Vector<String>> SongName = new ArrayList<Vector<String>>();
	private JPanel contentPane;
	private JPopupMenu pm;
	public static JList list;
	public static JList list_1;
	public static JLabel lblNewLabel_3;
	public static JLabel lblNewLabel_3_1;
	public static JLabel lblNewLabel_1;
	public static JTextPane textPane;
	public static JButton btnNewButton_3;
	public static JLabel lblNewLabel_6;
	public static boolean forcestart = false;
	private Vector<String> Chart = new Vector<String>();
	private Vector<String> ChartName = new Vector<String>();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	public static boolean login = false;
	public static JPanel panel_5;
	public static JPanel panel_1;
	public static JLabel loginLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MP3Frame frame = new MP3Frame();
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
	public MP3Frame() {
		setTitle("MP3Player");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1101, 644);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		pm = new JPopupMenu();
		pm.setVisible(true);
		JMenuItem delete = new JMenuItem("Delete");
		delete.setVisible(true);
		JMenuItem nchange = new JMenuItem("Change name");
		nchange.setVisible(true);
		
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MusicList.remove(rselected);
				ListName.remove(rselected);
				SongName.remove(rselected);
				list.setListData(ListName.toArray());
 			}
		});
    	nchange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(null,"변경할 이름을 입력해주세요", "Change PlayList Name",JOptionPane.QUESTION_MESSAGE);
				if(name!=null && !ListName.contains(name)) {
					ListName.set(rselected, name);
    				list.setListData(ListName.toArray());
				}
				else if(ListName.contains(name)){
					JOptionPane.showMessageDialog(contentPane,"이미 있는 이름입니다.", "오류", JOptionPane.INFORMATION_MESSAGE);
				}
 			}
		});
    	
		pm.add(delete);
		pm.add(nchange);
		
		JTextPane txtpnDisplaySelectmenu = new JTextPane();
		txtpnDisplaySelectmenu.setBounds(163, 209, 712, 388);
		contentPane.add(txtpnDisplaySelectmenu);
		txtpnDisplaySelectmenu.setFont(new Font("굴림", Font.PLAIN, 25));
		txtpnDisplaySelectmenu.setText("\r\n\r\n\r\n\t\t\tDisplay - SelectMenu\r\n\r\n\t\t\t1. Music Chart\r\n\t\t\t2. Users");
		txtpnDisplaySelectmenu.setBackground(Color.LIGHT_GRAY);
		txtpnDisplaySelectmenu.setEditable(false);	
		
		panel_5 = new JPanel();
		panel_5.setBounds(0, 99, 875, 110);
		contentPane.add(panel_5);
		panel_5.setLayout(null);
		
		loginLabel = new JLabel("");
		loginLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 25));
		loginLabel.setBounds(225, 24, 461, 54);
		panel_5.add(loginLabel);
		panel_5.setVisible(false);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(SystemColor.text);
		panel_3.setBounds(163, 209, 712, 388);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		panel_3.setVisible(false);
		
		JButton userButton = new JButton("로그아웃");
		userButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(contentPane,"로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_OPTION) {
					login = false;
					panel_3.setVisible(false);
					txtpnDisplaySelectmenu.setVisible(true);
					panel_1.setVisible(true);
					panel_5.setVisible(false);
					Login.userdata.clear();
				}
			}
		});
		userButton.setBackground(Color.LIGHT_GRAY);
		userButton.setBounds(453, 29, 105, 27);
		panel_3.add(userButton);
		
		JButton userButton_1 = new JButton("수정");
		userButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textField_1.isEditable()) {
					textField_1.setEditable(true);
					textField_2.setEditable(true);
					textField_3.setEditable(true);
					textField_4.setEditable(true);
					textField_5.setEditable(true);
					userButton_1.setText("저장");
				}
				else {
					textField_1.setEditable(false);
					textField_2.setEditable(false);
					textField_3.setEditable(false);
					textField_4.setEditable(false);
					textField_5.setEditable(false);
					userButton_1.setText("수정");
					File file = new File(System.getProperty("user.dir")+"\\data\\userdata.txt");
					String dummy = "";
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
						String line;
						int n=0;
						while((line = br.readLine())!=null) {
							String[] check = line.split("/");
							if(check[0].equals(Login.userdata.get(0))) {
								break;
							}
							n++;
						}
						BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
						for(int i=0; i<n; i++) {
						    line = br1.readLine();
						    dummy += (line + "\r\n" );
						}
						br1.readLine();
						String []email = textField_5.getText().split("@");
						dummy +=  Login.userdata.get(0) + "/" + textField_3.getText() + "/" + textField_1.getText() + "/" +
						textField_4.getText() + "/" + textField_2.getText() + "/" + email[0] + "/" + email[1] + "\r\n";
						Login.userdata.set(1, textField_3.getText());
						Login.userdata.set(2, textField_1.getText());
						Login.userdata.set(3, textField_4.getText());
						Login.userdata.set(4, textField_2.getText());
						Login.userdata.set(5, email[0]);
						Login.userdata.set(5, email[1]);
						while((line = br1.readLine())!=null) {
							dummy += (line + "\r\n" );
						}
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
						bw.write(dummy);			
						bw.close();
						br.close();
						br1.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		userButton_1.setBackground(Color.LIGHT_GRAY);
		userButton_1.setBounds(572, 29, 105, 27);
		panel_3.add(userButton_1);
		
		JLabel userLabel = new JLabel("마이페이지");
		userLabel.setFont(new Font("굴림", Font.PLAIN, 25));
		userLabel.setBounds(26, 9, 160, 59);
		panel_3.add(userLabel);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(255, 250, 240));
		panel_4.setBounds(26, 80, 656, 274);
		panel_3.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel userLabel_1 = new JLabel("ID:");
		userLabel_1.setBounds(47, 45, 149, 18);
		panel_4.add(userLabel_1);
		
		JLabel userLabel_2 = new JLabel("비밀번호:");
		userLabel_2.setBounds(319, 45, 62, 18);
		panel_4.add(userLabel_2);
		
		JLabel userLabel_3 = new JLabel("주소:");
		userLabel_3.setBounds(47, 117, 62, 18);
		panel_4.add(userLabel_3);
		
		JLabel userLabel_4 = new JLabel("좋아하는 노래:");
		userLabel_4.setBounds(319, 117, 94, 18);
		panel_4.add(userLabel_4);
		
		JLabel userLabel_5 = new JLabel("전화번호:");
		userLabel_5.setBounds(47, 195, 62, 18);
		panel_4.add(userLabel_5);
		
		JLabel userLabel_6 = new JLabel("Email:");
		userLabel_6.setBounds(319, 195, 62, 18);
		panel_4.add(userLabel_6);
		
		textField_1 = new JTextField();
		textField_1.setBounds(110, 114, 195, 24);
		panel_4.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(110, 192, 116, 24);
		panel_4.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(417, 42, 116, 24);
		panel_4.add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(417, 114, 116, 24);
		panel_4.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(417, 192, 161, 24);
		panel_4.add(textField_5);
		textField_5.setColumns(10);
		
		JButton userButton_2 = new JButton("비공개 전환");
		userButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(panel_4.isVisible()) {
					panel_4.setVisible(false);
					userButton_2.setText("공개 전환");
				}
				else {
					panel_4.setVisible(true);
					userButton_2.setText("비공개 전환");
				}
			}
		});
		userButton_2.setBackground(Color.LIGHT_GRAY);
		userButton_2.setBounds(164, 29, 117, 27);
		panel_3.add(userButton_2);
		
		JButton userButton_3 = new JButton("회원탈퇴");
		userButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(contentPane,"회원탈퇴 하시겠습니까?", "회원탈퇴", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_OPTION) {
					File file = new File(System.getProperty("user.dir")+"\\data\\userdata.txt");
					String dummy = "";
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
						String line;
						int n=0;
						while((line = br.readLine())!=null) {
							String[] check = line.split("/");
							if(check[0].equals(Login.userdata.get(0))) {
								break;
							}
							n++;
						}
						BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
						for(int i=0; i<n; i++) {
						    line = br1.readLine();
						    dummy += (line + "\r\n" );
						}
						br1.readLine();
						while((line = br1.readLine())!=null) {
							dummy += (line + "\r\n" );
						}
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
						bw.write(dummy);			
						bw.close();
						br.close();
						br1.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(contentPane,"계정이 탈퇴되었습니다.", "회원탈퇴", JOptionPane.INFORMATION_MESSAGE);
					login = false;
					panel_3.setVisible(false);
					txtpnDisplaySelectmenu.setVisible(true);
					panel_1.setVisible(true);
					panel_5.setVisible(false);
					Login.userdata.clear();
					
				}
			}
		});
		userButton_3.setBackground(Color.LIGHT_GRAY);
		userButton_3.setBounds(572, 361, 105, 27);
		panel_3.add(userButton_3);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 250, 240));
		panel_2.setBounds(163, 209, 712, 388);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		panel_2.setVisible(false);
		
		JLabel ChartLabel = new JLabel("Music Chart");
		ChartLabel.setFont(new Font("굴림", Font.PLAIN, 25));
		ChartLabel.setBounds(263, 0, 166, 58);
		panel_2.add(ChartLabel);
		
		JLabel ChartLabel_1 = new JLabel("음악 검색");
		ChartLabel_1.setBackground(Color.WHITE);
		ChartLabel_1.setBounds(448, 24, 68, 18);
		panel_2.add(ChartLabel_1);
		
		JLabel ChartLabel_2 = new JLabel("차트에 업로드중 . . . ");
		ChartLabel_2.setBounds(34, 72, 125, 18);
		panel_2.add(ChartLabel_2);
		ChartLabel_2.setVisible(false);
		
		JList Chartlist = new JList();
		Chartlist.setListData(Chart);
		Chartlist.setVisible(true);
		DefaultListCellRenderer cr = new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean CellHasFocus) {
				File f = new File(Chart.get(index));
				try {
					MP3File mp3 = (MP3File) AudioFileIO.read(f);
					Tag tag = mp3.getTag();
					Artwork cover = tag.getFirstArtwork();
					ChartName.add(tag.getFirst(FieldKey.TITLE));
					String text = String.format("%-30s", tag.getFirst(FieldKey.TITLE)) + String.format("%-30s", tag.getFirst(FieldKey.ARTIST))
					+  String.format("%-30s", tag.getFirst(FieldKey.ALBUM));
					ImageIcon icon = new ImageIcon(cover.getBinaryData());
					Image img = icon.getImage();
					icon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
					setIcon(icon);
					setText(text);
					setBackground(Color.white);
					if(isSelected) {
						setBackground(Color.LIGHT_GRAY);
					}
				} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
						| InvalidAudioFrameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				return this;
			}
		
		};
		Chartlist.setCellRenderer(cr);
		JScrollPane Chartscroll = new JScrollPane(Chartlist);
		Chartscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		Chartscroll.setBounds(99, 102, 550, 259);
		Chartscroll.setVisible(true);
		panel_2.add(Chartscroll);
		
		textField = new JTextField();
		textField.setBounds(518, 21, 116, 24);
		panel_2.add(textField);
		textField.setColumns(10);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inform = textField.getText();
				if(ChartName.contains(inform)) {
					int a = ChartName.indexOf(inform);
					Chartlist.setSelectedIndex(a);
					Chartscroll.getVerticalScrollBar().setValue(Chartscroll.getVerticalScrollBar().getMaximum()/Chart.size()*a);
				}
				else{
					JOptionPane.showMessageDialog(contentPane,"해당 노래가 존재하지 않습니다.", "오류", JOptionPane.WARNING_MESSAGE);
					textField.setText("");
				}
			}
		});
		
		JButton ChartButton = new JButton("");
		ChartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChartLabel_2.setVisible(true);
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				String saveFilePath = System.getProperty("user.dir") + "\\chart";
				int sv = jfc.showSaveDialog(jfc);
				if(sv==0) {
					try {			
						MP3File mp3 = (MP3File) AudioFileIO.read(jfc.getSelectedFile());
						Tag tag = mp3.getTag();
						Chart.add(saveFilePath + "\\" + jfc.getSelectedFile().getName());
						ChartName.add(tag.getFirst(FieldKey.TITLE));
			            
			            FileInputStream fis = new FileInputStream(jfc.getSelectedFile().getAbsolutePath()); 
			            FileOutputStream fos = new FileOutputStream(saveFilePath + "\\" + jfc.getSelectedFile().getName()); 
			            BufferedInputStream bis = new BufferedInputStream(fis);
			            BufferedOutputStream bos = new BufferedOutputStream(fos);
			            
			            int fileByte = 0; 
			            while((fileByte = bis.read()) != -1) {
				            bos.write(fileByte);
				        }
			            bis.close();
			            bos.close();
			            
			        } catch (FileNotFoundException ev) {
			            // TODO Auto-generated catch block
			            ev.printStackTrace();
			        } catch (IOException ev) {
			            // TODO Auto-generated catch block
			            ev.printStackTrace();
			        } catch (CannotReadException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (TagException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ReadOnlyFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvalidAudioFrameException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Chartlist.setListData(Chart);
			    }
				ChartLabel_2.setVisible(false);
			}
		});
		ChartButton.setBackground(SystemColor.text);
		ChartButton.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\addingmusicicon.png"));
		ChartButton.setBounds(34, 21, 55, 43);
		panel_2.add(ChartButton);
		
		JLabel ChartLabel_3 = new JLabel("               곡명                                   가수명                              앨범");
		ChartLabel_3.setBounds(124, 72, 539, 18);
		panel_2.add(ChartLabel_3);
		
		JButton ChartButton_1 = new JButton("");
		ChartButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!Chartlist.isSelectionEmpty()) {
					if(login && Login.userdata.get(0).equals("admin")) {
						int result = JOptionPane.showConfirmDialog(contentPane,"삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION);
						if(result==JOptionPane.YES_OPTION) {
							File file = new File(Chart.get(Chartlist.getSelectedIndex()));
							if(file.exists()) {
								file.delete();
								Chart.remove(Chartlist.getSelectedIndex());
								ChartName.remove(Chartlist.getSelectedIndex());
								Chartlist.setListData(Chart);
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(contentPane,"권한이 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		ChartButton_1.setBackground(SystemColor.text);
		ChartButton_1.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\deletemusicicon.png"));
		ChartButton_1.setBounds(104, 20, 55, 43);
		panel_2.add(ChartButton_1);
		
		JButton ChartButton_2 = new JButton("");
		ChartButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inform = textField.getText();
				if(ChartName.contains(inform)) {
					int a = ChartName.indexOf(inform);
					Chartlist.setSelectedIndex(a);
					Chartscroll.getVerticalScrollBar().setValue(Chartscroll.getVerticalScrollBar().getMaximum()/Chart.size()*a);
				}
				else{
					JOptionPane.showMessageDialog(contentPane,"해당 노래가 존재하지 않습니다.", "오류", JOptionPane.WARNING_MESSAGE);
					textField.setText("");
				}
			}
		});
		ChartButton_2.setBounds(635, 15, 45, 43);
		panel_2.add(ChartButton_2);
		ChartButton_2.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\search.png"));
		ChartButton_2.setBackground(SystemColor.text);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 1083, 101);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(myplayer != null)
					myplayer.volume(0);
			}
		});
		
		JButton btnNewButton_9 = new JButton("");
		btnNewButton_9.setBackground(Color.WHITE);
		btnNewButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(playstatus == Linear) {
					playstatus = Random;
					btnNewButton_9.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\random.png"));					
				}
				else if(playstatus == Random){
					playstatus = Loop;
					btnNewButton_9.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\반복재생.png"));
				}
				else {
					playstatus = Linear;
					btnNewButton_9.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\repeat.png"));
				}
					
 			}
		});
		btnNewButton_9.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\repeat.png"));
		btnNewButton_9.setBounds(58, 0, 56, 32);
		panel.add(btnNewButton_9);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\음소거.png"));
		btnNewButton.setBounds(0, 0, 57, 32);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(myplayer != null)
					myplayer.volume(volume);
			}
		});
		btnNewButton_1.setBackground(Color.WHITE);
		btnNewButton_1.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\소리켜기.png"));
		btnNewButton_1.setBounds(117, 0, 49, 32);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(myplayer != null) {					
					currentSong -= 2;
					myplayer.stop();
					if(!flag) {
						myplayer.pause();
						flag = false;
						btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\재생.png"));
					}
				}
			}
		});
		btnNewButton_2.setBackground(Color.WHITE);
		btnNewButton_2.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\이전곡.png"));
		btnNewButton_2.setBounds(0, 69, 57, 32);
		panel.add(btnNewButton_2);
		
		lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setFont(new Font("굴림", Font.PLAIN, 20));
		lblNewLabel_3.setBounds(170, 0, 175, 61);
		panel.add(lblNewLabel_3);
		
		lblNewLabel_3_1 = new JLabel("");
		lblNewLabel_3_1.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_3_1.setBounds(180, 69, 152, 32);
		panel.add(lblNewLabel_3_1);
		
		lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setBounds(0, 434, 165, 163);
		contentPane.add(lblNewLabel_1);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("굴림", Font.PLAIN, 12));
		textPane.setEditable(false);
		JScrollPane scroll2 = new JScrollPane(textPane);
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll2.setBounds(346, 0, 488, 101);
		scroll2.setVisible(true);
		panel.add(scroll2);
		btnNewButton_3 = new JButton("");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 try {
					 if(!flag) {
						 if(myplayer != null && myplayer.pause() == true) {
							 myplayer.play();
					         flag = true;
						 }
						 else {
							 if(!MusicList.isEmpty() && !MusicList.get(currentList).isEmpty()) {
								 currentSong = 0;
								 input = new FileInputStream(MusicList.get(currentList).get(currentSong));
								 myplayer = new MusicPlayer(input);
						         myplayer.play();
						         flag = true;
							 }
						 }
					 }
					 else {
						 myplayer.pause();
						 btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\재생.png"));
						 flag = false;
					 }
			        } catch (final Exception er) {
			            throw new RuntimeException(er);
			        }
			}
		});
		btnNewButton_3.setBackground(Color.WHITE);
		btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\재생.png"));
		btnNewButton_3.setBounds(53, 69, 57, 32);
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(myplayer != null) {
					myplayer.stop();
					if(!flag) {
						myplayer.pause();
						flag = false;
						btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\재생.png"));
					}
				}
			}
		});
		btnNewButton_4.setBackground(Color.WHITE);
		btnNewButton_4.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\다음곡.png"));
		btnNewButton_4.setBounds(109, 69, 57, 32);
		panel.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton_5.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\전원.png"));
		btnNewButton_5.setBounds(832, 0, 43, 61);
		panel.add(btnNewButton_5);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\코리아텍.jpg"));
		lblNewLabel.setBounds(877, 0, 206, 101);
		panel.add(lblNewLabel);
		
		JSlider slider = new JSlider(0, 100);
		slider.setBounds(0, 55, 166, 17);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(myplayer != null) {
					volume = 1.0F * ((float)slider.getValue()/100);
					myplayer.volume(volume);
				}
		}
		});
		
		panel.add(slider);
		
		lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setBackground(Color.LIGHT_GRAY);
		lblNewLabel_6.setBounds(1, 31, 165, 23);
		panel.add(lblNewLabel_6);
		
		JButton btnNewButton_6_1 = new JButton("Music Chart");
		btnNewButton_6_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Chart.size() == 0) {
					File chartdir = new File(System.getProperty("user.dir") + "\\chart");
					File []filelist = chartdir.listFiles();
					for(File file:filelist) {
						if(file.isFile()) {
							Chart.add(file.getAbsolutePath());
						}
					}
				}
				Chartlist.setListData(Chart);
				txtpnDisplaySelectmenu.setVisible(false);
				panel_2.setVisible(true);
				panel_3.setVisible(false);
			}
		});
		btnNewButton_6_1.setBackground(Color.LIGHT_GRAY);
		btnNewButton_6_1.setBounds(0, 208, 165, 116);
		contentPane.add(btnNewButton_6_1);
		
		JButton btnNewButton_6_1_1 = new JButton("Users");
		btnNewButton_6_1_1.setBackground(Color.LIGHT_GRAY);
		btnNewButton_6_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(login) {
					panel_3.setVisible(true);
					txtpnDisplaySelectmenu.setVisible(false);
					panel_2.setVisible(false);
					userLabel_1.setText("ID: "+Login.userdata.get(0));
					textField_1.setText(Login.userdata.get(2));
					textField_2.setText(Login.userdata.get(4));
					textField_3.setText(Login.userdata.get(1));
					textField_4.setText(Login.userdata.get(3));
					textField_5.setText(Login.userdata.get(5)+"@"+Login.userdata.get(6));
					textField_1.setEditable(false);
					textField_2.setEditable(false);
					textField_3.setEditable(false);
					textField_4.setEditable(false);
					textField_5.setEditable(false);
					
				}
				else {
					JOptionPane.showMessageDialog(contentPane,"로그인 해주세요.", "Failed", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		});
		btnNewButton_6_1_1.setBounds(0, 321, 165, 110);
		contentPane.add(btnNewButton_6_1_1);
		
		panel_1 = new JPanel();
		panel_1.setBounds(0, 99, 875, 110);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Login Menu");
		lblNewLabel_2.setFont(new Font("굴림", Font.PLAIN, 25));
		lblNewLabel_2.setBounds(420, 25, 168, 52);
		panel_1.add(lblNewLabel_2);
		
		JButton btnNewButton_7 = new JButton();
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame login = new Login();
				login.setVisible(true);
			}
		});
		btnNewButton_7.setBackground(Color.LIGHT_GRAY);
		btnNewButton_7.setBounds(257, 25, 64, 56);
		panel_1.add(btnNewButton_7);
		btnNewButton_7.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\login.png"));	
		
		list = new JList();
		list.setFont(new Font("휴먼모음T", Font.PLAIN, 18));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(UIManager.getColor("Button.background"));
		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(874, 144, 209, 453);
		scroll.setVisible(true);
		contentPane.add(scroll);
		
		JLabel lblNewLabel_4 = new JLabel("PlayList");
		lblNewLabel_4.setFont(new Font("굴림", Font.PLAIN, 25));
		scroll.setColumnHeaderView(lblNewLabel_4);
		
		
		list_1 = new JList();
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll1 = new JScrollPane(list_1);
		scroll1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll1.setBounds(874, 144, 209, 453);
		scroll1.setVisible(false);
		contentPane.add(scroll1);
		
		JLabel lblNewLabel_5 = new JLabel("MusicList");
		lblNewLabel_5.setBackground(Color.white);
		lblNewLabel_5.setFont(new Font("굴림", Font.PLAIN, 25));
		scroll1.setColumnHeaderView(lblNewLabel_5);
		
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList l = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {
		            // Double-click detected		        	
		            currentList = l.locationToIndex(evt.getPoint());
		            if(MusicList.get(currentList)!=null)
		            	list_1.setListData(SongName.get(currentList));
		            scroll.setVisible(false);
		            scroll1.setVisible(true);
		        }
		        else if(SwingUtilities.isRightMouseButton(evt)) {	
		        	rselected = l.locationToIndex(evt.getPoint());
		        	pm.show(evt.getComponent(), evt.getX(), evt.getY());		        	
		        }
		    }
		});
		
		list_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
		        JList l = (JList)evt.getSource();
		        if (evt.getClickCount() == 2) {
		            // Double-click detected
		        	if(myplayer != null) {
		        		forcestart = true;
			            myplayer.close();
			            forcestart = false;
		        	}
		        	currentSong = l.locationToIndex(evt.getPoint());
		            try {
						input = new FileInputStream(MusicList.get(currentList).get(currentSong));
						myplayer = new MusicPlayer(input);
				        myplayer.play();
					} catch (FileNotFoundException | JavaLayerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        flag = true;	
				}		           
		    }
		});
		
		JButton btnNewButton_8 = new JButton("");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(scroll.isVisible()) {
					scroll.setVisible(false);
				}
				else {
					scroll.setVisible(true);
				}
			}
		});
		btnNewButton_8.setBackground(Color.LIGHT_GRAY);
		btnNewButton_8.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\listicon.png"));
		btnNewButton_8.setBounds(876, 101, 45, 40);
		contentPane.add(btnNewButton_8);
		
		JButton btnNewButton_8_1 = new JButton("");
		btnNewButton_8_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(scroll.isVisible()) {
					String name = JOptionPane.showInputDialog(null,"재생목록 이름을 정해주세요", "New PlayList",JOptionPane.QUESTION_MESSAGE);
					if(name!=null && !ListName.contains(name)) {
						ListName.add(name);
						MusicList.add(new Vector<String>());
						SongName.add(new Vector<String>());
	    				list.setListData(ListName.toArray());
					}
					else if(ListName.contains(name)){
						JOptionPane.showMessageDialog(contentPane,"이미 있는 이름입니다.", "오류", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		btnNewButton_8_1.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\addlisticon.png"));
		btnNewButton_8_1.setBackground(Color.LIGHT_GRAY);
		btnNewButton_8_1.setBounds(923, 101, 45, 40);
		contentPane.add(btnNewButton_8_1);
		
		JButton btnNewButton_8_2 = new JButton("");
		btnNewButton_8_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(scroll1.isVisible() && !scroll.isVisible()) {
					JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
					int sv = jfc.showSaveDialog(jfc);
					if(sv==0) {
						MusicList.get(currentList).add(jfc.getSelectedFile().getAbsolutePath());
						SongName.get(currentList).add(jfc.getSelectedFile().getName());
						list_1.setListData(SongName.get(currentList));
					}
				}
			}
		});
		btnNewButton_8_2.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\addingmusicicon.png"));
		btnNewButton_8_2.setBackground(Color.LIGHT_GRAY);
		btnNewButton_8_2.setBounds(992, 101, 45, 40);
		contentPane.add(btnNewButton_8_2);
		
		JButton btnNewButton_8_3 = new JButton("");
		btnNewButton_8_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!list_1.isSelectionEmpty() && !scroll.isVisible()) {
					int result = JOptionPane.showConfirmDialog(contentPane,"삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION);
					if(result==JOptionPane.YES_OPTION) {
						MusicList.get(currentList).remove(list_1.getSelectedIndex());
						SongName.get(currentList).remove(list_1.getSelectedIndex());
						list_1.setListData(SongName.get(currentList));
					}
				}
			}
		});
		btnNewButton_8_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\deletemusicicon.png"));
		btnNewButton_8_3.setBackground(Color.LIGHT_GRAY);
		btnNewButton_8_3.setBounds(1038, 101, 45, 40);
		contentPane.add(btnNewButton_8_3);
	}
}
