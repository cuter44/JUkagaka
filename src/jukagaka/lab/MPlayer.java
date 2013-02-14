package jukagaka.lab;

/* I/O */
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
/* Data Structure */
import java.util.ArrayList;
/* Math */
import java.util.Random;
/* Threads & Process */
import java.lang.Runnable;
import java.lang.Runtime;
/* Ukagaka */
import jukagaka.UkaComponent;
import jukagaka.UkaDaemon;

/**
 * 5+0 的实验项目
 * 试图用 MPlayer 的命令行调用方式实现各种媒体播放功能
 * 正在实现
 * * 播放
 * * 随机播放
 * * 同步歌词
 */
public class MPlayer implements UkaComponent,Runnable
{
  // UkaComponent
  //   Code Seg
    public static boolean onLoad()
    {
        return(true);
    }

    public static boolean onStart()
    {
        return(true);
    }

    public static boolean onExit()
    {
        return(true);
    }

    public static boolean onInstall()
    {
        return(true);
    }

    public static boolean onUnistall()
    {
        return(true);
    }

  // Playlist
  //   Data Seg
    private ArrayList<String> playlist = new ArrayList<String>();
  //   Code Seg
    /**
     * Read Playlist
     *
     * In plain-text, .m3u (i.e. line commnet # and blank line is skipped) supported.
     *
     */
    public boolean loadPlaylist(String strFileName)
    {
        FileInputStream playlistFile = null;

        try
        {
             playlistFile = new FileInputStream(strFileName);
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            return(false);
        }

        Scanner scnPlaylist = new Scanner(playlistFile);
        String buffer = null;

        while (scnPlaylist.hasNextLine())
        {
            buffer = scnPlaylist.nextLine();
            buffer = buffer.trim();
            if (buffer.equals(""))
                continue;
            if (buffer.startsWith("#"))
                continue;
            //else
            this.playlist.add(buffer);
        }

        return(true);
    }

    public void clearPlaylist()
    {
        this.playlist.clear();

        return;
    }

  // Thread & Process
  //   Data Seg
    private Process mpProcess = null;
    private Thread mpDaemon = null;
  //   Code Seg
    public void run()
    {
        int currentPlay = -1;
        Random randomer = new Random();

        while (this.next)
        {
            if (this.random)
                currentPlay = randomer.nextInt(this.playlist.size());
            else
            {
                currentPlay++;
                if (this.loop)
                    currentPlay = 0;
            }

            try
            {
                this.mpProcess = Runtime
                    .getRuntime()
                    .exec("mplayer \"" + this.playlist.get(currentPlay) + "\"");

                InputStream mpOutputStream = this.mpProcess.getInputStream();
                BufferedReader mpOutputReader= new BufferedReader(new InputStreamReader(mpOutputStream));
                String buffer = null;

                while (true)
                {
                    //while (mpOutputStream.available() > 0)
                    //{
                        //mpOutputStream.skip(mpOutputStream.available());
                        //Thread.sleep(100);
                    //}

                    while ((buffer = mpOutputReader.readLine()) != null)
                    {
                        //System.out.println(buffer);
                        //Thread.sleep(50);
                    }

                    try
                    {
                        mpProcess.exitValue();
                        break;
                    }
                    catch (IllegalThreadStateException ex)
                    {
                        continue;
                    }

                }

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                //break;
                //continue;
            }
            //catch (InterruptedException ex)
            //{
                //ex.printStackTrace();
                ////break;
                ////continue;
            //}
        }

        return;
    }

  // Play & Stop
  //   Data Seg
    public boolean loop = false;
    public boolean random = false;
    private boolean next = true;
  //   Code Seg
    public void switchLoop(boolean argLoop)
    {
        this.loop = argLoop;

        return;
    }

    public void switchRandom(boolean argRandom)
    {
        this.random = argRandom;

        return;
    }

    public void startDaemon()
    {
        if (this.mpDaemon != null)
            return;

        this.mpDaemon = new Thread(this);
        mpDaemon.start();

        return;
    }

    private void stopProc()
    {
        if (this.mpProcess == null)
            return;

        this.mpProcess.destroy();

        return;
    }

    public void pause()
    {
        if (this.mpProcess == null)
            return;

        try
        {
            OutputStream mpInputStm = this.mpProcess.getOutputStream();
            mpInputStm.write(' ');
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void next()
    {
        this.next = true;
        this.stopProc();

        return;
    }

    public void stop()
    {
        this.next = false;
        this.stopProc();

        return;
    }

  // Miscellaneous
    public static void main(String[] args)
    {
        MPlayer mp = new MPlayer();

        mp.loadPlaylist("C:\\Users\\Galin\\Music\\all.m3u");
        //System.out.println(mp.playlist);

        //Thread mpThread = new Thread(mp);
        //mpThread.start();

        mp.switchRandom(true);
        mp.startDaemon();

        return;
    }
}
