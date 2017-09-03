#!/bin/bash
# Set environment variables
echo ""
source 00_setEnv.sh
echo "---------------Set up enviroment variables----------"
echo "Exporting {usr_glassfish_home_prefix} = "${usr_glassfish_home_prefix}
echo "Exporting {usr_glassfish_deployed_apps_home} = "${usr_glassfish_deployed_apps_home}
echo "Exporting {usr_webapp_group_id} = "${usr_webapp_group_id}
echo "Exporting {usr_webapp_artifact_id} = "${usr_webapp_artifact_id}
echo "Exporting {usr_webapp_name} = "${usr_webapp_name}
echo "----------------------------------------------------"
echo ""

# Generate project from standard archetype
echo "---------------Generate project from standard archetype----------"
mvn archetype:generate -DgroupId=${usr_webapp_group_id} -DartifactId=${usr_webapp_artifact_id} -DarchetypeArtifactId=webapp-javaee7 -DarchetypeGroupId=org.codehaus.mojo.archetypes  -DarchetypeVersion=1.1 -Dversion=1.0-SNAPSHOT --batch-mode
tree
echo ""

# Overwrite pom file
echo "---------------Modifying pom.xml for Glassfish server----------"
cp pom_foil.xml ${usr_webapp_artifact_id}/pom.xml

echo "---------------First deploy----------"
cd ${usr_webapp_artifact_id}
mvn deploy
echo "Archived app-------------------------"
tree ${usr_glassfish_home_prefix}/${usr_glassfish_deployed_apps_home}/snapshot/${usr_webapp_group_id}/${usr_webapp_artifact_id}
echo ""

# Deploy
