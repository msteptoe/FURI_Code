#include <jni.h>
#include <android/log.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "stb_image.c"
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image_write.h"

#define  LOG_TAG    "Convert"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

unsigned char *image;
int imageWidth, imageHeight, comp;
const char *jpeg_Path;
const char *bmp_Path;
JavaVM *gJavaVM;

extern "C"
{
JNIEXPORT void JNICALL Java_com_original_Convert_createBMP(JNIEnv * myEnv, jobject  Mythis,  jstring  jpeg_path, jstring bmp_path);
};
void loadJPG(const char* filename,  int width, int height, int bitdepth);
void init();

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv *env;
	gJavaVM = vm;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return -1;
	}
	return JNI_VERSION_1_4;
}

void loadJPG(const char* filename,  int width, int height, int bitdepth)
{
	image = stbi_load(filename, &width, &height, &bitdepth, 0);
	LOGI("filename = %s, width = %d, height = %d, bitdepth = %d", filename, width, height, bitdepth);
	imageWidth = width;
	imageHeight = height;
	comp = bitdepth;
}

void init()
{
	/* Load in a PNG image */
	int loadCorrectly = 0;
	loadJPG(jpeg_Path,  imageWidth, imageHeight, comp);
	//printf("Load Successful\n");
}
/*
int main()
{
	
	// init some states
	
	return 0;
}
 */

JNIEXPORT void JNICALL Java_com_original_Convert_createBMP(JNIEnv * myEnv, jobject  obj,  jstring  jpeg_PathJ, jstring bmp_PathJ)
{
	jpeg_Path = myEnv->GetStringUTFChars(jpeg_PathJ, NULL);
	bmp_Path = myEnv->GetStringUTFChars(bmp_PathJ, NULL);
	LOGI("Destination = %s",bmp_Path);
	init();
	stbi_write_bmp(bmp_Path, imageWidth, imageHeight, comp, image);
	stbi_image_free(image);
	LOGI("Looking Good");
}
