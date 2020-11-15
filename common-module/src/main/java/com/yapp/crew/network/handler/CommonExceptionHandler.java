package com.yapp.crew.network.handler;

import com.yapp.crew.domain.errors.AddressNotFoundException;
import com.yapp.crew.domain.errors.AlreadyAppliedException;
import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.BoardTimeInvalidException;
import com.yapp.crew.domain.errors.CategoryNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.EvaluateImpossibleException;
import com.yapp.crew.domain.errors.GuestApplyNotFoundException;
import com.yapp.crew.domain.errors.InactiveUserException;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.MessageNotFoundException;
import com.yapp.crew.domain.errors.SuspendedUserException;
import com.yapp.crew.domain.errors.TagNotFoundException;
import com.yapp.crew.domain.errors.UnAuthorizedEventException;
import com.yapp.crew.domain.errors.UserDuplicateFieldException;
import com.yapp.crew.domain.errors.UserNotFoundException;
import com.yapp.crew.domain.errors.WrongGuestException;
import com.yapp.crew.domain.errors.WrongHostException;
import com.yapp.crew.domain.type.ResponseType;
import com.yapp.crew.network.model.SimpleResponse;
import java.sql.SQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@ControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(value = InternalServerError.class)
	public ResponseEntity<?> handleInternalServerException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = InternalServerErrorException.class)
	public ResponseEntity<?> handleInternalServerErrorException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = InvalidRequestBodyException.class)
	public ResponseEntity<?> handleInvalidRequestBodyException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_REQUEST_BODY);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MissingRequestHeaderException.class)
	public ResponseEntity<?> handleMissingRequestHeaderException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.AUTHORIZATION_HEADER_REQUIRED);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_METHOD);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = SuspendedUserException.class)
	public ResponseEntity<?> handleSuspendedUserException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.SUSPENDED_USER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = InactiveUserException.class)
	public ResponseEntity<?> handleInactiveUserException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INACTIVE_USER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.USER_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = BoardNotFoundException.class)
	public ResponseEntity<?> handleBoardNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.BOARD_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = ChatRoomNotFoundException.class)
	public ResponseEntity<?> handleChatRoomNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.CHATROOM_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = TagNotFoundException.class)
	public ResponseEntity<?> handleTagNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.TAG_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = CategoryNotFoundException.class)
	public ResponseEntity<?> handleCategoryNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.CATEGORY_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = AddressNotFoundException.class)
	public ResponseEntity<?> handleAddressNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.ADDRESS_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MessageNotFoundException.class)
	public ResponseEntity<?> handleMessageNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.MESSAGE_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = WrongHostException.class)
	public ResponseEntity<?> handleWrongHostException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.WRONG_CHATROOM_HOST);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = WrongGuestException.class)
	public ResponseEntity<?> handleWrongGuestException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.WRONG_CHATROOM_GUEST);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = AlreadyApprovedException.class)
	public ResponseEntity<?> handleAlreadyApprovedException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.ALREADY_APPROVED);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = AlreadyAppliedException.class)
	public ResponseEntity<?> handleAlreadyAppliedException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.ALREADY_APPLIED);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = GuestApplyNotFoundException.class)
	public ResponseEntity<?> handleGuestApplyNotFoundException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.GUEST_APPLY_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_REQUEST_PARAM);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = UserDuplicateFieldException.class)
	public ResponseEntity<?> handleUserDuplicateFieldException(Exception ex) {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.SIGN_UP_DUPLICATE, ex.getMessage());
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = SQLException.class)
	public ResponseEntity<?> handleSQLException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleHttpRequestMethodNotSupportedException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_METHOD);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = BoardTimeInvalidException.class)
	public ResponseEntity<?> handleBoardTimeInvalidException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.BOARD_TIME_INVALID);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = UnAuthorizedEventException.class)
	public ResponseEntity<?> handleUnAuthorizedEventException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.UNAUTHORIZED, ResponseType.UNAUTORIZED_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = EvaluateImpossibleException.class)
	public ResponseEntity<?> handleEvaluateImpossibleException() {
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.UNAUTHORIZED, ResponseType.EVALUATE_IMPOSSIBLE);
		return ResponseEntity.ok().body(responseBody);
	}
}
