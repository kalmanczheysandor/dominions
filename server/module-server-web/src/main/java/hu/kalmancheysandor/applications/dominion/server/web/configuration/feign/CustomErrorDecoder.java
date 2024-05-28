package hu.kalmancheysandor.applications.dominion.server.web.configuration.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import hu.kalmancheysandor.application.dominion.server.game.exceptionhandling.ErrorDetails;
import hu.kalmancheysandor.application.dominion.server.game.exceptionhandling.ExceptionBox;
import hu.kalmancheysandor.applications.dominion.server.web.proxy.ProxyException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.awt.color.ProfileDataException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
//        System.out.println("FEIGN-Decoder");
        ObjectMapper mapper = new ObjectMapper();
        ExceptionMessage message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            String content = inputStreamToString(bodyIs);
//            System.out.println("FEIGN-Decoder:::: Method:" + methodKey + ", Content:" +content);
            if (isParseable(content, ErrorDetails.class)) {
                //System.out.println("Is parseable");
                ErrorDetails errorDetails = mapper.readValue(content, ErrorDetails.class);
                return new ProxyException(errorDetails);
            }
        } catch (IOException e) {
//            System.out.println("E1");
            System.out.println(e.getMessage());
        }


//        System.out.println("FEIGN-Decoder:::: Return default");

        return new Default().decode(methodKey, response);
    }
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        System.out.println("FEIGN-Decoder");
//        ObjectMapper mapper = new ObjectMapper();
//        ExceptionMessage message = null;
//        try (InputStream bodyIs = response.body().asInputStream()) {
//            String content = inputStreamToString(bodyIs);
//            System.out.println("FEIGN-Decoder:::: Method:" + methodKey + ", Content:" +content);
//            if (!isParseable(content, ExceptionBox.class)) {
//                System.out.println("Is parseable");
//                ExceptionBox exceptionBox = mapper.readValue(content, ExceptionBox.class);
//                System.out.println("Exception box:" + exceptionBox);
//                String className = exceptionBox.getFullName();
//                System.out.println("Class name:" + className);
//                Exception thrownExceptionObj = exceptionBox.getException();
//
//                Class thrownExceptionClass = Class.forName(className);
//                if (thrownExceptionClass.isInstance(thrownExceptionObj)) {
//                    Object theException = (Class) thrownExceptionClass.cast(thrownExceptionObj);
//                    System.out.println("FEIGN-Decoder:::: Before return");
//                    return (Class)theException;
//                }
//
//            }
//
//
//        } catch (IOException e) {
//            System.out.println("E1");
//            System.out.println(e.getMessage());
//        } catch (ClassNotFoundException e) {
//            System.out.println("E2");
//            throw new RuntimeException(e);
//        }
//
//
//        System.out.println("FEIGN-Decoder:::: Return default");
//
//        return new Default().decode(methodKey, response);
//    }
    private static boolean isParseable(String content, Class clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readValue(content, clazz);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    private static Object parse(String content, Class clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, clazz);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    private static String inputStreamToString(InputStream stream) {
        // Wrap the input stream with InputStreamReader to read characters
        InputStreamReader inputStreamReader = new InputStreamReader(stream);

        // Wrap InputStreamReader with BufferedReader for efficient reading
        BufferedReader reader = new BufferedReader(inputStreamReader);

        // Read content line by line
        String line;
        String output = "";
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
                output += line;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return output;
    }


//    @Override
//    public Exception decode(String methodKey, Response response) {
//        if (response.status() >= 400 && response.status() <= 499) {
//            // Do not wrap client errors (4xx) in FeignException, let them propagate as-is
//            return FeignException.errorStatus(methodKey, response);
//        }
//        // For all other status codes, use Feign's default error handling
//        return defaultDecoder().decode(methodKey, response);
//    }
//
//    // Default error decoder provided by Feign
//    private ErrorDecoder defaultDecoder() {
//        return new Default();
//    }
}
