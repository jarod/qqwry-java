package com.github.jarod.qqwry;

import lombok.*;

@ToString
@RequiredArgsConstructor
public class IPZone {
  @Getter @NonNull private final String ip;

  @Getter @Setter private String mainInfo = "";

  @Getter @Setter private String subInfo = "";
}
