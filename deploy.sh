git pull
sudo docker-compose down
sudo docker image prune -f
./gradlew clean build
sudo docker-compose build --no-cache
sudo docker-compose up -d
