/*
Copyright 2024 HJOW (hujinone22@naver.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.duckdns.hjow.commons.sound;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/** Class to play wav file - from Reflexioner - https://github.com/HJOW/Reflexioner */
public class SoundClip implements Serializable
{
    private static final long serialVersionUID = -4189707921602261694L;
    protected URL    url;
    protected double volume = 100.0;
    protected transient boolean flagStop = false;
    /** Create instance with URL to get wav file */
    public SoundClip(URL url)
    {
        this.url = url;
    }
    /** Create instance with wav file */
    public SoundClip(File f) throws MalformedURLException
    {
        this(f.toURI().toURL());
    }
    /** Play wav sound now */
    public void start()
    {
        if(url == null) return;
        flagStop = false;
        AudioInputStream inp1 = null;
        AudioInputStream inp2 = null;
        SourceDataLine   line = null;
        try
        {
            byte[] buffer = new byte[4096];
            int reads;
            
            // Load wav from URL
            inp1 = AudioSystem.getAudioInputStream(url);
            
            if(flagStop) return; // Return when stop() method called
            
            // Detect audio format of wav file and get converted input stream
            AudioFormat formatBase = inp1.getFormat();
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, formatBase.getSampleRate(), 16, formatBase.getChannels(), formatBase.getChannels() * 2, formatBase.getSampleRate(), false);
            inp2 = AudioSystem.getAudioInputStream(format, inp1);
            
            if(flagStop) return; // Return when stop() method called
            
            // Create SourceDataLine to play wav
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            Mixer mixer = AudioSystem.getMixer(null);
            line = (SourceDataLine) mixer.getLine(info);
            
            if(flagStop) return; // Return when stop() method called
            
            // Open SourceDataLine
            line.open(format);
            
            // This must be in here (after line opened !)
            FloatControl ctrl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            ctrl.setValue((float) (20.0f * Math.log10(volume / 100.0)));
            
            // Play Start
            line.start();
            
            while(! flagStop)
            {
                reads = inp2.read(buffer, 0, buffer.length);
                if(reads < 0) break; // Return when stop() method called
                line.write(buffer, 0, reads);
            }
            
            // END
            line.drain();
            line.stop();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try { line.close();  } catch(Exception ex) {}
            try { inp2.close();  } catch(Exception ex) {}
            try { inp1.close();  } catch(Exception ex) {}
            flagStop = false;
        }
    }
    /** Play wav sound now */
    public void play()
    {
        start();
    }
    /** Stop playing */
    public void stop()
    {
        flagStop = true;
    }
    /** Get volume (0 ~ 100) default 100 */
    public double getVolume()
    {
        return volume;
    }
    /** Set volume (0 ~ 100) default 100 */
    public void setVolume(double volume)
    {
        this.volume = volume;
        if(this.volume < 0) this.volume = 0;
    }
    /** Get URL of wav file */
    public URL getUrl() {
        return url;
    }
    /** Set wav file */
    public void setUrl(URL url) {
        this.url = url;
    }
}