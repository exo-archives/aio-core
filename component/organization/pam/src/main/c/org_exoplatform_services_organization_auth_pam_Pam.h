/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_exoplatform_services_organization_auth_pam_Pam */

#ifndef _Included_org_exoplatform_services_organization_auth_pam_Pam
#define _Included_org_exoplatform_services_organization_auth_pam_Pam
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_exoplatform_services_organization_auth_pam_Pam
 * Method:    isSharedLibraryWorking
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_exoplatform_services_organization_auth_pam_Pam_isSharedLibraryWorking
  (JNIEnv *, jobject);

/*
 * Class:     org_exoplatform_services_organization_auth_pam_Pam
 * Method:    authenticate
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)I
 */
JNIEXPORT jint JNICALL Java_org_exoplatform_services_organization_auth_pam_Pam_authenticate
  (JNIEnv *, jobject, jstring, jstring, jstring, jboolean);

/*
 * Class:     org_exoplatform_services_organization_auth_pam_Pam
 * Method:    groups
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_exoplatform_services_organization_auth_pam_Pam_groups
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
