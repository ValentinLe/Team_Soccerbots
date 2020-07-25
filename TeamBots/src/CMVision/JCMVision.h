/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class JCMVision */

#ifndef _Included_JCMVision
#define _Included_JCMVision
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: libraryLoaded */
#undef JCMVision_CMV_MAX_COLORS
#define JCMVision_CMV_MAX_COLORS 32L
#undef JCMVision_CMV_MAX_BLOBS
#define JCMVision_CMV_MAX_BLOBS 1024L
/*
 * Class:     JCMVision
 * Method:    getNumRegions
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_JCMVision_getNumRegions
  (JNIEnv *, jobject, jint);

/*
 * Class:     JCMVision
 * Method:    getSerRegions
 * Signature: ([III)V
 */
JNIEXPORT void JNICALL Java_JCMVision_getSerRegions
  (JNIEnv *, jobject, jintArray, jint, jint);

/*
 * Class:     JCMVision
 * Method:    init
 * Signature: (III)Z
 */
JNIEXPORT jboolean JNICALL Java_JCMVision_init
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     JCMVision
 * Method:    processFrame
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_JCMVision_processFrame
  (JNIEnv *, jobject);

/*
 * Class:     JCMVision
 * Method:    quit
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_JCMVision_quit
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
