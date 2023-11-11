package com.ncinga.timer.dtos.responseDto;

import com.ncinga.timer.utilities.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String message;
    private Object data;
    private Object error;
    private ResponseCode responseCode;

    public static ResponseDto getInstance(String message, Object data, Object error, ResponseCode responseCode){
        ResponseDto responseDto = new ResponseDto(message, data, error, responseCode);
        return responseDto;
    }

}
