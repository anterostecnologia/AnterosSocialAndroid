package br.com.anteros.social.image;

import android.graphics.Bitmap;


public class ChainImageProcessor implements ImageProcessor {

    ImageProcessor[] mProcessors;

    public ChainImageProcessor(ImageProcessor... processors) {
        mProcessors = processors;
    }

    public Bitmap processImage(Bitmap bitmap) {
        for (ImageProcessor processor : mProcessors) {
            bitmap = processor.processImage(bitmap);
        }
        return bitmap;
    }

}
