#/bin/sh
DIR="Location"

cd $DIR
rm -rf $DIR/redeploy/

git clone https://github.com/DenuxPlays/Perry-Cox.git redeploy
cd redeploy

bash gradlew :shadowJar

if [ $? -eq 0 ]; then
        rm $DIR/PerryCox.jar
        mv $DIR/redeploy/build/libs/"Perry Cox-GIT_DEPLOY-all.jar" $DIR/PerryCox.jar
else
        echo FAILED TO COMPILE
fi