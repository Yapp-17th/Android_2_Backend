sudo docker-compose down
sudo docker rmi $(sudo docker images -f "dangling=true" -q)
git pull
./gradlew clean build
sudo docker-compose build --no-cache
sudo docker-compose up -d
