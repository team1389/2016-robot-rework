package com.team1389.watch;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.USBCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class CameraTesting {
    
    public void robotInit() {
            new Thread(() -> {
                USBCamera camera = CameraServer.getInstance().startAutomaticCapture();
                camera.setResolution(640, 480);
                
                CvSink cvSink = CameraServer.getInstance().getVideo();
                CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
                
                Mat source = new Mat();
                Mat output = new Mat();
                
                while(true) {
                    cvSink.grabFrame(source);
                    Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                    outputStream.putFrame(output);
                }
            }).start();
    }
}

