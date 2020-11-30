package com.yapp.crew.network.handler;

import com.yapp.crew.domain.errors.AddressNotFoundException;
import com.yapp.crew.domain.errors.AlreadyAppliedException;
import com.yapp.crew.domain.errors.AlreadyApprovedException;
import com.yapp.crew.domain.errors.AlreadyExitedException;
import com.yapp.crew.domain.errors.BoardNotFoundException;
import com.yapp.crew.domain.errors.BoardTimeInvalidException;
import com.yapp.crew.domain.errors.CannotApplyException;
import com.yapp.crew.domain.errors.CannotApproveException;
import com.yapp.crew.domain.errors.CannotDisapproveException;
import com.yapp.crew.domain.errors.CategoryNotFoundException;
import com.yapp.crew.domain.errors.ChatRoomNotFoundException;
import com.yapp.crew.domain.errors.EvaluateImpossibleException;
import com.yapp.crew.domain.errors.GuestApplyNotFoundException;
import com.yapp.crew.domain.errors.InactiveUserException;
import com.yapp.crew.domain.errors.InternalServerErrorException;
import com.yapp.crew.domain.errors.InvalidRequestBodyException;
import com.yapp.crew.domain.errors.MessageNotFoundException;
import com.yapp.crew.domain.errors.ReportCodeNotFoundException;
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
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@Slf4j(topic = "Common Exception Handler")
@ControllerAdvice
public class CommonExceptionHandler {

	@MessageExceptionHandler
	public ResponseEntity<?> handleMessageException(Exception ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = InternalServerError.class)
	public ResponseEntity<?> handleInternalServerException() {
		log.error("Internal server exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = InternalServerErrorException.class)
	public ResponseEntity<?> handleInternalServerErrorException() {
		log.error("Internal server error exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = InvalidRequestBodyException.class)
	public ResponseEntity<?> handleInvalidRequestBodyException(InvalidRequestBodyException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_REQUEST_BODY);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MissingRequestHeaderException.class)
	public ResponseEntity<?> handleMissingRequestHeaderException() {
		log.error("Missing request header exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.AUTHORIZATION_HEADER_REQUIRED);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors()
				.forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
		log.error("Method argument not valid exception: " + errors);

		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_REQUEST_BODY);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = SuspendedUserException.class)
	public ResponseEntity<?> handleSuspendedUserException(SuspendedUserException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.SUSPENDED_USER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = InactiveUserException.class)
	public ResponseEntity<?> handleInactiveUserException(InactiveUserException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.FORBIDDEN, ResponseType.INACTIVE_USER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.USER_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = BoardNotFoundException.class)
	public ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.BOARD_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = ChatRoomNotFoundException.class)
	public ResponseEntity<?> handleChatRoomNotFoundException(ChatRoomNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.CHATROOM_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = TagNotFoundException.class)
	public ResponseEntity<?> handleTagNotFoundException(TagNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.TAG_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = CategoryNotFoundException.class)
	public ResponseEntity<?> handleCategoryNotFoundException(CategoryNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.CATEGORY_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = AddressNotFoundException.class)
	public ResponseEntity<?> handleAddressNotFoundException(AddressNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.ADDRESS_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MessageNotFoundException.class)
	public ResponseEntity<?> handleMessageNotFoundException(MessageNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.MESSAGE_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = WrongHostException.class)
	public ResponseEntity<?> handleWrongHostException(WrongHostException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.WRONG_CHATROOM_HOST);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = WrongGuestException.class)
	public ResponseEntity<?> handleWrongGuestException(WrongGuestException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.WRONG_CHATROOM_GUEST);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = AlreadyExitedException.class)
	public ResponseEntity<?> handleAlreadyExitedException(AlreadyExitedException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.ALREADY_EXITED);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = AlreadyApprovedException.class)
	public ResponseEntity<?> handleAlreadyApprovedException(AlreadyApprovedException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.ALREADY_APPROVED);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = AlreadyAppliedException.class)
	public ResponseEntity<?> handleAlreadyAppliedException(AlreadyAppliedException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.ALREADY_APPLIED);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = CannotApplyException.class)
	public ResponseEntity<?> handleCannotApplyException(CannotApplyException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.CANNOT_APPLY);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = CannotApproveException.class)
	public ResponseEntity<?> handleCannotApproveException(CannotApproveException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.CANNOT_APPROVE);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = CannotDisapproveException.class)
	public ResponseEntity<?> handleCannotDisapproveException(CannotDisapproveException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.CANNOT_DISAPPROVE);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = GuestApplyNotFoundException.class)
	public ResponseEntity<?> handleGuestApplyNotFoundException(GuestApplyNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.GUEST_APPLY_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException() {
		log.error("Missing Servlet Request Param Exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_REQUEST_PARAM);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = UserDuplicateFieldException.class)
	public ResponseEntity<?> handleUserDuplicateFieldException(Exception ex) {
		log.error("User Duplicate Field Exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.SIGN_UP_DUPLICATE, ex.getMessage());
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = SQLException.class)
	public ResponseEntity<?> handleSQLException() {
		log.error("SQL Exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, ResponseType.INTERNAL_SERVER_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleHttpRequestMethodNotSupportedException() {
		log.error("HTTP Request Method Not Supported Exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.INVALID_METHOD);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = BoardTimeInvalidException.class)
	public ResponseEntity<?> handleBoardTimeInvalidException() {
		log.error("Board time invalid exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.BAD_REQUEST, ResponseType.BOARD_TIME_INVALID);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = UnAuthorizedEventException.class)
	public ResponseEntity<?> handleUnAuthorizedEventException() {
		log.error("Unauthorized event exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.UNAUTHORIZED, ResponseType.UNAUTHORIZED_FAIL);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = EvaluateImpossibleException.class)
	public ResponseEntity<?> handleEvaluateImpossibleException() {
		log.error("Evaluate impossible exception");
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.UNAUTHORIZED, ResponseType.EVALUATE_IMPOSSIBLE);
		return ResponseEntity.ok().body(responseBody);
	}

	@ExceptionHandler(value = ReportCodeNotFoundException.class)
	public ResponseEntity<?> handleReportCodeNotFoundException(ReportCodeNotFoundException ex) {
		log.error(ex.getMessage());
		SimpleResponse responseBody = SimpleResponse.fail(HttpStatus.NOT_FOUND, ResponseType.REPORT_CODE_NOT_FOUND);
		return ResponseEntity.ok().body(responseBody);
	}
}
