# obsolete, moved to Dockerbuild
svnmucc -U http://localhost:80/svn/TEST \
-m "Add structure to Test Repository" \
--no-auth-cache --non-interactive \
--username test --password TestP4ss \
-- \
mkdir 7MCPMOB1 \
mkdir 7MCPMOB1/branches \
mkdir 7MCPMOB1/branches/correttiva \
mkdir 7MCPMOB1/branches/correttiva/xmodel \
mkdir 7MCPMOB1/branches/evolutiva \
mkdir 7MCPMOB1/Reference \
mkdir 7MCPMOB1/TagVersioni