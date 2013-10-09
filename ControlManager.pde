import japplemenubar.*;
import controlP5.*;
import ddf.minim.*;

class ControlManager {
  // Decide controls
  ControlP5      controlP5;
  ControlWindow  controlWindow;

  // Decide sliders
  controlP5.Controller  slider0;
  controlP5.Controller  slider1;
  controlP5.Controller  slider2;
  controlP5.Controller  slider3;
  controlP5.Controller  slider4;
  controlP5.Controller  slider5;
  controlP5.Controller  slider6;
  controlP5.Controller  slider7;
  controlP5.Controller  slider8;
  controlP5.Controller  slider9;
  controlP5.Controller  slider10;
  controlP5.Controller  slider11;
  controlP5.Controller  slider12;
  controlP5.Controller  slider13;
  controlP5.Controller  slider14;
  controlP5.Controller  slider15;
  controlP5.Controller  slider16;
  controlP5.Controller  slider17;
  controlP5.Controller  slider18;
  controlP5.Controller  slider19;
  controlP5.Controller  slider20;
  controlP5.Controller  slider21;
  controlP5.Controller  slider22;
  controlP5.Controller  slider23;
  controlP5.Controller  slider24;
  controlP5.Controller  slider25;
  controlP5.Controller  slider26;
  controlP5.Controller  slider27;
  controlP5.Controller  slider28;
  controlP5.Controller  slider29;
  controlP5.Controller  slider30;

  // Decide toggle
  Toggle  fillSwitch;
  Toggle  strokeSwitch;
  Toggle  lightSwitch;
  Toggle  recSwitch;
  Toggle  particleSwitch;
  Toggle  dollySwitch;

  // Decide radio button
  RadioButton  radioButton;

  // Decide paramater
  int   SCENE_COUNT = 4;
  float   sceneNumber = 2;
  float eyeX;
  float eyeY;
  float eyeZ;

  float speed;
  float fillAlpha;
  float lineAlpha;
  float particleFillAlpha;
  float particleLineAlpha;
  float objSize;
  float fillRed;
  float fillGreen;
  float fillBlue;
  float lineRed;
  float lineGreen;
  float lineBlue;
  float redOffset;
  float greenOffset;
  float blueOffset;
  float centerX;
  float centerY;
  float centerZ;

  float customSize;
  float lineWeight;

  float  grX;
  float  grY;
  float  grZ;
  float  grXSpeed;
  float  grYSpeed;
  float  grZSpeed;

  int    sd;

  float  soundLevel;
  float  leftSound;
  float  rightSound;
  
  int    noiseCo;
  
  float  fovLevel;
  float  rainbowLevel;

  boolean     FILL = true;
  boolean     STROKE = true;
  boolean     LIGHT = true;
  boolean     REC = false;
  boolean     PARTICLE = false;
  boolean     DOLLY = false;

  // Decide minim
  Minim minim;
  AudioInput in;

