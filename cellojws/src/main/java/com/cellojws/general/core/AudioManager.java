package com.cellojws.general.core;

import javax.sound.sampled.Clip;


public class AudioManager
{

	private boolean playing = false;
	
	private Clip clip = null;

	public AudioManager()
	{
		
	}
	
	public void stop()
	{
		if( clip != null )
		{
			clip.stop();
			clip.close();
			playing = false;
		}
	
	}
	
	public boolean isPlaying()
	{
		return playing;
	}

	public boolean playAudio(final String filename, final boolean loopContinuously)
	{
		return true;
		/*stop();
		
		try
		{			
			clip = AudioSystem.getClip();
		}
		catch(LineUnavailableException e)
		{
			return false;
		}
		
		final File file = new File(filename);
		try
		{
			AudioInputStream input = AudioSystem.getAudioInputStream(file);
			clip.close();
			clip.open(input);
			if( loopContinuously )
			{
				//TODO: Uncomment this to turn music back on
			//	clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			else
			{
				//TODO: Uncomment this to turn music back on
				//clip.start();
			}
		}
		catch(Exception e)
		{
			return false;
		}
				
		playing = true;
		return true;*/
	}
	
}
