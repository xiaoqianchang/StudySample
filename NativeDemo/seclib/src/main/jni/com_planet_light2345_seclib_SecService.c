#include "com_planet_light2345_seclib_SecService.h"
#include "b64.h"
#include <string.h>
#include "md5.h"

#include <android/log.h>
#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

#define GET_SIGNATURES 0x00000040

static int checked = 0;

int error_check(JNIEnv *env)
{
	if ((*env)->ExceptionCheck(env)) {
 		return 1;
 	}
 	return 0;
}

int checkSignature(JNIEnv *env, jobject context)
{
    if(checked){
        return 1;
    }

	jclass contextClass = (*env)->GetObjectClass(env, context);
	if(error_check(env) || contextClass == NULL){
		return 0;
	}

	jmethodID getPackageNameMethodID = (*env)->GetMethodID(env, contextClass, "getPackageName", "()Ljava/lang/String;");
	if(error_check(env)){
		return 0;
	}

	jstring packageName = (*env)->CallObjectMethod(env, context, getPackageNameMethodID);
	if(packageName == NULL){
		return 0;
	}

	jmethodID getPackageManagerMethodID = (*env)->GetMethodID(env, contextClass, "getPackageManager", "()Landroid/content/pm/PackageManager;");
	if(error_check(env)){
		return 0;
	}

	jobject packageManager = (*env)->CallObjectMethod(env, context, getPackageManagerMethodID);
	jclass packageManagerClass = (*env)->GetObjectClass(env, packageManager);


	jmethodID getPackageInfoMethodID = (*env)->GetMethodID(env, packageManagerClass, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
	if(error_check(env)){
		return 0;
	}

	jobject packageInfo = (*env)->CallObjectMethod(env, packageManager, getPackageInfoMethodID, packageName, GET_SIGNATURES);
	if(packageInfo == NULL){
		return 0;
	}

	jclass packageInfoClass = (*env)->GetObjectClass(env, packageInfo);
	jfieldID signatureFieldID = (*env)->GetFieldID(env, packageInfoClass, "signatures", "[Landroid/content/pm/Signature;");
	jobjectArray signatures = (*env)->GetObjectField(env, packageInfo, signatureFieldID);
	int size = (*env)->GetArrayLength(env, signatures);
	if(size == 0){
		return 0;
	}

	jobject signature = (*env)->GetObjectArrayElement(env, signatures, 0);
	jclass signatureClass = (*env)->GetObjectClass(env, signature);
	jmethodID toByteArrayMethodID = (*env)->GetMethodID(env, signatureClass,  "toCharsString", "()Ljava/lang/String;");
	if(error_check(env)){
		return 0;
	}
	jstring strSign = (*env)->CallObjectMethod(env, signature, toByteArrayMethodID);
	 char *c_sign = (char *) (*env)->GetStringUTFChars(env, strSign, 0);

	//printf("usage: %s 'string'\n", c_sign);
	//LOGI("************************************");
   // LOGI("sign %s",c_sign);
   // LOGI("************************************");
    I("str = %s",c_sign);
    //I("isValidPackage1");

    if(!strcmp(c_sign,SIGNATURE)){
        checked = 1;
    }

    free(c_sign);
	return checked;
}

JNIEXPORT jbyteArray JNICALL Java_com_planet_light2345_seclib_SecService_encryptIn
    (JNIEnv * env, jclass clazz, jobject context, jbyteArray content){
  if(!checkSignature(env,context)){
    return NULL;
  }
  LOGI("encryptIn");
  // key
  return encrypt2(env, clazz, context, content, NORMAL_KEY, 0);
}

JNIEXPORT jbyteArray JNICALL Java_com_planet_light2345_seclib_SecService_encryptP1
  (JNIEnv * env, jclass clazz, jobject context, jbyteArray content, jbyteArray key){

  if(!checkSignature(env,context)){
    return NULL;
  }

  LOGI("encryptP1");
  signed char * java_bytes_key = (*env)->GetByteArrayElements(env,key,0);
  long key_len = (*env)->GetArrayLength(env, key);
  char * native_string_key = malloc(sizeof(char) * (key_len + 1));
  memcpy(native_string_key, java_bytes_key, key_len);
  native_string_key[key_len] = '\0';

  (*env)->ReleaseByteArrayElements(env,key,java_bytes_key,0);
  // need md5(key)
  return encrypt2(env, clazz, context, content, native_string_key, 0);
}

JNIEXPORT jbyteArray JNICALL Java_com_planet_light2345_seclib_SecService_encrypt2
    (JNIEnv * env, jclass clazz, jobject context, jbyteArray content, jbyteArray p1, jbyteArray p2){
  if(!checkSignature(env,context)){
    return NULL;
  }

  LOGI("encrypt2");
  signed char * java_bytes_p1 = (*env)->GetByteArrayElements(env,p1,0);
  long p1_len = (*env)->GetArrayLength(env, p1);
  char * native_string_p1 = malloc(sizeof(char) * (p1_len + 1));
  memcpy(native_string_p1, java_bytes_p1, p1_len);
  native_string_p1[p1_len] = '\0';

  signed char * java_bytes_p2 = (*env)->GetByteArrayElements(env,p2,0);
  long p2_len = (*env)->GetArrayLength(env, p2);
  char * native_string_p2 = malloc(sizeof(char) * (p2_len + 1));
  memcpy(native_string_p2, java_bytes_p2, p2_len);
  native_string_p2[p2_len] = '\0';

  // strcat: p1 + normalKey + p2
  char *resultTemp = joinChar(native_string_p1, NORMAL_KEY);
  char *result = joinChar(resultTemp, native_string_p2);

  free(native_string_p1);
  free(native_string_p2);
  (*env)->ReleaseByteArrayElements(env,p1,java_bytes_p1,0);
  (*env)->ReleaseByteArrayElements(env,p2,java_bytes_p2,0);
  // need md5(key)
  return encrypt2(env, clazz, context, content, result, 1);
}

JNIEXPORT jbyteArray JNICALL Java_com_planet_light2345_seclib_SecService_decryptIn
    (JNIEnv * env, jclass clazz, jobject context, jbyteArray content){

  if(!checkSignature(env,context)){
    return NULL;
  }

  LOGI("decryptIn");
  signed char * java_bytes = (*env)->GetByteArrayElements(env,content,0);
  long len = (*env)->GetArrayLength(env, content);

  char * native_string = malloc(sizeof(char) * (len + 1));
  memcpy(native_string, java_bytes, len);
  native_string[len] = '\0';

  jbyteArray jbarray = decode(env, native_string, NORMAL_KEY, strlen(NORMAL_KEY));
  free(native_string);
  (*env)->ReleaseByteArrayElements(env,content,java_bytes,0);
  return jbarray;
}

jbyteArray decode(JNIEnv * env, const char *native_string, const char * native_string_key, long key_len) {
  unsigned char * result;

  jbyteArray jbarray;

  size_t decsize = 0;
  int strle = strlen(native_string);
  result = b64_decode_ex(native_string, strle, &decsize);

  int result_arr_len = decsize;

  char result_arr[result_arr_len + 1];
  result_arr[result_arr_len] = '\0';

  int i;
  for(i = 0; i < result_arr_len; i++){
    int k = i % key_len;
    char x = result[i];
    char y = native_string_key[k];
    char z = x ^ y;
    result_arr[i] = z;
  }

  free(result);

  jbarray = (*env)->NewByteArray(env, result_arr_len);
  (*env)->SetByteArrayRegion(env,jbarray, 0, result_arr_len, (jbyte*)result_arr);
  return jbarray;
}

jbyteArray decodeWidthMd5(JNIEnv * env, const char *native_string, const char * native_string_key){
  unsigned char * result;

  jbyteArray jbarray;

  char md5_str[33] = {0};
  md5_str[32] = '\0';
  md5str(native_string_key,strlen(native_string_key),md5_str);

  size_t decsize = 0;
  int strle = strlen(native_string);
  result = b64_decode_ex(native_string, strle, &decsize);

  int result_arr_len = decsize;

  char result_arr[result_arr_len + 1];
  result_arr[result_arr_len] = '\0';

  int i;
  for(i = 0; i < result_arr_len; i++){
    int k = i % 32;
    char x = result[i];
    char y = md5_str[k];
    char z = x ^ y;
    result_arr[i] = z;
  }

  free(result);

  jbarray = (*env)->NewByteArray(env, result_arr_len);
  (*env)->SetByteArrayRegion(env,jbarray, 0, result_arr_len, (jbyte*)result_arr);
  return jbarray;
}

JNIEXPORT jbyteArray JNICALL Java_com_planet_light2345_seclib_SecService_decryptP1
    (JNIEnv * env, jclass clazz, jobject context, jbyteArray content, jbyteArray key){

  if(!checkSignature(env,context)){
    return NULL;
  }

  LOGI("decryptP1");
  signed char * java_bytes = (*env)->GetByteArrayElements(env,content,0);
  long len = (*env)->GetArrayLength(env, content);
  char * native_string = malloc(sizeof(char) * (len + 1));
  memcpy(native_string, java_bytes, len);
  native_string[len] = '\0';

  signed char * java_bytes_key = (*env)->GetByteArrayElements(env,key,0);
  long key_len = (*env)->GetArrayLength(env, key);
  char * native_string_key = malloc(sizeof(char) * (key_len + 1));
  memcpy(native_string_key, java_bytes_key, key_len);
  native_string_key[key_len] = '\0';

  jbyteArray jbarray = decode(env, native_string, native_string_key, key_len);

  free(native_string);
  free(native_string_key);

  (*env)->ReleaseByteArrayElements(env,content,java_bytes,0);
  (*env)->ReleaseByteArrayElements(env,key,java_bytes_key,0);
  return jbarray;
}

JNIEXPORT jbyteArray JNICALL Java_com_planet_light2345_seclib_SecService_decrypt2
    (JNIEnv * env, jclass clazz, jobject context, jbyteArray content, jbyteArray p1, jbyteArray p2){

  if(!checkSignature(env,context)){
    return NULL;
  }

  LOGI("decrypt2");
  signed char * java_bytes_p1 = (*env)->GetByteArrayElements(env,p1,0);
  long p1_len = (*env)->GetArrayLength(env, p1);
  char * native_string_p1 = malloc(sizeof(char) * (p1_len + 1));
  memcpy(native_string_p1, java_bytes_p1, p1_len);
  native_string_p1[p1_len] = '\0';

  signed char * java_bytes_p2 = (*env)->GetByteArrayElements(env,p2,0);
  long p2_len = (*env)->GetArrayLength(env, p2);
  char * native_string_p2 = malloc(sizeof(char) * (p2_len + 1));
  memcpy(native_string_p2, java_bytes_p2, p2_len);
  native_string_p2[p2_len] = '\0';

  // strcat: p1 + normalKey + p2
  char *resultTemp = joinChar(native_string_p1, NORMAL_KEY);
  char *result = joinChar(resultTemp, native_string_p2);

  free(native_string_p1);
  free(native_string_p2);
  (*env)->ReleaseByteArrayElements(env,p1,java_bytes_p1,0);
  (*env)->ReleaseByteArrayElements(env,p2,java_bytes_p2,0);

  signed char * java_bytes = (*env)->GetByteArrayElements(env,content,0);
  long len = (*env)->GetArrayLength(env, content);
  char * native_string = malloc(sizeof(char) * (len + 1));
  memcpy(native_string, java_bytes, len);
  native_string[len] = '\0';

  jbyteArray jbarray = decodeWidthMd5(env, native_string, result);

  free(native_string);
  free(result);

  (*env)->ReleaseByteArrayElements(env,content,java_bytes,0);
  return jbarray;
}

jbyteArray encrypt2
    (JNIEnv * env, jclass clazz, jobject context, jbyteArray content, const char * key, int md5){
  signed char * java_bytes = (*env)->GetByteArrayElements(env,content,0);
  long len = (*env)->GetArrayLength(env, content);

  char * native_string = malloc(sizeof(char) * (len + 1));
  memcpy(native_string, java_bytes, len);
  native_string[len] = '\0';
  int strle = strlen(native_string);

  LOGI("encrypt2");
  jbyteArray jbarray;
  char * result;
  if (md5) {// !=0, md5 to key
    result = salt_b64_encode(native_string,key);
  } else {// no md5
    result = salt_b64_encode_nomd5(native_string,key);
  }

  jbarray = (*env)->NewByteArray(env, strlen(result));
  (*env)->SetByteArrayRegion(env,jbarray, 0, strlen(result), (jbyte*)result);
  free(result);
  free(native_string);

  (*env)->ReleaseByteArrayElements(env,content,java_bytes,0);
  return jbarray;
}

char * salt_b64_encode(const char * content, char * salt ){
    char md5_str[33] = {0};
    md5_str[32] = '\0';
    md5str(salt,strlen(salt),md5_str);
    //LOGI("md5str %s",strlen(md5_str));

    int native_str_len = strlen(content);
   char result_arr[native_str_len + 1];
    result_arr[native_str_len] = '\0';
    int i;
    for(i = 0;i < native_str_len;i++){
        int k = i % 32;
        char x = content[i];
        char y = md5_str[k];
        char z = x ^ y;
        result_arr[i] = z;
    }
    return b64_encode(result_arr,native_str_len);
}

char * salt_b64_encode_nomd5(const char * content, char * salt){
  int salt_len = strlen(salt);
  int native_str_len = strlen(content);
  char result_arr[native_str_len + 1];
  result_arr[native_str_len] = '\0';
  int i;
  for(i = 0;i < native_str_len;i++){
    int k = i % salt_len;
    char x = content[i];
    char y = salt[k];
    char z = x ^ y;
    result_arr[i] = z;
  }
  return b64_encode(result_arr,native_str_len);
}

char * joinChar(char *s1, char *s2) {
  char *result = malloc(strlen(s1)+strlen(s2)+1);//+1 for the zero-terminator
  strcpy(result, s1);
  strcat(result, s2);
  return result;
}
