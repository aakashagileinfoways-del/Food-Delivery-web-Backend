// package com.fooddelivery.common;
// import com.fasterxml.jackson.annotation.JsonPropertyOrder;
// import com.fooddelivery.dto.response.AuthResponse;

// @JsonPropertyOrder({"statusCode","success", "message", "data"})
// public class ApiResponse<T> {

//     private boolean success;
//     private int statusCode; 
//     private String message;
//     private T data;

//     public ApiResponse(boolean success, String message, T data , int statusCode) {
//         this.success = success;
//         this.statusCode = statusCode;
//         this.message = message;
//         this.data = data;
//     }

//     public boolean isSuccess() {
//         return success;
//     }

//     public String getMessage() {
//         return message;
//     }

//     public T getData() {
//         return data;
//     }

//     public int getStatusCode() {
//         return statusCode;
//     }

//     public static AuthResponse success(AuthResponse authResponse) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'success'");
//     }






// }




package com.fooddelivery.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"statusCode", "success", "message", "data", "timestamp"})
public class ApiResponse<T> {

    private boolean success;
    private int statusCode;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(boolean success, int statusCode, String message, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        // this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}