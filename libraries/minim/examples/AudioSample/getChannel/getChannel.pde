/**
  * This sketch demonstrates how to get a channel of audio from an AudioSample and then manipulate it to change the AudioSample after it
  * has been loaded. 
  */

import ddf.minim.*;

Minim minim;
AudioSample jingle;

void setup()
{
  size(512, 200, P3D);

  minim = new Minim(this);
  
  jingle = minim.loadSample("jingle.mp3", 2048);
  // get the left channel of the audio as a float array
  // getChannel is defined in the interface BuffereAudio, 
  // which also defines two constants to use as an argument
  // BufferedAudio.LEFT and BufferedAudio.RIGHT
  float[] leftChannel = jingle.getChannel(BufferedAudio.LEFT);
  // now we are just going to reverse the left channel
  float[] reversed = reverse(leftChannel);
  arraycopy(reversed, 0, leftChannel, 0, leftChannel.length);
}

void draw()
{
  background(0);
  stroke(255);
  for(int i = 0; i < jingle.bufferSize() - 1; i++)
  {
    line(i, 50 - jingle.left.get(i)*50, i+1, 50 - jingle.left.get(i+1)*50);
    line(i, 150 - jingle.right.get(i)*50, i+1, 150 - jingle.right.get(i+1)*50);
  }
}

void keyPressed()
{
  jingle.trigger();
}

void stop()
{
  // always close Minim audio classes when you finish with them
  jingle.close();
  minim.stop();
  
  super.stop();
}
