package hu.kalmancheysandor.application.dominion.server.game.exceptionhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionBox {
        private String fullName;
        private Exception exception;
}
