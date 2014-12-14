mkdir build
git clone https://github.com/AwesomeSauceMods/AwesomeSauceCore.git build/src
git clone https://github.com/AwesomeSauceMods/CoolInfoStuff.git
mv CoolInfoStuff/* build
cd build
mkdir RnDTech
cd OpenAutomation
git clone https://github.com/AwesomeSauceMods/OpenAutomation.git src
cd ..
cd RnDTech
git clone https://github.com/AwesomeSauceMods/RnDTech.git src
cd ..
bash gradlew setupCiWorkspace
bash gradlew OpenAutomation:build