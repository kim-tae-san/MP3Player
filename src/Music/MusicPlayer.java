package Music;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Frame.MP3Frame;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.sound.sampled.Line.Info;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;


public class MusicPlayer {

    private final static int NOTSTARTED = 0;
    private final static int PLAYING = 1;
    private final static int PAUSED = 2;
    private final static int FINISHED = 3;
    private long start;
    private long stoppedtime;
    private long totalstop = 0;
    private int tracklength;

    // the player actually doing all the work
    private Player player;
    
    // locking object used to communicate with player thread
    private final Object playerLock = new Object();

    // status variable what player thread is doing/supposed to do
    private int playerStatus = NOTSTARTED;
    

    public MusicPlayer(final InputStream inputStream) throws JavaLayerException {
        this.player = new Player(inputStream);

    }

    public MusicPlayer(final InputStream inputStream, final AudioDevice audioDevice) throws JavaLayerException {
        this.player = new Player(inputStream, audioDevice);
    }

    /**
     * Starts playback (resumes if paused)
     */
    public void play() throws JavaLayerException {
        synchronized (playerLock) {
            switch (playerStatus) {
                case NOTSTARTED:
                    final Runnable r = new Runnable() {
                        public void run() {
                        	int nlist = MP3Frame.currentList;
                        	while(true) {
                        		File file = new File(MP3Frame.MusicList.get(nlist).get(MP3Frame.currentSong));
   							 	MP3File mp3;
   								MP3Frame.btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\일시정지.png"));
								try {
									mp3 = (MP3File) AudioFileIO.read(file);
									Tag tag = mp3.getTag();
									MP3Frame.lblNewLabel_3.setText(tag.getFirst(FieldKey.TITLE));
	   							 	MP3Frame.lblNewLabel_3_1.setText(tag.getFirst(FieldKey.ARTIST));
	   							 	MP3Frame.textPane.setText(tag.getFirst(FieldKey.LYRICS));
	   							 	tracklength = mp3.getAudioHeader().getTrackLength();
	   							 	Artwork cover = tag.getFirstArtwork();
	   							 if(cover != null) {
	   								ImageIcon icon = new ImageIcon(cover.getBinaryData());
	   								Image img = icon.getImage();
	   								icon = new ImageIcon(img.getScaledInstance(165, 163, Image.SCALE_SMOOTH));
	   								MP3Frame.lblNewLabel_1.setIcon(icon);
	   							 }
	   							 else {
	   								 MP3Frame.lblNewLabel_1.setIcon(null);
	   							 }
								} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
										| InvalidAudioFrameException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								start = System.currentTimeMillis();
                        		playInternal();                        		
                        		playerStatus = PLAYING;
                        		totalstop = 0;                    
                        		if(MP3Frame.forcestart)
                        		{
                        			break;
                        		}
                        		if(MP3Frame.playstatus == 0) {
                        			MP3Frame.currentSong++;
                                }
                        		else if(MP3Frame.playstatus == 1) {
                        			MP3Frame.currentSong = new Random().nextInt(MP3Frame.MusicList.get(MP3Frame.currentList).size());
                        		}                        		
                        		if(MP3Frame.currentSong == MP3Frame.MusicList.get(nlist).size() || MP3Frame.currentSong < 0)
                        		{
                        			MP3Frame.currentSong = 0;                     
                        			close();
                        			MP3Frame.flag = false;
                        			break;
                        		}
                        		try {
									player = new Player(new FileInputStream(MP3Frame.MusicList.get(nlist).get(MP3Frame.currentSong)));
								} catch (FileNotFoundException | JavaLayerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}  
                        	}   
                        }
                    };
                    final Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setPriority(Thread.MAX_PRIORITY);
                    playerStatus = PLAYING;
                    t.start();
                    break;
                case PAUSED:
                	MP3Frame.btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\일시정지.png"));
                    resume();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Pauses playback. Returns true if new state is PAUSED.
     */
    public boolean pause() {
        synchronized (playerLock) {
            if (playerStatus == PLAYING) {
                playerStatus = PAUSED;
                stoppedtime = System.currentTimeMillis();
                MP3Frame.btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\재생.png"));
            }
            return playerStatus == PAUSED;
        }
    }

    /**
     * Resumes playback. Returns true if the new state is PLAYING.
     */
    public boolean resume() {
        synchronized (playerLock) {
            if (playerStatus == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
                totalstop += System.currentTimeMillis() -  stoppedtime;
            }
            return playerStatus == PLAYING;
        }
    }

    /**
     * Stops playback. If not playing, does nothing
     */
    public void stop() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
            playerLock.notifyAll();
        }
    }

    private void playInternal() {
        while (playerStatus != FINISHED) {
        	MP3Frame.lblNewLabel_6.setText("Elapsed Time: " + Integer.toString((int)((System.currentTimeMillis() - start - totalstop) / 1000)/60) 
        	 + ":" + Integer.toString((int)((System.currentTimeMillis() - start - totalstop) / 1000)%60)  + "   /   " + Integer.toString(tracklength/60)
        	 + ":" + Integer.toString(tracklength%60));
            try {
                if (!player.play(1)) {
                    break;
                }
            } catch (final JavaLayerException e) {
                break;
            }
                       
            // check if paused or terminated
            synchronized (playerLock) {
                while (playerStatus == PAUSED) {
                    try {
                        playerLock.wait();
                    } catch (final InterruptedException e) {
                        // terminate player
                        break;
                    }
                }
            }
        }
    }

    /**
     * Closes the player, regardless of current state.
     */
    public void close() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
        }
        try {
            player.close();
            MP3Frame.btnNewButton_3.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\image\\재생.png"));
            MP3Frame.lblNewLabel_6.setText(null);
            
        } catch (final Exception e) {
            // ignore, we are terminating anyway
        }
    }
    public void volume(float gain) {
    	Info source = Port.Info.HEADPHONE;
    	Info source2 = Port.Info.SPEAKER; 		  
    	try {
    		if (AudioSystem.isLineSupported(source)){
    			Port outline = (Port) AudioSystem.getLine(source);
    			outline.open();
    			FloatControl volumeControl = (FloatControl) outline.getControl(FloatControl.Type.VOLUME);   
    			volumeControl.setValue(gain);
    		}
    		else if(AudioSystem.isLineSupported(source2)) {
    			Port outline2 =  (Port) AudioSystem.getLine(source2);
    			outline2.open();
    			FloatControl volumeControl2 = (FloatControl) outline2.getControl(FloatControl.Type.VOLUME); 
    			volumeControl2.setValue(gain);
    		}
    	}
    	catch (LineUnavailableException ex) 
    	{ 
    	    System.err.println("source not supported"); 
    	    ex.printStackTrace();
    	}   
    } 
}