#ifndef MD5_H
#define MD5_H

#include <stdint.h>

void md5(const uint8_t *initial_msg, size_t initial_len, uint8_t *digest);

void md5str(const uint8_t *initial_msg, size_t initial_len,char * md5_str);

#endif