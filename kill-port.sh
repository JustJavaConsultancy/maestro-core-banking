#!/bin/bash
sudo netstat -tlnp | grep 8084 | awk '{print $3":"$4}' | xargs sudo fuser -k 8084/tcp
sudo netstat -tlnp | grep 8004 | awk '{print $3":"$4}' | xargs sudo fuser -k 8004/tcp