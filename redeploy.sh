#/bin/sh
DIR="Location"
GITHUB_NAME=""
GITHUB_TOKEN=""

cd $DIR
rm -rf $DIR/redeploy/

git clone https://$GITHUB_NAME:$GITHUB_TOKEN@github.com/Perry-Cox.git redeploy
cd redeploy

bash gradlew :shadowJar

if [ $? -eq 0 ]; then
        rm $DIR/DragonSharksBot.jar
        mv $DIR/redeploy/build/libs/"Perry Cox-GIT_DEPLOY-all.jar" $DIR/PerryCox.jar
else
        echo FAILED TO COMPILE
fi