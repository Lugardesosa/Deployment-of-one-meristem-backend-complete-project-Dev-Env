package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RiskAppetite {

    CONSERVATIVE("Conservative", """
            You are a Conservative Investor
            You are risk averse, you prefer safety and preservation of capital.
            Consider investing in our Conservative Funds
            """, "High Risk Appetite Investor"),

    BALANCED("Balanced", """
            You are a Balanced Investor
            You have a low-risk appetite, you strike a balance between risk and return.
            Consider investing in our Balanced Funds
            """, "Low Risk Appetite Investor"),

    AGGRESSIVE("Aggressive", """
            You are a Growth Oriented Investor
            You have a high-risk appetite, you are comfortable with high volatility.
            Consider investing in our Aggressive Funds
            """, "High Risk Appetite Investor");

    private final String riskType;
    private final String description;
    private final String riskProfile;
}