  // Decide constructor
  ControlManager (BOX myBox)
  {
    // Create ControlP5
    controlP5 = new ControlP5(myBox);
    controlP5.setAutoDraw(false);

    // Create control window
    controlWindow = controlP5.addControlWindow("controlP5 window", 0, 0, 450, 750);

    // Configure control window
    controlWindow.setBackground(color(40));
    controlWindow.setTitle("controller window");

    int         sliderX = 20;
    int         sliderY = 70;
    int         sliderWidth = 300;
    int         sliderHeight = 20;
    float       minValue = -5000.0;
    float       maxValue = 5000.0;

    // Create controllers
    // For sliders
    slider0 = controlP5.addSlider("eyeX", minValue, maxValue, sliderX, sliderY, sliderWidth, sliderHeight);
    slider1 = controlP5.addSlider("eyeY", minValue, maxValue, sliderX, sliderY + ((sliderHeight+1)*1), sliderWidth, sliderHeight);
    slider2 = controlP5.addSlider("eyeZ", minValue, maxValue, sliderX, sliderY + ((sliderHeight+1)*2), sliderWidth, sliderHeight);
    slider3 = controlP5.addSlider("speed", minValue*100, maxValue*100, sliderX, sliderY + ((sliderHeight+1)*3), sliderWidth, sliderHeight);
    slider4 = controlP5.addSlider("fillAlpha", 0, 255, sliderX, sliderY + ((sliderHeight+1)*4), sliderWidth, sliderHeight);
    slider5 = controlP5.addSlider("lineAlpha", 0, 255, sliderX, sliderY + ((sliderHeight+1)*5), sliderWidth, sliderHeight);
    slider6 = controlP5.addSlider("objSize", 0, 2000, sliderX, sliderY + ((sliderHeight+1)*6), sliderWidth, sliderHeight);
    slider7 = controlP5.addSlider("fillRed", 0, 255, sliderX, sliderY + ((sliderHeight+1)*7), sliderWidth, sliderHeight);
    slider8 = controlP5.addSlider("fillGreen", 0, 255, sliderX, sliderY + ((sliderHeight+1)*8), sliderWidth, sliderHeight);
    slider9 = controlP5.addSlider("fillBlue", 0, 255, sliderX, sliderY + ((sliderHeight+1)*9), sliderWidth, sliderHeight);
    slider10 = controlP5.addSlider("lineRed", 0, 255, sliderX, sliderY + ((sliderHeight+1)*10), sliderWidth, sliderHeight);
    slider11 = controlP5.addSlider("lineGreen", 0, 255, sliderX, sliderY + ((sliderHeight+1)*11), sliderWidth, sliderHeight);
    slider12 = controlP5.addSlider("lineBlue", 0, 255, sliderX, sliderY + ((sliderHeight+1)*12), sliderWidth, sliderHeight);
    slider13 = controlP5.addSlider("centerX", 0, 10000, sliderX, sliderY + ((sliderHeight+1)*13), sliderWidth, sliderHeight);
    slider14 = controlP5.addSlider("centerY", 0, 10000, sliderX, sliderY + ((sliderHeight+1)*14), sliderWidth, sliderHeight);
    slider15 = controlP5.addSlider("centerZ", 0, 10000, sliderX, sliderY + ((sliderHeight+1)*15), sliderWidth, sliderHeight);
    slider16 = controlP5.addSlider("customSize", 0, 10000, sliderX, sliderY + ((sliderHeight+1)*16), sliderWidth, sliderHeight);
    slider17 = controlP5.addSlider("lineWeight", 0, 100, sliderX, sliderY + ((sliderHeight+1)*17), sliderWidth, sliderHeight);
    slider18 = controlP5.addSlider("globalRotateXSpeed", -0.1, 0.1, sliderX, sliderY + ((sliderHeight+1)*18), sliderWidth, sliderHeight);
    slider19 = controlP5.addSlider("globalRotateYSpeed", -0.1, 0.1, sliderX, sliderY + ((sliderHeight+1)*19), sliderWidth, sliderHeight);
    slider20 = controlP5.addSlider("globalRotateZSpeed", -0.1, 0.1, sliderX, sliderY + ((sliderHeight+1)*20), sliderWidth, sliderHeight);
    slider21 = controlP5.addSlider("sd", 3, 10, sliderX, sliderY + ((sliderHeight+1)*21), sliderWidth, sliderHeight);
    slider22 = controlP5.addSlider("soundLevel", 1, 10000, sliderX, sliderY + ((sliderHeight+1)*22), sliderWidth, sliderHeight);
    slider23 = controlP5.addSlider("redOffset", -100, 100, sliderX, sliderY + ((sliderHeight+1)*23), sliderWidth, sliderHeight);
    slider24 = controlP5.addSlider("greenOffset", -100, 100, sliderX, sliderY + ((sliderHeight+1)*24), sliderWidth, sliderHeight);
    slider25 = controlP5.addSlider("blueOffset", -100, 100, sliderX, sliderY + ((sliderHeight+1)*25), sliderWidth, sliderHeight);
    slider26 = controlP5.addSlider("noiseCo", 0, 1000, sliderX, sliderY + ((sliderHeight+1)*26), sliderWidth, sliderHeight);
    slider27 = controlP5.addSlider("fovLevel", 1, 100, sliderX, sliderY + ((sliderHeight+1)*27), sliderWidth, sliderHeight);
    slider28 = controlP5.addSlider("rainbowLevel", 0, 100, sliderX, sliderY + ((sliderHeight+1)*28), sliderWidth, sliderHeight);
    slider29 = controlP5.addSlider("particleFillAlpha", 0, 255, sliderX, sliderY + ((sliderHeight+1)*29), sliderWidth, sliderHeight);
    slider30 = controlP5.addSlider("particleLineAlpha", 0, 255, sliderX, sliderY + ((sliderHeight+1)*30), sliderWidth, sliderHeight);

    // Set initial value
    slider0.setValue(minValue);
    slider1.setValue(minValue);
    slider2.setValue(minValue);
    slider3.setValue(minValue*100);
    slider4.setValue(255);
    slider5.setValue(255);
    slider6.setValue(1000);
    slider7.setValue(255);
    slider8.setValue(255);
    slider9.setValue(255);  
    slider10.setValue(255);
    slider11.setValue(255);
    slider12.setValue(255);
    slider13.setValue(0);
    slider14.setValue(0);  
    slider15.setValue(0);
    slider16.setValue(0);
    slider17.setValue(0.1);
    slider18.setValue(0);
    slider19.setValue(0);
    slider20.setValue(0);
    slider21.setValue(3);
    slider22.setValue(1);
    slider23.setValue(0);
    slider24.setValue(0);
    slider25.setValue(0);
    slider26.setValue(0);
    slider27.setValue(3);


    // Set controller for window
    slider0.setWindow(controlWindow);
    slider1.setWindow(controlWindow);
    slider2.setWindow(controlWindow);
    slider3.setWindow(controlWindow);
    slider4.setWindow(controlWindow);
    slider5.setWindow(controlWindow);
    slider6.setWindow(controlWindow);
    slider7.setWindow(controlWindow);
    slider8.setWindow(controlWindow);
    slider9.setWindow(controlWindow);
    slider10.setWindow(controlWindow);
    slider11.setWindow(controlWindow);
    slider12.setWindow(controlWindow);
    slider13.setWindow(controlWindow);
    slider14.setWindow(controlWindow);
    slider15.setWindow(controlWindow);
    slider16.setWindow(controlWindow);
    slider17.setWindow(controlWindow);
    slider18.setWindow(controlWindow);
    slider19.setWindow(controlWindow);
    slider20.setWindow(controlWindow);
    slider21.setWindow(controlWindow);
    slider22.setWindow(controlWindow);
    slider23.setWindow(controlWindow);
    slider24.setWindow(controlWindow);
    slider25.setWindow(controlWindow);
    slider26.setWindow(controlWindow);
    slider27.setWindow(controlWindow);
    slider28.setWindow(controlWindow);
    slider29.setWindow(controlWindow);
    slider30.setWindow(controlWindow);
    
    int toggleX = 40;
    int toggleY = 25;
    // For toggle
    fillSwitch = controlP5.addToggle("fillSwitch", true, toggleX + 60, toggleY, 20, 20);
    strokeSwitch = controlP5.addToggle("strokeSwitch", true, toggleX + 120, toggleY, 20, 20);
    lightSwitch = controlP5.addToggle("lightSwitch", true, toggleX + 180, toggleY, 20, 20);
    recSwitch = controlP5.addToggle("recSwitch", false, toggleX + 240, toggleY, 20, 20);
    particleSwitch = controlP5.addToggle("particleSwitch", false, toggleX + 300, toggleY, 20, 20);
    dollySwitch = controlP5.addToggle("dollySwitch", false, toggleX + 360, toggleY, 20, 20);
    
    // Register toggle with window
    fillSwitch.setWindow(controlWindow);
    strokeSwitch.setWindow(controlWindow);
    lightSwitch.setWindow(controlWindow);
    recSwitch.setWindow(controlWindow);
    particleSwitch.setWindow(controlWindow);
    dollySwitch.setWindow(controlWindow);

    // For scene radio button
    radioButton = controlP5.addRadioButton("sceneRadio", 0, 0);
    for (int i = 0; i < SCENE_COUNT; i++) {
      addToRadioButton(radioButton, "scene" + i, i);
    }

    // Get slider params
    eyeX = slider0.value();
    eyeY = slider1.value();
    eyeZ = slider2.value();

    speed = slider3.value();
    fillAlpha = slider4.value();
    lineAlpha = slider5.value();
    objSize = slider6.value();
    fillRed = slider7.value();
    fillGreen = slider8.value();
    fillBlue = slider9.value();
    lineRed = slider10.value();
    lineGreen = slider11.value();
    lineBlue = slider12.value();
    centerX = slider13.value();
    centerY = slider14.value();
    centerZ = slider15.value();

    customSize = slider16.value();
    lineWeight = slider17.value();

    // Create minim
    minim = new Minim(myBox);

    // get a line in from Minim, default bit depth is 16
    in = minim.getLineIn(Minim.STEREO, 512);
  }

