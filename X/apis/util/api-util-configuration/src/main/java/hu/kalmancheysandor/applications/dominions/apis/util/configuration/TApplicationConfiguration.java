package hu.kalmancheysandor.applications.dominions.apis.util.configuration;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public abstract class TApplicationConfiguration implements IApplicationConfiguration {
    private SiteServer siteServer;
    private AdminServer adminServer;
    private GameServer gameServer;
    private LizServer lizServer;


}
