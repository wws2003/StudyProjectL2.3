#!/bin/bash
read -p "Commit message: " msg
git commit -m "$msg"
./21_git_log_recent.sh 2