  void addToRadioButton(RadioButton theRadioButton, String theName, int theValue ) {
    Toggle t = theRadioButton.addItem(theName, theValue);
    t.captionLabel().setColorBackground(color(80));
    t.captionLabel().style().movePadding(2, 0, -1, 2);
    t.captionLabel().style().moveMargin(-2, 0, 0, -3);
    t.captionLabel().style().backgroundWidth = 46;
    t.setWindow(controlWindow);
  }

  float soundLeft()
  {
    return highPassFilter(in.left.level(), true) * soundLevel;
  }

  float soundRight()
  {
    return highPassFilter(in.right.level(), false) * soundLevel;
  }

  float highPassFilter(float sound, boolean bool)
  {
    float filteringFactor = 0.9; // basic 0.1
    if (bool) {
      // low pass fillter
      //      leftSound = (sound * filteringFactor) + (leftSound * (1.0 - filteringFactor));
      // high pass fillter
      leftSound = sound - ( (sound * filteringFactor) + (leftSound * (1.0 - filteringFactor)) );
      return leftSound;
    }
    else {
      // low pass fillter
      //      rightSound = (sound * filteringFactor) + (rightSound * (1.0 - filteringFactor));
      // high pass fillter
      rightSound = sound - ( (sound * filteringFactor) + (rightSound * (1.0 - filteringFactor)) );
      return rightSound;
    }
  }
}

