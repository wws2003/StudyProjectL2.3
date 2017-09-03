Steps to statically deploy (and also archive) a J2EE7 skeletonized webapp into local Glassfish server

Step 1
- Modify 00_setEnv.sh respecting local paths of Glassfish server, project group id, project artifact id, etc.

Step 2
- Run 01_setupAll.sh

Step 3 (optional)
- In the case the project created should be opened in an IDE, modify the environment variables set in pom.xml to the actual values so that the IDE can recognize to avoid errors on IDE.
