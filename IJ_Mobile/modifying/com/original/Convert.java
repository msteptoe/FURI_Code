package com.original;

public class Convert {
	static {
        System.loadLibrary("convert");
    }
	
	
	public native void createBMP(String picLoc, String bmpLoc);
}