// slider event functions
void eyeX(float value) 
{
  ctr.eyeX = value;
}

void eyeY(float value) 
{
  ctr.eyeY = value;
}

void eyeZ(float value) 
{
  ctr.eyeZ = value;
}

void centerX(float value) 
{
  ctr.centerX = value;
}

void centerY(float value) 
{
  ctr.centerY = value;
}

void centerZ(float value) 
{
  ctr.centerZ = value;
}

void fillRed(float value) 
{
  ctr.fillRed = value;
}

void fillGreen(float value) 
{
  ctr.fillGreen = value;
}

void fillBlue(float value) 
{
  ctr.fillBlue = value;
}

void lineRed(float value) 
{
  ctr.lineRed = value;
}

void lineGreen(float value) 
{
  ctr.lineGreen = value;
}

void lineBlue(float value) 
{
  ctr.lineBlue = value;
}

void fillAlpha(float value) 
{
  ctr.fillAlpha = value;
}

void lineAlpha(float value) 
{
  ctr.lineAlpha = value;
}

void redOffset(float value) 
{
  ctr.redOffset = value;
}

void greenOffset(float value) 
{
  ctr.greenOffset = value;
}

void blueOffset(float value) 
{
  ctr.blueOffset = value;
}

void objSize(float value) 
{
  ctr.objSize = value;
}

void customSize(float value) 
{
  ctr.customSize = value;
}

void lineWeight(float value) 
{
  ctr.lineWeight = value;
}

void speed(float value) 
{
  ctr.speed = value;
}

void globalRotateXSpeed(float value) 
{
  ctr.grXSpeed = value;
}

void globalRotateYSpeed(float value) 
{
  ctr.grYSpeed = value;
}

void globalRotateZSpeed(float value) 
{
  ctr.grZSpeed = value;
}

void sd(float value) 
{
  ctr.sd = (int)value;
}

void soundLevel(float value) 
{
  ctr.soundLevel = (int)value;
}

void noiseCo(float value) 
{
  ctr.noiseCo = (int)value;
}

void fovLevel(float value) 
{
  ctr.fovLevel = (int)value;
}

void rainbowLevel(float value) 
{
  ctr.rainbowLevel = (int)value;
}

void particleFillAlpha(float value) 
{
  ctr.particleFillAlpha = (int)value;
}

void particleLineAlpha(float value) 
{
  ctr.particleLineAlpha = (int)value;
}

// Decide toggle event functions
void fillSwitch(boolean theFlag) {
  ctr.FILL = theFlag;
}

void strokeSwitch(boolean theFlag) {
  ctr.STROKE = theFlag;
}

void lightSwitch(boolean theFlag) {
  ctr.LIGHT = theFlag;
}

void recSwitch(boolean theFlag) {
  ctr.REC = theFlag;
}

void particleSwitch(boolean theFlag) {
  ctr.PARTICLE = theFlag;
}

void dollySwitch(boolean theFlag) {
  ctr.DOLLY = theFlag;
}

void controlEvent(ControlEvent theEvent) {
 ctr.sceneNumber = theEvent.group().value();
}
