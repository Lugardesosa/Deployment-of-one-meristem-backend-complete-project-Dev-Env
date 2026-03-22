package org.meristem.oneapp.coreservices.notifications.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailTemplate {

    ADDRESS_VERIFICATION_SUCCESSFUL_EMAIL("address-verification-successful-email.html"),
    BIRTHDAY_GREETING_EMAIL("birthday-greeting-email.html"),
    BVN_VERIFICATION_FAILED_EMAIL("bvn-verification-failed-email.html"),
    CONFIRM_EMAIL_ADDRESS("confirm-email-address.html"),
    CONFIRM_VERIFICATION_CODE("confirm-verification-code.html"),
    DEPOSIT_SUCCESSFUL("deposit-successful.html"),
    FORGOT_PASSWORD("forgot-password.html"),
    IDLE_FUNDS_REMINDER_EMAIL("idle-funds-reminder-email.html"),
    INVESTMENT_CONFIRMATION_EMAIL("investment-confirmation-email.html"),
    INVESTMENT_OPPORTUNITY_EMAIL("investment-opportunity-email.html"),
    LOGIN_NOTIFICATION("login-notification.html"),
    MARKET_INSIGHT_EMAIL("market-insight-email.html"),
    MERISTEM_MARKET_REPORT_EMAIL("meristem-market-report-email.html"),
    MONTHLY_VALUATION_STATEMENT_EMAIL("monthly-valuation-statement-email.html"),
    SCHEDULED_MAINTENANCE("scheduled-maintenance.html"),
    SELL_REQUEST_PROCESSING_EMAIL("sell-request-processing-email.html"),
    STOCK_SELL_REQUEST_PROCESSING_EMAIL("stock-sell-request-processing-email.html"),
    TRADE_ORDER_CANCELLED_EMAIL("trade-order-cancelled-email.html"),
    TRADE_ORDER_NOT_EXECUTED_EMAIL("trade-order-not-executed-email.html"),
    TRADING_ACCOUNT_READY_EMAIL("trading-account-ready-email.html"),
    WALLET_FUNDING_CONFIRMATION_EMAIL("wallet-funding-confirmation-email.html"),
    WATCHLIST_PRICE_TRIGGER_EMAIL("watchlist-price-trigger-email.html"),
    WEEKLY_MARKET_INSIGHT_EMAIL("weekly-market-insight-email.html"),
    WELCOME_ONBOARDING_EMAIL("welcome-onboarding-email.html"),
    WITHDRAWAL_SUCCESSFUL("withdrawal-successful.html");

    private final String fileName;

}
