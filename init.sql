DROP TABLE IF EXISTS `report`;
DROP TABLE IF EXISTS `hidden_board`;
DROP TABLE IF EXISTS `evaluation`;
DROP TABLE IF EXISTS `message`;
DROP TABLE IF EXISTS `chat_room`;
DROP TABLE IF EXISTS `book_mark`;
DROP TABLE IF EXISTS `applied_user`;
DROP TABLE IF EXISTS `board`;
DROP TABLE IF EXISTS `user_exercise`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `alarm`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exercise` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `alarm` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `type` int NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `access_token` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `intro` varchar(255) NOT NULL,
  `nickname` varchar(100) NOT NULL,
  `oauth_id` varchar(100) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'ACTIVE',
  `username` varchar(100) NOT NULL,
  `address_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname` (`nickname`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `access_token` (`access_token`),
  UNIQUE KEY `oauth_id` (`oauth_id`),
  KEY `FK_User_addressId` (`address_id`),
  CONSTRAINT `FK_User_addressId` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_exercise` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_UserExercise_categoryId` (`category_id`),
  KEY `FK_UserExercise_userId` (`user_id`),
  CONSTRAINT `FK_UserExercise_categoryId` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FK_UserExercise_userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `board` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `place` varchar(255) NOT NULL,
  `recruit_count` int NOT NULL,
  `status` int NOT NULL DEFAULT 'RECRUITING',
  `title` varchar(255) NOT NULL,
  `address_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `starts_at` datetime(6) NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_Board_addressId` (`address_id`),
  KEY `FK_Board_categoryId` (`category_id`),
  KEY `FK_Board_tagId` (`tag_id`),
  KEY `FK_Board_userId` (`user_id`),
  CONSTRAINT `FK_Board_categoryId` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FK_Board_addressId` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FK_Board_userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_Board_tagId` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `applied_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` varchar(255) NOT NULL DEFAULT 'PENDING',
  `board_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_AppliedUser_boardId` (`board_id`),
  KEY `FK_AppliedUser_userId` (`user_id`),
  CONSTRAINT `FK_AppliedUser_userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_AppliedUser_boardId` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `book_mark` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `board_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_BookMark_boardId` (`board_id`),
  KEY `FK_BookMark_userId` (`user_id`),
  CONSTRAINT `FK_BookMark_boardId` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`),
  CONSTRAINT `FK_BookMark_userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `chat_room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` varchar(255) NOT NULL DEFAULT 'ACTIVE',
  `board_id` bigint NOT NULL,
  `guest_id` bigint NOT NULL,
  `host_id` bigint NOT NULL,
  `guest_exited` boolean NOT NULL DEFAULT FALSE,
  `host_exited` boolean NOT NULL DEFAULT FALSE,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_ChatRoom_boardId` (`board_id`),
  KEY `FK_ChatRoom_guestId` (`guest_id`),
  KEY `FK_ChatRoom_hostId` (`host_id`),
  CONSTRAINT `FK_ChatRoom_guestId` FOREIGN KEY (`guest_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_ChatRoom_hostId` FOREIGN KEY (`host_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_ChatRoom_boardId` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL,
  `is_guest_read` boolean NOT NULL DEFAULT FALSE,
  `is_host_read` boolean NOT NULL DEFAULT FALSE,
  `type` varchar(255) NOT NULL,
  `chat_room_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `message_id` int NOT NULL DEFAULT '0',
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_Message_chatRoomId` (`chat_room_id`),
  KEY `FK_Message_senderId` (`sender_id`),
  CONSTRAINT `FK_Message_chatRoomId` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`id`),
  CONSTRAINT `FK_Message_senderId` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `evaluation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `evaluate_id` bigint NOT NULL,
  `evaluated_id` bigint NOT NULL,
  `is_dislike` boolean NOT NULL DEFAULT FALSE,
  `is_like` boolean NOT NULL DEFAULT FALSE,
  `board_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_Evaluation_boardId` (`board_id`),
  CONSTRAINT `FK_Evaluation_boardId` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hidden_board` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `board_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_HiddenBoard_boardId` (`board_id`),
  KEY `FK_HiddenBoard_userId` (`user_id`),
  CONSTRAINT `FK_HiddenBoard_userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_HiddenBoard_boardId` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NULL,
  `type` int NOT NULL,
  `reported_id` bigint NOT NULL,
  `reporter_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (`id`),
  KEY `FK_Report_reportedId` (`reported_id`),
  KEY `FK_Report_reporterId` (`reporter_id`),
  CONSTRAINT `FK_Report_reportedId` FOREIGN KEY (`reported_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_Report_reporterId` FOREIGN KEY (`reporter_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
