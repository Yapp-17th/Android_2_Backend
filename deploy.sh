sudo docker-compose down
sudo docker image prune
git pull
./gradlew clean build
sudo docker-compose build --no-cache
sudo docker-compose up -d
