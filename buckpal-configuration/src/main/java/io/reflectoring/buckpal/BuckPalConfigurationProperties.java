package io.reflectoring.buckpal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "buckpal")
public class BuckPalConfigurationProperties {

  private long transferThreshold = Long.MAX_VALUE;

  public long getTransferThreshold() {
    return transferThreshold;
  }

  public void setTransferThreshold(long transferThreshold) {
    this.transferThreshold = transferThreshold;
  }
}
