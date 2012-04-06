package com.tasty.fish.utils;

import android.media.*;

public class AndroidAudioDevice
{
	int sampleRate = 44100;
	int format = AudioFormat.ENCODING_PCM_8BIT;
   AudioTrack track;
   byte[] buffer = new byte[1024];
 
   public AndroidAudioDevice( )
   {
      int minSize =AudioTrack.getMinBufferSize( sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO, format);        
      track = new AudioTrack( AudioManager.STREAM_MUSIC, sampleRate, 
                                        AudioFormat.CHANNEL_CONFIGURATION_MONO, format, 
                                        minSize, AudioTrack.MODE_STREAM);
      track.play();
   }	   
 
   public void stop()
   {
	   track.stop();
   }
   
   public void writeSamples(byte[] samples) 
   {	
      fillBuffer( samples );
      track.write( buffer, 0, samples.length );
      
   }
 
   private void fillBuffer( byte[] samples )
   {
      if( buffer.length < samples.length )
         buffer = new byte[samples.length];
 
      for( int i = 0; i < samples.length; i++ )
         buffer[i] = (byte)(samples[i] * Short.MAX_VALUE);;
   }		
}
