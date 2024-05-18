package hu.kalmanczheysandor.applications.dominion.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldError {
    private String field;
    private String errorMessage;
}
