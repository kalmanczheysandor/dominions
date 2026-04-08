package hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.error.GeneralErrorResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.dto.failure.GeneralFailureResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy.GeneralErrorResponseProxyException;
import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy.GeneralFailureResponseProxyException;
import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy.BrokenResponseProxyException;
import hu.kalmancheysandor.applications.dominions.apis.server.common.service.exception.proxy.NotParseableResponseProxyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@Slf4j
public class ProxyResponseDecoder implements ErrorDecoder {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream bodyContentStream = response.body().asInputStream()) {
            String content = convertInputStreamToString(bodyContentStream);
            if (isParseable(content, GeneralFailureResponse.class )) {
                throw new GeneralFailureResponseProxyException(objectMapper.readValue(content, GeneralFailureResponse.class));
            }
            else if (isParseable(content, GeneralErrorResponse.class )) {
                throw new GeneralErrorResponseProxyException(objectMapper.readValue(content, GeneralErrorResponse.class));
            }
            else {
                throw new NotParseableResponseProxyException(content);
                //return new Default().decode(methodKey, response);
            }
        } catch (IOException e) {
            throw new BrokenResponseProxyException();
        }
    }

    private boolean isParseable(String content, Class clazz) {
        try {
            objectMapper.readValue(content, clazz);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    private static String convertInputStreamToString(InputStream stream) {
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
}
