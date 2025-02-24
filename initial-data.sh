#!/bin/bash

# array with data
json_array='[
  {"number": "RR10029.11001", "name": "Body", "type": "PART", "status": "DESIGN"},
  {"number": "RR10209.11002", "name": "Spool", "type": "PART", "status": "DESIGN"},
  {"number": "RR10309.11003", "name": "Valve", "type": "PART", "status": "DESIGN"},
  {"number": "OV.10049.56.44", "name": "Ring", "type": "VZK", "status": "TEST"},
  {"number": "SCS.20309.56.10", "name": "Screw", "type": "VZK", "status": "TEST"},
  {"number": "HEDX.532009.52.44", "name": "Hex", "type": "VZK", "status": "TEST"}
]';

# URL, на который нужно отправлять данные
# url="http://archive-svc.prodms.svc:8080/api/v1/units"
url="http://localhost:8080/api/v1/units"

# Используем jq для итерации по массиву JSON
echo "$json_array" | jq -c '.[]' | while read -r item; do
    # Отправляем POST-запрос с элементом в качестве тела
    response=$(curl -s -X POST "$url" \
        -H "Content-Type: application/json" \
        -d "$item")

#    response=$(wget --header "Content-Type: application/json" --post-data "$item" $url)

    echo "Ответ от сервера: $response"
done