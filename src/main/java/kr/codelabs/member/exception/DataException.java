package kr.codelabs.member.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataException extends RuntimeException {

    @Getter
    private String code;

    @Getter
    private String message;

    public DataException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
