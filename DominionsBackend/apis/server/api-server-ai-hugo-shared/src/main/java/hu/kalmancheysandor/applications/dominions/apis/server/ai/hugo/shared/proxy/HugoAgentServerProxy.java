package hu.kalmancheysandor.applications.dominions.apis.server.ai.hugo.shared.proxy;

import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.ai.common.dto.AiDecisionResponse;
import hu.kalmancheysandor.applications.dominions.apis.server.common.configuration.feign.PrimaryFeignProxyConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "server-ai-hugo-agent",
        contextId = "HugoAgentServerProxy",
        configuration = PrimaryFeignProxyConfig.class
//        path = "/concept"
)
public interface HugoAgentServerProxy {
    @PostMapping("/call")
    public AiDecisionResponse generateResponse(@RequestBody AiDecisionRequest request);
}
