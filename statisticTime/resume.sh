#!/bin/bash

# 设置PYTHON文件路径
PY_FILE="./testXlib.py"

# 循环运行PYTHON脚本
while true; do
  # 运行PYTHON脚本
  python3 ${PY_FILE}
  
  # 获取PYTHON进程ID
  PID=$(pgrep -f ${PY_FILE})
  
  # 等待PYTHON进程结束
  wait ${PID}
  
  # 打印信息，然后重新运行PYTHON脚本
  echo "Python script ended at $(date), restarting..."
  sleep 1
done