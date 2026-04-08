package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.proxy;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="server-ai-liz-agent")
public interface LizAgentServerProxy {
    @PostMapping("/call")
    public AiDecisionResponse generateResponse(@RequestBody AiDecisionRequest request);
}
