cmd /c (start /b /d "E:\BKP_GENILDO_OPPY\WORKSPACES\workspace-desafio\Hdoismemorytest" "%cd%" "C:\Program Files\Git\git-bash.exe" -i -c "git add .;git commit -m "%date%"; git push https://github.com/genildoaraujo77/desafio-redis.git master -f; exec bash") && pause