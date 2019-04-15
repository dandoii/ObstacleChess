#!/usr/bin/env bash
clear

echo "----------Starting Script----------"
echo
echo "----------Generating Output Files----------"

begin_val=1
end_val=1

read begin_val
read end_val

javac ObstacleCL.java

for ((i=$begin_val;i<=$end_val;i+=1))
do
    echo ------------------ test{$i.board,$i.game,$i.out} ------------------------------------

    java ObstacleCL GitTest/test{$i.board,$i.game,$i.out}
    echo
    echo
    sleep 1
done





#xvar="hello"

#if [ $xvar == "hello" ]
#then
#    echo $xvar
#else
#    echo "false"
#fi
#
#echo -e "Enter the name of the file : \c"
#read file_name
#
#if [ -e $file_name ]
#then
#    echo "$file_name found"
#else
#    echo "$file_name not found"
#fi
