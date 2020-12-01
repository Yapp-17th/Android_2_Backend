./gradlew :$1:clean :$1:build
sudo docker-compose build --no-cache $1 && sudo docker-compose up -d $1
