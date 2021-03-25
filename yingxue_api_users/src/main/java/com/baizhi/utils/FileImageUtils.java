package com.baizhi.utils;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileImageUtils {

    public static InputStream randomGrabberFFmpegImage(String filePath) {
        InputStream is = null;
        try {
            FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
            ff.start();
            String rotate = ff.getVideoMetadata("rotate");
            Frame f;
            int i = 0;
            while (i < 5) {
                f = ff.grabImage();
                opencv_core.IplImage src = null;
                if (null != rotate && rotate.length() > 1) {
                    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                    src = converter.convert(f);
                    f = converter.convert(rotate(src, Integer.valueOf(rotate)));
                }
                is = doExecuteFrame(f);
                i++;
            }
            ff.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return is;
    }

    /*
     * 旋转角度的。这个是为了保证截取到的图和视频中的旋转信息一致
     */
    public static opencv_core.IplImage rotate(opencv_core.IplImage src, int angle) {
        opencv_core.IplImage img = opencv_core.IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, angle);
        return img;
    }


//    public static File doExecuteFrame(Frame f, String targerFilePath, String targetFileName) {
//        if (null == f || null == f.image) {
//            return null;
//        }
//        Java2DFrameConverter converter = new Java2DFrameConverter();
//        String imageMat = "jpg";
//        String FileName = targerFilePath + File.separator + targetFileName + "." + imageMat;
//        BufferedImage bi = converter.getBufferedImage(f);
//        File output = new File(FileName);
//        try {
//            ImageIO.write(bi, imageMat, output);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return output;
//    }

    public static InputStream doExecuteFrame(org.bytedeco.javacv.Frame f) {
        if (null == f || null == f.image) {
            return null;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        InputStream inputStream = bufferedImageToInputStream(bi);
        return inputStream;
    }

    /**
     * BufferedImage 转为 inputStream
     *
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
    }
}
