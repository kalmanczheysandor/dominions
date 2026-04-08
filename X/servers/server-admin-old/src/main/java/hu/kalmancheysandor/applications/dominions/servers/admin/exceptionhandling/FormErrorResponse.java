package hu.kalmancheysandor.applications.dominions.servers.admin.exceptionhandling;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormErrorResponse {
    private List<ErrorItem> errors  = new ArrayList<ErrorItem>();
    public void addErrorItem(ErrorItem error ) {
        errors.add(error);
    }
}
