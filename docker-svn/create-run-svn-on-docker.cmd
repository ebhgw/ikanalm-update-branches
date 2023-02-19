rem cd to correct directory
cd D:\Workspace\rma\ikan\ikan-phase-lib-2\docker-svn
rem Create the image
docker build . -t ebomitali/local-svn-server
rem run the image -p external/host:internal/container)
docker run -d -p 8088:80 -p 3690:3690 --name svn-server ebomitali/local-svn-server