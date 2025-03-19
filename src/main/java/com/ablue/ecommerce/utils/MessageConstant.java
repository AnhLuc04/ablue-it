package com.ablue.ecommerce.utils;

public class MessageConstant {

    // AUTHENTICATION & AUTHORIZATION
    public static final String UNAUTHORIZED = "Unauthorized"; // Không có quyền truy cập (401)
    public static final String FORBIDDEN = "Forbidden"; // Bị chặn truy cập (403)
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ACCOUNT_LOCKED = "Account is locked";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String TOKEN_REQUIRED = "Token is required";
    public static final String SESSION_EXPIRED = "Session expired, please log in again";

    // USER & ACCOUNT
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String PASSWORD_MISMATCH = "Passwords do not match";
    public static final String PASSWORD_TOO_WEAK = "Password is too weak";
    public static final String PASSWORD_INCORRECT = "Incorrect password";
    public static final String PASSWORD_MATCH_OLD_PASSWORD = "Please enter new password";
    public static final String EMAIL_NOT_VERIFIED = "Email is not verified";
    public static final String PHONE_NOT_VERIFIED = "Phone number is not verified";
    public static final String PHONE_NUMBER_ALREADY_EXISTED = "Phone number already exists";
    public static final String INVALID_PHONE_NUMBER = "Invalid phone number";

    // PERMISSIONS & ACCESS
    public static final String ACCESS_DENIED = "Access denied";
    public static final String INSUFFICIENT_PERMISSIONS = "Insufficient permissions";
    public static final String INVALID_ACCESS = "Invalid access attempt";

    // DATA & REQUEST
    public static final String INVALID_INPUT = "Invalid input";
    public static final String DATA_NOT_FOUND = "Data not found";
    public static final String OPERATION_FAILED = "Operation failed";
    public static final String DUPLICATE_ENTRY = "Duplicate entry";
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String BAD_REQUEST = "Bad request";
    public static final String MISSING_REQUIRED_FIELDS = "Missing required fields";
    public static final String INVALID_FILE_FORMAT = "Invalid file format";
    public static final String FILE_UPLOAD_FAILED = "File upload failed";
    public static final String ID_MUST_BE_GREATER_0 = "Id must be greater 0";

    // SYSTEM & SERVER
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String SERVICE_UNAVAILABLE = "Service is currently unavailable";
    public static final String TIMEOUT_ERROR = "Request timed out";


    // AUTHENTICATION & AUTHORIZATION
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String TOKEN_REFRESHED = "Token refreshed successfully";
    public static final String USERNAME_AND_PASSWORD_INCORRECT = "Username and password incorrect";

    // USER & ACCOUNT
    public static final String REGISTER_SUCCESS = "Registration successful";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successful";
    public static final String PASSWORD_UPDATED = "Password updated successfully";
    public static final String EMAIL_VERIFIED = "Email verification successful";
    public static final String PROFILE_UPDATED = "Profile updated successfully";

    // DATA & REQUEST
    public static final String OPERATION_SUCCESS = "Operation completed successfully";
    public static final String DATA_RETRIEVED = "Data retrieved successfully";
    public static final String DATA_SAVED = "Data saved successfully";
    public static final String DATA_UPDATED = "Data updated successfully";
    public static final String DATA_DELETED = "Data deleted successfully";
    public static final String FILE_UPLOADED = "File uploaded successfully";
    public static final String FILE_DELETED = "File deleted successfully";

    // SYSTEM & SERVER
    public static final String SERVICE_AVAILABLE = "Service is available";
    public static final String CONNECTION_SUCCESS = "Connection successful";


}
