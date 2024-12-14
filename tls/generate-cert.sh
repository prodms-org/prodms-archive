#!/bin/bash

# generate domain key-pair
openssl genrsa -des3 -out ./prodms-archive.key -passout pass:root 2048

# generate csr for before generated key-pair
## TODO: need auto compliting fileds (file or inline)
openssl req -key ./prodms-archive.key -passin pass:root -new -out prodms-archive.csr -passout pass:root

# openssl x509 -signkey prodms-archive.key -passin pass:root -in prodms-archive.csr -req -days 365 -out prodms-archive.crt
