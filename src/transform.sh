#! /bin/bash

echo "0: $0"
echo "1: $1"

for file in $(ls)
do
  if [ "$file" -ne "$0" ]
  then
    #printf "$file\n"
    printf 'a'
  fi
done
