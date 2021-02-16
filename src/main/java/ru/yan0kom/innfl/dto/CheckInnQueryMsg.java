package ru.yan0kom.innfl.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CheckInnQueryMsg {
    private Long personId;
    private BigInteger fnsQueryId;
}
