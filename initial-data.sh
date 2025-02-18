#!/bin/bash

# array with data
json_array='[
  {"number": "RGR100.11001", "name": "Body", "type": "PART", "status": "DESIGN"},
  {"number": "RGR100.11002", "name": "Spool", "type": "PART", "status": "DESIGN"},
  {"number": "RGR100.11003", "name": "Valve", "type": "PART", "status": "DESIGN"},
  {"number": "OR.100.56.44", "name": "Ring", "type": "VZK", "status": "TEST"},
  {"number": "SC.200.56.10", "name": "Screw", "type": "VZK", "status": "TEST"},
  {"number": "HEX.500.52.44", "name": "Hex", "type": "VZK", "status": "TEST"}
]';

# URL, на который нужно отправлять данные
url="http://localhost:8070/api/v1/units"

# Используем jq для итерации по массиву JSON
echo "$json_array" | jq -c '.[]' | while read -r item; do
    # Отправляем POST-запрос с элементом в качестве тела
    response=$(curl -s -X POST "$url" \
        -H "Content-Type: application/json" \
        -d "$item")

    echo "Ответ от сервера: $response"
done